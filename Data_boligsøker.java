package boligformidling;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klassen representerer tabellene søkerinfo, søkerkrav og boligsøker.
 * <p>
 * Sistnevnte er supertypen til de to foregående, derfor hører disse sammen.
 * <p>
 * Foruten enkle select,insert,update og delete statements, kan man gjøre noen hakket
 * mer kompliserte inner joins
 * <p>
 * Samtlige metoder er rimelig rett frem med unntak av de to siste, som er
 * forklart med egne kommentarer
 *
 * @author Petter Gjerstad
 * @version 1.00, 14 mai 2014
 */
public class Data_boligsøker extends Database
{

    // Tabeller
    final static String TABLE_LEIEKONTRAKT = "Leiekontrakt";
    final static String TABLE_BOLIG = "Bolig";
    final static String TABLE_LEIEKONTRAKT_FORESPØRSEL = "Leiekontrakt_forespørsel";
    final static String TABLE_VISTE_BOLIGER = "VIste_Boliger";
    final static String TABLE_SØKERINFO = "SøkerInfo";
    final static String TABLE_SØKERKRAV = "SøkerKrav";
    final static String TABLE_BOLIGSØKER = "Boligsøker";

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
    final static String COLUMN_PEIS = "Peis";
    final static String COLUMN_PARKERING = "Parkering";
    final static String COLUMN_BOLIGSØKER_BRUKER_PERSONNUMMER = "Boligsøker.Bruker_Personnummer";
    final static String COLUMN_SØKERKRAV_BOLIGSØKER_BRUKER_PERSONNUMMER = "SøkerKrav.Boligsøker_Bruker_Personnummer";
    final static String COLUMN_ANTALL_PERSONER = "Antall_personer";
    final static String COLUMN_SIVILSTATUS = "Sivilstatus";
    final static String COLUMN_YRKE = "Yrke";
    final static String COLUMN_RØYKER = "Røyker";
    final static String COLUMN_HUSDYR = "Husdyr";

    public static void deletespecfromdSøkerInfo(String personnummer) throws SQLException
    {
        String sql = "delete from " + TABLE_SØKERINFO + " where " + COLUMN_BOLIGSØKER_PERSONNUMMER + "=" + personnummer + ";";
        execUpdate(sql);
    }

    public static void deletespecfromSøkerKrav(String personnummer) throws SQLException
    {
        String sql = "delete from " + TABLE_SØKERKRAV + " where " + COLUMN_BOLIGSØKER_PERSONNUMMER + "=" + personnummer + ";";
        execUpdate(sql);
    }

    public static void deletespecfromBoligsøker(String personnummer) throws SQLException
    {
        String sql = "delete from " + TABLE_BOLIGSØKER + " where " + COLUMN_BRUKER_PERSONNUMMER + "=" + personnummer + ";";
        execUpdate(sql);
    }

    public static ResultSet selectspecRSfromboligsøker(String personnummer) throws SQLException
    {
        String sql = "select * from " + TABLE_BOLIGSØKER + " where " + COLUMN_BRUKER_PERSONNUMMER + "=" + personnummer + ";";
        return execQuery(sql);
    }

    public static ResultSet selectspecRSfromsøkerinfo(String personnummer) throws SQLException
    {
        String sql = "select * from " + TABLE_SØKERINFO + " where " + COLUMN_BOLIGSØKER_PERSONNUMMER + "='" + personnummer + "';";
        return execQuery(sql);
    }

    public static ResultSet selectspecRSfromSøkerkrav(String personnummer) throws SQLException
    {
        String sql = "select * from " + TABLE_SØKERKRAV + " where " + COLUMN_BOLIGSØKER_PERSONNUMMER + "='" + personnummer + "';";
        return execQuery(sql);
    }

    public static void insertspecintoboligsøker(String personnummer) throws SQLException
    {
        String sql = "insert into " + TABLE_BOLIGSØKER + " (" + COLUMN_BRUKER_PERSONNUMMER + ") VALUES('" + personnummer + "');";
        execUpdate(sql);
    }

    public static void insertintosøkerinfo(String personnummer, int ant_persint, String sivilstatusstring, String yrkestring, int røykerint, int husdyrint) throws SQLException
    {
        String sql = "Insert into " + TABLE_SØKERINFO + " (" + COLUMN_BOLIGSØKER_PERSONNUMMER + "," + COLUMN_ANTALL_PERSONER
                + "," + COLUMN_SIVILSTATUS + "," + COLUMN_YRKE + "," + COLUMN_RØYKER + "," + COLUMN_HUSDYR + ") "
                + "VALUES('" + personnummer + "','" + ant_persint + "','"
                + sivilstatusstring + "','" + yrkestring + "'," + røykerint + ",'" + husdyrint + "');";
        execUpdate(sql);
    }

