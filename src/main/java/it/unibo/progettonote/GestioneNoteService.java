package it.unibo.progettonote;

public class GestioneNoteService {

    private final NotaService notaService;

    public GestioneNoteService(NotaService notaService) {
        this.notaService = notaService;
    }

    public void salvaNotaSeAutorizzato(Nota nota, String user) throws IllegalAccessException {
        if (!nota.isModificabile(user)) {
            throw new IllegalAccessException("Utente non autorizzato");
        }
        notaService.salvaNota(nota, user);
    }
}
