package it.unibo.progettonote;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NavigazioneService {
    private final NotaService notaService;

    public NavigazioneService(NotaService notaService) {
        this.notaService = notaService;
    }

    public List<Nota> listaNoteUtenteOrdinate(String owner) {
        return notaService.getNoteRepo().stream()
                .filter(n -> n.getOwner().equals(owner))
                .sorted((a,b) -> b.getDataUltimaModifica().compareTo(a.getDataUltimaModifica()))
                .collect(Collectors.toList());
    }

    public Nota dettaglioNota(String id, String owner) {
        Optional<Nota> n = notaService.getNoteRepo().stream()
                .filter(note -> note.getId().equals(id) && note.getOwner().equals(owner))
                .findFirst();
        return n.orElse(null);
    }
}
