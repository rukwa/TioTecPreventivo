package cl.duoc.tiotec.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidacionesTest {

    // ---------- nombre ----------
    @Test
    fun nombre_vacio_devuelveError() {
        assertEquals("El nombre es obligatorio", Validaciones.nombre("   "))
    }

    @Test
    fun nombre_muyCorto_devuelveError() {
        assertEquals("Mínimo 3 caracteres", Validaciones.nombre("Al"))
    }

    @Test
    fun nombre_conNumeros_devuelveError() {
        assertEquals("Solo letras y espacios", Validaciones.nombre("Juan123"))
    }

    @Test
    fun nombre_conTildesYEnie_esValido() {
        assertNull(Validaciones.nombre("José Muñoz"))
    }

    // ---------- correo ----------
    @Test
    fun correo_valido_devuelveNull() {
        assertNull(Validaciones.correo("tio@tiotec.cl"))
    }

    @Test
    fun correo_sinArroba_devuelveError() {
        assertNotNull(Validaciones.correo("tiotec.cl"))
    }

    @Test
    fun correo_sinDominio_devuelveError() {
        assertNotNull(Validaciones.correo("tio@"))
    }

    // ---------- largo mínimo ----------
    @Test
    fun largoMinimo_textoCorto_devuelveError() {
        assertNotNull(Validaciones.largoMinimo("Ayuda", 20))
    }

    @Test
    fun largoMinimo_textoSuficiente_devuelveNull() {
        assertNull(Validaciones.largoMinimo("No puedo recuperar mi cuenta de correo", 20))
    }

    // ---------- versión ----------
    @Test
    fun version_numerica_esValida() {
        assertNull(Validaciones.versionSistema("17.2"))
    }

    @Test
    fun version_conLetras_devuelveError() {
        assertNotNull(Validaciones.versionSistema("catorce"))
    }

    // ---------- selección y casilla ----------
    @Test
    fun seleccion_nula_devuelveError() {
        assertEquals("Selecciona una opción", Validaciones.seleccion(null))
    }

    @Test
    fun aceptado_sinMarcar_devuelveMensaje() {
        assertEquals("Debes aceptar", Validaciones.aceptado(false, "Debes aceptar"))
    }
}
