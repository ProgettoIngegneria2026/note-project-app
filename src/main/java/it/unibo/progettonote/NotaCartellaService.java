package it.unibo.progettonote;

import java.util.List;
import java.util.stream.Collectors;

public class NotaCartellaService {
    private final NotaService notaService;

    public NotaCartellaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public List<Nota> listaCartelleUtente(String owner) {
        return notaService.getNoteRepo().stream()
                .filter(n -> n.getOwner().equals(owner))
                .collect(Collectors.toList());
    }

    public void spostaNotaInCartella(String idNota, String idCartella, String owner) {
        notaService.getNoteRepo().stream()
                .filter(n -> n.getId().equals(idNota) && n.getOwner().equals(owner))
                .findFirst()
                .ifPresent(n -> n.setIdCartella(idCartella));
    }
}
