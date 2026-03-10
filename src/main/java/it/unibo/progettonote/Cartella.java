package it.unibo.progettonote;

import java.io.Serializable;
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

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
