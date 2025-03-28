package jbl;

// Imports necesarios para la aplicación
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

// Clase CreadorNotas que extiende JFrame para crear una aplicación de escritorio
public class CreadorNotas extends JFrame {
    private JTextField txtTitulo;
    private JTextArea txtContenido;
    private JTextField txtBuscar;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar, btnBuscar;
    private JList<String> listaNotas;
    private DefaultListModel<String> modeloNotas;
    private final String directorioNotas = "Notas"; // Carpeta global
    private String emailUsuario; // Email del usuario actual

    public CreadorNotas(String email) {
        this.emailUsuario = email;
        setTitle("Creador de Notas - " + email);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Crear carpeta global si no existe
        new File(directorioNotas).mkdirs();

        // Inicializar componentes (igual que antes)
        txtTitulo = new JTextField();
        txtContenido = new JTextArea(5, 20);
        txtBuscar = new JTextField(15);
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnBuscar = new JButton("Buscar");
        modeloNotas = new DefaultListModel<>();
        listaNotas = new JList<>(modeloNotas);
        cargarNotasUsuario();

        // Crear scroll panes para el área de contenido y la lista de notas
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        JScrollPane scrollLista = new JScrollPane(listaNotas);

        // Etiquetas para los campos de texto
        JLabel lblTitulo = new JLabel("Título:");
        JLabel lblContenido = new JLabel("Contenido:");
        JLabel lblBuscar = new JLabel("Buscar:");

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem salirItem = new JMenuItem("Salir");
        salirItem.addActionListener(e -> System.exit(0)); // Acción para salir de la aplicación
        archivoMenu.add(salirItem);
        menuBar.add(archivoMenu);
        setJMenuBar(menuBar);

        // Configurar el layout de la ventana
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Configuración horizontal del layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblTitulo)
                        .addComponent(lblContenido)
                        .addComponent(scrollLista))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtTitulo)
                        .addComponent(scrollContenido)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblBuscar)
                            .addComponent(txtBuscar)
                            .addComponent(btnBuscar))))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnLimpiar)
                    .addComponent(btnGuardar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar))
        );

        // Configuración vertical del layout
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitulo)
                    .addComponent(txtTitulo))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContenido)
                    .addComponent(scrollContenido))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(scrollLista)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBuscar)
                            .addComponent(txtBuscar)
                            .addComponent(btnBuscar))))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpiar)
                    .addComponent(btnGuardar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar))
        );

        // Añadir listeners a los botones
        btnGuardar.addActionListener(e -> guardarNota());
        btnEditar.addActionListener(e -> editarNota());
        btnEliminar.addActionListener(e -> eliminarNota());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> buscarNota());
        listaNotas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cargarNota();
                }
            }
        });

        pack(); // Ajustar el tamaño de la ventana
    }

    // Método para guardar una nota
    private void guardarNota() {
        String titulo = txtTitulo.getText().trim(); // Prefijo único
        String contenido = txtContenido.getText().trim();
        if (!titulo.isEmpty() && !contenido.isEmpty()) {
            try (FileWriter writer = new FileWriter(directorioNotas + "/" + titulo + ".txt")) {
                writer.write(contenido);
                modeloNotas.addElement(titulo);
                limpiarCampos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la nota.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
        }
    }
    
    private void editarNota() {
    String tituloOriginal = listaNotas.getSelectedValue(); // Título con prefijo
    String tituloNuevo = txtTitulo.getText().trim();
    String contenido = txtContenido.getText().trim();

    if (tituloOriginal != null && !contenido.isEmpty()) {
        File notaOriginal = new File(directorioNotas + "/" + tituloOriginal + ".txt");
        File notaNueva = new File(directorioNotas + "/" + tituloNuevo + ".txt");

        if (notaOriginal.exists()) {
            try {
                // Renombrar archivo si el título cambió
                if (!tituloOriginal.equals(tituloNuevo)) {
                    notaOriginal.renameTo(notaNueva);
                }
                // Escribir contenido
                try (FileWriter writer = new FileWriter(notaNueva)) {
                    writer.write(contenido);
                    modeloNotas.removeElement(tituloOriginal);
                    modeloNotas.addElement(tituloNuevo);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al editar la nota.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "La nota no existe.");
        }
    }
}

    // Método para editar una nota existente
    private void cargarNotasUsuario() {
        modeloNotas.clear();
        File dir = new File(directorioNotas);
        File[] archivos = dir.listFiles((d, name) -> 
            name.startsWith("[Usuario_" + emailUsuario + "]"));
        if (archivos != null) {
            for (File archivo : archivos) {
                modeloNotas.addElement(archivo.getName().replace(".txt", ""));
            }
        }
    }

    // Método para eliminar una nota
    private void eliminarNota() {
    String titulo = listaNotas.getSelectedValue(); // Ya incluye el prefijo
    if (titulo != null) {
        File nota = new File(directorioNotas + "/" + titulo + ".txt");
        if (nota.delete()) {
            modeloNotas.removeElement(titulo);
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar la nota.");
        }
    }
}

    // Método para limpiar los campos de texto
    private void limpiarCampos() {
        txtTitulo.setText("");
        txtContenido.setText("");
    }

    // Método para cargar una nota seleccionada
    private void cargarNota() {
    String titulo = listaNotas.getSelectedValue(); // Con prefijo
    if (titulo != null) {
        File nota = new File(directorioNotas + "/" + titulo + ".txt");
        try {
            String contenido = new String(Files.readAllBytes(nota.toPath()));
            // Mostrar solo el título sin prefijo en el JTextField
            txtTitulo.setText(titulo.replace("[Usuario_" + emailUsuario + "] ", ""));
            txtContenido.setText(contenido);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la nota.");
        }
    }
}
    
    // Método para buscar notas por título
    private void buscarNota() {
        String termino = txtBuscar.getText().trim().toLowerCase();
        modeloNotas.clear();
        File dir = new File(directorioNotas);
        File[] archivos = dir.listFiles((d, name) -> name.toLowerCase().contains(termino));
        if (archivos != null) {
            for (File archivo : archivos) {
                modeloNotas.addElement(archivo.getName().replace(".txt", ""));
            }
        }
    }
}

