package it.unibo.progettonote;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class DettaglioNotaFrame {

    private final Nota nota;

    public DettaglioNotaFrame(Nota nota) {
        this.nota = nota;
        initUI();
    }

    private void initUI() {
        JFrame frame = new JFrame("Dettaglio Nota");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new GridLayout(3, 1));
        header.add(new JLabel("Titolo: " + safe(nota.getTitolo())));
        header.add(new JLabel("Proprietario: " + safe(nota.getProprietario())));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String mod = (nota.getDataUltimaModifica() == null) ? "-" : sdf.format(nota.getDataUltimaModifica());
        header.add(new JLabel("Ultima modifica: " + mod));

        JTextArea area = new JTextArea();
        area.setText(safe(nota.getContenuto()));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        frame.add(header, BorderLayout.NORTH);
        frame.add(new JScrollPane(area), BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}