# 📁 Actividad 1 — Gestor de Archivos (Jetpack Compose)

## Descripción
Aplicación académica desarrollada en **Kotlin + Jetpack Compose** para la materia de **Aplicaciones Móviles**.  
Permite **explorar el almacenamiento** (vía SAF), abrir archivos de **texto** e **imágenes**, administrar elementos (crear carpeta, renombrar, eliminar, copiar/cortar/pegar) y **cambiar de tema** entre **Guinda (IPN)** y **Azul (ESCOM)** **en caliente** usando **DataStore**.

La interfaz está construida con **Material 3**, navegación con **Navigation Compose** y estado con **ViewModel**.


---

## Características principales
- 🗂️ **Explorador SAF**: selección de carpeta raíz y navegación por subdirectorios.
- 🍞 **Breadcrumb** clicable para saltar a cualquier nivel de la ruta.
- 📝 **Visor de texto** con scroll.
- 🖼️ **Visor de imágenes** (Coil).
- ⭐ **Favoritos** persistentes.
- ➕ **Crear carpeta**, ✏️ **Renombrar**, 🗑️ **Eliminar**.
- 📋 **Copiar / Cortar / Pegar** (mover emulado por copiar+eliminar).
- 🎨 **Temas dinámicos**: Guinda (IPN) y Azul (ESCOM), con adaptación al modo claro/oscuro del sistema.
- 🧭 **Navegación** declarativa con `NavHost`.
- 💾 **Persistencia** con DataStore (tema, árbol raíz, favoritos).
- ⚠️ Manejo básico de errores y restricciones de **Scoped Storage** (Android 10+).

> **Pendiente (roadmap):** búsqueda por nombre/tipo/fecha, miniaturas cacheadas, historial de recientes, previews de PDF, animaciones de transición y refinar mensajes de error.

---

## Requisitos
- **Android Studio Ladybug+** (o superior).
- **Gradle JDK 17** (Settings → Gradle → *Gradle JDK* = 17).
- **minSdk 24**, **target/compileSdk 34**.
- Dispositivo o emulador con Android 7.0+ (recomendado Android 10+ por Scoped Storage).

---

## Stack técnico
- **Kotlin** 1.9.24
- **Compose BOM** 2024.09.01 (Material 3 + UI)
- **Navigation Compose** 2.8.0
- **Lifecycle ViewModel Compose** 2.8.6
- **DataStore Preferences** 1.1.1
- **Coil Compose** 2.6.0
- **DocumentFile** 1.0.1

### Version Catalog (extracto sugerido de `libs.versions.toml`)
```toml
[versions]
agp = "8.5.2"
kotlin = "1.9.24"
composeBom = "2024.09.01"
navigationCompose = "2.8.0"
lifecycle = "2.8.6"
datastore = "1.1.1"
coil = "2.6.0"
documentfile = "1.0.1"
coreKtx = "1.13.1"
appcompat = "1.7.0"
material = "1.12.0"
activity = "1.9.2"
constraintlayout = "2.1.4"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"

[libraries]
compose-bom = { module = "androidx.compose:compose-bom", version.ref = "composeBom" }
compose-ui = { module = "androidx.compose.ui:ui" }
compose-material3 = { module = "androidx.compose.material3:material3" }
compose-icons-extended = { module = "androidx.compose.material:material-icons-extended" }
compose-ui-tooling = { module = "androidx.compose.ui:ui-tooling" }
compose-ui-tooling-preview = { module = "androidx.compose.ui:ui-tooling-preview" }
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "activity" }
navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigationCompose" }
lifecycle-runtime-ktx = { module = "androidx.lifecycle:lifecycle-runtime-ktx", version.ref = "lifecycle" }
lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycle" }
datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }
documentfile = { module = "androidx.documentfile:documentfile", version.ref = "documentfile" }
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coil" }
androidx-core-ktx = { module = "androidx.core:core-ktx", version.ref = "coreKtx" }
androidx-appcompat = { module = "androidx.appcompat:appcompat", version.ref = "appcompat" }
material = { module = "com.google.android.material:material", version.ref = "material" }
androidx-constraintlayout = { module = "androidx.constraintlayout:constraintlayout", version.ref = "constraintlayout" }
junit = { module = "junit:junit", version.ref = "junit" }
androidx-junit = { module = "androidx.test.ext:junit", version.ref = "junitVersion" }
androidx-espresso-core = { module = "androidx.test.espresso:espresso-core", version.ref = "espressoCore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

---

## Estructura del proyecto (paquetes)
```
com.example.actividad1
│
├─ MainActivity.kt
│
├─ data/
│   └─ datastore/
│      └─ PrefsRepository.kt
│
├─ domain/
│   ├─ model/    ← FsItem, FsType
│   └─ repo/     ← FileRepository, SafOps
│
├─ ui/
│   ├─ navigation/   ← AppNavGraph.kt + Destinations
│   ├─ components/   ← EmptyState, etc.
│   ├─ screens/
│   │   ├─ browser/      ← BrowserScreen, BrowserContent, FileItem, BrowserTopBar
│   │   ├─ textviewer/   ← TextViewerScreen
│   │   ├─ imageviewer/  ← ImageViewerScreen
│   │   └─ settings/     ← SettingsScreen
│   └─ theme/        ← Color.kt, Theme.kt, Typography.kt
│
└─ vm/
    ├─ BrowserViewModel.kt
    └─ TextViewerViewModel.kt
