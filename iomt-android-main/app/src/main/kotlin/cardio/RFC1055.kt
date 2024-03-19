package cardio

object RFC1055 {
    const val END: Byte = 0xC0.toByte()
    const val ESC: Byte = 0xDB.toByte()
    const val ESC_END: Byte = 0xDC.toByte()
    const val ESC_ESC: Byte = 0xDD.toByte()

    fun encode(data: List<Byte>): ByteArray {
        val list = mutableListOf(END)
        for (byte in data) {
            when (byte) {
                END -> {
                    list.add(ESC)
                    list.add(ESC_END)
                }
                ESC -> {
                    list.add(ESC)
                    list.add(ESC_ESC)
                }
                else -> list.add(byte)
            }
        }
        list.add(END)
        return list.toByteArray()
    }

    fun decode(data: List<Byte>, count: Int): Pair<ByteArray?, Int> {
        var bytesProcessed = 0
        if (data.isEmpty() || count == 0) return Pair(null, bytesProcessed)

        var b = 0
        while (b < count && data[b] == END) b++
        bytesProcessed = b
        var e = b
        while (e < count && data[e] != END) e++
        if (e == count) return Pair(null, bytesProcessed)

        val list = mutableListOf<Byte>()
        var n = b
        while (n < e) {
            if (data[n] != ESC)
                list.add(data[n])
            else {
                if (n < e - 1) {
                    when (data[n + 1]) {
                        ESC_END -> {
                            list.add(END)
                            n++
                        }
                        ESC_ESC -> {
                            list.add(ESC)
                            n++
                        }
                   }
                } else
                    list.add(ESC)
            }
            n++
        }
        bytesProcessed = e + 1
        return Pair(list.toByteArray(), bytesProcessed)
    }
}