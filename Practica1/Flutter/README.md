
Demo de UI en Flutter con NavigationBar
==========================================

DESCRIPCIÓN
-----------
Esta aplicación Flutter demuestra el uso de 1 pantalla principal
(MyHomePage) con un Scaffold que incluye una barra de navegación
inferior (NavigationBar) para cambiar entre 5 secciones (widgets).

Cada sección muestra diferentes elementos de interfaz de usuario
y cómo hacerlos interactivos.

------------------------------------------
ESTRUCTURA DE LA APP
------------------------------------------

MyHomePage:
- Contenedor principal con AppBar y NavigationBar.
- Controla la navegación entre pantallas.

Pantallas (Widgets):
1) TextFieldsPage
   - Ejemplos de TextField (nombre y correo).
   - Botón que valida y muestra la entrada en pantalla.
   - Breve explicación del uso de campos de texto.

<img width="359" height="779" alt="TextFields" src="https://github.com/user-attachments/assets/857f2311-39b0-47b2-8671-092acd9e664f" />


2) ButtonsPage
   - Demostración de FilledButton, TonalButton, OutlinedButton,
     IconButton y FloatingActionButton.
   - Cada botón lanza un Snackbar.
   - Long-press en el botón principal cambia automáticamente a la pantalla de Lista.
   - Breve explicación del uso de botones.

<img width="359" height="808" alt="Botones" src="https://github.com/user-attachments/assets/678bc6c1-191d-4da6-90dd-0f9be91d3c25" />

3) SelectionPage
   - Incluye CheckboxListTile, RadioListTile y SwitchListTile.
   - El estado seleccionado se muestra en un resumen.
   - Breve explicación de elementos de selección.


<img width="353" height="801" alt="Seleccion" src="https://github.com/user-attachments/assets/eca3e418-754a-45ba-bb2e-54ba86c78aaa" />


4) ListPage
   - Implementación de ListView.builder con Cards y ListTiles.
   - Al hacer clic en un elemento se muestra un Snackbar.
   - Explicación de las ventajas de usar listas.


<img width="354" height="800" alt="Lista" src="https://github.com/user-attachments/assets/b68184ee-4abd-48e9-b55a-3c76257e64b8" />



5) InfoPage
   - Contiene Text, Image, CircularProgressIndicator y LinearProgressIndicator.
   - Botón que simula una carga progresiva.
   - Breve explicación de elementos de información.
  

<img width="360" height="803" alt="Info" src="https://github.com/user-attachments/assets/9cabbdf3-3d20-42a4-a12e-9c7299272960" />

  

------------------------------------------
REQUISITOS TÉCNICOS
------------------------------------------
- Flutter 3.x
- Dart 3.x
- Estructura mínima:
  * lib/main.dart
  * pubspec.yaml

------------------------------------------
CÓMO USAR LA APP
------------------------------------------
1. Clonar el repositorio.
2. Ejecutar en terminal:
   flutter pub get
   flutter run
3. Usar el menú inferior (NavigationBar) para navegar entre pantallas.


------------------------------------------
AUTOR
------------------------------------------
Nombre: Escárcega Hernández Steven Arturo
Materia: Desarrollo de Aplicaciones Móviles
Fecha de entrega: 22 de septiembre de 2025
