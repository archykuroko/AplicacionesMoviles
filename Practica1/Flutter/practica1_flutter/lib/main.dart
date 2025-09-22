import 'package:flutter/material.dart';

void main() {
  runApp(const UIDemoApp());
}

class UIDemoApp extends StatelessWidget {
  const UIDemoApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'UI Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF6750A4)),
        useMaterial3: true,
      ),
      home: const MyHomePage(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _index = 0;

  void _goToTab(int newIndex) {
    setState(() => _index = newIndex);
  }

  @override
  Widget build(BuildContext context) {
    final pages = <Widget>[
      const TextFieldsPage(),
      ButtonsPage(onGoToList: () => _goToTab(3)),
      const SelectionPage(),
      const ListPage(),
      const InfoPage(),
    ];

    final titles = [
      'TextFields',
      'Botones',
      'Selección',
      'Lista',
      'Información',
    ];

    return Scaffold(
      appBar: AppBar(title: Text(titles[_index]), centerTitle: true),
      body: pages[_index],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _index,
        onDestinationSelected: _goToTab,
        destinations: const [
          NavigationDestination(icon: Icon(Icons.edit), label: 'Text'),
          NavigationDestination(
            icon: Icon(Icons.smart_button),
            label: 'Botones',
          ),
          NavigationDestination(icon: Icon(Icons.tune), label: 'Selección'),
          NavigationDestination(icon: Icon(Icons.list), label: 'Lista'),
          NavigationDestination(icon: Icon(Icons.info), label: 'Info'),
        ],
      ),
    );
  }
}

// =============== 1) TEXTFIELDS ===============
class TextFieldsPage extends StatefulWidget {
  const TextFieldsPage({super.key});

  @override
  State<TextFieldsPage> createState() => _TextFieldsPageState();
}

class _TextFieldsPageState extends State<TextFieldsPage> {
  final _nameCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  String _output = 'Salida...';

  @override
  void dispose() {
    _nameCtrl.dispose();
    _emailCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: ListView(
        children: [
          const Text(
            'EditText (TextFields)',
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 8),
          const Text('Permiten capturar texto del usuario.'),
          const SizedBox(height: 16),
          TextField(
            controller: _nameCtrl,
            decoration: const InputDecoration(
              labelText: 'Nombre',
              border: OutlineInputBorder(),
            ),
          ),
          const SizedBox(height: 12),
          TextField(
            controller: _emailCtrl,
            keyboardType: TextInputType.emailAddress,
            decoration: const InputDecoration(
              labelText: 'Correo (ejemplo@dominio.com)',
              border: OutlineInputBorder(),
            ),
          ),
          const SizedBox(height: 16),
          OutlinedButton(
            onPressed: () {
              final name = _nameCtrl.text.trim();
              final email = _emailCtrl.text.trim();
              final emailOk = RegExp(r'^[^@]+@[^@]+\.[^@]+$').hasMatch(email);
              if (name.isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Escribe tu nombre')),
                );
              } else if (!emailOk) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Correo inválido')),
                );
              } else {
                setState(() => _output = 'Hola $name\nCorreo: $email');
              }
            },
            child: const Text('Mostrar'),
          ),
          const SizedBox(height: 8),
          Text(_output, style: const TextStyle(fontSize: 18)),
        ],
      ),
    );
  }
}

// =============== 2) BOTONES ===============
class ButtonsPage extends StatelessWidget {
  final VoidCallback onGoToList;
  const ButtonsPage({super.key, required this.onGoToList});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: ListView(
        children: [
          const Text(
            'Botones (Filled, Outlined, Icon, FAB)',
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 8),
          const Text('Disparan acciones: de texto, ícono o flotantes.'),
          const SizedBox(height: 16),
          FilledButton(
            onPressed: () => ScaffoldMessenger.of(
              context,
            ).showSnackBar(const SnackBar(content: Text('Filled presionado'))),
            onLongPress: onGoToList, // long press -> ir a Lista
            child: const Text('Acción primaria (long-press: ir a Lista)'),
          ),
          const SizedBox(height: 8),
          FilledButton.tonal(
            onPressed: () => ScaffoldMessenger.of(
              context,
            ).showSnackBar(const SnackBar(content: Text('Tonal presionado'))),
            child: const Text('Acción tonal'),
          ),
          const SizedBox(height: 8),
          OutlinedButton(
            onPressed: () => ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('Outlined presionado')),
            ),
            child: const Text('Acción alternativa'),
          ),
          const SizedBox(height: 16),
          IconButton(
            onPressed: () => ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('IconButton presionado')),
            ),
            icon: const Icon(Icons.camera_alt),
            iconSize: 32,
            tooltip: 'IconButton',
          ),
          const SizedBox(height: 24),
          Align(
            alignment: Alignment.centerRight,
            child: FloatingActionButton(
              onPressed: () => ScaffoldMessenger.of(
                context,
              ).showSnackBar(const SnackBar(content: Text('FAB presionado'))),
              child: const Icon(Icons.add),
            ),
          ),
        ],
      ),
    );
  }
}

