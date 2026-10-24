# TíoTec Preventivo

App Android para evaluar y mejorar la seguridad digital de personas y pequeños negocios (Caso 005V).
El usuario responde un cuestionario, obtiene su nivel de riesgo y un plan de acciones con fechas para mejorar.

**Asignatura:** DSY1105 Desarrollo de Aplicaciones Móviles · Evaluación Parcial 2 y 3 · Duoc UC

## Integrantes

| Nombre | GitHub |
|---|---|
| Matías Echeverría | [rukwa](https://github.com/rukwa) |
| Jorge Zárate | [jorge-zarate](https://github.com/jorge-zarate) |

## Enlaces

- Repositorio: https://github.com/rukwa/TioTecPreventivo
- Tablero Trello: https://trello.com/b/rZJQsJGn/tiotec-preventivo-dsy1105

## Funcionalidades

- [x] Crear perfil (nombre, correo y nivel de experiencia digital, solo con datos ficticios) con validaciones
- [x] Evaluación de seguridad digital con banco de preguntas por área
- [x] Resultado con puntaje, nivel de riesgo (bajo, medio, alto) y detalle por área
- [x] Plan de acciones generado según las respuestas, con prioridad, fecha límite y filtros
- [x] Historial de evaluaciones (ver resultado y eliminar)
- [x] Registro de dispositivos con foto desde cámara o galería
- [x] Sección Aprende con contenidos educativos por área
- [x] Solicitud de asistencia técnica con formulario validado
- [x] Recordatorio de revisión con notificación

## Tecnologías

- Kotlin + Jetpack Compose + Material 3
- Arquitectura MVVM (ViewModel + StateFlow)
- Room para guardar los datos en el teléfono (5 tablas: perfil, dispositivos, solicitudes, evaluaciones y acciones)
- Navigation Compose con barra inferior y ruta con argumento (`resultado/{evaluacionId}`)
- WorkManager para programar el recordatorio
- Pruebas unitarias con JUnit

## Estructura del proyecto

```
cl.duoc.tiotec
├── data
│   ├── local        Entidades, DAOs y AppDatabase (Room)
│   ├── model        Modelos y banco de preguntas
│   └── repository   Repositorios (puente entre ViewModel y Room)
├── ui
│   ├── components   Componentes reutilizables (campos con error, foto, etiquetas)
│   ├── navigation   Rutas y NavHost
│   ├── screens      Una carpeta por pantalla
│   └── theme        Colores, tipografía y tema
├── utils            Validaciones, cálculo de riesgo, acciones, fechas, notificaciones
├── viewmodel        Un ViewModel por pantalla
└── TioTecApp.kt     Crea la base de datos y los repositorios
```

## Recursos nativos

- **Cámara:** foto del dispositivo con `TakePicture` y `FileProvider`, pidiendo el permiso de cámara.
- **Galería:** selector de fotos del sistema (`PickVisualMedia`), no necesita permiso.
- **Notificaciones:** canal propio, permiso `POST_NOTIFICATIONS` (Android 13+) y aviso programado con WorkManager.

## Validaciones y animaciones

- Validaciones centralizadas en `utils/Validaciones.kt`; cada campo muestra su error con ícono y mensaje.
- Animaciones: aparición de errores (`AnimatedVisibility`), cambio de pregunta (`AnimatedContent`), puntaje y barras animadas, transición entre pantallas y animación de listas.

## Pruebas unitarias

En `app/src/test`:

- `ValidacionesTest`
- `BancoPreguntasTest`
- `CalculadoraRiesgoTest`

Para ejecutarlas: clic derecho en la carpeta `test` → **Run 'Tests in ...'**.

## Cómo ejecutar

1. Clonar el repositorio y abrirlo en Android Studio.
2. Esperar la sincronización de Gradle.
3. Elegir un emulador o teléfono (Android 8.0 o superior) y presionar **Run ▶**.

## Flujo de trabajo

- `main`: versión final entregada.
- `develop`: integración de las funcionalidades.
- `feature/*`: una rama por funcionalidad, integrada a `develop` mediante Pull Request revisado por el compañero.
