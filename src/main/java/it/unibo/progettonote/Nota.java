package it.unibo.progettonote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Nota {

    private String id;
    private String titolo;
    private String contenuto;
    private String proprietario;
    private List<String> collaboratori; // lista di utenti collaboratori
    private String idCartella; // opzionale, cartella dove è salvata
    private Date dataUltimaModifica;

    public Nota() {
        this.collaboratori = new ArrayList<>();
    }
    public Nota(String titolo, String contenuto, String proprietario) {
    this(); // chiama il costruttore vuoto per inizializzare collaboratori
    this.titolo = titolo;
    this.contenuto = contenuto;
    this.proprietario = proprietario;
    this.dataUltimaModifica = new Date(); // opzionale, data corrente
}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public List<String> getCollaboratori() {
        return collaboratori;
    }

    public void setCollaboratori(List<String> collaboratori) {
        this.collaboratori = collaboratori;
    }

    public String getIdCartella() {
        return idCartella;
    }

    public void setIdCartella(String idCartella) {
        this.idCartella = idCartella;
    }

    public Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(Date dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }
}
