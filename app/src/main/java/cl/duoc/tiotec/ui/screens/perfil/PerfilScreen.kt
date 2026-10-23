package cl.duoc.tiotec.ui.screens.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tiotec.ui.components.CampoConError
import cl.duoc.tiotec.ui.components.CasillaConError
import cl.duoc.tiotec.ui.components.SelectorOpciones
import cl.duoc.tiotec.viewmodel.PerfilViewModel

/**
 * Formulario 1: crear perfil.
 * La pantalla solo MUESTRA el estado del ViewModel y le avisa cada cambio;
 * no valida ni guarda nada por sí misma (MVVM).
 */
@Composable
fun PerfilScreen(
    alTerminar: () -> Unit,
    viewModel: PerfilViewModel = viewModel(factory = PerfilViewModel.Factory)
) {
    val ui by viewModel.ui.collectAsStateWithLifecycle()
    val perfil by viewModel.perfilGuardado.collectAsStateWithLifecycle()

    // Si ya existe un perfil (recién guardado o de antes), pasa directo a Inicio
    LaunchedEffect(perfil) {
        if (perfil != null) alTerminar()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Shield,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(56.dp)
        )
        Text("Bienvenido a TíoTec", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Crea tu perfil para empezar. Usa solo datos ficticios: la app no guarda información real.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoConError(
            valor = ui.nombre,
            onValorChange = viewModel::onNombreChange,
            etiqueta = "Nombre ficticio",
            error = ui.errorNombre
        )
        CampoConError(
            valor = ui.correo,
            onValorChange = viewModel::onCorreoChange,
            etiqueta = "Correo ficticio",
            error = ui.errorCorreo,
            tipoTeclado = KeyboardType.Email
        )
        SelectorOpciones(
            titulo = "Nivel de experiencia digital",
            opciones = PerfilViewModel.NIVELES,
            seleccion = ui.nivel,
            onSeleccion = viewModel::onNivelChange,
            error = null
        )
        CasillaConError(
            texto = "Confirmo que usaré solo datos ficticios",
            marcada = ui.datosFicticios,
            onCambio = viewModel::onDatosFicticiosChange,
            error = ui.errorFicticios
        )

        Button(
            onClick = viewModel::guardar,
            enabled = ui.esValido && !ui.guardando,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (ui.guardando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Crear perfil")
            }
        }
    }
}
