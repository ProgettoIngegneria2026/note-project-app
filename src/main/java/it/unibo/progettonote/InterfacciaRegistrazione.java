package it.unibo.progettonote;

public class InterfacciaRegistrazione {

    private String utente;

    public InterfacciaRegistrazione(String utente) {
        this.utente = utente;
    }

    public void mostra() {
        DashboardNote dashboard = new DashboardNote(utente);
        dashboard.mostraDashboard();
    }
}
