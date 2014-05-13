package Boligformidling;

import java.awt.Image;
/**
 * Representerer en bolig ifra databasen
 */
public class Bolig{
    private int id;
    private String eier;
    private String adresse;
    private int postadresse;
    private int areal;
    private int rom;
    private int byggår;
    private String beskrivelse;
    private String avertert;
    private long pris;

    public Bolig(int ID, String EIER, String ADRESSE, int POSTADRESSE, int AREAL, int ROM, int BYGGÅR, String BESKRIVELSE, String AVERTERT, long PRIS) {
        id = ID;
        eier = EIER;
        adresse = ADRESSE;
        postadresse = POSTADRESSE;
        areal = AREAL;
        rom = ROM;
        byggår = BYGGÅR;
        beskrivelse = BESKRIVELSE;
        avertert = AVERTERT;
        pris = PRIS;
    }
    Image getBilde(int index){
        return Data_Boliger.getBilde(this, index);
    }
    int getAntallBilder(){
        return Data_Boliger.getAntallBilder(this);
    }
    String getEier(){
        return eier;
    }
    String getAvertert() {
        return avertert;
    }
    long getPris() {
        return pris;
    }
    int getId() {
        return id;
    }
    String getAdresse() {
        return adresse;
    }
    int getPostadresse() {
        return postadresse;
    }
    int getAreal() {
        return areal;
    }
    int getAntRom() {
        return rom;
    }
    int getByggår() {
        return byggår;
    }
    String getBesk() {
        return beskrivelse;
    }
    public String toString() {
        return "Addresse: " + adresse + "\n"
                + "Postadresse: " + postadresse + "\n"
                + "Areal: " + areal + "\n"
                + "Rom: " + rom + "\n"
                + "Byggår: " + byggår + "\n"
                + "Beskrivelse: " + beskrivelse + "\n";
    }
}//End class bolig