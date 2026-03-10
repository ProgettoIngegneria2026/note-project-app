package it.unibo.progettonote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardNote {

    private JTable table;
    private DefaultTableModel model;
    private JLabel infoLabel;

    // Se NoteService e CartService non esistono, commentali o implementali nel tuo progetto
    private Object noteService; // placeholder
    private Object cartService; // placeholder

    // Costruttore senza argomenti
    public DashboardNote() {
        model = new DefaultTableModel(new Object[]{"ID", "Titolo", "Cartella", "Ultima Modifica"}, 0);
        table = new JTable(model);
        infoLabel = new JLabel();

        // placeholder per evitare errori di compilazione
        noteService = new Object();
        cartService = new Object();
    }

    // Costruttore con argomento string (opzionale se ti serve)
    public DashboardNote(String arg) {
        this(); // chiama il costruttore base
    }

    private void aggiornaTabella(List<?> note) {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Object obj : note) {
            // Placeholder: qui devi mappare gli oggetti Nota reali
            String id = "ID";
            String titolo = "Titolo";
            String nomeCartella = "Root";
            String dataUltimaModifica = sdf.format(new java.util.Date());

            model.addRow(new Object[]{id, titolo, nomeCartella, dataUltimaModifica});
        }

        infoLabel.setText("Risultati ricerca: " + note.size());
    }

    // Placeholder per ricerca
    public void ricercaNote(String keyword) {
        aggiornaTabella(List.of());
    }

    // Placeholder per creazione nuova nota
    private void creaNuovaNota() {
        JTextField titoloField = new JTextField();
        int result = JOptionPane.showConfirmDialog(null, titoloField, "Titolo nuova nota", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            aggiornaTabella(List.of());
        }
    }

    // Placeholder per spostamento nota
    private void spostaNota(String notaId, String idDest, String proprietario) {
        aggiornaTabella(List.of());
    }

    public void initGUI() {
        JFrame frame = new JFrame("Dashboard Note");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, "Center");
        frame.add(infoLabel, "South");

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        DashboardNote dashboard = new DashboardNote();
        dashboard.initGUI();
    }
}
