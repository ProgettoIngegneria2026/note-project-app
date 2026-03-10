package it.unibo.progettonote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service per la ricerca di note (UC7).
 */
public class RicercaService {

    /**
     * Cerca le note di un utente che contengono una parola chiave nel titolo o nel contenuto.
     * La ricerca viene effettuata su tutte le note dell'utente, indipendentemente dalla cartella.
     *sss
     * @param parolaChiave La stringa da cercare (case-insensitive).
     * @param proprietario L'email dell'utente proprietario delle note.
     * @return Una lista di note che corrispondono ai criteri.
     */
    public List<Nota> cercaPerParolaChiave(String parolaChiave, String proprietario) {
        if (parolaChiave == null || parolaChiave.trim().isEmpty() || proprietario == null) {
            return new ArrayList<>();
        }

        String lowerCaseKeyword = parolaChiave.toLowerCase();

        return DatabaseNote.getNoteRepo().values().stream()
                .filter(nota -> proprietario.equals(nota.getProprietario()))
                .filter(nota -> (nota.getTitolo() != null && nota.getTitolo().toLowerCase().contains(lowerCaseKeyword)) ||
                                 (nota.getContenuto() != null && nota.getContenuto().toLowerCase().contains(lowerCaseKeyword)))
                .collect(Collectors.toList());
    }

    /**
     * Cerca le note di un utente create o modificate in un intervallo di date.
     * La ricerca viene effettuata su tutte le note dell'utente, indipendentemente dalla cartella.
     *
     * @param inizio       La data di inizio dell'intervallo.
     * @param fine         La data di fine dell'intervallo.
     * @param proprietario L'email dell'utente proprietario delle note.
     * @return Una lista di note che corrispondono ai criteri.
     */
    public List<Nota> cercaPerData(Date inizio, Date fine, String proprietario) {
        if (inizio == null || fine == null || proprietario == null) {
            return new ArrayList<>();
        }

        return DatabaseNote.getNoteRepo().values().stream()
                .filter(nota -> proprietario.equals(nota.getProprietario()))
                .filter(nota -> !nota.getDataUltimaModifica().before(inizio) && !nota.getDataUltimaModifica().after(fine))
                .collect(Collectors.toList());
    }
}