
Demo de UI con Activities y Fragments
==========================================

DESCRIPCIÓN
-----------
Esta aplicación Android, desarrollada en Kotlin, demuestra el uso de 
1 Activity principal y 5 Fragments, cada uno dedicado a diferentes 
elementos de interfaz de usuario. 

Se utiliza un menú de navegación inferior (BottomNavigationView) 
para cambiar entre fragments de manera sencilla.

------------------------------------------
ESTRUCTURA DE LA APP
------------------------------------------

MainActivity:
- Contenedor principal con Toolbar y BottomNavigationView.
- Controla la navegación entre fragments.

Fragments:
1) TextFieldsFragment
   - Ejemplos de EditText y TextInputLayout (nombre y correo).
   - Botón que valida y muestra la entrada en pantalla.
   - Breve explicación de para qué sirven los campos de texto.
  
 <img width="360" height="780" alt="TextFields" src="https://github.com/user-attachments/assets/5d336952-e970-4362-9ace-05b494a47733" />


2) ButtonsFragment
   - Demostración de MaterialButton (filled, outlined), ImageButton y FloatingActionButton.
   - Cada botón lanza un mensaje (Toast o Snackbar).
   - Mantener presionado el botón principal cambia automáticamente al Fragment de Lista.
   - Breve explicación del uso de botones.


<img width="360" height="780" alt="Botones" src="https://github.com/user-attachments/assets/9c42df96-2737-43ab-ae3c-9250f4c62359" />


3) SelectionFragment
   - Incluye CheckBox, RadioButton y Switch.
   - El estado seleccionado se resume en un TextView.
   - Breve explicación del uso de elementos de selección.

<img width="360" height="780" alt="Seleccion" src="https://github.com/user-attachments/assets/5f2a93a8-edb4-46b5-8ff7-4cd0e2200bb0" />



4) ListFragment
   - Implementación de un RecyclerView con ítems en tarjetas.

<img width="360" height="780" alt="Listas" src="https://github.com/user-attachments/assets/882db36b-93e9-4808-bc81-b2d92cd0cecf" />



5) InfoFragment
   - Contiene TextView, ImageView y ProgressBar (indeterminada y determinada).
   - Botón que simula una carga progresiva.


<img width="360" height="780" alt="Info" src="https://github.com/user-attachments/assets/6a9d088e-324a-42b4-bb4f-e8890c680864" />


------------------------------------------
REQUISITOS TÉCNICOS
------------------------------------------
- Android Studio Giraffe o superior
- Kotlin habilitado
- minSdk 21
- compileSdk 34
- Dependencias principales:
  * com.google.android.material:material:1.12.0
  * androidx.recyclerview:recyclerview:1.3.2
  * androidx.fragment:fragment-ktx:1.8.2

------------------------------------------
CÓMO USAR LA APP
------------------------------------------
1. Abrir el proyecto en Android Studio.
2. Sincronizar dependencias (Gradle Sync).
3. Ejecutar en emulador o dispositivo físico.
4. Usar el menú inferior para navegar entre fragments.


------------------------------------------
AUTOR
------------------------------------------
Nombre: Escárcega Hernández Steven Arturo
Materia: Desarrollo de Aplicaciones Móviles
Fecha de entrega: 3 de septiembre de 2025
