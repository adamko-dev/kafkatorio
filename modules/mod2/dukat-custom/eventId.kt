
external class TsUInt : tsstdlib.Number

external class EventId<DataT: table, FilterT: table?> {
  @JsName("_eventData")
  val eventData: DataT

  @JsName("_filter")
  val filter: FilterT

}