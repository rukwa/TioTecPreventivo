package cl.duoc.tiotec.data.model

/**
 * Contenidos educativos breves de la pantalla "Aprende", 3 consejos por área.
 * Están en código para que la app funcione sin internet y desde la primera vez.
 */
object ContenidoAprende {

    val consejos: Map<Area, List<String>> = mapOf(
        Area.RESPALDOS to listOf(
            "Activa la copia automática en la nube para tus fotos y contactos.",
            "Revisa una vez al mes que la última copia sea reciente.",
            "Guarda los documentos importantes en dos lugares distintos."
        ),
        Area.ACTUALIZACIONES to listOf(
            "Instala las actualizaciones del sistema apenas aparezcan: corrigen fallas de seguridad.",
            "Activa la actualización automática de aplicaciones.",
            "Descarga aplicaciones solo desde la tienda oficial."
        ),
        Area.AUTENTICACION to listOf(
            "Usa una contraseña distinta para cada cuenta importante.",
            "Activa la verificación en dos pasos en tu correo principal.",
            "Bloquea tu teléfono con PIN, patrón o huella."
        ),
        Area.RECUPERACION to listOf(
            "Mantén actualizado el teléfono de recuperación de tu correo.",
            "Registra un correo alternativo en tus cuentas principales.",
            "Guarda los códigos de respaldo en un lugar seguro fuera del teléfono."
        )
    )

    fun para(area: Area): List<String> = consejos[area].orEmpty()
}