```

---

## Temas (Guinda / Azul) con recomposición
- El **tema** se lee de `PrefsRepository.themeFlow` y se aplica en `AppTheme(prefs)`.
- Al llamar `prefs.setTheme("guinda" | "azul")` desde `SettingsScreen`, **Compose recompone** la UI automáticamente.
- Esquemas de color definidos en `ui/theme/Color.kt`:
  - `LightGuindaScheme` / `DarkGuindaScheme`
  - `LightAzulScheme` / `DarkAzulScheme`

---

## Navegación
Rutas principales en `AppNavGraph`:
- `browser` → Explorador de archivos (pantalla principal).
- `textviewer?uri={uri}` → Visor de texto.
- `imageviewer?uri={uri}` → Visor de imágenes.
- `settings` → Configuración de tema.

---

## SAF (Storage Access Framework)
1. Al abrir por primera vez, toca **“Seleccionar carpeta”** para elegir el **árbol raíz**.
2. La app guarda el permiso con `takePersistableUriPermission` y lo **recuerda** en `DataStore`.
3. Desde el navegador:
   - Tap en carpeta → entra.
   - Tap en archivo de texto → se abre en el visor.
   - Tap en imagen → visor de imágenes.
   - Menú ⋮ del item → Copiar / Cortar / Renombrar / Eliminar.
   - FAB: **Nueva carpeta** o **Pegar/Mover aquí** si hay contenido en el portapapeles.

> **Nota:** Mover = Copiar + Eliminar (limitación habitual con SAF).

---

## Build & Run
1. Abre el proyecto en Android Studio.
2. Verifica:
   - `Gradle JDK = 17`
   - `compileSdk/targetSdk = 34`, `minSdk = 24`
3. **Sync Project** y ejecuta en emulador o dispositivo.
4. Si tuviste un fallo de compilador IR: alinea `kotlin = 1.9.24` y `kotlinCompilerExtensionVersion = 1.5.14`.

---

## Troubleshooting
- **“You need to use a Theme.Material3 theme…”**  
  Asegúrate de que `android:theme="@style/Theme.Actividad1"` exista y herede de `Theme.Material3.DayNight.NoActionBar`.
- **No carga el árbol raíz / `SecurityException`:**  
  Vuelve a seleccionar la carpeta → el sistema te pedirá permisos.  
- **No aparece “Pegar/Mover aquí”**:  
  Primero usa **Copiar** o **Cortar** en el menú ⋮ de un elemento para llenar el portapapeles del ViewModel.
- **Cambiar tema no surte efecto:**  
  Verifica que estés invocando `AppTheme(prefs = PrefsRepository(context))` en `MainActivity` y que `SettingsScreen` llame `prefs.setTheme("guinda" | "azul")`.

---

## Roadmap (pendiente por completar)
- 🔎 Búsqueda por nombre/tipo/fecha (con filtros).
- 🖼️ Miniaturas con caché para imágenes.
- 🧾 Historial de “recientes”.
- 📄 Previsualización PDF / JSON / XML.
- 🧼 Mejorar manejo de errores y snackbars con acciones (undo donde aplique).
- ✨ Animaciones en navegación y en cambios de tema.

---

## 👤 Autor
Desarrollado por **Escárcega Hernández Steven Arturo**  
Aplicación académica con estética **cyberpunk** para **Aplicaciones Móviles**.

---


