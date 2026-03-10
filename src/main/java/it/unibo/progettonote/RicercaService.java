package it.unibo.progettonote;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RicercaService {
    private final NotaService notaService;

    public RicercaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public List<Nota> cercaPerParolaChiave(String owner, String parola) {
        return notaService.getNoteRepo().stream()
                .filter(n -> n.getOwner().equals(owner))
                .filter(n -> n.getTitolo().contains(parola) || n.getContenuto().contains(parola))
                .collect(Collectors.toList());
    }

    public List<Nota> cercaPerData(Date inizio, Date fine, String owner) {
        return notaService.getNoteRepo().stream()
                .filter(n -> n.getOwner().equals(owner))
                .filter(n -> !n.getDataUltimaModifica().before(inizio) && !n.getDataUltimaModifica().after(fine))
                .collect(Collectors.toList());
    }
}