// =============== 3) SELECCIÓN ===============
class SelectionPage extends StatefulWidget {
  const SelectionPage({super.key});

  @override
  State<SelectionPage> createState() => _SelectionPageState();
}

class _SelectionPageState extends State<SelectionPage> {
  bool chkA = false;
  bool chkB = false;
  bool sw = false;
  int? radio = 1;

  String get resumen =>
      'CheckBox: A=$chkA, B=$chkB | Radio: ${radio ?? "Ninguno"} | Switch: ${sw ? "ON" : "OFF"}';

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: ListView(
        children: [
          const Text(
            'Elementos de selección',
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 8),
          const Text(
            'Permiten elegir opciones: múltiples, únicas o activar/desactivar.',
          ),
          const SizedBox(height: 16),
          CheckboxListTile(
            value: chkA,
            title: const Text('Opción A'),
            onChanged: (v) => setState(() => chkA = v ?? false),
          ),
          CheckboxListTile(
            value: chkB,
            title: const Text('Opción B'),
            onChanged: (v) => setState(() => chkB = v ?? false),
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              Expanded(
                child: RadioListTile<int>(
                  value: 1,
                  groupValue: radio,
                  title: const Text('Radio 1'),
                  onChanged: (v) => setState(() => radio = v),
                ),
              ),
              Expanded(
                child: RadioListTile<int>(
                  value: 2,
                  groupValue: radio,
                  title: const Text('Radio 2'),
                  onChanged: (v) => setState(() => radio = v),
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          SwitchListTile(
            value: sw,
            title: const Text('Switch'),
            onChanged: (v) => setState(() => sw = v),
          ),
          const SizedBox(height: 12),
          Text(resumen, style: const TextStyle(fontStyle: FontStyle.italic)),
        ],
      ),
    );
  }
}

// =============== 4) LISTA ===============
class ListPage extends StatelessWidget {
  const ListPage({super.key});

  @override
  Widget build(BuildContext context) {
    final items = List.generate(12, (i) => 'Elemento ${i + 1}');
    return ListView.builder(
      padding: const EdgeInsets.all(8),
      itemCount: items.length,
      itemBuilder: (context, i) {
        return Card(
          margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 8),
          child: ListTile(
            title: Text(
              items[i],
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
            subtitle: Text('Descripción corta del elemento $i'),
            onTap: () => ScaffoldMessenger.of(
              context,
            ).showSnackBar(SnackBar(content: Text('Click: ${items[i]}'))),
          ),
        );
      },
    );
  }
}

// =============== 5) INFO ===============
class InfoPage extends StatefulWidget {
  const InfoPage({super.key});

  @override
  State<InfoPage> createState() => _InfoPageState();
}

class _InfoPageState extends State<InfoPage> {
  double progress = 0;

  void _simulateLoad() async {
    setState(() => progress = 0);
    while (progress < 1) {
      await Future.delayed(const Duration(milliseconds: 120));
      setState(() => progress = (progress + 0.05).clamp(0, 1));
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        const Text(
          'Elementos de información',
          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 8),
        const Text('Textos, imágenes y progreso comunican estado al usuario.'),
        const SizedBox(height: 16),
        ClipRRect(
          borderRadius: BorderRadius.circular(12),
          child: Image.network(
            'https://picsum.photos/800/300',
            height: 180,
            width: double.infinity,
            fit: BoxFit.cover,
          ),
        ),
        const SizedBox(height: 16),
        const Center(child: CircularProgressIndicator()), // indeterminada
        const SizedBox(height: 16),
        LinearProgressIndicator(value: progress == 0 ? null : progress),
        const SizedBox(height: 8),
        Text('Cargando: ${(progress * 100).toInt()}%'),
        const SizedBox(height: 12),
        OutlinedButton(
          onPressed: _simulateLoad,
          child: const Text('Iniciar carga demo'),
        ),
      ],
    );
  }
}
