package Boligformidling;
/**
 * Representerer en Enebolig i databasen.
 * @author Arlen Syver Wasserman, s193956, IT 1 år
 * @version 1.0 13 Mai 2014
 */
public class Enebolig extends Bolig {

    private int etasjer;
    private boolean kjeller;
    private int total_areal;

    public Enebolig(int ID, String EIER, String ADRESSE, int POSTADRESSE, int AREAL, int ROM, int BYGGÅR, String BESKRIVELSE, String AVERTERT, long PRIS, int ETASJER, boolean KJELLER, int TOTAL_AREAL) {
        super(ID, EIER, ADRESSE, POSTADRESSE, AREAL, ROM, BYGGÅR, BESKRIVELSE, AVERTERT, PRIS);
        etasjer = ETASJER;
        kjeller = KJELLER;
        total_areal = TOTAL_AREAL;
    }
    int getEtasjer() {
        return etasjer;
    }
    boolean getKjeller() {
        return kjeller;
    }
    int getTotalAreal() {
        return total_areal;
    }
    public String toString() {
        return super.toString()
                + "Etasjer: " + etasjer + "\n"
                + "Kjeller: " + kjeller + "\n"
                + "Total Areal: " + total_areal + "\n";
    }
}//End class Enebolig