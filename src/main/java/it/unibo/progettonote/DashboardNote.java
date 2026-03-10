package it.unibo.progettonote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardNote {

    private JTable table;
    private DefaultTableModel model;
    private JLabel infoLabel;

    private NoteService noteService;
    private CartService cartService;

    public DashboardNote() {
        // inizializzazione componenti GUI
        model = new DefaultTableModel(new Object[]{"ID", "Titolo", "Cartella", "Ultima Modifica"}, 0);
        table = new JTable(model);
        infoLabel = new JLabel();

        noteService = new NoteService();
        cartService = new CartService();
    }

    // aggiorna la tabella con i risultati
    private void aggiornaTabella(List<Nota> note) {
        model.setRowCount(0);  // pulisco la tabella
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Nota n : note) {
            String nomeCartella = "Root";
            if (n.getIdCartella() != null) {
                Cartella c = DatabaseCartelle.getCartelleRepo().get(n.getIdCartella());
                if (c != null) {
                    nomeCartella = c.getNome();
                }
            }

            model.addRow(new Object[]{
                n.getId(),
                n.getTitolo(),
                nomeCartella,
                sdf.format(n.getDataUltimaModifica())
            });
        }

        infoLabel.setText("Risultati ricerca: " + note.size());
    }

    // ricerca note per parola chiave
    public void ricercaNote(String keyword) {
        List<Nota> risultati = noteService.ricerca(keyword);
        aggiornaTabella(risultati);
    }

    // crea nuova nota
    private void creaNuovaNota() {
        JTextField titoloField = new JTextField();
        int result = JOptionPane.showConfirmDialog(null, titoloField, "Titolo nuova nota", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String titolo = titoloField.getText().trim();
            if (!titolo.isEmpty()) {
                Nota nuovaNota = noteService.creaNota(titolo);

                // aggiorno tabella
                List<Nota> tutteNote = noteService.getAllNote();
                aggiornaTabella(tutteNote);
            } else {
                JOptionPane.showMessageDialog(null, "Titolo non può essere vuoto");
            }
        }
    }

    // sposta nota in un'altra cartella
    private void spostaNota(String notaId, String idDest, String proprietario) {
        try {
            cartService.spostaNotaInCartella(notaId, idDest, proprietario);

            // ricarico note aggiornate
            List<Nota> tutteNote = noteService.getAllNote();
            aggiornaTabella(tutteNote);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Errore nello spostamento: " + e.getMessage());
        }
    }

    // esempio metodo per inizializzare GUI (aggiungi bottoni ecc.)
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
