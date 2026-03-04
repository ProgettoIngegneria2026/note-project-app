package it.unibo.progettonote;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Cartella implements Serializable {

    private final String id = UUID.randomUUID().toString();
    private String nome;
    private final String proprietario; // username dell’utente

    public Cartella(String nome, String proprietario) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della cartella non può essere vuoto");
        }
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Il proprietario non può essere nullo");
        }
        this.nome = nome.trim();
        this.proprietario = proprietario.trim();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della cartella non può essere vuoto");
        }
        this.nome = nome.trim();
    }

    public String getProprietario() {
        return proprietario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cartella)) return false;
        Cartella cartella = (Cartella) o;
        return Objects.equals(id, cartella.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}