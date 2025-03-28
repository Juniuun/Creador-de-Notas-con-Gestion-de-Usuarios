package jbl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class CreadorNotas extends JFrame {
    // Componentes de la interfaz
    private JTextField txtTitulo;
    private JTextArea txtContenido;
    private JTextField txtBuscar;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar, btnBuscar;
    private JList<String> listaNotas;
    private DefaultListModel<String> modeloNotas;
    private String emailUsuario;
    private String directorioUsuario;

    public CreadorNotas(String email) {
        this.emailUsuario = email;
        this.directorioUsuario = "Usuarios/" + email;
        
        // Configuración básica de la ventana
        setTitle("Creador de Notas - " + email);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        // Crear carpeta si no existe
        new File(directorioUsuario).mkdirs();

        // Inicialización de componentes
        txtTitulo = new JTextField(20);
        txtContenido = new JTextArea();
        txtBuscar = new JTextField(15);
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnBuscar = new JButton("Buscar");
        
        // Configuración del modelo de lista
        modeloNotas = new DefaultListModel<>();
        listaNotas = new JList<>(modeloNotas);
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configuración de ScrollPanes
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        JScrollPane scrollLista = new JScrollPane(listaNotas);
        scrollLista.setPreferredSize(new Dimension(200, 400));
        scrollContenido.setPreferredSize(new Dimension(400, 400));
        
        // Configuración del panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configuración del panel izquierdo (lista de notas)
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Tus notas"));
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);
        
        // Configuración del panel de búsqueda
        JPanel panelBuscar = new JPanel(new BorderLayout(5, 5));
        panelBuscar.add(new JLabel("Buscar:"), BorderLayout.WEST);
        panelBuscar.add(txtBuscar, BorderLayout.CENTER);
        panelBuscar.add(btnBuscar, BorderLayout.EAST);
        panelIzquierdo.add(panelBuscar, BorderLayout.SOUTH);

        // Configuración del panel derecho (editor)
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        
        // Configuración del panel del título
        JPanel panelTitulo = new JPanel(new BorderLayout(5, 5));
        panelTitulo.add(new JLabel("Título:"), BorderLayout.WEST);
        panelTitulo.add(txtTitulo, BorderLayout.CENTER);
        panelDerecho.add(panelTitulo, BorderLayout.NORTH);
        
        // Configuración del panel del contenido
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBorder(BorderFactory.createTitledBorder("Contenido"));
        panelContenido.add(scrollContenido, BorderLayout.CENTER);
        panelDerecho.add(panelContenido, BorderLayout.CENTER);
        
        // Configuración del panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelDerecho.add(panelBotones, BorderLayout.SOUTH);

        // Ensamblado final de la interfaz
        panelPrincipal.add(panelIzquierdo, BorderLayout.WEST);
        panelPrincipal.add(panelDerecho, BorderLayout.CENTER);

        // Asignación de listeners a los botones
        btnGuardar.addActionListener(e -> guardarNota());
        btnEditar.addActionListener(e -> editarNota());
        btnEliminar.addActionListener(e -> eliminarNota());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> buscarNota());
        
        // Listener para doble clic en la lista
        listaNotas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cargarNota();
                }
            }
        });
        
        add(panelPrincipal);
        cargarNotasUsuario(); // Carga las notas al iniciar
    }

    // Guarda una nueva nota en el directorio del usuario
    private void guardarNota() {
        String titulo = txtTitulo.getText().trim();
        String contenido = txtContenido.getText().trim();
        if (!titulo.isEmpty() && !contenido.isEmpty()) {
            try (FileWriter writer = new FileWriter(directorioUsuario + "/" + titulo + ".txt")) {
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
    
    // Edita una nota existente
    private void editarNota() {
        String tituloOriginal = listaNotas.getSelectedValue();
        String tituloNuevo = txtTitulo.getText().trim();
        String contenido = txtContenido.getText().trim();

        if (tituloOriginal != null && !contenido.isEmpty()) {
            File notaOriginal = new File(directorioUsuario + "/" + tituloOriginal + ".txt");
            File notaNueva = new File(directorioUsuario + "/" + tituloNuevo + ".txt");

            if (notaOriginal.exists()) {
                try {
                    // Renombra el archivo si el título cambió
                    if (!tituloOriginal.equals(tituloNuevo)) {
                        notaOriginal.renameTo(notaNueva);
                    }
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

    // Carga todas las notas del usuario
    private void cargarNotasUsuario() {
        modeloNotas.clear();
        File dir = new File(directorioUsuario);
        File[] archivos = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (archivos != null) {
            for (File archivo : archivos) {
                modeloNotas.addElement(archivo.getName().replace(".txt", ""));
            }
        }
    }

    // Elimina la nota seleccionada
    private void eliminarNota() {
        String titulo = listaNotas.getSelectedValue();
        if (titulo != null) {
            File nota = new File(directorioUsuario + "/" + titulo + ".txt");
            if (nota.delete()) {
                modeloNotas.removeElement(titulo);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la nota.");
            }
        }
    }

    // Limpia los campos de edición
    private void limpiarCampos() {
        txtTitulo.setText("");
        txtContenido.setText("");
    }

    // Carga el contenido de la nota seleccionada
    private void cargarNota() {
        String titulo = listaNotas.getSelectedValue();
        if (titulo != null) {
            File nota = new File(directorioUsuario + "/" + titulo + ".txt");
            try {
                String contenido = new String(Files.readAllBytes(nota.toPath()));
                txtTitulo.setText(titulo);
                txtContenido.setText(contenido);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la nota.");
            }
        }
    }
    
    // Busca notas que contengan el término de búsqueda
    private void buscarNota() {
        String termino = txtBuscar.getText().trim().toLowerCase();
        modeloNotas.clear();
        File dir = new File(directorioUsuario);
        File[] archivos = dir.listFiles((d, name) -> name.toLowerCase().contains(termino));
        if (archivos != null) {
            for (File archivo : archivos) {
                modeloNotas.addElement(archivo.getName().replace(".txt", ""));
            }
        }
    }
}
