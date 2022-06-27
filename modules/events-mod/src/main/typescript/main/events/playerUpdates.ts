import {Converters} from "./converters";
import EventUpdatesManager from "../cache/EventDataCache";
import {
  KafkatorioPacketData,
  PlayerUpdateKey
} from "../../generated/kafkatorio-schema";
import Type = KafkatorioPacketData.Type;
import packetEmitter from "../PacketEmitter";


type PlayerUpdater = (player: LuaPlayer, data: KafkatorioPacketData.PlayerUpdate) => void


type PlayerUpdateEvent =
    | OnPlayerJoinedGameEvent
    | OnPlayerChangedPositionEvent
    | OnPlayerChangedSurfaceEvent
    | OnPlayerDiedEvent
    | OnPlayerBannedEvent
    | OnPlayerUnbannedEvent
    | OnPlayerKickedEvent
    | OnPrePlayerLeftGameEvent
    | OnPlayerRemovedEvent


function playerUpdateThrottle(
    event: PlayerUpdateEvent,
    mutate: PlayerUpdater,
    expirationDurationTicks: uint | undefined = undefined,
) {
  const playerIndex = event.player_index
  if (playerIndex == undefined) {
    return
  }

  const eventName = Converters.eventNameString(event.name)

  EventUpdatesManager.throttle<KafkatorioPacketData.PlayerUpdate>(
      {index: playerIndex},
      Type.PlayerUpdate,
      data => {
        const player = game.players[playerIndex]
        if (player != undefined) {
          mutate(player, data)
        }

        data.events ??= {}
        data.events[eventName] ??= []
        data.events[eventName].push(event.tick)
      },
      expirationDurationTicks,
  )
}


function playerUpdateImmediate(
    event: PlayerUpdateEvent,
    mutate: PlayerUpdater,
) {
  playerUpdateThrottle(event, mutate, 0)
}


// function playerUpdateDebounce(playerIndex: uint, mutate: PlayerUpdater) {
//   EventUpdatesManager.debounce<"PLAYER">(
//       {index: playerIndex, updateType: "PLAYER"},
//       data => {
//         const player = game.players[playerIndex]
//         mutate(player, data)
//       }
//   )
// }

function playerOnlineInfo(player: LuaPlayer, data: KafkatorioPacketData.PlayerUpdate) {
  data.lastOnline = player.last_online
  data.onlineTime = player.online_time
  data.afkTime = player.afk_time
  data.isConnected = player.connected
}

script.on_event(
    defines.events.on_player_joined_game,
    (e: OnPlayerJoinedGameEvent) => {
      playerUpdateThrottle(
          e,
          (player, data) => {
            data.isAdmin = player.admin
            data.characterUnitNumber = player.character?.unit_number ?? null
            data.chatColour = Converters.mapColour(player.chat_color)
            data.colour = Converters.mapColour(player.color)
            data.forceIndex = player.force.index
            data.name = player.name
            data.isShowOnMap = player.show_on_map
            data.isSpectator = player.spectator
            data.surfaceIndex = player.surface.index
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
          e,
          (player, data) => {
            data.position = [player.position.x, player.position.y]
          }
      )
    }
)


script.on_event(
    defines.events.on_player_changed_surface,
    (e: OnPlayerChangedSurfaceEvent) => {
      playerUpdateThrottle(
          e,
          (player, data) => {
            data.position = [player.position.x, player.position.y]
            data.surfaceIndex = player.surface.index
          }
      )
    }
)


script.on_event(
    defines.events.on_player_died,
    (e: OnPlayerDiedEvent) => {
      playerUpdateThrottle(
          e,
          (player, data) => {
            data.ticksToRespawn = player.ticks_to_respawn ?? null
            playerOnlineInfo(player, data)
            if (e.cause != undefined) {
              data.diedCause = {
                unitNumber: e.cause.unit_number ?? null,
                name: e.cause.name,
                protoType: e.cause.type
              }
            }
          }
      )
    }
)


script.on_event(defines.events.on_player_banned, handleBannedEvent)
script.on_event(defines.events.on_player_unbanned, handleBannedEvent)

function handleBannedEvent(event: OnPlayerBannedEvent | OnPlayerUnbannedEvent) {
  playerUpdateThrottle(
      event,
      (player, data) => {
        data.bannedReason = event.reason ?? null
        playerOnlineInfo(player, data)
      }
  )
}


script.on_event(
    defines.events.on_player_kicked,
    (event: OnPlayerKickedEvent) => {
      log(`on_player_kicked ${event.tick} ${event.name}`)
      playerUpdateImmediate(
          event,
          (player, data) => {
            data.kickedReason = event.reason ?? null
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
    (event: OnPrePlayerLeftGameEvent) => {
      log(`on_pre_player_left_game ${event.tick} ${event.name}`)

      const playerUpdateKey: PlayerUpdateKey = {
        index: event.player_index
      }

      const playerUpdate: KafkatorioPacketData.PlayerUpdate = {
        type: KafkatorioPacketData.Type.PlayerUpdate,
        key: playerUpdateKey,
        disconnectReason: disconnectReasons.get(event.reason)
      }

      packetEmitter.emitKeyedPacket(playerUpdate)

      // playerUpdateImmediate(
      //     event,
      //     (player, data) => {
      //       data.disconnectReason = disconnectReasons.get(event.reason)
      //       playerOnlineInfo(player, data)
      //     }
      // )
    }
)

script.on_event(
    defines.events.on_player_removed,
    (event: OnPlayerRemovedEvent) => {
      log(`on_player_removed ${event.tick} ${event.name}`)
      playerUpdateImmediate(
          event,
          (player, data) => {
            data.isRemoved = true
            playerOnlineInfo(player, data)
          }
      )
    }
)
