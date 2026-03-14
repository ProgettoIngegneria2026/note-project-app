package it.unibo.progettonote.client;

import java.io.Serializable;
import java.util.Objects;

public class Cartella implements Serializable {

    private String id;
    private String nome;
    private String proprietario; // username dell’utente

    public Cartella() {
        // Necessario per GWT RPC
    }

    public Cartella(String nome, String proprietario) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della cartella non può essere vuoto");
        }
        if (proprietario == null || proprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Il proprietario non può essere nullo");
        }
        this.id = String.valueOf(System.currentTimeMillis()) + Math.random();
        this.nome = nome.trim();
        this.proprietario = proprietario.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { // GWT potrebbe averne bisogno in alcune situazioni di deserializzazione
        this.id = id;
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

    public void setProprietario(String proprietario) { // Utile per framework di mapping
        this.proprietario = proprietario;
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