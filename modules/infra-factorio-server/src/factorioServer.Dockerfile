# build rcon client
FROM debian:stable-slim AS rcon-builder
RUN apt-get -q update \
    && DEBIAN_FRONTEND=noninteractive apt-get -qy install \
    build-essential

WORKDIR /src
COPY rcon/ /src
RUN make


# Download and unpack Factorio server archive
FROM debian:stable-slim AS server-dl
RUN apt-get -q update \
    && DEBIAN_FRONTEND=noninteractive apt-get -qy install \
    build-essential \
    ca-certificates \
    curl

SHELL ["/bin/bash", "-eo", "pipefail", "-c"]

# version checksum of the archive to download
ARG FactorioVersion
ARG FactorioLinuxSha256

LABEL factorio.version=${FactorioVersion}
LABEL factorio.linuxSha256=${FactorioLinuxSha256}

ENV VERSION=${FactorioVersion} \
    SHA256=${FactorioLinuxSha256}

WORKDIR /src

RUN if [[ "${VERSION}" == "" ]]; then \
        echo "build-arg VERSION is required" \
        && exit 1; \
    fi \
    && if [[ "${SHA256}" == "" ]]; then \
        echo "build-arg SHA256 is required" \
        && exit 1; \
    fi

RUN archive="factorio_headless_x64_$VERSION.tar.xz" \
    && curl -sSL "https://www.factorio.com/get-download/$VERSION/headless/linux64" -o "$archive" --retry 8 \
    && echo "$SHA256 $archive" | sha256sum -c \
    || (sha256sum "$archive" && file "$archive" && exit 1) \
    && tar xf "$archive" \
    && rm "$archive"


# Download and unpack Box64 server archive
FROM debian:stable-slim AS box64-dl

SHELL ["/bin/bash", "-eo", "pipefail", "-c"]

RUN apt-get -q update \
    && DEBIAN_FRONTEND=noninteractive apt-get -qy install --no-install-recommends \
       curl \
       ca-certificates \
       unzip

ARG Box64Version=v0.2.4

ENV BOX64_VERSION=${Box64Version}

WORKDIR /src

RUN archive="box64.zip" \
    && curl -sSL "https://github.com/ptitSeb/box64/releases/download/${BOX64_VERSION}/box64-GENERIC_ARM-RelWithDebInfo.zip" -o "$archive" \
    && unzip "$archive" \
    && rm "$archive"


# build factorio image
FROM debian:stable-slim AS build
LABEL maintainer="https://github.com/factoriotools/factorio-docker"

SHELL ["/bin/bash", "-eo", "pipefail", "-c"]

ARG USER=factorio
#ENV HOME /home/$USER
ARG GROUP=factorio
ARG PUID=845
ARG PGID=845
#ARG BOX64_VERSION=v0.2.4

# optionally utilize a built-in map-gen-preset (see data/base/prototypes/map-gen-presets
# if this is used, the preset will be used over any .json files supplied
# vanilla factorio provides the following presets:
# rich-resources, marathon, death-world, death-world-marathon, rail-world, ribbon-world, island
# a modded factorio example for using this:
# space-exploration
ARG PRESET

# number of retries that curl will use when pulling the headless server tarball
ARG CURL_RETRIES=8

ENV PORT=34197 \
    RCON_PORT=27015 \
    SAVES=/factorio/saves \
    PRESET="$PRESET" \
    CONFIG=/factorio/config \
    MODS=/factorio/mods \
    SCENARIOS=/factorio/scenarios \
    SCRIPTOUTPUT=/factorio/script-output \
    PUID="$PUID" \
    PGID="$PGID" \
    DLC_SPACE_AGE="true"

RUN apt-get -q update \
    && DEBIAN_FRONTEND=noninteractive apt-get -qy install --no-install-recommends \
      ca-certificates \
      jq \
      pwgen \
      xz-utils \
      procps \
      gettext-base \
    && rm -rf /var/lib/apt/lists/*

COPY --from=box64-dl --chmod=755 /src/box64 /bin/box64

RUN addgroup --system --gid "$PGID" "$GROUP" \
    && adduser --system --uid "$PUID" --gid "$PGID" --no-create-home --disabled-password --shell /bin/sh "$USER"

USER $USER
#WORKDIR $HOME

ARG FactorioVersion

LABEL factorio.version=${FactorioVersion}

ENV FactorioVersion=${FactorioVersion} \
    VERSION=${FactorioVersion}

COPY --from=server-dl --chown=$USER --chmod=755 /src /factorio
COPY --from=rcon-builder /src/rcon /bin/rcon

COPY ./files/*.sh /
COPY ./files/config.ini /factorio/config/

RUN ls -la /factorio/config/

VOLUME /factorio
EXPOSE $PORT/udp $RCON_PORT/tcp
ENTRYPOINT ["/docker-entrypoint.sh"]
