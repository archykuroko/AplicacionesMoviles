package mx.escom.cardjitsu.juego.dominio


//  Reglas de contras entre elementos:
//- AGUA vence a FUEGO
//- FUEGO vence a NIEVE
//- NIEVE vence a AGUA


object Reglas {

    private val venceA: Map<Elemento, Elemento> = mapOf(
        Elemento.AGUA  to Elemento.FUEGO,
        Elemento.FUEGO to Elemento.NIEVE,
        Elemento.NIEVE to Elemento.AGUA
    )


    // Función que compara dos elecciones y devuelve el resultado de la ronda

    fun enfrentar(p1: Elemento, p2: Elemento): ResultadoRonda {
        if (p1 == p2) return ResultadoRonda.EMPATE
        return if (venceA[p1] == p2) ResultadoRonda.GANA_J1 else ResultadoRonda.GANA_J2
    }

    // Función de utilidad: ¿A vence a B?
    fun vence(a: Elemento, b: Elemento): Boolean = venceA[a] == b
}
