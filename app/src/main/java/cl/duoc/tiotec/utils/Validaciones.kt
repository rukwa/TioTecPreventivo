package cl.duoc.tiotec.utils

/**
 * Validaciones centralizadas de todos los formularios.
 *
 * Regla: cada función devuelve el MENSAJE DE ERROR si el valor es inválido,
 * o null si es válido. No sabe nada de la interfaz: la pantalla solo muestra
 * el mensaje que le entrega el ViewModel. Por eso se puede probar con tests.
 *
 * Usamos Regex y no android.util.Patterns porque Patterns devuelve null
 * en las pruebas unitarias y las hace fallar.
 */
object Validaciones {

    private val regexCorreo = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")
    private val regexSoloLetras = Regex("^[\\p{L} ]+$")
    private val regexVersion = Regex("^\\d{1,2}(\\.\\d{1,2}){0,2}$")

    /** Campo de texto que no puede quedar vacío. */
    fun obligatorio(valor: String): String? =
        if (valor.isBlank()) "Este campo es obligatorio" else null

    /** Nombre ficticio: 3 a 40 caracteres, solo letras y espacios (acepta tildes y ñ). */
    fun nombre(valor: String): String? {
        val limpio = valor.trim()
        return when {
            limpio.isEmpty() -> "El nombre es obligatorio"
            limpio.length < 3 -> "Mínimo 3 caracteres"
            limpio.length > 40 -> "Máximo 40 caracteres"
            !regexSoloLetras.matches(limpio) -> "Solo letras y espacios"
            else -> null
        }
    }

    /** Correo con formato nombre@dominio.ext */
    fun correo(valor: String): String? {
        val limpio = valor.trim()
        return when {
            limpio.isEmpty() -> "El correo es obligatorio"
            !regexCorreo.matches(limpio) -> "Correo no válido (ej: nombre@correo.cl)"
            else -> null
        }
    }

    /** Texto con un largo mínimo, por ejemplo la descripción de una solicitud. */
    fun largoMinimo(valor: String, minimo: Int): String? {
        val limpio = valor.trim()
        return when {
            limpio.isEmpty() -> "Este campo es obligatorio"
            limpio.length < minimo -> "Debe tener al menos $minimo caracteres (llevas ${limpio.length})"
            else -> null
        }
    }

    /** Versión de sistema operativo: 14, 17.2 o 10.0.1 */
    fun versionSistema(valor: String): String? {
        val limpio = valor.trim()
        return when {
            limpio.isEmpty() -> "La versión es obligatoria"
            !regexVersion.matches(limpio) -> "Usa solo números y puntos (ej: 14 o 17.2)"
            else -> null
        }
    }

    /** Listas desplegables: categoría, tipo de dispositivo, nivel de experiencia, etc. */
    fun seleccion(valor: String?): String? =
        if (valor.isNullOrBlank()) "Selecciona una opción" else null

    /** Casillas que el usuario debe marcar, como "Uso solo datos ficticios". */
    fun aceptado(marcado: Boolean, mensaje: String): String? =
        if (marcado) null else mensaje
}
