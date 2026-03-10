package it.unibo.progettonote;

import java.util.HashMap;
import java.util.Map;

public class GestioneNoteService {
    private NotaService notaService;

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
