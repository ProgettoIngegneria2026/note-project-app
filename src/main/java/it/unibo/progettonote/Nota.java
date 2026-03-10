package it.unibo.progettonote;

import java.io.Serializable;
import java.util.*;

public class Nota implements Serializable {

    private String id;
    private String titolo;
    private String contenuto;
    private Date dataCreazione;
    private Date dataUltimaModifica;
    private String proprietario;
    private String idCartella; 

    private boolean solaLettura;
    private Set<String> collaboratori = new HashSet<>();

    public Nota() {}

    public Nota(String titolo, String contenuto, String proprietario) {
        this.id = java.util.UUID.randomUUID().toString();
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.proprietario = proprietario;
        this.dataCreazione = new Date();
        this.dataUltimaModifica = new Date();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }
    public String getProprietario() { return proprietario; }
    public Date getDataCreazione() { return dataCreazione; }
    public Date getDataUltimaModifica() { return dataUltimaModifica; }
    public String getIdCartella() { return idCartella; }
    public void setIdCartella(String idCartella) { this.idCartella = idCartella; }
    public Set<String> getCollaboratori() { return collaboratori; }

    public boolean isModificabile(String user) {
        return proprietario.equals(user) && !solaLettura;
    }
}
