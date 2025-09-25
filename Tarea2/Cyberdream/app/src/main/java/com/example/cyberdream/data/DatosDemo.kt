package com.example.cyberdream.data

object DatosDemo {

    val afterlife = Edificio(
        id = "afterlife",
        nombre = "Afterlife Club",
        descripcion = "Bar clandestino legendario con ambiente neón.",
        puntos = listOf(
            PuntoInteres("barra", "Barra de Cromo", "Hologramas sirven tragos."),
            PuntoInteres("dj", "Cabina de DJs Cyborg", "Visuales glitch 24/7."),
            PuntoInteres("vip", "Sala VIP Roja", "Tratos clandestinos.")
        )
    )

    val estacion = Edificio(
        id = "estacion",
        nombre = "Estación Fantasma",
        descripcion = "Antiguo metro convertido en refugio urbano.",
        puntos = listOf(
            PuntoInteres("anden", "Andén Abandonado", "Grafitis casi vivos."),
            PuntoInteres("vagon", "Vagón Oxidado", "Refugio improvisado."),
            PuntoInteres("control", "Sala de Control Vieja", "Luces parpadeantes.")
        )
    )

    val mods = Edificio(
        id = "mods",
        nombre = "Callejón de Mods",
        descripcion = "Implantes baratos, riesgo alto.",
        puntos = listOf(
            PuntoInteres("taller", "Taller Improvisado", "Cromo de dudosa procedencia."),
            PuntoInteres("anuncios", "Pared de Anuncios Digitales", "Propaganda de implantes."),
            PuntoInteres("reciclaje", "Zona de Reciclaje", "Piezas robóticas apiladas.")
        )
    )

    val oldDowntown = Barrio(
        id = "old_downtown",
        nombre = "Old Downtown",
        descripcion = "Decadencia brillante: neón, lluvia y secretos.",
        edificios = listOf(afterlife, estacion, mods)
    )
}
