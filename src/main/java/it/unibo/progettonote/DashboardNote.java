package it.unibo.progettonote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardNote {
    private final String proprietario;
    private final NavigazioneService navService = new NavigazioneService();
    private final NotaCartellaService cartService = new NotaCartellaService();
    private final RicercaService ricercaService = new RicercaService();
    private final NotaService notaService = new NotaService(); // Aggiunto per UC4

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLabel infoLabel;

    // Componenti per UC7 (Ricerca)
    private JTextField searchField;
    private JSpinner dateInizio;
    private JSpinner dateFine;

    public DashboardNote(String proprietario) {
        this.proprietario = proprietario;
        initUI();
        caricaNote(); // Carica tutte le note all'avvio
    }

    private void initUI() {
        frame = new JFrame("Dashboard Note - " + proprietario);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(950, 550);
        frame.setLayout(new BorderLayout());

        // --- NORD: BARRA DI RICERCA (UC7) ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Filtra le tue note"));

        searchField = new JTextField(12);

        // Spinner per le date (Inizio e Fine)
        Calendar cal = Calendar.getInstance();
        Date oggi = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date unMeseFa = cal.getTime();

        dateInizio = new JSpinner(new SpinnerDateModel(unMeseFa, null, null, Calendar.DAY_OF_MONTH));
        dateFine = new JSpinner(new SpinnerDateModel(oggi, null, null, Calendar.DAY_OF_MONTH));

        JSpinner.DateEditor editorInizio = new JSpinner.DateEditor(dateInizio, "dd/MM/yyyy");
        JSpinner.DateEditor editorFine = new JSpinner.DateEditor(dateFine, "dd/MM/yyyy");
        dateInizio.setEditor(editorInizio);
        dateFine.setEditor(editorFine);

        JButton btnCerca = new JButton("Cerca");
        btnCerca.addActionListener(e -> eseguiRicerca());

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> {
            searchField.setText("");
            caricaNote();
        });

        searchPanel.add(new JLabel("Testo:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel(" Dal:"));
        searchPanel.add(dateInizio);
        searchPanel.add(new JLabel(" Al:"));
        searchPanel.add(dateFine);
        searchPanel.add(btnCerca);
        searchPanel.add(btnReset);

        // --- CENTRO: TABELLA ---
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 500);
        frame.setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Titolo", "Cartella", "Ultima modifica"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0); // ID Nascosto

        // --- SUD: AZIONI (UC6) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        infoLabel = new JLabel(" Benvenuto!");
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

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSposta = new JButton("Sposta in Cartella");
        btnSposta.addActionListener(e -> spostaNotaSelezionata());

        JButton btnApri = new JButton("Apri Nota");
        btnApri.addActionListener(e -> apriNotaSelezionata());

        JButton btnGestisciCartelle = new JButton("Gestisci Cartelle");
        btnGestisciCartelle.addActionListener(e -> {
            new DashboardCartelle(proprietario);
        });

        buttons.add(btnGestisciCartelle);
        buttons.add(btnSposta);
        buttons.add(btnApri);

        bottomPanel.add(infoLabel, BorderLayout.WEST);
        bottomPanel.add(buttons, BorderLayout.EAST);

        // Assemblaggio
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void caricaNote() {
        List<Nota> note = navService.listaNoteUtenteOrdinate(proprietario);
        aggiornaTabella(note);
        infoLabel.setText(" Totale note: " + note.size());
    }

    private void eseguiRicerca() {
        String query = searchField.getText().trim();
        Date inizio = (Date) dateInizio.getValue();
        Date fine = (Date) dateFine.getValue();

        List<Nota> risultati;
        if (!query.isEmpty()) {
            // Cerca per testo
            risultati = ricercaService.cercaPerParolaChiave(query, proprietario);
        } else {
            // Cerca per data se il testo è vuoto
            risultati = ricercaService.cercaPerData(inizio, fine, proprietario);
        }

        aggiornaTabella(risultati);
        infoLabel.setText(" Risultati ricerca: " + risultati.size());
    }
    private void aggiornaTabella(List<Nota> note) {
    model.setRowCount(0);
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
            JOptionPane.showMessageDialog(frame, "Seleziona una nota dalla tabella!");
            return;
        }

        String notaId = (String) model.getValueAt(row, 0);
        List<Cartella> cartelle = cartService.listaCartelleUtente(proprietario);

        if (cartelle.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Crea prima una cartella!");
            return;
        }

        String[] nomi = new String[cartelle.size() + 1];
        nomi[0] = "Sposta in Root";
        for (int i = 0; i < cartelle.size(); i++) nomi[i+1] = cartelle.get(i).getNome();
        String[] opzioni = new String[cartelle.size() + 1];
        opzioni[0] = "Sposta in Root (Nessuna)";
        for (int i = 0; i < cartelle.size(); i++) opzioni[i+1] = cartelle.get(i).getNome();

        String scelta = (String) JOptionPane.showInputDialog(frame, "Sposta in:", "Sposta Nota",
                JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);

        if (scelta != null) {
            String idDest = null;
            if (!scelta.equals(nomi[0])) {
                idDest = cartelle.stream().filter(c -> c.getNome().equals(scelta)).findFirst().get().getId();
            }
            cartService.spostaNotaInCartella(notaId, idDest, proprietario);
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