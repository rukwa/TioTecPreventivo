package cl.duoc.tiotec.data.model

/** Las 4 áreas que evalúa TíoTec, tal como las define el caso de MCmovil. */
enum class Area(val titulo: String, val descripcion: String) {
    RESPALDOS(
        titulo = "Respaldos",
        descripcion = "Copias de seguridad de fotos, documentos y contactos"
    ),
    ACTUALIZACIONES(
        titulo = "Actualizaciones",
        descripcion = "Sistema operativo y aplicaciones al día"
    ),
    AUTENTICACION(
        titulo = "Autenticación",
        descripcion = "Contraseñas seguras y verificación en dos pasos"
    ),
    RECUPERACION(
        titulo = "Recuperación de cuentas",
        descripcion = "Correo y teléfono de respaldo para recuperar el acceso"
    )
}
