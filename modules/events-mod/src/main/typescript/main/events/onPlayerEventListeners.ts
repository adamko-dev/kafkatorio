import {EventDataCache} from "../cache/EventDataCache";
import {Converters} from "./converters";
import CacheData = EventDataCache.CacheData;

type PlayerUpdater = (player: LuaPlayer, data: CacheData<"PLAYER">) => void

function playerUpdateThrottle(
    playerIndex: uint,
    eventName: string,
    mutate: PlayerUpdater,
) {
  EventDataCache.throttle<"PLAYER">(
      {index: playerIndex, updateType: "PLAYER"},
      (data => {
        const player = game.players[playerIndex]
        if (player != undefined) {
          mutate(player, data)
        }

        if (data.events == null) {
          data.events = []
        }
        if (eventName ! in data.events) {
          data.events.push(eventName)
        }
      })
  )
}

function playerUpdateDebounce(playerIndex: uint, mutate: PlayerUpdater) {
  EventDataCache.debounce<"PLAYER">(
      {index: playerIndex, updateType: "PLAYER"},
      (data => {
        const player = game.players[playerIndex]
        mutate(player, data)
      })
  )
}

function playerOnlineInfo(player: LuaPlayer, data: CacheData<"PLAYER">) {
  data.lastOnline = player.last_online
  data.onlineTime = player.online_time
  data.afkTime = player.afk_time
  data.isConnected = player.connected
}

script.on_event(
    defines.events.on_player_joined_game,
    (e: OnPlayerJoinedGameEvent) => {
      playerUpdateThrottle(
          e.player_index,
          Converters.eventNameString(e.name),
          (player, data) => {
            data.isAdmin = player.admin
            data.characterUnitNumber = player.character?.unit_number ?? null
            data.chatColour = Converters.mapColour(player.chat_color)
            data.colour = Converters.mapColour(player.color)
            data.forceIndex = player.force.index
            data.name = player.name
            data.isShowOnMap = player.show_on_map
            data.isSpectator = player.spectator
            data.tag = player.tag
            playerOnlineInfo(player, data)
          }
      )
    }
)

script.on_event(
    defines.events.on_player_changed_position,
    (e: OnPlayerChangedPositionEvent) => {
      playerUpdateThrottle(
          e.player_index,
          Converters.eventNameString(e.name),
          (player, data) => {
            data.position = player.position
          }
      )
    }
)

script.on_event(
    defines.events.on_player_changed_surface,
    (e: OnPlayerChangedSurfaceEvent) => {
      playerUpdateThrottle(
          e.player_index,
          Converters.eventNameString(e.name),
          (player, data) => {
            data.position = player.position
            data.surfaceIndex = player.surface.index
          }
      )
    }
)

script.on_event(
    defines.events.on_player_died,
    (e: OnPlayerDiedEvent) => {
      playerUpdateThrottle(
          e.player_index,
          Converters.eventNameString(e.name),
          (player, data) => {
            data.ticksToRespawn = player.ticks_to_respawn ?? null
            playerOnlineInfo(player, data)
            if (e.cause != undefined) {
              data.diedCause = {
                unitNumber: e.cause.unit_number ?? null,
                name: e.cause.name,
                type: e.cause.type
              }
            }
          }
      )
    }
)

script.on_event(defines.events.on_player_banned, handleBannedEvent)
script.on_event(defines.events.on_player_unbanned, handleBannedEvent)

function handleBannedEvent(e: OnPlayerBannedEvent | OnPlayerUnbannedEvent) {

  if (e.player_index != undefined) {
    playerUpdateThrottle(
        e.player_index,
        Converters.eventNameString(e.name),
        (player, data) => {
          data.bannedReason = e.reason ?? null
          playerOnlineInfo(player, data)
        }
    )
  }
}

script.on_event(
    defines.events.on_player_kicked,
    (e: OnPlayerKickedEvent) => {
      playerUpdateThrottle(
          e.player_index,
          Converters.eventNameString(e.name),
          (player, data) => {
            data.kickedReason = e.reason ?? null
            playerOnlineInfo(player, data)
          }
      )
    }
)


const disconnectReasons = new LuaTable<defines.disconnect_reason, keyof typeof defines.disconnect_reason>()
for (const [name, disconnectId] of pairs(defines.disconnect_reason)) {
  disconnectReasons.set(disconnectId, name)
}

script.on_event(
    defines.events.on_pre_player_left_game,
    (e: OnPrePlayerLeftGameEvent) => {
      playerUpdateThrottle(
          e.player_index,
          Converters.eventNameString(e.name),
          (player, data) => {
            data.disconnectReason = disconnectReasons.get(e.reason)
            playerOnlineInfo(player, data)
          }
      )
    }
)

script.on_event(
    defines.events.on_player_removed,
    (e: OnPlayerRemovedEvent) => {
      playerUpdateThrottle(
          e.player_index,
          Converters.eventNameString(e.name),
          (player, data) => {
            data.isRemoved = true
            playerOnlineInfo(player, data)
          }
      )
    }
)
