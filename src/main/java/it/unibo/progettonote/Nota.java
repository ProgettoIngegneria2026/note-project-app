package it.unibo.progettonote;

public class Nota {
    private String id;
    private String titolo;
    private String contenuto;
    private String owner;

    public Nota(String id, String titolo, String contenuto, String owner) {
        this.id = id;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.owner = owner;
    }

    public String getId() { return id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }
    public String getOwner() { return owner; }
}
