# 🌆 Cyberdream  
**Aplicación Android estilo Cyberpunk (Kotlin + Material3)**  

---

## 📖 Descripción
Cyberdream es una aplicación Android que ofrece una experiencia inmersiva ambientada en una ciudad ficticia con estética **cyberpunk**.  
El usuario puede explorar la **ciudad**, descubrir **barrios**, y adentrarse en los **edificios emblemáticos**, cada uno con información, mapas internos, galerías y puntos de interés (POIs).  

La app se construyó con una navegación jerárquica de tres niveles:  

1. **Ciudad** → Vista general de la metrópoli.  
2. **Barrios** → Exploración de zonas temáticas (ej. *Old Downtown*).  
3. **Edificios** → Lugares icónicos con detalle, mapas y galerías.  

---

## 🚀 Características principales
- **Jerarquía de navegación**:
  - `CiudadActivity`: Explora los barrios de la ciudad.  
  - `BarrioActivity`: Accede a la información del barrio y a sus ambientes.  
  - `EdificioActivity`: Consulta información detallada de cada edificio.  

- **Fragments organizados**:
  - Ciudad → Lista de barrios, noticias y mapa general.  
  - Barrio → Tabs para *Mapa*, *Edificios* y *Ambiente*.  
  - Edificio → Tabs para *Info*, *Galería* y *POIs*.  

- **Estilo Cyberpunk**:
  - Fondos dinámicos que cambian según la pantalla.  
  - Paleta neón (cyan, rosa, amarillo).  
  - Tipografía personalizada **Orbitron** para títulos y encabezados.  
  - Botones con gradientes y bordes redondeados estilo *holograma*.  

- **Animaciones personalizadas**:
  - Transiciones entre Activities (`slide`, `fade`).  
  - Animaciones de entrada/salida en Fragments.  
  - Efecto de **lluvia** en el fragmento de ambiente del barrio.  

- **POIs (Puntos de Interés)**:
  - Implementados con **RecyclerView + CardViews**.  
  - Cada tarjeta muestra:  
    - Nombre del punto.  
    - Descripción breve.  
    - Imagen representativa.  

- **Galerías de edificios**:
  - Cada edificio tiene 2 imágenes únicas (total: 6).  
  - Galería estilo *grid* con opción de ampliación fullscreen.  

---

## 📂 Estructura del proyecto
```
app/
 └─ src/main/java/com/example/cyberdream/
    ├─ comun/        # utilidades y helpers
    ├─ data/         # modelos de datos
    └─ ui/
       ├─ ciudad/    # CiudadActivity + Fragments de barrios y noticias
       ├─ barrio/    # BarrioActivity + Fragments de mapa, edificios, ambiente
       └─ edificio/  # EdificioActivity + Fragments de info, galería y POIs
```

En `res/` se organizan:  
- `drawable/` → fondos dinámicos, gradientes, botones y recursos gráficos.  
- `anim/` → animaciones XML (slide, fade, hold).  
- `font/` → tipografía Orbitron.  
- `layout/` → vistas XML para Activities, Fragments y ítems de listas.  

---

## 🛠️ Tecnologías usadas
- **Lenguaje:** Kotlin  
- **Frameworks/Libs:**  
  - AndroidX  
  - Material Design 3  
  - RecyclerView + GridLayoutManager  
- **Diseño:** ViewBinding + XML  
- **Estilos:** Orbitron font, gradientes neón, temas Material3 personalizados  

---

## 📸 Capturas de pantalla

> 🔽 Aquí irán tus imágenes exportadas desde el emulador.  

### 🌆 Ciudad
- [CAPTURA_CIUDAD_1]  
- [CAPTURA_CIUDAD_2]  

### 🏙️ Barrios
- [CAPTURA_BARRIO_1]  
- [CAPTURA_BARRIO_2]  

### 🏢 Edificios
- [CAPTURA_EDIFICIO_INFO]  
- [CAPTURA_EDIFICIO_POIS]  
- [CAPTURA_EDIFICIO_GALERIA]  

---

## 📦 Instalación
1. Clonar el repositorio:  
   ```bash
   git clone https://github.com/tuusuario/cyberdream.git
   ```
2. Abrir en **Android Studio**.  
3. Sincronizar dependencias de Gradle.  
4. Ejecutar en un dispositivo físico o emulador con Android 7.0+ (API 24).  

---

## ✍️ Decisiones de diseño
- Se usó **Material3** como base para aprovechar compatibilidad y personalización.  
- La fuente **Orbitron** refuerza la estética futurista.  
- Fondos dinámicos para diferenciar cada nivel jerárquico.  
- POIs implementados con **RecyclerView** para escalabilidad.  
- Galerías con GridLayout para un look organizado y minimalista.  

---

## ⚔️ Retos y Soluciones
- **Problema:** Errores con estilos personalizados (Material3).  
  - **Solución:** Volver al theme base por defecto y extenderlo con gradientes propios.  

- **Problema:** Navegación entre Activities/Fragments se confundía.  
  - **Solución:** Implementación de animaciones XML y control manual del backstack.  

- **Problema:** Recursos gráficos (fondos e íconos) no se enlazaban bien.  
  - **Solución:** Reorganización de carpetas `drawable/` y uso correcto de qualifiers (`nodpi`).  

- **Problema:** POIs iniciales eran texto plano.  
  - **Solución:** Migración a tarjetas con imagen + texto dentro de RecyclerView.  

---

## 👤 Autor
Desarrollado por **[Tu Nombre]**  
Aplicación académica estilo cyberpunk para la materia de **Aplicaciones Móviles**.  
