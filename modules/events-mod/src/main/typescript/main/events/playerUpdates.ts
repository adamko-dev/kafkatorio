import {Converters} from "./converters";
import {ForceIndex, KafkatorioPacketData, PlayerUpdateKey} from "../../generated/kafkatorio-schema";
import packetEmitter from "../PacketEmitter";
import PlayerUpdates from "./handlers/PlayerUpdateHandler";


// function playerUpdateDebounce(playerIndex: uint, mutate: PlayerUpdater) {
//   EventUpdatesManager.debounce<"PLAYER">(
//       {index: playerIndex, updateType: "PLAYER"},
//       data => {
//         const player = game.players[playerIndex]
//         mutate(player, data)
//       }
//   )
// }


script.on_event(defines.events.on_player_joined_game, (e: OnPlayerJoinedGameEvent) => {
  PlayerUpdates.playerUpdateThrottle(
      e,
      (player, data) => {
        data.isAdmin = player.admin
        data.characterUnitNumber = player.character?.unit_number ?? null
        data.chatColour = Converters.mapColour(player.chat_color)
        data.colour = Converters.mapColour(player.color)
        data.forceIndex = player.force.index as ForceIndex
        data.name = player.name
        data.isShowOnMap = player.show_on_map
        data.isSpectator = player.spectator
        data.surfaceIndex = player.surface.index
        data.tag = player.tag
        Converters.playerOnlineInfo(player, data)
      }
  )
})


script.on_event(defines.events.on_player_changed_position, (e: OnPlayerChangedPositionEvent) => {
  PlayerUpdates.playerUpdateThrottle(
      e,
      (player, data) => {
        data.position = [player.position.x, player.position.y]
      }
  )
})


script.on_event(defines.events.on_player_changed_surface, (e: OnPlayerChangedSurfaceEvent) => {
  PlayerUpdates.playerUpdateThrottle(
      e,
      (player, data) => {
        data.position = [player.position.x, player.position.y]
        data.surfaceIndex = player.surface.index
      }
  )
})


script.on_event(defines.events.on_player_died, (e: OnPlayerDiedEvent) => {
  PlayerUpdates.playerUpdateThrottle(
      e,
      (player, data) => {
        data.ticksToRespawn = player.ticks_to_respawn ?? null
        Converters.playerOnlineInfo(player, data)
        if (e.cause != undefined) {
          data.diedCause = {
            unitNumber: e.cause.unit_number ?? null,
            protoId: Converters.prototypeId(e.cause.type, e.cause.name),
          }
        }
      }
  )
})


script.on_event(defines.events.on_player_banned, (event: OnPlayerBannedEvent) => {
  PlayerUpdates.handleBannedEvent(event)
})
script.on_event(defines.events.on_player_unbanned, (event: OnPlayerBannedEvent) => {
  PlayerUpdates.handleBannedEvent(event)
})


script.on_event(defines.events.on_player_kicked, (event: OnPlayerKickedEvent) => {
  log(`on_player_kicked ${event.tick} ${event.name}`)
  PlayerUpdates.playerUpdateImmediate(
      event,
      (player, data) => {
        data.kickedReason = event.reason ?? null
        Converters.playerOnlineInfo(player, data)
      }
  )
})


script.on_event(defines.events.on_pre_player_left_game, (event: OnPrePlayerLeftGameEvent) => {
  log(`on_pre_player_left_game ${event.tick} ${event.name}`)

  const playerUpdateKey: PlayerUpdateKey = {
    index: event.player_index,
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
})

script.on_event(defines.events.on_player_removed, (event: OnPlayerRemovedEvent) => {
  log(`on_player_removed ${event.tick} ${event.name}`)
  PlayerUpdates.playerUpdateImmediate(
      event,
      (player, data) => {
        data.isRemoved = true
        Converters.playerOnlineInfo(player, data)
      }
  )
})


const disconnectReasons = new LuaTable<defines.disconnect_reason, keyof typeof defines.disconnect_reason>()
for (const [name, disconnectId] of pairs(defines.disconnect_reason)) {
  disconnectReasons.set(disconnectId, name)
}
