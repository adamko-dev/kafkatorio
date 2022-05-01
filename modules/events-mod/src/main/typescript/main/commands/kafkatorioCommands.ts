import {emitPrototypes} from "../config-update/prototypes";
import {initGlobal} from "../global-init";
import floor = math.floor;
import {Queue} from "../queue/queue";


commands.add_command(
    "kafkatorio",
    "kafkatorio innit bruv",
    (e: CustomCommandData) => {

      const player = (e.player_index != null) ? game.get_player(e.player_index) : null

      if (e.parameter == undefined) {
        if (player != null) {
          player.print("no parameter")
        }
        // do nothing
      } else {

        const paramUppercase: string = e.parameter.toUpperCase()

        if ("PROTOTYPES" == paramUppercase) {
          emitPrototypes()
        } else if (paramUppercase.startsWith("CHUNKS")) {

          const size = paramUppercase.split(" ")?.[1] ?? null
          let radius = (size != null) ? parseInt(size) : 1
          // radius = Math.max(radius, 10)

          if (e.player_index != undefined) {
            let player = game.players[e.player_index]

            const chunkPosition: ChunkPosition = {
              x: floor(player.position.x / 32),
              y: floor(player.position.y / 32),
            }

            const chunkXMin = chunkPosition.x - radius
            const chunkXMax = chunkPosition.x + radius
            const chunkYMin = chunkPosition.y - radius
            const chunkYMax = chunkPosition.y + radius

            // let i = 100
            let chunkCount = 0
            for (const [, surface] of game.surfaces) {
              for (const chunk of surface.get_chunks()) {
                if (
                    (chunk.x >= chunkXMin && chunk.x <= chunkXMax)
                    &&
                    (chunk.y >= chunkYMin && chunk.y <= chunkYMax)
                ) {

                  const data: OnChunkGeneratedEvent = {
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
                  chunkCount++
                  // handleChunkGeneratedEvent(data, i)
                  // i = i + 10
                }
              }
            }
            player.print(`enqueued ${chunkCount} chunks`)
          }
        } else if (paramUppercase.startsWith("INIT_GLOBAL")) {
          initGlobal(paramUppercase.startsWith("INIT_GLOBAL FORCE"))
        }
      }
    }
)
