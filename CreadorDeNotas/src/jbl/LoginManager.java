package jbl;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.security.*;

public class LoginManager extends JFrame {
    // Componentes de la interfaz
    private JTextField txtEmail, header;
    private JPasswordField txtPassword;
    private JButton btnlogin;
    private JToggleButton btnRegister;
    private JLabel email, contraseña;
    // Archivo y directorio para almacenar usuarios
    private static final String USERS_FILE = "users.txt";
    private static final String USERS_DIR = "Usuarios/";

    public LoginManager() {
        // Configuración básica de la ventana
        setTitle("Inicio de Sesión");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    // Inicializa los componentes de la interfaz
    private void initComponents() {
        email = new javax.swing.JLabel();
        contraseña = new javax.swing.JLabel();
        header = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnlogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JToggleButton();
        txtEmail = new javax.swing.JTextField();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        // Configuración de etiquetas
        email.setText("Correo electrónico");
        contraseña.setText("Contraseña");
        
        // Configuración del encabezado
        header.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        header.setText("Ingrese las siguientes credenciales");
        header.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header.setEditable(false);

        // Configuración de botones
        btnlogin.setText("Iniciar sesión");
        btnlogin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnlogin.addActionListener(e -> iniciarSesion());

        btnRegister.setText("Registrarse");
        btnRegister.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegister.addActionListener(e -> registrarUsuario());

        // Configuración del layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(btnlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPassword)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );
    }

    // Maneja el proceso de inicio de sesión
    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (verificarCredenciales(email, password)) {
            SwingUtilities.invokeLater(() -> {
                this.dispose();
                CreadorNotas notas = new CreadorNotas(email);
                notas.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
        }
    }

    // Maneja el proceso de registro de nuevos usuarios
    private void registrarUsuario() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Validación de formato de email
        if (email.isEmpty() || !email.contains("@") || !email.contains(".com")) {
            JOptionPane.showMessageDialog(this, "Email inválido. El campo no puede estar vacío o incompleto.");
            return;
        }

        // Validación de fortaleza de contraseña
        if(password.isEmpty() || password.length() < 6){
            JOptionPane.showMessageDialog(this, "El campo no puede estar vacío o debe tener más de 6 carácteres.");
        }
        
        // Intento de registro
        if (guardarUsuario(email, password)) {
            JOptionPane.showMessageDialog(this, "Registro exitoso.");
        } else {
            JOptionPane.showMessageDialog(this, "Usuario ya registrado.");
        }
    }

    // Verifica si las credenciales coinciden con las almacenadas
    private boolean verificarCredenciales(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(email) && parts[1].equals(hashPassword(password))) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Guarda un nuevo usuario en el sistema
    private boolean guardarUsuario(String email, String password) {
        if (usuarioExiste(email)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(email + ":" + hashPassword(password));
            writer.newLine();
            new File(USERS_DIR + email).mkdirs(); // Crea directorio personal
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Comprueba si un usuario ya está registrado
    private boolean usuarioExiste(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(email + ":")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Aplica hash SHA-256 a la contraseña
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al cifrar la contraseña.");
        }
    }
}