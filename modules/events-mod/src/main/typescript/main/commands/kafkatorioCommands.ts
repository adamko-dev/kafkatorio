import {emitPrototypes} from "../config-update/prototypes";
import {handleChunkUpdate} from "../events/handlers";
import {Queue} from "../queue/queue";
import floor = math.floor;


commands.add_command(
    "kafkatorio",
    "kafkatorio innit bruv",
    (e: CustomCommandData) => {
      if (e.parameter == undefined) {
        // do nothing
      } else if ("PROTOTYPES" == e.parameter.toUpperCase()) {
        emitPrototypes()
      } else if ("CURRENT_CHUNK" == e.parameter.toUpperCase()) {

        if (e.player_index != undefined) {

          let player = game.players[e.player_index]

          let chunkPosition: ChunkPosition = {
            x: floor(player.position.x / 32),
            y: floor(player.position.y / 32),
          }

          let chunkLeftTop: Position = {
            x: chunkPosition.x * 32,
            y: chunkPosition.y * 32,
          }
          let chunkBottomRight: Position = {
            x: chunkLeftTop.x + 32 - 1,
            y: chunkLeftTop.y + 32 - 1,
          }
          let chunkArea: BoundingBoxRead = {
            left_top: chunkLeftTop,
            right_bottom: chunkBottomRight
          }

          handleChunkUpdate(
              e.tick,
              "player-command-" + e.parameter,
              player.surface.index,
              chunkPosition,
              chunkArea,
          )
        }
      } else if ("CHUNKS" == e.parameter.toUpperCase()) {

        if (e.player_index != undefined) {
          let player = game.players[e.player_index]

          let chunkPosition: ChunkPosition = {
            x: floor(player.position.x / 32),
            y: floor(player.position.y / 32),
          }

          let delta = 1

          let chunkXMin = chunkPosition.x - delta
          let chunkXMax = chunkPosition.x + delta
          let chunkYMin = chunkPosition.y - delta
          let chunkYMax = chunkPosition.y + delta

          for (let [, surface] of game.surfaces) {
            for (let chunk of surface.get_chunks()) {
              if (
                  (chunk.x >= chunkXMin || chunk.x <= chunkXMax)
                  &&
                  (chunk.y >= chunkYMin || chunk.y <= chunkYMax)
              ) {

                let data: OnChunkGeneratedEvent = {
                  name: defines.events.on_chunk_generated,
                  position: {x: chunk.x, y: chunk.y},
                  area: chunk.area,
                  surface: surface,
                  tick: e.tick
                }

                Queue.enqueue(
                    `${surface.index}${chunk.x}${chunk.y}`,
                    data,
                    50
                )
              }
            }
          }
        }
      } else if ("QUEUE_RESET" == e.parameter.toUpperCase()) {
        Queue.reset()
      } else if ("QUEUE_INIT" == e.parameter.toUpperCase()) {
        Queue.init()
      }
    }
)
