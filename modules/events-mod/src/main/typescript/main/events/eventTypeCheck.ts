
export function isEventType<ED extends EventData | CustomInputEvent>(
    eventData: EventData,
    type: ED extends CustomInputEvent ? string : EventId<ED>
): eventData is ED {
  let id: EventId<EventData> | string = eventData.name
  return id == type
}
