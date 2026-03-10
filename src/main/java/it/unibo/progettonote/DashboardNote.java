package it.unibo.progettonote;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardNote {
    private String currentUser;
    private NotaService notaService;

    public DashboardNote(String currentUser) {
        this.currentUser = currentUser;
        this.notaService = new NotaService();
        initUI();
    }

    private void initUI() {
        JFrame frame = new JFrame("Dashboard Note");
        JButton btnModifica = new JButton("Modifica Nota");

        btnModifica.addActionListener(e -> {
            Nota nota = notaService.getNota("id1"); // esempio
            if (!nota.isModificabile(currentUser)) {
                JOptionPane.showMessageDialog(frame, "Non puoi modificare questa nota (solo lettura)");
                return;
            }
            nota.setContenuto("Nuovo contenuto");
            try {
                notaService.salvaNota(nota, currentUser);
                JOptionPane.showMessageDialog(frame, "Nota aggiornata!");
            } catch (IllegalAccessException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        frame.add(btnModifica);
        frame.setSize(300,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
