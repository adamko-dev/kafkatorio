import PacketEmitter from "../PacketEmitter";
import {KafkatorioPacketData} from "../../generated/kafkatorio-schema";
import Type = KafkatorioPacketData.Type;


script.on_event(
    defines.events.on_console_chat,
    (event: OnConsoleChatEvent) => {
      const update: KafkatorioPacketData.ConsoleChatUpdate = {
        type: Type.ConsoleChatUpdate,

        authorPlayerIndex: event.player_index ?? null,
        content: event.message
      }
      PacketEmitter.emitInstantPacket(update)
    }
)


script.on_event(
    defines.events.on_console_command,
    (event: OnConsoleCommandEvent) => {
      const update: KafkatorioPacketData.ConsoleCommandUpdate = {
        type: Type.ConsoleCommandUpdate,

        authorPlayerIndex: event.player_index ?? null,
        command: event.command,
        parameters: event.parameters,
      }

      PacketEmitter.emitInstantPacket(update)
    }
)
