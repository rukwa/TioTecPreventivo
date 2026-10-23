package cl.duoc.tiotec.data.model

/**
 * Las 12 preguntas de la evaluación preventiva: 3 por cada área.
 * Están en código (no en la base de datos) para que la app funcione desde cero
 * en cualquier computador, sin necesitar datos precargados.
 */
object BancoPreguntas {

    private val opcionesEstandar = listOf(
        Opcion("Sí", 2),
        Opcion("A veces / no estoy seguro", 1),
        Opcion("No", 0)
    )

    val preguntas: List<Pregunta> = listOf(
        // ---------------- RESPALDOS ----------------
        Pregunta(
            id = 1,
            area = Area.RESPALDOS,
            enunciado = "¿Tienes activado el respaldo automático de fotos y contactos en la nube?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Activa el respaldo automático",
                detalle = "En Ajustes > Google > Copia de seguridad (Android) o Ajustes > iCloud (iPhone), activa la copia automática.",
                prioridad = Prioridad.ALTA
            )
        ),
        Pregunta(
            id = 2,
            area = Area.RESPALDOS,
            enunciado = "¿Has revisado en el último mes que tu respaldo se esté realizando?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Revisa la fecha de tu último respaldo",
                detalle = "Entra a la configuración de copia de seguridad y confirma que la última copia sea de los últimos días.",
                prioridad = Prioridad.MEDIA
            )
        ),
        Pregunta(
            id = 3,
            area = Area.RESPALDOS,
            enunciado = "¿Guardas una copia de tus documentos importantes fuera del teléfono (nube o disco externo)?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Guarda una segunda copia de tus documentos",
                detalle = "Sube tus documentos importantes a la nube o cópialos a un disco externo por si pierdes el teléfono.",
                prioridad = Prioridad.MEDIA
            )
        ),

        // ---------------- ACTUALIZACIONES ----------------
        Pregunta(
            id = 4,
            area = Area.ACTUALIZACIONES,
            enunciado = "¿Instalas las actualizaciones del sistema operativo cuando aparecen?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Instala las actualizaciones pendientes",
                detalle = "Ve a Ajustes > Sistema > Actualización de software. Las actualizaciones corrigen fallas de seguridad.",
                prioridad = Prioridad.ALTA
            )
        ),
        Pregunta(
            id = 5,
            area = Area.ACTUALIZACIONES,
            enunciado = "¿Tienes activada la actualización automática de aplicaciones?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Activa la actualización automática de apps",
                detalle = "En la tienda de aplicaciones, entra a Ajustes y activa la actualización automática.",
                prioridad = Prioridad.MEDIA
            )
        ),
        Pregunta(
            id = 6,
            area = Area.ACTUALIZACIONES,
            enunciado = "¿Descargas aplicaciones solo desde la tienda oficial?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Instala apps solo desde la tienda oficial",
                detalle = "Evita archivos APK o enlaces de origen desconocido: pueden contener programas maliciosos.",
                prioridad = Prioridad.ALTA
            )
        ),

        // ---------------- AUTENTICACIÓN ----------------
        Pregunta(
            id = 7,
            area = Area.AUTENTICACION,
            enunciado = "¿Usas una contraseña distinta para cada cuenta importante?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Usa contraseñas distintas en cada cuenta",
                detalle = "Si una contraseña se filtra, las demás cuentas siguen protegidas. Un gestor de contraseñas ayuda a recordarlas.",
                prioridad = Prioridad.ALTA
            )
        ),
        Pregunta(
            id = 8,
            area = Area.AUTENTICACION,
            enunciado = "¿Tienes activada la verificación en dos pasos en tu correo principal?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Activa la verificación en dos pasos",
                detalle = "En la configuración de seguridad de tu correo, activa la verificación en dos pasos con una app o con SMS.",
                prioridad = Prioridad.ALTA
            )
        ),
        Pregunta(
            id = 9,
            area = Area.AUTENTICACION,
            enunciado = "¿Tu teléfono se bloquea con PIN, patrón o huella?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Configura un bloqueo de pantalla",
                detalle = "En Ajustes > Seguridad, elige PIN, patrón o huella para que nadie use tu teléfono sin permiso.",
                prioridad = Prioridad.MEDIA
            )
        ),

        // ---------------- RECUPERACIÓN DE CUENTAS ----------------
        Pregunta(
            id = 10,
            area = Area.RECUPERACION,
            enunciado = "¿Tu correo principal tiene un teléfono de recuperación actualizado?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Agrega un teléfono de recuperación",
                detalle = "Si pierdes el acceso, el código de recuperación llegará a ese número. Revísalo en la configuración de tu cuenta.",
                prioridad = Prioridad.ALTA
            )
        ),
        Pregunta(
            id = 11,
            area = Area.RECUPERACION,
            enunciado = "¿Tienes un correo alternativo registrado para recuperar tus cuentas?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Registra un correo alternativo",
                detalle = "Agrega un segundo correo en la sección de seguridad de tus cuentas principales.",
                prioridad = Prioridad.MEDIA
            )
        ),
        Pregunta(
            id = 12,
            area = Area.RECUPERACION,
            enunciado = "¿Guardas en un lugar seguro los códigos de respaldo de tus cuentas?",
            opciones = opcionesEstandar,
            recomendacion = Recomendacion(
                titulo = "Guarda tus códigos de respaldo",
                detalle = "Descarga los códigos de recuperación y guárdalos impresos o en un lugar seguro fuera del teléfono.",
                prioridad = Prioridad.BAJA
            )
        )
    )

    /** Preguntas de una sola área (útil para la pantalla Aprende y para calcular puntajes). */
    fun porArea(area: Area): List<Pregunta> = preguntas.filter { it.area == area }
}
