package cl.duoc.tiotec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.tiotec.ui.navigation.AppNavigation
import cl.duoc.tiotec.ui.theme.TioTecPreventivoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TioTecPreventivoTheme {
                AppNavigation()
            }
        }
    }
}
