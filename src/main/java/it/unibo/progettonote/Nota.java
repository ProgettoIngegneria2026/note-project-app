package it.unibo.progettonote;

import java.util.HashSet;
import java.util.Set;

public class Nota {
    private String id;
    private String titolo;
    private String contenuto;
    private String owner;
    private boolean solaLettura; // nuovo campo
    private Set<String> collaboratori;

    public Nota(String id, String titolo, String contenuto, String owner) {
        this.id = id;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.owner = owner;
        this.solaLettura = false;
        this.collaboratori = new HashSet<>();
    }

    // --- getter e setter ---
    public String getId() { return id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }
    public String getOwner() { return owner; }
    public boolean isSolaLettura() { return solaLettura; }
    public void setSolaLettura(boolean solaLettura) { this.solaLettura = solaLettura; }

    public Set<String> getCollaboratori() { return collaboratori; }
    public void aggiungiCollaboratore(String user) { collaboratori.add(user); }

    // --- logica controllo permessi ---
    public boolean isModificabile(String user) {
        return owner.equals(user) && !solaLettura;
    }
}
