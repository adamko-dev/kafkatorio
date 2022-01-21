import {emitPrototypes} from "../config-update/prototypes";
import {handleChunkUpdate} from "../events/handlers";
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
              "player-command",
              player.surface.index,
              chunkPosition,
              chunkArea,
          )
        }
      } else if ("CHUNKS" == e.parameter.toUpperCase()) {

        for (let [, surface] of game.surfaces) {
          for (let chunk of surface.get_chunks()) {
            if (
                (chunk.x >= -16 || chunk.x <= 16)
                &&
                (chunk.y >= -16 || chunk.y <= 16)
            ) {
              handleChunkUpdate(
                  e.tick,
                  "player-command",
                  surface.index,
                  chunk,
                  chunk.area,
              )
            }
          }
        }
      }
    }
)
