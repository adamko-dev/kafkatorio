import {emitPrototypes} from "../config-update/prototypes";
import {Queue} from "../queue/queue";
import {initGlobal} from "../global-init";
import floor = math.floor;


commands.add_command(
    "kafkatorio",
    "kafkatorio innit bruv",
    (e: CustomCommandData) => {

      const player = (e.player_index != null) ? game.get_player(e.player_index) : null

      const paramUppercase = e.parameter?.toUpperCase()

      if (paramUppercase == undefined) {
        if (player != null) {
          player.print("no parameter")
        }
        // do nothing
      } else if ("PROTOTYPES" == paramUppercase) {
        emitPrototypes()
      } else if (paramUppercase.startsWith("CHUNKS")) {

        let size = paramUppercase.split(" ")?.[1] ?? null
        let radius = (size != null) ? parseInt(size) : 1

        if (e.player_index != undefined) {
          let player = game.players[e.player_index]

          let chunkPosition: ChunkPosition = {
            x: floor(player.position.x / 32),
            y: floor(player.position.y / 32),
          }

          let chunkXMin = chunkPosition.x - radius
          let chunkXMax = chunkPosition.x + radius
          let chunkYMin = chunkPosition.y - radius
          let chunkYMax = chunkPosition.y + radius

          for (let [, surface] of game.surfaces) {
            for (let chunk of surface.get_chunks()) {
              if (
                  (chunk.x >= chunkXMin && chunk.x <= chunkXMax)
                  &&
                  (chunk.y >= chunkYMin && chunk.y <= chunkYMax)
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
      } else if (paramUppercase.startsWith("INIT_GLOBAL")) {
        initGlobal(paramUppercase.startsWith("INIT_GLOBAL FORCE"))
      }
    }
)
