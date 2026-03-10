package it.unibo.progettonote;

public class DashboardNote {
    private String utente;

    public DashboardNote(String utente) {
        this.utente = utente;
    }

    public void mostraDashboard() {
        System.out.println("Benvenuto " + utente + "! Dashboard caricata.");
    }
}
