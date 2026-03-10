package it.unibo.progettonote;

import java.util.List;

public class DashboardCartelle {

    private final GestioneCartelleService service;
    private final String currentUser;

    public DashboardCartelle(GestioneCartelleService service, String currentUser) {
        this.service = service;
        this.currentUser = currentUser;
    }

    public void eliminaCartella(String cartellaId) {
        service.eliminaCartellaSeVuota(cartellaId, currentUser);
    }

    public List<Cartella> listaCartelle() {
        return service.getCartelle(currentUser);
    }
}
