package it.unibo.progettonote;

import javax.swing.*;

public class DettaglioNotaFrame extends JFrame {
    
    private Nota nota;

    public DettaglioNotaFrame(Nota nota, String utente) {
        this.nota = nota;
        boolean modificabile = nota.isModificabile(utente);
        JLabel label = new JLabel("Nota " + (modificabile ? "modificabile" : "sola lettura"));
        add(label);
        setSize(300, 200);
        setVisible(true);
    }
}
