import {KafkatorioPacketData} from "../../generated/kafkatorio-schema";
import KafkatorioPacketQueue from "../emitting/KafkatorioPacketQueue";


script.on_event(defines.events.on_console_chat, (event: OnConsoleChatEvent) => {
  const update: KafkatorioPacketData.ConsoleChatUpdate = {
    type: KafkatorioPacketData.Type.ConsoleChatUpdate,

    authorPlayerIndex: event.player_index ?? null,
    content: event.message
  }
  KafkatorioPacketQueue.enqueue(update)
})


script.on_event(defines.events.on_console_command, (event: OnConsoleCommandEvent) => {
  const update: KafkatorioPacketData.ConsoleCommandUpdate = {
    type: KafkatorioPacketData.Type.ConsoleCommandUpdate,

    authorPlayerIndex: event.player_index ?? null,
    command: event.command,
    parameters: event.parameters,
  }

  KafkatorioPacketQueue.enqueue(update)
})
