import PacketEmitter from "../PacketEmitter";
import {KafkatorioPacketData2} from "../../generated/kafkatorio-schema/kafkatorio-schema";
import Type = KafkatorioPacketData2.Type;

script.on_event(
    defines.events.on_console_chat,
    (event: OnConsoleChatEvent) => {
      const update: KafkatorioPacketData2.ConsoleChatUpdate = {
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
      const update: KafkatorioPacketData2.ConsoleCommandUpdate = {
        type: Type.ConsoleCommandUpdate,

        authorPlayerIndex: event.player_index ?? null,
        command: event.command,
        parameters: event.parameters,
      }

      PacketEmitter.emitInstantPacket(update)
    }
)
