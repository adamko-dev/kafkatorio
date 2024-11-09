import PacketEmitter from "../emitting/PacketEmitter";
import {KafkatorioPacketData} from "../../generated/kafkatorio-schema";
import {LuaSurface, OnTickEvent} from "factorio:runtime";
import Type = KafkatorioPacketData.Type;


export function handleSurfaceUpdate(
  event: OnTickEvent,
  eventType: string,
  surface: LuaSurface,
) {
  const update: KafkatorioPacketData.SurfaceUpdate = {
    type: Type.SurfaceUpdate,

    name: surface.name,
    index: surface.index,
    daytime: surface.daytime,
  }

  PacketEmitter.emitInstantPacket(update)
}
