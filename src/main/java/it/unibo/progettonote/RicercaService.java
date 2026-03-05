package it.unibo.progettonote;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



public class RicercaService {

    /**
     * Filtra le note per testo (titolo o contenuto) e proprietario.
     */
    public List<Nota> cercaPerParolaChiave(String query, String proprietario) {
        String q = query.toLowerCase().trim();
        return DatabaseNote.getNoteRepo().values().stream()
                .filter(n -> n.getProprietario().equals(proprietario))
                .filter(n -> n.getTitolo().toLowerCase().contains(q) ||
                        n.getContenuto().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    /**
     * Filtra le note per intervallo di date.
     */
    public List<Nota> cercaPerData(Date inizio, Date fine, String proprietario) {
        return DatabaseNote.getNoteRepo().values().stream()
                .filter(n -> n.getProprietario().equals(proprietario))
                .filter(n -> {
                    Date d = n.getDataUltimaModifica();
                    if (d == null) return false;
                    // Controlla se d è compresa tra inizio e fine
                    return !d.before(inizio) && !d.after(fine);
                })
                .collect(Collectors.toList());
    }
}