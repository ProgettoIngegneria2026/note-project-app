package it.unibo.progettonote;

import java.util.List;
import java.util.stream.Collectors;

public class GestioneNoteService {

    private DatabaseNote dbNote;

    public GestioneNoteService() {
        this.dbNote = new DatabaseNote();
    }

    public List<Nota> listaNote(String owner) {
        return dbNote.findByOwner(owner);
    }

    public boolean aggiungiNota(String titolo, String contenuto, String owner) {
        Nota n = new Nota(titolo, contenuto, owner);
        dbNote.aggiungiNota(n);
        return true;
    }
}
