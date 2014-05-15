package boligformidling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klassen tar seg av alle koblinger og spørringer opp mot databasen.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public abstract class Database {
        
	// SQL objekter
	private static Connection connect = null;
	private static Statement statement = null;

	// Databasekobling variabler
	private final static String server = "student.cs.hioa.no";
	private final static String database = "s193956";
	private final static String brukernavn = "s193956";
	private final static String passord = "passord";
	
	// Tabeller
	final static String TABLE_BRUKER = "Bruker";
	final static String TABLE_UTLEIER = "Utleier";
	final static String TABLE_BOLIGSØKER = "Boligsøker";

        
        
	final static String TABLE_BRUKER_PASSORD_REGISTER = "Bruker_PassordRegister";
	final static String TABLE_BEHANDLER = "Kundebehandler";
	
	final static String TABLE_BOLIG = "Bolig";
	final static String TABLE_ENEBOLIG_OG_REKKEHUS = "Enebolig_og_Rekkehus";
	final static String TABLE_LEILIGHET = "Leilighet";
	final static String TABLE_VISTE_BOLIGER = "Viste_Boliger";
	
	final static String TABLE_LEIEKONTRAKT = "Leiekontrakt";
	final static String TABLE_LEIEKONTRAKT_FORESPØRSEL = "Leiekontrakt_forespørsel";
	
	final static String TABLE_SØKERINFO = "SøkerInfo";
	final static String TABLE_SØKERKRAV = "SøkerKrav";

        static final String TABLE_BILDE = "Bolig_bilde";

	// Kolonner
	final static String COLUMN_PERSONNUMMER = "Personnummer";
	final static String COLUMN_NAVN = "Navn";
	final static String COLUMN_ADRESSE = "Adresse";
	final static String COLUMN_EMAIL = "Email";
	final static String COLUMN_TELEFON = "Telefon";
	final static String COLUMN_POSTNUMMER = "Postnummer";
	final static String COLUMN_OPPRETTET = "Opprettet";
	final static String COLUMN_BRUKER_PERSONNUMMER = "Bruker_Personnummer";
	final static String COLUMN_FIRMA = "Firma";
	final static String COLUMN_PASSORD = "Passord";
	
	final static String COLUMN_BOLIG_BOLIG_ID = "Bolig_BoligID";
	final static String COLUMN_UTLEIER_PERSONNUMMER = "Utleier_Bruker_Personnummer";
	final static String COLUMN_BOLIGSØKER_PERSONNUMMER = "Boligsøker_Bruker_Personnummer";
        final static String COLUMN_BEHANDLER_BRUKER_PERSONNUMMER = "Kundebehandler_Bruker_Personnummer";
	final static String COLUMN_LEIEPRIS = "Leiepris_pr_måned";
	final static String COLUMN_AVTALE_START = "Avtale_start";
	final static String COLUMN_AVTALE_SLUTT = "Avtale_slutt";
	
	final static String COLUMN_OPPRETTET_DATO = "Opprettet_Dato";
	final static String COLUMN_BOLIG_ID = "BoligID";
	final static String COLUMN_PÅTATT = "Påtatt";
	final static String COLUMN_AVERTERT = "Avertert";
	final static String COLUMN_UTLEIE_PRIS = "Utleie_pris";

        static final String COLUMN_BOAREAL = "Boareal";
        static final String COLUMN_ANTALL_ROM = "Antall_Rom";
        static final String COLUMN_BESKRIVELSE = "Beskrivelse";
        static final String CLOUMN_BYGGÅR = "Byggår";
        static final String COLUMN_KJELLER = "Kjeller";
        static final String COLUMN_ETASJER = "Antall_etasjer";
        static final String COLUMN_TOTAL_AREAL = "Tomt_areal";
        static final String COLUMN_ETASJE = "Etasje";
        static final String COLUMN_HEIS = "Heis";
        static final String COLUMN_BALKONG = "Balkong";
        static final String COLUMN_GARASJE = "Garasje";
        static final String COLUMN_FELLESVASK = "Fellesvaskeri";
        
        static final  String COLUMN_DIGEST = "Digest";
        static final  String COLUMN_BILDE = "Bilde";
	// Vindu
	private static Vindu_Main vindu = null;

	/**
	 * Setter opp databasetilkobling, Ut ifra resultatet vil metoden returnere
	 * true/false. I tilfelle et problem skulle skje under koblingen vil metoden
	 * ta imot et Vindu_Main objekt for å bruke dette senere til å bytte
	 * MainPanel.
	 * 
	 * @param vm
	 *            Vindu_Main objekt som rammer inn hele GUIen.
	 * @return true/false.
	 */
	public static boolean setupDatabaseConnection(Vindu_Main vm) {
		vindu = vm;
		try {
			setupConnection();
			setupStatement();
			return true;
		} catch (Exception e) {
			System.out.println("Feil i setupDatabase: " + e);
			return false;
		}

	}// end of setupDatabase

	/**
	 * Setter opp koblingen mot databasen.
	 * 
	 * @throws SQLException
	 *             Hvis et problem skulle oppstå under koblingen.
	 * @throws ClassNotFoundException
	 *             Hvis driveren ikke ble funnet.
	 */
	private static void setupConnection() throws SQLException,
			ClassNotFoundException {
		if (connect == null) {

			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://" + server
					+ "/" + database, brukernavn, passord);

		}

	}// end of setupConnection

	/**
	 * Setter opp Statement for database, alle spørringer mot databasen vil bli
	 * gjort gjennom dette "statementet"
	 * 
	 * @throws SQLException
	 *             Hvis et problem skulle oppstå under koblingen, eller metoden
	 *             blir kallet mens connect er lukket.
	 * @throws NullPointerException
	 *             Hvis connect er null.
	 */
	private static void setupStatement() throws SQLException,
			NullPointerException {
		if (statement == null) {

			statement = connect.createStatement();
		}

	}// end of setupStatement

	/**
	 * Returnerer Connection objektet som ble brukt for å koble til databasen.
	 */
	protected static Connection getConnection() {
		if (connect != null) {
			return connect;
		} else {
			return null;
		}
	}// end of getConnection

	/**
	 * Lukker statement og kobling til databasen;
	 */
	protected static void closeConnection() {

		try {
			statement.close();
		} catch (SQLException e) {
			System.out
					.println("feil i closeConnection,kunne ikke lukke statement: "
							+ e);
		} catch (NullPointerException npe) {
			System.out.println("feil i closeConnection,statement er null: "
					+ npe);
		}

		try {
			connect.close();
		} catch (SQLException e) {
			System.out
					.println("feil i closeConnection,kunne ikke lukke connection: "
							+ e);
		} catch (NullPointerException npe) {
			System.out.println("feil i closeConnection,connection er null: "
					+ npe);
		}

		statement = null;
		connect = null;

	}// end of closeConnection

	/**
	 * Sender en spørring til databasen for oppdatering av tabeller. Ut ifra
	 * hvordan spørringen gikk vil metoden returnere true/false.
	 */
	protected static boolean execUpdate(String sql) throws SQLException {

		try {
			if (statement.executeUpdate(sql) != -1) {
				return true;
			}
		} catch (
				com.mysql.jdbc.exceptions.jdbc4.CommunicationsException
				| com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException ex) {

			closeConnection();
			vindu.swap(new MainPanel_NotConnected(vindu, null));
			vindu.checkForConnection();
		}

		return false;

	}// end of execUpdate

	/**
	 * 
	 * Sender en spørring til databasen for å hente ut data fra en eller flere
	 * tabeller. Hvis ingen feil oppstår vil et ResultSet bli returnert. Dersom
	 * koblingen blir avbrutt, eller spørringen som ble sendt hadde syntaks feil
	 * vil metoden returnere null.
	 */
	protected static ResultSet execQuery(String sql) throws SQLException {

		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
		} catch (
				com.mysql.jdbc.exceptions.jdbc4.CommunicationsException
				| com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException ex) {

			closeConnection();
			vindu.swap(new MainPanel_NotConnected(vindu, null));
			vindu.checkForConnection();
		}

		if (rs != null) {
			return rs;
		}
		return null;

	}// end of execQuery

}// end of class Database
