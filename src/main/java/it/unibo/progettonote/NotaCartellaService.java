package it.unibo.progettonote;

import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.stream.Collectors;

public class NotaCartellaService {

    private final NotaService notaService;
    private final GestioneCartelleService gestioneCartelleService;
    private final ConcurrentNavigableMap<String, Nota> noteRepo;
    private final ConcurrentNavigableMap<String, Cartella> cartelleRepo;

    public NotaCartellaService(NotaService notaService) {
        this.notaService = notaService;
        this.noteRepo = DatabaseNote.getNoteRepo();
        this.cartelleRepo = DatabaseCartelle.getCartelleRepo();
        this.gestioneCartelleService = new GestioneCartelleService();
    }

    public List<Cartella> listaCartelleUtente(String proprietario) {
        return gestioneCartelleService.listaCartelle(proprietario);
    }

    public void spostaNotaInCartella(String idNota, String idCartella, String owner) {
        notaService.getNoteRepo().stream()
            .filter(n -> n.getId().equals(idNota) && n.getProprietario().equals(owner))
            .findFirst()
            .ifPresent(n -> n.setIdCartella(idCartella));
    }
}
