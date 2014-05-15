package boligformidling;

/**
 * Klassen representerer tabellen Utleier.
 *
 * @author Petter Gjerstad
 * @version 1.00, 14 mai 2014
 */
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klassen representerer tabellen utleier.
 * Klassen har alle relevante select,delete,update og insert * metoder.
 *
 * @author bush
 */
public class Data_utleier extends Database
{

    // Tabeller
    final static String TABLE_LEIEKONTRAKT = "Leiekontrakt";
    final static String TABLE_BOLIG = "Bolig";
    final static String TABLE_LEIEKONTRAKT_FORESPØRSEL = "Leiekontrakt_forespørsel";
    final static String TABLE_VISTE_BOLIGER = "VIste_Boliger";
    final static String TABLE_SØKERINFO = "SøkerInfo";
    final static String TABLE_SØKERKRAV = "SøkerKrav";
    final static String TABLE_BOLIGSØKER = "Boligsøker";
    final static String TABLE_UTLEIER = "Utleier";

    // Kolonner
    final static String COLUMN_BOLIG_BOLIG_ID = "Bolig_BoligID";
    final static String COLUMN_UTLEIER_PERSONNUMMER = "Utleier_Bruker_Personnummer";
    final static String COLUMN_BOLIGSØKER_PERSONNUMMER = "Boligsøker_Bruker_Personnummer";
    final static String COLUMN_BEHANDLER_PERSONNUMMER = "Kundebehandler_Bruker_Personnummer";
    final static String COLUMN_BRUKER_PERSONNUMMER = "Bruker_Personnummer";
    final static String COLUMN_LEIEPRIS = "Leiepris_pr_måned";
    final static String COLUMN_AVTALE_START = "Avtale_start";
    final static String COLUMN_AVTALE_SLUTT = "Avtale_slutt";
    final static String COLUMN_OPPRETTET_DATO = "Opprettet_Dato";
    final static String COLUMN_BEHANDLER_BRUKER_PERSONNUMMER = "Kundebehandler_Bruker_Personnummer";
    final static String COLUMN_BOLIG_ID = "BoligID";
    final static String COLUMN_PÅTATT = "Påtatt";
    final static String COLUMN_LEIEKONTRAKTFORESPØRSEL_PERSONNUMMER = "Leiekontrakt_Forespørsel.Boligsøker_Bruker_Personnummer";

    final static String COLUMN_MIN_AREAL = "Min_Areal";
    final static String COLUMN_MAX_AREAL = "Max_Areal";
    final static String COLUMN_MIN_SOVEROM = "Min_Soverom";
    final static String COLUMN_MIN_BYGGÅR = "Min_Byggår";
    final static String COLUMN_MIN_PRIS = "Min_Pris";
    final static String COLUMN_MAX_PRIS = "Max_Pris";
    final static String COLUMN_PEIS = "Peis;";
    final static String COLUMN_PARKERING = "Parkering";
    final static String COLUMN_BOLIGSØKER_BRUKER_PERSONNUMMER = "Boligsøker.Bruker_Personnummer";
    final static String COLUMN_SØKERKRAV_BOLIGSØKER_BRUKER_PERSONNUMMER = "SøkerKrav.Boligsøker_Bruker_Personnummer";
    final static String COLUMN_ANTALL_PERSONER = "Antall_personer";
    final static String COLUMN_SIVILSTATUS = "Sivilstatus";
    final static String COLUMN_YRKE = "Yrke";
    final static String COLUMN_RØYKER = "Røyker";
    final static String COLUMN_HUSDYR = "Husdyr";
    final static String COLUMN_FIRMA = "Firma";

    public static ResultSet selectspecRSfromUtleier(String personnummer) throws SQLException
    {
        String sql = "select * from " + TABLE_UTLEIER + " where " + COLUMN_BRUKER_PERSONNUMMER + "=" + personnummer + ";";
        return execQuery(sql);
    }

    public static void insertspecintoutleier(String personnummer, String firmanavn) throws SQLException
    {
        String sql = "Insert into " + TABLE_UTLEIER + " (" + COLUMN_BRUKER_PERSONNUMMER + "," + COLUMN_FIRMA + ") VALUES('" + personnummer + "','" + firmanavn + "'); ";
        execUpdate(sql);
    }

    public static void updateutleier(String Firma, String personnummer) throws SQLException
    {
        String sql = "UPDATE " + TABLE_UTLEIER + " "
                + "SET "
                + "" + COLUMN_FIRMA + "='" + Firma + "' "
                + "WHERE " + COLUMN_BRUKER_PERSONNUMMER + "=" + personnummer + ";";
        execUpdate(sql);
    }

    public static void delete(String personnummer) throws SQLException
    {
        String sql = "delete from " + TABLE_UTLEIER + " where " + COLUMN_BRUKER_PERSONNUMMER + "=" + personnummer + ";";
        execUpdate(sql);
    }
}
