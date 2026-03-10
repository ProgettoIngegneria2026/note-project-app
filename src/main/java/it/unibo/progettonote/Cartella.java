package it.unibo.progettonote;

public class Cartella {
    private String nome;
    private String owner;

    public Cartella(String nome, String owner) {
        this.nome = nome;
        this.owner = owner;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getOwner() { return owner; }
}
