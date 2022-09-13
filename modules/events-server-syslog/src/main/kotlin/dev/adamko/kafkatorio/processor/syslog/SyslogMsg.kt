package dev.adamko.kafkatorio.processor.syslog

import com.github.palindromicity.syslog.dsl.SyslogFieldKeys

data class SyslogMsg(
  val src: Map<String, String?>,
) {

  val message: String?
    get() = src[SyslogFieldKeys.MESSAGE.field]

  val headerAppName: String?
    get() = src[SyslogFieldKeys.HEADER_APPNAME.field]

  val headerHostName: String?
    get() = src[SyslogFieldKeys.HEADER_HOSTNAME.field]

  val headerPri: String?
    get() = src[SyslogFieldKeys.HEADER_PRI.field]

  val headerPriSeverity: String?
    get() = src[SyslogFieldKeys.HEADER_PRI_SEVERITY.field]

  val headerPriFacility: String?
    get() = src[SyslogFieldKeys.HEADER_PRI_FACILITY.field]

  val headerProcId: String?
    get() = src[SyslogFieldKeys.HEADER_PROCID.field]

  val headerTimestamp: String?
    get() = src[SyslogFieldKeys.HEADER_TIMESTAMP.field]

  val headerMsgId: String?
    get() = src[SyslogFieldKeys.HEADER_MSGID.field]

  val headerVersion: String?
    get() = src[SyslogFieldKeys.HEADER_VERSION.field]

  val structuredBase: String?
    get() = src[SyslogFieldKeys.STRUCTURED_BASE.field]

  val structuredElementIdFmt: String?
    get() = src[SyslogFieldKeys.STRUCTURED_ELEMENT_ID_FMT.field]

  val structuredElementIdPnameFmt: String?
    get() = src[SyslogFieldKeys.STRUCTURED_ELEMENT_ID_PNAME_FMT.field]

  val structuredElementIdPnamePattern: String?
    get() = src[SyslogFieldKeys.STRUCTURED_ELEMENT_ID_PNAME_PATTERN.field]

}
