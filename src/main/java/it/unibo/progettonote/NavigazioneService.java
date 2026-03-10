package it.unibo.progettonote;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NavigazioneService {

    /**
     * Restituisce la lista delle note accessibili dall'utente, ordinate per data ultima modifica.
     */
    public List<Nota> listaNoteUtenteOrdinate(String proprietario) {
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Proprietario non valido");
        }

        // Usa la nuova query che include collaboratori
        List<Nota> note = DatabaseNote.findAccessibili(proprietario);

        note.sort(Comparator.comparing(
                Nota::getDataUltimaModifica,
                Comparator.nullsLast(Date::compareTo)
        ).reversed());

        return note;
    }

    /**
     * Restituisce il dettaglio di una nota se l'utente ha accesso
     */
    public Nota dettaglioNota(String notaId, String proprietario) {
        if (notaId == null || notaId.trim().isEmpty()) {
            throw new IllegalArgumentException("notaId non valido");
        }
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("proprietario non valido");
        }

        Nota n = DatabaseNote.findById(notaId);
        if (n == null || !n.puoAccedere(proprietario)) {
            throw new IllegalArgumentException("Nota inesistente o non accessibile dall'utente");
        }

        return n;
    }
}
