package it.unibo.progettonote;
public class Nota implements Serializable {
    private String titolo;
    private String contenuto; // Max 280 caratteri 
    private Date dataCreazione;
    private Date dataUltimaModifica;
    private String proprietario; // Username dell'utente [cite: 9]
    private String idCartella; // Riferimento alla cartella contenitrice [cite: 10, 194]

    // Costruttore, Getter e Setter
}