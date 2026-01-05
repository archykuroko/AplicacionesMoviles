package ste.archykuroko.examenfinal.tracking

enum class TrackingInterval(val label: String, val millis: Long) {
    S10("10s", 10_000L),
    S60("60s", 60_000L),
    M5("5min", 300_000L)
}
