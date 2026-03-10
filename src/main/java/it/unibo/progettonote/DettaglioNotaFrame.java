package it.unibo.progettonote;

import javax.swing.*;

public class DettaglioNotaFrame extends JFrame {
    public DettaglioNotaFrame(Nota nota, String currentUser) {
        setTitle("Dettaglio Nota: " + nota.getTitolo());
        JTextArea area = new JTextArea(nota.getContenuto());
        area.setEditable(nota.isModificabile(currentUser));
        add(area);
        setSize(400,300);
        setVisible(true);
    }
}
