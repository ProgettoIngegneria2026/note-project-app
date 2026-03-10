package it.unibo.progettonote;

import java.util.Date;
import java.util.List;

public class RicercaService {

    private final NotaService notaService;

    public RicercaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public List<Nota> cercaPerParolaChiave(String parola, String userId) {
        return notaService.getNoteRepo().stream()
                .filter(n -> n.getTesto().contains(parola) && n.getOwner().equals(userId))
                .toList();
    }

    public List<Nota> cercaPerData(Date from, Date to, String userId) {
        return notaService.getNoteRepo().stream()
                .filter(n -> !n.getDataCreazione().before(from)
                          && !n.getDataCreazione().after(to)
                          && n.getOwner().equals(userId))
                .toList();
    }
}
