package it.unibo.progettonote;

import javax.swing.*;
import java.awt.*;

public class InterfacciaRegistrazione {
    public static void mostra() {
        JFrame frame = new JFrame("NoteApp - Accesso");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- PANNELLO LOGIN ---
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JTextField logEmail = new JTextField();
        JPasswordField logPass = new JPasswordField();
        JButton btnLogin = new JButton("Accedi");

        loginPanel.add(new JLabel("Email:")); loginPanel.add(logEmail);
        loginPanel.add(new JLabel("Password:")); loginPanel.add(logPass);
        loginPanel.add(new JLabel("")); loginPanel.add(btnLogin);

        // --- PANNELLO REGISTRAZIONE ---
        JPanel regPanel = new JPanel(new GridLayout(4, 2));
        JTextField regUser = new JTextField();
        JTextField regEmail = new JTextField();
        JPasswordField regPass = new JPasswordField();
        JButton btnReg = new JButton("Registrati");

        regPanel.add(new JLabel("Username:")); regPanel.add(regUser);
        regPanel.add(new JLabel("Email:")); regPanel.add(regEmail);
        regPanel.add(new JLabel("Password:")); regPanel.add(regPass);
        regPanel.add(new JLabel("")); regPanel.add(btnReg);

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Registrazione", regPanel);

        // LOGICA BOTTONI
        GestioneUtenti gu = new GestioneUtenti();

        btnLogin.addActionListener(e -> {
            String emailIn = logEmail.getText().trim();
            String passIn = new String(logPass.getPassword());

            if (gu.login(emailIn, passIn)) {
                // 1. Chiudiamo la finestra attuale (opzionale, ma consigliato)
                frame.dispose();

                // 2. Lanciamo la Dashboard passando l'email dell'utente
                // Il costruttore di DashboardNote si occupa di tutto il resto
                new DashboardNote(emailIn);

                System.out.println("DEBUG: DashboardNote avviata per " + emailIn);
            } else {
                JOptionPane.showMessageDialog(frame, "Credenziali errate (controlla la console!)");
            }
        });

        btnReg.addActionListener(e -> {
            boolean ok = gu.registraNuovoUtente(regUser.getText(), regEmail.getText(), new String(regPass.getPassword()));
            if(ok) JOptionPane.showMessageDialog(frame, "Registrato! Ora puoi fare il login.");
            else JOptionPane.showMessageDialog(frame, "Errore registrazione.");
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}