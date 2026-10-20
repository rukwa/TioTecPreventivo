# TíoTec Preventivo

Aplicación móvil Android que ayuda a personas no especialistas a evaluar sus hábitos básicos de seguridad digital, identificar riesgos y mantener un plan preventivo de acciones con seguimiento.

> MVP académico. Toda la información que se registra en la app (perfiles, dispositivos y solicitudes) es **ficticia**. La app no almacena incidentes reales ni datos sensibles.

## Contexto del caso

**MCmovil SpA** es una pyme de soporte técnico, reparación, optimización y capacitación TI que impulsa **TíoTec** como iniciativa de prevención y educación tecnológica.

Hoy las personas buscan orientación en videos, redes sociales o recomendaciones informales, pero no tienen un proceso único y periódico para revisar medidas básicas como respaldos, actualizaciones, autenticación reforzada y recuperación de cuentas. Por eso, la prevención suele llegar recién después de un incidente.

TíoTec Preventivo ofrece un proceso simple y guiado para:

- evaluar hábitos de seguridad por área,
- detectar brechas y priorizar acciones,
- dar seguimiento a lo pendiente y recordar cuándo volver a evaluar.

## Integrantes

| Nombre | GitHub |
| --- | --- |
| Matías Echeverría | [@rukwa](https://github.com/rukwa) |
| Jorge Zárate | [@jorge-zarate](https://github.com/jorge-zarate) |

**Asignatura:** DSY1105 · Desarrollo de Aplicaciones Móviles · Duoc UC
**Evaluaciones:** Parcial 2 (encargo) y Parcial 3 (defensa técnica)

## Funcionalidades

- [ ] **Crear perfil:** nombre y correo ficticios y nivel de experiencia digital, con validación por campo.
- [ ] **Inicio:** último puntaje, acciones pendientes y próxima fecha de revisión.
- [ ] **Mis dispositivos:** registrar, editar y eliminar dispositivos de prueba (tipo, sistema operativo y versión), con foto desde la cámara o la galería.
- [ ] **Evaluación preventiva:** 12 preguntas en 4 áreas (respaldos, actualizaciones, autenticación y recuperación de cuentas).
- [ ] **Resultado:** puntaje de 0 a 100 y nivel de riesgo por área, con recomendaciones.
- [ ] **Plan de acciones:** checklist priorizado (Alta, Media, Baja) que permite marcar cada acción como pendiente o completada.
- [ ] **Historial:** evaluaciones anteriores con fecha, dispositivo y puntaje.
- [ ] **Aprende:** contenidos educativos breves por área.
- [ ] **Solicitud de asistencia:** formulario con categoría y estado simulado.
- [ ] **Recordatorios:** notificación cuando llega la fecha de revisión.

## Tecnologías

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Arquitectura MVVM (ViewModel + StateFlow)
- Room (SQLite) para la persistencia local
- Navigation Compose
- WorkManager para las notificaciones programadas
- Coil para la carga de imágenes

## Arquitectura

El proyecto aplica el patrón **MVVM**:

- **Screens y Composables** (`ui/`) muestran el estado y envían los eventos del usuario.
- **ViewModels** (`viewmodel/`) guardan el estado de cada pantalla con `StateFlow` y aplican la lógica.
- **Repositories** (`data/repository/`) son la única puerta de acceso a los datos.
- **Room** (`data/local/`) guarda la información en SQLite.
- **Utilidades** (`utils/`) contienen la lógica reutilizable: validaciones de formularios y cálculo de riesgo.

```text
cl.duoc.tiotec
├── data
│   ├── local        → AppDatabase, entidades y DAOs
│   ├── model        → Pregunta, Area, Recomendacion, Contenido
│   └── repository   → acceso a datos por entidad
├── ui
│   ├── screens      → una carpeta por pantalla
│   ├── components   → componentes reutilizables
│   ├── navigation   → rutas y NavHost
│   └── theme        → colores, tipografía y tema Material 3
├── viewmodel        → un ViewModel por pantalla o flujo
├── utils            → Validaciones, CalculadoraRiesgo, Notificaciones
└── MainActivity.kt
```

## Recursos nativos y permisos

| Recurso | Uso en la app | Permiso |
| --- | --- | --- |
| Cámara | Foto del dispositivo de prueba | `CAMERA`, solicitado en tiempo de ejecución |
| Galería | Elegir una foto existente | No requiere permiso (Photo Picker) |
| Notificaciones | Recordatorio de revisión preventiva | `POST_NOTIFICATIONS`, en Android 13 o superior |

## Requisitos

- Android Studio (versión estable reciente)
- JDK 17, incluido con Android Studio
- Emulador o dispositivo físico con Android 7.0 (API 24) o superior

## Cómo ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/rukwa/TioTecPreventivo.git
   ```
   También se puede descargar como .zip desde **Code → Download ZIP** y descomprimirlo.
2. Abrir Android Studio y seleccionar **Open**. Elegir la carpeta `TioTecPreventivo`.
3. Esperar a que termine el **Gradle Sync**. La primera vez puede tardar varios minutos.
4. Crear o elegir un emulador en **Device Manager**. Se recomienda API 34 o superior.
5. Presionar **Run ▶**.
6. Si se prefiere instalar sin compilar, se puede descargar el APK desde la sección **Releases** y arrastrarlo al emulador.

## Gestión del proyecto

- **Planificación (Trello):** [enlace al tablero](PEGAR_AQUI_EL_LINK_DE_TRELLO)
- **Ramas:**
  - `main`: versión estable entregada.
  - `develop`: integración del trabajo.
  - `feature/<tarea>`: una rama por tarea.
- **Commits:** siguen la convención `tipo(módulo): descripción`, con los tipos `feat`, `fix`, `refactor`, `test`, `docs`, `style` y `chore`.
- **Issues:** cada tarea tiene un issue que se cierra con su Pull Request, revisado por el otro integrante.

## Alcance

Esta versión corresponde al avance de las Evaluaciones Parciales 2 y 3. Quedan para la Evaluación Final Transversal:

- el consumo de una API REST de prueba con Retrofit,
- los roles de administrador de contenidos y de supervisor técnico.
