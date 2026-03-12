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
    private final NotaService notaService = new NotaService();

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLabel infoLabel;

    private JTextField searchField;
    private JSpinner dateInizio;
    private JSpinner dateFine;

    public DashboardNote(String proprietario) {
        this.proprietario = proprietario;
        initUI();
        caricaNote();
    }

    private void initUI() {
        frame = new JFrame("Dashboard Note - " + proprietario);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(950, 550);
        frame.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Filtra le tue note"));

        searchField = new JTextField(12);

        Calendar cal = Calendar.getInstance();
        Date oggi = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date unMeseFa = cal.getTime();

        dateInizio = new JSpinner(new SpinnerDateModel(unMeseFa, null, null, Calendar.DAY_OF_MONTH));
        dateFine = new JSpinner(new SpinnerDateModel(oggi, null, null, Calendar.DAY_OF_MONTH));
        dateInizio.setEditor(new JSpinner.DateEditor(dateInizio, "dd/MM/yyyy"));
        dateFine.setEditor(new JSpinner.DateEditor(dateFine, "dd/MM/yyyy"));

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

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoLabel = new JLabel(" Benvenuto!");

        JButton btnNuova = new JButton("Nuova Nota");
        btnNuova.addActionListener(e -> creaNuovaNota());

        JButton btnCartelle = new JButton("Gestione Cartelle");
        btnCartelle.addActionListener(e -> new DashboardCartelle(proprietario));

        JButton btnRefresh = new JButton("Aggiorna");
        btnRefresh.addActionListener(e -> caricaNote());

        topButtons.add(btnNuova);
        topButtons.add(btnCartelle);
        topButtons.add(btnRefresh);

        topPanel.add(infoLabel, BorderLayout.WEST);
        topPanel.add(topButtons, BorderLayout.EAST);

        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(searchPanel, BorderLayout.NORTH);
        northContainer.add(topPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(new Object[]{"ID", "Titolo", "Cartella", "Ultima modifica"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnSposta = new JButton("Sposta");
        btnSposta.addActionListener(e -> spostaNotaSelezionata());

        JButton btnApri = new JButton("Apri");
        btnApri.addActionListener(e -> apriNotaSelezionata());

        JButton btnModifica = new JButton("Modifica");
        btnModifica.addActionListener(e -> modificaNotaSelezionata());
        JButton btnElimina = new JButton("Elimina");
        btnElimina.addActionListener(e -> eliminaNotaSelezionata());
        JButton btnRipristina = new JButton("Storico Versioni");
        btnRipristina.addActionListener(e -> ripristinaNotaSelezionata());

        bottomPanel.add(btnSposta);
        bottomPanel.add(btnApri);
        bottomPanel.add(btnModifica);
        bottomPanel.add(btnElimina); // AGGIUNTO QUI
        bottomPanel.add(btnRipristina);

        frame.add(northContainer, BorderLayout.NORTH);
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
        if (!query.isEmpty()) risultati = ricercaService.cercaPerParolaChiave(query, proprietario);
        else risultati = ricercaService.cercaPerData(inizio, fine, proprietario);

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
                if (c != null) nomeCartella = c.getNome();
            }
            model.addRow(new Object[]{n.getId(), n.getTitolo(), nomeCartella, sdf.format(n.getDataUltimaModifica())});
        }
    }
    private void eliminaNotaSelezionata() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una nota da eliminare!");
            return;
        }

        String idNota = (String) model.getValueAt(row, 0);
        
        int conferma = JOptionPane.showConfirmDialog(frame, 
            "Sei sicuro di voler eliminare definitivamente questa nota?", 
            "Conferma Eliminazione", 
            JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            boolean eliminata = notaService.eliminaNota(idNota, proprietario);
            if (eliminata) {
                caricaNote();
                JOptionPane.showMessageDialog(frame, "Nota eliminata con successo!");
            } else {
                JOptionPane.showMessageDialog(frame, "Errore: impossibile eliminare la nota.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void creaNuovaNota() {
        JTextField titoloField = new JTextField();
        JTextArea contenutoArea = new JTextArea(5, 20);
        contenutoArea.setLineWrap(true);
        contenutoArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel titoloPanel = new JPanel(new BorderLayout(5, 5));
        titoloPanel.add(new JLabel("Titolo:"), BorderLayout.NORTH);
        titoloPanel.add(titoloField, BorderLayout.CENTER);

        JPanel contenutoPanel = new JPanel(new BorderLayout(5, 5));
        contenutoPanel.add(new JLabel("Contenuto (max 280 car.):"), BorderLayout.NORTH);
        contenutoPanel.add(new JScrollPane(contenutoArea), BorderLayout.CENTER);

        panel.add(titoloPanel, BorderLayout.NORTH);
        panel.add(contenutoPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Crea Nuova Nota", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            boolean creata = notaService.creaNuovaNota(titoloField.getText(), contenutoArea.getText(), proprietario);
            if (creata) caricaNote();
            else JOptionPane.showMessageDialog(frame, "Errore: contenuto non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(frame, "Crea prima una cartella!");
            return;
        }

        String[] nomi = new String[cartelle.size() + 1];
        nomi[0] = "Sposta in Root";
        for (int i = 0; i < cartelle.size(); i++) nomi[i + 1] = cartelle.get(i).getNome();

        String scelta = (String) JOptionPane.showInputDialog(frame, "Sposta in:", "Sposta Nota", JOptionPane.QUESTION_MESSAGE, null, nomi, nomi[0]);

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
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una nota!");
            return;
        }

        String id = (String) model.getValueAt(row, 0);
        try {
            new DettaglioNotaFrame(navService.dettaglioNota(id, proprietario));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificaNotaSelezionata() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una nota!");
            return;
        }

        String idNota = (String) model.getValueAt(row, 0);
        Nota nota;
        try {
            nota = navService.dettaglioNota(idNota, proprietario);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField titoloField = new JTextField(nota.getTitolo());
        JTextArea contenutoArea = new JTextArea(nota.getContenuto(), 5, 20);
        contenutoArea.setLineWrap(true);
        contenutoArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel titoloPanel = new JPanel(new BorderLayout(5, 5));
        titoloPanel.add(new JLabel("Titolo:"), BorderLayout.NORTH);
        titoloPanel.add(titoloField, BorderLayout.CENTER);

        JPanel contenutoPanel = new JPanel(new BorderLayout(5, 5));
        contenutoPanel.add(new JLabel("Contenuto:"), BorderLayout.NORTH);
        contenutoPanel.add(new JScrollPane(contenutoArea), BorderLayout.CENTER);

        panel.add(titoloPanel, BorderLayout.NORTH);
        panel.add(contenutoPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Modifica Nota", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // L'aggiunta del doppio salvataggio che creava bug è stata rimossa da qui.
            // Ci pensa da solo NotaService a farlo se il testo cambia.
            boolean aggiornata = notaService.updateNota(idNota, titoloField.getText(), contenutoArea.getText(), proprietario);
            if (aggiornata) {
                caricaNote();
                JOptionPane.showMessageDialog(frame, "Nota aggiornata!");
            } else {
                JOptionPane.showMessageDialog(frame, "Errore: contenuto non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ripristinaNotaSelezionata() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Seleziona una nota!");
            return;
        }

        String idNota = (String) model.getValueAt(row, 0);
        Nota nota;
        try {
            nota = navService.dettaglioNota(idNota, proprietario);
        } catch (IllegalArgumentException e) {
            return;
        }

        if (nota.getVersioni() == null || nota.getVersioni().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessuna versione precedente disponibile.");
            return;
        }

        String[] versioniStr = new String[nota.getVersioni().size()];
        for (int i = 0; i < nota.getVersioni().size(); i++) {
            VersioneNota v = nota.getVersioni().get(i);
            String dataStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(v.getData());
            versioniStr[i] = "Versione " + v.getNumeroVersione() + " - " + dataStr;
        }

        String scelta = (String) JOptionPane.showInputDialog(
                frame, "Seleziona versione:", "Storico",
                JOptionPane.QUESTION_MESSAGE, null, versioniStr, versioniStr[0]);

        if (scelta != null) {
            int numVer = Integer.parseInt(scelta.split(" ")[1]);
            boolean ok = notaService.ripristinaVersione(idNota, numVer, proprietario);
            if (ok) {
                caricaNote();
                JOptionPane.showMessageDialog(frame, "Ripristinata con successo!");
            }
        }
    }
}