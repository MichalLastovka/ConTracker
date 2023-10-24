package eu.example.contracker

data class ContainerData(
    val containerid: String,
    val umschlaege: List<Umschlaege>
) {
    data class Umschlaege(
        val type: String,
        val angeliefert: Boolean,
        val archiviert: Boolean,
        val ausgeliefert: Boolean,
        val reedercode: Reedercode,
        val terminal: Terminal,
        val umschlagid: String,
        val vorgemeldet: Boolean
    ) {
        data class Reedercode(
            val valid: Boolean,
            val value: String
        )

        data class Terminal(
            val valid: Boolean,
            val value: String
        )
    }
}