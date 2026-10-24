package cl.duoc.tiotec.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/*
 * Componentes reutilizables para TODOS los formularios de la app.
 * Regla de validación visual (pauta IE 2.1.2): cuando un campo tiene error se muestra
 * en rojo, con un ícono de error y el mensaje debajo, que aparece con una animación.
 * Los componentes no validan nada: solo muestran el error que les entrega el ViewModel.
 */

/** Campo de texto con ícono y mensaje de error. */
@Composable
fun CampoConError(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    error: String?,
    modifier: Modifier = Modifier,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    lineas: Int = 1,
    ayuda: String? = null
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = error != null,
        singleLine = lineas == 1,
        minLines = lineas,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        trailingIcon = {
            if (error != null) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        supportingText = {
            Column {
                MensajeError(error)
                if (error == null && ayuda != null) Text(ayuda)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

/** Lista de opciones donde se elige una sola (botones de radio). */
@Composable
fun SelectorOpciones(
    titulo: String,
    opciones: List<String>,
    seleccion: String?,
    onSeleccion: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            color = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        Column(Modifier.selectableGroup()) {
            opciones.forEach { opcion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = opcion == seleccion,
                            onClick = { onSeleccion(opcion) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = opcion == seleccion, onClick = null)
                    Spacer(Modifier.width(8.dp))
                    Text(opcion, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        MensajeError(error)
    }
}

/** Casilla que el usuario debe marcar, con su mensaje de error. */
@Composable
fun CasillaConError(
    texto: String,
    marcada: Boolean,
    onCambio: (Boolean) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(value = marcada, onValueChange = onCambio, role = Role.Checkbox)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = marcada, onCheckedChange = null)
            Spacer(Modifier.width(8.dp))
            Text(texto, style = MaterialTheme.typography.bodyLarge)
        }
        MensajeError(error)
    }
}

/** Mensaje de error con ícono que aparece y desaparece con animación. */
@Composable
fun MensajeError(error: String?) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
