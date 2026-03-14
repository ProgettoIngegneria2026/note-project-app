package it.unibo.progettonote.client; // <-- CAMBIA QUESTO IN BASE AL TUO PACKAGE ORIGINALE

import java.io.Serializable;
import java.util.Date;

public class VersioneNota implements Serializable {
    private int numeroVersione;
    private String contenuto;
    private String autoreModifica;
    private Date dataModifica;

    public VersioneNota() {}

    public VersioneNota(int numeroVersione, String contenuto, String autoreModifica, Date dataModifica) {
        this.numeroVersione = numeroVersione;
        this.contenuto = contenuto;
        this.autoreModifica = autoreModifica;
        this.dataModifica = dataModifica;
    }

    public int getNumeroVersione() { return numeroVersione; }
    public void setNumeroVersione(int numeroVersione) { this.numeroVersione = numeroVersione; }
    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }
    public String getAutoreModifica() { return autoreModifica; }
    public void setAutoreModifica(String autoreModifica) { this.autoreModifica = autoreModifica; }
    public Date getDataModifica() { return dataModifica; }
    public void setDataModifica(Date dataModifica) { this.dataModifica = dataModifica; }
}