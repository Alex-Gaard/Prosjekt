package boligformidling;

import java.sql.SQLException;

/**
 * Inneholder metoder for å hente ut og bearbeide informasjon fra databasen.
 * Metodene er relatert til brukere.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class Data_Bruker extends Database {

	// Tabeller
	final static String TABLE_BRUKER = "bruker";
	final static String TABLE_SØKER = "boligsøker";
	final static String TABLE_UTLEIER = "utleier";
	final static String TABLE_PASSORD_REGISTER = "bruker_passordregister";
	final static String TABLE_BEHANDLER = "kundebehandler";

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

	/**
	 * Setter inn en ny bruker i databasen. Utifra om innsettingen var vellykket
	 * eller ikke, vil metoden returnere true/false.
	 * 
	 * @param personnummer
	 *            Personnummeret til brukeren.
	 * @param navn
	 *            Navnet til brukeren.
	 * @param adresse
	 *            Adressen til brukeren.
	 * @param email
	 *            Emailen til brukeren.
	 * @param telefon
	 *            Telefonnummeret til brukeren.
	 * @param postnummer
	 *            Postnummeret til brukeren.
	 * @return true/false.
	 */
	public static boolean insertBruker(String personnummer, String navn,
			String adresse, String email, String telefon, String postnummer) {
		String sql = "insert into " + TABLE_BRUKER + "(" + COLUMN_PERSONNUMMER
				+ "," + COLUMN_NAVN + "," + COLUMN_ADRESSE + "," + COLUMN_EMAIL
				+ "," + COLUMN_TELEFON + "," + COLUMN_POSTNUMMER + ","
				+ COLUMN_OPPRETTET + ")" + "values('" + personnummer + "','"
				+ navn + "','" + adresse + "','" + email + "','" + telefon
				+ "','" + postnummer + "'," + "curdate())";

		try {
			execUpdate(sql);
			return true;
		} catch (SQLException ex) {
			System.out.println("Feil i insertBruker: " + ex);
			return false;
		}

	}// End of insertBruker

	/**
	 * Setter inn en ny utleier i databasen. Utifra om innsettingen var
	 * vellykket eller ikke, vil metoden returnere true/false.
	 * 
	 * @param personnummer
	 *            Personnummeret til utleieren.
	 * @param firma
	 *            Firmaet som utleieren representerer.
	 * @return true/false.
	 */
	public static boolean insertUtleier(String personnummer, String firma) {

		String sql = "insert into " + TABLE_UTLEIER + " ("
				+ COLUMN_BRUKER_PERSONNUMMER + "," + COLUMN_FIRMA + ")"
				+ "values('" + personnummer + "','" + firma + "')";

		try {
			execUpdate(sql);
			return true;
		} catch (SQLException ex) {
			System.out.println("Feil i insertBruker: " + ex);
			return false;
		}
	}// end of insertUtleier

	/**
	 * Setter inn en ny boligsøker i databasen. Utifra om innsettingen var
	 * vellykket eller ikke, vil metoden returnere true/false.
	 * 
	 * @param personnummer
	 *            Personnummeret til boligsøkeren.
	 * @return true/false.
	 */
	public static boolean insertSøker(String personnummer) {

		String sql = "insert into " + TABLE_SØKER + "("
				+ COLUMN_BRUKER_PERSONNUMMER + ")" + " values('" + personnummer
				+ "')";

		try {
			execUpdate(sql);
			return true;
		} catch (SQLException ex) {
			System.out.println("Feil i insertBruker: " + ex);
			return false;
		}

	}// end of insertSøker

	/**
	 * Setter inn en ny kundebehandler i databasen. Utifra om innsettingen var
	 * vellykket eller ikke, vil metoden returnere true/false.
	 * 
	 * @param personnummer
	 *            Personnummeret til behandleren.
	 * @return true/false.
	 */
	public static boolean insertBehandler(String personnummer) {

		String sql = "insert into " + TABLE_BEHANDLER + "("
				+ COLUMN_BRUKER_PERSONNUMMER + ")" + " values('" + personnummer
				+ "')";

		try {
			execUpdate(sql);
			return true;
		} catch (SQLException ex) {
			System.out.println("Feil i insertBehandler: " + ex);
			return false;
		}

	}// end of insertBehandler

	/**
	 * Setter inn et nytt passord og bruker inn i passord registeret i
	 * databasen. Utifra om innsettingen var vellykket eller ikke, vil metoden
	 * returnere true/false.
	 * 
	 * @param personnummer
	 *            Personnummeret til brukeren.
	 * @param passord
	 *            Passordet til brukeren.
	 * @return true/false.
	 */
	public static boolean insertPassord(String personnummer, String passord) {

		String sql = "insert into " + TABLE_PASSORD_REGISTER + "("
				+ COLUMN_BRUKER_PERSONNUMMER + "," + COLUMN_PASSORD + ")"
				+ " values('" + personnummer + "',password('" + passord + "'))";

		try {
			execUpdate(sql);
			return true;
		} catch (SQLException ex) {
			System.out.println("Feil i insertBruker: " + ex);
			return false;
		}

	}// end of insertPassord

}// end of class Data_Bruker
