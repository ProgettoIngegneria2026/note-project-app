package it.unibo.progettonote;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Cartella implements Serializable {
    private String id;
    private String nome;
    private String proprietario;

    public Cartella(String nome, String proprietario) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.proprietario = proprietario;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getProprietario() { return proprietario; }

    public void setNome(String nome) { this.nome = nome; }

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