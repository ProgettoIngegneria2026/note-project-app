package it.unibo.progettonote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardCartelle {
    private final String proprietario;
    private final GestioneCartelleService service = new GestioneCartelleService();

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLabel infoLabel;

    public DashboardCartelle(String proprietario) {
        this.proprietario = proprietario;
        initUI();
        caricaCartelle();
    }

    private void initUI() {
        frame = new JFrame("Cartelle - Le mie cartelle");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 380);
        frame.setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        infoLabel = new JLabel(" ");

        JPanel top = new JPanel(new BorderLayout());
        top.add(infoLabel, BorderLayout.CENTER);

        JButton refresh = new JButton("Aggiorna");
        refresh.addActionListener(e -> caricaCartelle());
        top.add(refresh, BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton crea = new JButton("Crea cartella");
        crea.addActionListener(e -> creaCartella());
        bottom.add(crea);

        JButton rinomina = new JButton("Rinomina");
        rinomina.addActionListener(e -> rinominaCartella());
        bottom.add(rinomina);

        JButton elimina = new JButton("Elimina");
        elimina.addActionListener(e -> eliminaCartella());
        bottom.add(elimina);

        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void caricaCartelle() {
        model.setRowCount(0);
        try {
            List<Cartella> cartelle = service.listaCartelle(proprietario);
            infoLabel.setText(cartelle.isEmpty() ? "Nessuna cartella." : "Totale cartelle: " + cartelle.size());
            for (Cartella c : cartelle) {
                model.addRow(new Object[]{c.getId(), c.getNome()});
            }
        } catch (Exception ex) {
            infoLabel.setText("Errore caricamento cartelle.");
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void creaCartella() {
        String nome = JOptionPane.showInputDialog(frame, "Nome nuova cartella:");
        if (nome == null) return;
        try {
            service.creaCartella(nome, proprietario);
            caricaCartelle();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rinominaCartella() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una cartella.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        String oldName = (String) model.getValueAt(row, 1);
        String nuovoNome = JOptionPane.showInputDialog(frame, "Nuovo nome:", oldName);
        if (nuovoNome == null) return;
        try {
            service.rinominaCartella(id, nuovoNome, proprietario);
            caricaCartelle();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminaCartella() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una cartella.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = (String) model.getValueAt(row, 0);

        int ok = JOptionPane.showConfirmDialog(frame, "Eliminare la cartella selezionata?", "Conferma",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        try {
            service.eliminaCartellaSeVuota(id, proprietario);
            caricaCartelle();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // test manuale
    public static void main(String[] args) {
        DatabaseCore.enableTestMode(); // se lo avete
        new DashboardCartelle("mario");
    }
}