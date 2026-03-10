package it.unibo.progettonote;

public class InterfacciaRegistrazione {

    private final DashboardNote dashboard;

    public InterfacciaRegistrazione(DashboardNote dashboard) {
        this.dashboard = dashboard;
    }

    public void registraUtente(Utente u) {
        // logica registrazione utente
        dashboard.mostraDashboard();
    }
}
