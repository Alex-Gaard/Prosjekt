package boligformidling;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klassen representerer tabellen Leiekontrakt_forespørseler.
 *
 * Klassen har alle relevante select,delete,update og insert * metoder.
 * @author Petter Gjerstad
 * @version 1.00, 14 mai 2014
 */
public class Data_Leiekontrakt_Forespørsel extends Database
{

    // Tabeller
    final static String TABLE_LEIEKONTRAKT = "Leiekontrakt";
    final static String TABLE_BOLIG = "Bolig";
    final static String TABLE_LEIEKONTRAKT_FORESPØRSEL = "Leiekontrakt_forespørsel";

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
    final static String COLUMN_LEIEKONTRAKTFORESPØRSEL_PERSONNUMMER = "Leiekontrakt_forespørsel.Boligsøker_Bruker_Personnummer";

    static void Update(String boligID, String Opprettet, String Kundebehandler, String påtatt, String personnummer) throws SQLException
    {
        String sql = "UPDATE " + TABLE_LEIEKONTRAKT_FORESPØRSEL + " "
                + "SET "
                + COLUMN_BOLIG_BOLIG_ID + "='" + boligID + "'," + COLUMN_OPPRETTET_DATO + "='" + Opprettet + "'," + COLUMN_BEHANDLER_BRUKER_PERSONNUMMER + "='"
                + Kundebehandler + "'," + COLUMN_PÅTATT + "='" + påtatt + "' "
                + "WHERE " + COLUMN_BOLIGSØKER_PERSONNUMMER + "=" + personnummer + ";";
        execUpdate(sql);
    }

    static ResultSet selectAll(String boligsøker) throws SQLException
    {
        String sql = "select * from " + TABLE_LEIEKONTRAKT_FORESPØRSEL + " where " + COLUMN_LEIEKONTRAKTFORESPØRSEL_PERSONNUMMER + "=" + boligsøker + ";";
        return execQuery(sql);
    }

}
