package it.unibo.progettonote;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NavigazioneService {

    public List<Nota> listaNoteUtenteOrdinate(String proprietario) {
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Proprietario non valido");
        }

        List<Nota> note = DatabaseNote.findByOwner(proprietario);

        note.sort(Comparator.comparing(
                Nota::getDataUltimaModifica,
                Comparator.nullsLast(Date::compareTo)
        ).reversed());

        return note;
    }

    public Nota dettaglioNota(String notaId, String proprietario) {
        if (notaId == null || notaId.trim().isEmpty()) {
            throw new IllegalArgumentException("notaId non valido");
        }
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("proprietario non valido");
        }

        Nota n = DatabaseNote.findByIdAndOwner(notaId, proprietario);
        if (n == null) {
            throw new IllegalArgumentException("Nota inesistente o non appartenente all'utente");
        }
        return n;
    }
}