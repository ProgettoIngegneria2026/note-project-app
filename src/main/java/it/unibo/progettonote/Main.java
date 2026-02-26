package it.unibo.progettonote;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Avvia l'interfaccia grafica nel thread corretto di Swing
        SwingUtilities.invokeLater(() -> {
            InterfacciaRegistrazione.mostra();
        });
    }
}