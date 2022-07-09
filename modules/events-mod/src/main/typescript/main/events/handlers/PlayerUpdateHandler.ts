import {KafkatorioPacketData} from "../../../generated/kafkatorio-schema";
import {Converters} from "../converters";
import EventUpdates from "../../emitting/EventDataCache";


export type PlayerUpdater = (player: LuaPlayer, data: KafkatorioPacketData.PlayerUpdate) => void


export type PlayerUpdateEvent =
    | OnPlayerJoinedGameEvent
    | OnPlayerChangedPositionEvent
    | OnPlayerChangedSurfaceEvent
    | OnPlayerDiedEvent
    | OnPlayerBannedEvent
    | OnPlayerUnbannedEvent
    | OnPlayerKickedEvent
    | OnPrePlayerLeftGameEvent
    | OnPlayerRemovedEvent


export class PlayerUpdateHandler {

  handleBannedEvent(event: OnPlayerBannedEvent | OnPlayerUnbannedEvent) {
    this.playerUpdateThrottle(
        event,
        (player, data) => {
          data.bannedReason = event.reason ?? null
          Converters.playerOnlineInfo(player, data)
        }
    )
  }


  playerUpdateThrottle(
      event: PlayerUpdateEvent,
      mutate: PlayerUpdater,
      expirationDurationTicks: uint | undefined = undefined,
  ) {
    const playerIndex = event.player_index
    if (playerIndex == undefined) {
      return
    }

    const eventName = Converters.eventNameString(event.name)

    EventUpdates.throttle<KafkatorioPacketData.PlayerUpdate>(
        {index: playerIndex},
        KafkatorioPacketData.Type.PlayerUpdate,
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


  playerUpdateImmediate(
      event: PlayerUpdateEvent,
      mutate: PlayerUpdater,
  ) {
    this.playerUpdateThrottle(event, mutate, 0)
  }

}

const PlayerUpdates = new PlayerUpdateHandler()

export default PlayerUpdates
