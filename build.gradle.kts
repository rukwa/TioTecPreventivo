// Archivo de la RAÍZ del proyecto: solo declara los plugins (apply false).
// La configuración de la app (android { }, dependencias) va en app/build.gradle.kts.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
