package it.unibo.progettonote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Nota implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String titolo;
    private String contenuto;
    private Date dataCreazione;
    private Date dataUltimaModifica;
    private String proprietario;
    private String idCartella;
    private List<VersioneNota> versioni = new ArrayList<>();

    public Nota() {}

    public Nota(String titolo, String contenuto, String proprietario) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.proprietario = proprietario;
        this.dataCreazione = new Date();
        this.dataUltimaModifica = new Date();
    }

    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setProprietario(String proprietario) { this.proprietario = proprietario; }
    public void setIdCartella(String idCartella) { this.idCartella = idCartella; }
    public void setDataCreazione(Date date) { this.dataCreazione = date; }
    public void setDataUltimaModifica(Date date) { this.dataUltimaModifica = date; }
    public List<VersioneNota> getVersioni() { return versioni; }
}