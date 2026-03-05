package it.unibo.progettonote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardNote {
    private final String proprietario;
    private final NavigazioneService navService = new NavigazioneService();
    private final NotaCartellaService cartService = new NotaCartellaService();
    private final NotaService notaService = new NotaService(); // Aggiunto per UC4

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
        frame = new JFrame("Dashboard Note - " + proprietario);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 500);
        frame.setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Titolo", "Cartella", "Ultima modifica"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // --- Pannello Superiore ---
        JPanel top = new JPanel(new BorderLayout());
        infoLabel = new JLabel(" Benvenuto!");
        
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnNuova = new JButton("Nuova Nota");
        btnNuova.addActionListener(e -> creaNuovaNota());
        
        JButton btnCartelle = new JButton("Gestione Cartelle");
        btnCartelle.addActionListener(e -> new DashboardCartelle(proprietario));
        
        JButton refresh = new JButton("Aggiorna");
        refresh.addActionListener(e -> caricaNote());
        
        topButtons.add(btnNuova);
        topButtons.add(btnCartelle);
        topButtons.add(refresh);
        
        top.add(infoLabel, BorderLayout.CENTER);
        top.add(topButtons, BorderLayout.EAST);

        // --- Pannello Inferiore ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnSposta = new JButton("Sposta in Cartella");
        btnSposta.addActionListener(e -> spostaNotaSelezionata());

        JButton btnApri = new JButton("Apri");
        btnApri.addActionListener(e -> apriNotaSelezionata());

        bottom.add(btnSposta);
        bottom.add(btnApri);

        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void caricaNote() {
        model.setRowCount(0);
        try {
            List<Nota> note = navService.listaNoteUtenteOrdinate(proprietario);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Nota n : note) {
                String nomeCartella = "Nessuna (Root)";
                if (n.getIdCartella() != null) {
                    Cartella c = DatabaseCartelle.getCartelleRepo().get(n.getIdCartella());
                    if (c != null) nomeCartella = c.getNome();
                }

                model.addRow(new Object[]{
                        n.getId(),
                        n.getTitolo(),
                        nomeCartella,
                        sdf.format(n.getDataUltimaModifica())
                });
            }
            infoLabel.setText(" Totale note: " + note.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage());
        }
    }

    private void creaNuovaNota() {
        JTextField titoloField = new JTextField();
        JTextArea contenutoArea = new JTextArea(5, 20);
        contenutoArea.setLineWrap(true);
        contenutoArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Titolo:"), BorderLayout.NORTH);
        panel.add(titoloField, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(new JLabel("Contenuto (max 280 car.):"), BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(contenutoArea), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(frame, panel, 
                 "Crea Nuova Nota", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            boolean creata = notaService.creaNuovaNota(titoloField.getText(), contenutoArea.getText(), proprietario);
            if (creata) {
                caricaNote(); // Aggiorna automaticamente la tabella
            } else {
                JOptionPane.showMessageDialog(frame, "Errore: il contenuto supera i 280 caratteri o è vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void spostaNotaSelezionata() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una nota!");
            return;
        }

        String notaId = (String) model.getValueAt(row, 0);
        List<Cartella> cartelle = cartService.listaCartelleUtente(proprietario);

        if (cartelle.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Non hai ancora creato cartelle!");
            return;
        }

        String[] opzioni = new String[cartelle.size() + 1];
        opzioni[0] = "Sposta in Root (Nessuna)";
        for (int i = 0; i < cartelle.size(); i++) opzioni[i+1] = cartelle.get(i).getNome();

        String scelta = (String) JOptionPane.showInputDialog(frame, "Sposta in:", "Sposta Nota",
                JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[0]);

        if (scelta != null) {
            String idDestinazione = null;
            if (!scelta.equals(opzioni[0])) {
                idDestinazione = cartelle.stream()
                        .filter(c -> c.getNome().equals(scelta))
                        .findFirst().get().getId();
            }

            cartService.spostaNotaInCartella(notaId, idDestinazione, proprietario);
            caricaNote(); 
        }
    }

    private void apriNotaSelezionata() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) model.getValueAt(row, 0);
            new DettaglioNotaFrame(navService.dettaglioNota(id, proprietario));
        }
    }
}