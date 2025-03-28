package jbl;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.security.*;

public class LoginManager extends JFrame {
   
    private JTextField txtEmail, txtNombre;
    private JPasswordField txtPassword;
    private static final String USERS_FILE = "users.txt";
    

    public LoginManager() {
        setTitle("Inicio de Sesión");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Componentes (igual que antes)
        txtNombre = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegister = new JButton("Registrarse");

        // Listeners
        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegister.addActionListener(e -> registrarUsuario());

        // Panel y layout
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);
        add(panel);

        setVisible(true);
    }

    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (verificarCredenciales(email, password)) {
            dispose();
            new CreadorNotas(email).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
        }
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || !email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
            return;
        }

        if (guardarUsuario(email, password)) {
            JOptionPane.showMessageDialog(this, "Registro exitoso.");
        } else {
            JOptionPane.showMessageDialog(this, "Usuario ya registrado.");
        }
    }

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

    private boolean guardarUsuario(String email, String password) {
        if (usuarioExiste(email)) return false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(email + ":" + hashPassword(password));
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public static void main(String[] args) {
        new LoginManager();
    }
}