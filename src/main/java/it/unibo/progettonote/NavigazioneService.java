package it.unibo.progettonote;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NavigazioneService {

    private final NotaService notaService;

    public NavigazioneService(NotaService notaService) {
        this.notaService = notaService;
    }

    public List<Nota> listaNoteUtenteOrdinate(String owner) {
        return notaService.getNoteRepo().stream()
                .filter(n -> n.getProprietario().equals(owner))
                .sorted(Comparator.comparing(Nota::getDataUltimaModifica).reversed())
                .collect(Collectors.toList());
    }

    public Nota dettaglioNota(String id, String owner) {
        return notaService.getNoteRepo().stream()
                .filter(n -> n.getId().equals(id) && n.getProprietario().equals(owner))
                .findFirst()
                .orElse(null);
    }
}
