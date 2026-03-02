package it.unibo.progettonote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardNote {

    private final String proprietario;
    private final NavigazioneService service = new NavigazioneService();

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLabel infoLabel;

    public DashboardNote(String proprietario) {
        this.proprietario = proprietario;
        initUI();
        caricaNote();
    }

    private void initUI() {
        frame = new JFrame("Dashboard - Le mie note");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Titolo", "Ultima modifica"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel top = new JPanel(new BorderLayout());
        infoLabel = new JLabel(" ");
        top.add(infoLabel, BorderLayout.CENTER);

        JButton refresh = new JButton("Aggiorna");
        refresh.addActionListener(e -> caricaNote());
        top.add(refresh, BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton apri = new JButton("Apri nota");
        apri.addActionListener(e -> apriNotaSelezionata());
        bottom.add(apri);

        // doppio click per aprire
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    apriNotaSelezionata();
                }
            }
        });

        frame.add(top, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void caricaNote() {
        model.setRowCount(0);

        try {
            List<Nota> note = service.listaNoteUtenteOrdinate(proprietario);

            if (note.isEmpty()) {
                infoLabel.setText("Nessuna nota presente.");
            } else {
                infoLabel.setText("Totale note: " + note.size());
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Nota n : note) {
                String data = (n.getDataUltimaModifica() == null) ? "-" : sdf.format(n.getDataUltimaModifica());
                model.addRow(new Object[]{n.getId(), n.getTitolo(), data});
            }

        } catch (Exception ex) {
            infoLabel.setText("Errore di caricamento note.");
            JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apriNotaSelezionata() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una nota.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String notaId = (String) model.getValueAt(row, 0);
        try {
            Nota nota = service.dettaglioNota(notaId, proprietario);
            new DettaglioNotaFrame(nota);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Impossibile aprire la nota: " + ex.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // per avvio rapido (test manuale)
    public static void main(String[] args) {
        // Sostituisci con l'utente loggato reale
        new DashboardNote("mario");
    }
}