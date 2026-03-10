package it.unibo.progettonote;

import javax.swing.JFrame;

public class DettaglioNotaFrame extends JFrame {

    public DettaglioNotaFrame(Nota nota, String currentUser) {
        // inizializza GUI con la nota e controlla i permessi
        setTitle("Nota di " + currentUser);
        // logica di visualizzazione/modifica
    }
}