    public static void insertintosøkerkrav(String personnummer, int minareal, int maxareal, int antsoverom, int byggår, int minpris, int maxpris, int peis, int parkering) throws SQLException
    {
        String sql = "Insert into " + TABLE_SØKERKRAV + " (" + COLUMN_BOLIGSØKER_PERSONNUMMER
                + "," + COLUMN_MIN_AREAL + "," + COLUMN_MAX_AREAL + "," + COLUMN_MIN_SOVEROM + "," + COLUMN_MIN_BYGGÅR
                + "," + COLUMN_MIN_PRIS + "," + COLUMN_MAX_PRIS + "," + COLUMN_PEIS + "," + COLUMN_PARKERING + ") "
                + "VALUES('" + personnummer + "'," + minareal + "," + maxareal + "," + antsoverom + ","
                + byggår + "," + minpris + "," + maxpris + "," + peis + "," + parkering + "); ";
        execUpdate(sql);

    }

    public static void updatesøkerkrav(int minareal, int maxareal, int antsoverom, int minpris, int maxpris, int peis, int parkering, String personnummer, int byggår) throws SQLException
    {
        String sql = "UPDATE " + TABLE_SØKERKRAV + " "
                + "SET "
                + COLUMN_MIN_AREAL + "='" + minareal + "'," + COLUMN_MAX_AREAL + "='" + maxareal + "'," + COLUMN_MIN_SOVEROM + "='" + antsoverom + "',"
                + COLUMN_MIN_BYGGÅR + "='" + byggår + "'," + COLUMN_MIN_PRIS + "='" + minpris + "'," + COLUMN_MAX_PRIS + "='"
                + maxpris + "', " + COLUMN_PEIS + "='" + peis + "'," + COLUMN_PARKERING + "='" + parkering + "' "
                + "WHERE " + COLUMN_BOLIGSØKER_PERSONNUMMER + "='" + personnummer + "';";
        execUpdate(sql);
    }

    public static void updatesøkerinfo(int ant_pers, String sivilstatus, String yrke, int røyker, int husdyr, String personnummer) throws SQLException
    {
        String sql = "UPDATE " + TABLE_SØKERINFO + " "
                + "SET "
                + "Antall_personer='" + ant_pers + "', Sivilstatus='" + sivilstatus + "', Yrke='" + yrke + "'"
                + ",Røyker='" + røyker + "', Husdyr='" + husdyr + "' "
                + "WHERE " + COLUMN_BOLIGSØKER_PERSONNUMMER + "='" + personnummer + "';";
        execUpdate(sql);
    }

    /**
     *
     * @param PersNr
     * @return
     * @throws SQLException
     */
    static ResultSet selectAllboligsøkernaturaljoinsøkerinfo(String PersNr) throws SQLException
    {
        String sql = "select * from " + TABLE_BOLIGSØKER + " inner join " + TABLE_SØKERINFO + ""
                + " where " + COLUMN_BOLIGSØKER_BRUKER_PERSONNUMMER + "=" + TABLE_SØKERINFO + "." + COLUMN_BOLIGSØKER_PERSONNUMMER + ""
                + " and " + TABLE_SØKERINFO + "." + COLUMN_BOLIGSØKER_PERSONNUMMER + "=" + PersNr + ";";
        return execQuery(sql);
    }

    static ResultSet selectALlboligsøkernaturaljoinsøkerkrav(String PersNr) throws SQLException
    {
        String sql = "select " + COLUMN_MIN_AREAL + "," + COLUMN_MAX_AREAL + "," + COLUMN_MIN_SOVEROM + "," + COLUMN_MIN_BYGGÅR + ","
                + COLUMN_MIN_PRIS + "," + COLUMN_MAX_PRIS + "," + COLUMN_PEIS + "," + COLUMN_PARKERING + " from " + TABLE_BOLIGSØKER + " inner join " + TABLE_SØKERKRAV + ""
                + " where " + COLUMN_BOLIGSØKER_BRUKER_PERSONNUMMER + "=" + COLUMN_SØKERKRAV_BOLIGSØKER_BRUKER_PERSONNUMMER
                + " and " + COLUMN_SØKERKRAV_BOLIGSØKER_BRUKER_PERSONNUMMER + "=" + PersNr + ";";
        return execQuery(sql);
    }

}
