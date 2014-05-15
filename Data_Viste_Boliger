package boligformidling;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klassen representerer tabellen Viste_Boliger.
 * <p> Klassen har alle relevante select,delete,update og insert * metoder.
 *
 * @author Petter Gjerstad
 * @version 1.00, 14 mai 2014
 */
public class Data_Viste_Boliger extends Database
{

    // Tabeller
    final static String TABLE_LEIEKONTRAKT = "Leiekontrakt";
    final static String TABLE_BOLIG = "Bolig";
    final static String TABLE_LEIEKONTRAKT_FORESPØRSEL = "Leiekontrakt_forespørsel";
    final static String TABLE_VISTE_BOLIGER = "Viste_Boliger";

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

    public static void deletespecfromViste_Boliger(String personnummer) throws SQLException
    {
        String sql = "delete from " + TABLE_VISTE_BOLIGER + " where " + COLUMN_BRUKER_PERSONNUMMER + "=" + personnummer + ";";
        execUpdate(sql);
    }

    public static ResultSet selectspecRSfromViste_Boliger(String personnummer) throws SQLException
    {
        String sql = "select * from " + TABLE_VISTE_BOLIGER + " where " + COLUMN_BRUKER_PERSONNUMMER + "='" + personnummer + "';";
        return execQuery(sql);
    }

    public static void insertSpecintoViste_Boliger(String personnummer, int BoligID) throws SQLException
    {
        String sql = "Insert into " + TABLE_VISTE_BOLIGER + " (" + COLUMN_BOLIG_BOLIG_ID + "," + COLUMN_BRUKER_PERSONNUMMER + ") "
                + "VALUES(" + BoligID + "," + personnummer + "); ";
        execUpdate(sql);
    }

}
