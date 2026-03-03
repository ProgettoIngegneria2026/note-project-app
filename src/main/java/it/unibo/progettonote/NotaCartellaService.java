package it.unibo.progettonote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

public class NotaCartellaService {

    private final ConcurrentNavigableMap<String, Nota> noteRepo;
    private final ConcurrentNavigableMap<String, Cartella> cartelleRepo;

    public NotaCartellaService() {
        this.noteRepo = DatabaseNote.getNoteRepo();
        this.cartelleRepo = DatabaseCartelle.getCartelleRepo();
    }

    public void spostaNotaInCartella(String notaId, String cartellaId, String proprietario) {
        if (notaId == null || notaId.isBlank()) throw new IllegalArgumentException("notaId non valido");
        if (cartellaId == null || cartellaId.isBlank()) throw new IllegalArgumentException("cartellaId non valido");
        if (proprietario == null || proprietario.isBlank()) throw new IllegalArgumentException("proprietario non valido");

        Cartella cartella = cartelleRepo.get(cartellaId);
        if (cartella == null) throw new IllegalArgumentException("Cartella inesistente");
        if (!cartella.getProprietario().equals(proprietario)) throw new IllegalArgumentException("Cartella non appartenente all'utente");

        Nota nota = noteRepo.get(notaId);
        if (nota == null) throw new IllegalArgumentException("Nota inesistente");
        if (nota.getProprietario() == null || !nota.getProprietario().equals(proprietario)) {
            throw new IllegalArgumentException("Nota non appartenente all'utente");
        }

        nota.setIdCartella(cartellaId);
        noteRepo.put(notaId, nota);
        DatabaseCore.commit();
    }

    public List<Nota> listaNotePerCartella(String cartellaId, String proprietario) {
        if (cartellaId == null || cartellaId.isBlank()) throw new IllegalArgumentException("cartellaId non valido");
        if (proprietario == null || proprietario.isBlank()) throw new IllegalArgumentException("proprietario non valido");

        Cartella cartella = cartelleRepo.get(cartellaId);
        if (cartella == null) throw new IllegalArgumentException("Cartella inesistente");
        if (!cartella.getProprietario().equals(proprietario)) throw new IllegalArgumentException("Cartella non appartenente all'utente");

        List<Nota> risultato = new ArrayList<>();
        for (Nota n : noteRepo.values()) {
            if (proprietario.equals(n.getProprietario()) && cartellaId.equals(n.getIdCartella())) {
                risultato.add(n);
            }
        }
        return risultato;
    }
}