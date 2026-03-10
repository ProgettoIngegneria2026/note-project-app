package it.unibo.progettonote;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

public class NotaService {

    public boolean creaNuovaNota(String titolo, String contenuto, String proprietario) {
        if (titolo == null || contenuto == null || proprietario == null) return false;
        if (contenuto.length() > 280) return false;

        String idUnivoco = UUID.randomUUID().toString();
        Nota nuovaNota = new Nota(titolo, contenuto, proprietario);
        nuovaNota.setId(idUnivoco);

        ConcurrentNavigableMap<String, Nota> repo = DatabaseNote.getNoteRepo();
        repo.put(idUnivoco, nuovaNota);
        DatabaseCore.commit();
        return true;
    }

    public void salvaNota(Nota nota, String user) {
        DatabaseNote.getNoteRepo().put(nota.getId(), nota);
    }

    public java.util.List<Nota> getNoteRepo() {
        return new java.util.ArrayList<>(DatabaseNote.getNoteRepo().values());
    }

    public boolean updateNota(String idNota, String titolo, String contenuto, String proprietario) {
        return creaNuovaNota(titolo, contenuto, proprietario);
    }
}
