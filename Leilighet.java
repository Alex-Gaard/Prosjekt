package boligformidling;
/**
* Representerer en Leilighet ifra databasen.
* @author Arlen Syver Wasserman, s193956, IT 1 år
* @version 1.0 13 Mai 2014
*/
public class Leilighet extends Bolig {
    private int etasje;
    private boolean heis;
    private boolean balkong;
    private boolean garasje;
    private boolean fellesvask;

    public Leilighet(int ID, String EIER, String ADRESSE, int POSTADRESSE, int AREAL, int ROM, int BYGGÅR, String BESKRIVELSE, String AVERTERT, long PRIS, int ETASJE, boolean HEIS, boolean BALKONG, boolean GARASJE, boolean FELLESVASK) {
        super(ID, EIER, ADRESSE, POSTADRESSE, AREAL, ROM, BYGGÅR, BESKRIVELSE, AVERTERT, PRIS);
        etasje = ETASJE;
        heis = HEIS;
        balkong = BALKONG;
        garasje = GARASJE;
        fellesvask = FELLESVASK;
    }

    int getEtasje() {
        return etasje;
    }

    boolean getHeis() {
        return heis;
    }

    boolean getBalkong() {
        return balkong;
    }

    boolean getGarasje() {
        return garasje;
    }

    boolean getFellesvask() {
        return fellesvask;
    }

    public String toString() {
        return super.toString()
                + "Etasje: " + etasje + "\n"
                + "Heis: " + heis + "\n"
                + "Balkong: " + balkong + "\n"
                + "Garasje: " + garasje + "\n"
                + "Fellesvask: " + fellesvask + "\n";
    }
}//End class Leilighet
