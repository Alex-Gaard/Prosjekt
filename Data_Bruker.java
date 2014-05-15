package boligformidling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inneholder metoder for å hente ut og bearbeide informasjon fra databasen.
 * Metodene er relatert til brukere.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class Data_Bruker extends Database {

	/**
	 * Setter inn en ny bruker i databasen. Hvis innsettingen var vellykket vil
	 * metoden returnere true. Hvis en feil skulle oppstå vil metoden throwe en
	 * SQLException.
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
	 * @throws SQLException
	 *             Hvis en feil skulle oppstå under innsettingen.
	 * @return true/throw SQLException.
	 * 
	 */
	public static boolean insertBruker(String personnummer, String navn,
			String adresse, String email, String telefon, String postnummer)
			throws SQLException {

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
			throw ex;
		}

	}// End of insertBruker

	/**
	 * Setter inn en ny utleier i databasen. Ut ifra om innsettingen var
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
	 * Setter inn en ny boligsøker i databasen. Ut ifra om innsettingen var
	 * vellykket eller ikke, vil metoden returnere true/false.
	 * 
	 * @param personnummer
	 *            Personnummeret til boligsøkeren.
	 * @return true/false.
	 */
	public static boolean insertSøker(String personnummer) {

		String sql = "insert into " + TABLE_BOLIGSØKER + "("
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
	 * Setter inn en ny kundebehandler i databasen. Ut ifra om innsettingen var
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
	 * databasen. Ut ifra om innsettingen var vellykket eller ikke, vil metoden
	 * returnere true/false.
	 * 
	 * @param personnummer
	 *            Personnummeret til brukeren.
	 * @param passord
	 *            Passordet til brukeren.
	 * @return true/false.
	 */
	public static boolean insertPassord(String personnummer, String passord) {

		String sql = "insert into " + TABLE_BRUKER_PASSORD_REGISTER + "("
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

	// OBS NY Petter
	/**
	 * 
	 * @param personnummer
	 * @param navnstring
	 * @param adressestring
	 * @param emailstring
	 * @param tlfstring
	 * 
	 *            Gir klassen mulighet til å oppdatere en brukers data.
	 * 
	 *            <p>
	 *            Det er ikke mulig å oppdatere bare en kolonne omgangen, alt må
	 *            gjøres samtidig.
	 * 
	 * @author Petter Gjerstad
	 * @version 1.00, 14 mai 2014
	 * @throws SQLException
	 */
	public static void updateBruker(String personnummer, String navnstring,
			String adressestring, String emailstring, String tlfstring)
			throws SQLException {
		String sql = "UPDATE " + TABLE_BRUKER + " " + "SET " + COLUMN_NAVN
				+ "='" + navnstring + "', " + COLUMN_ADRESSE + "='"
				+ adressestring + "', " + COLUMN_EMAIL + "='" + emailstring
				+ "', " + COLUMN_TELEFON + "='" + tlfstring + "' " + "WHERE "
				+ COLUMN_PERSONNUMMER + "='" + personnummer + "';";
		execUpdate(sql);
	}

	// OBS NY Petter
	/**
	 * Gir klassen mulighet til å slette et spesifik personnummer fra bruker;
	 * 
	 * <p>
	 * Tenkt brukt som del av slett profil
	 * 
	 * @param personnummer
	 * @throws SQLException
	 * 
	 * @author Petter Gjerstad
	 * @version 1.00, 14 mai 2014
	 */
	public static void deleteBruker(String personnummer) throws SQLException {
		String sql = "delete from " + TABLE_BRUKER + " where "
				+ COLUMN_PERSONNUMMER + "=" + personnummer + ";"; // todo
		execUpdate(sql);
	}

	// OBS NY Petter
	/**
	 * 
	 * @param PersNrT
	 * @param NavnT
	 * @param AdresseT
	 * @param EmailT
	 * @param TelefonT
	 *            Returnerer et resultat basert på SubPanel_Kunder sine
	 *            søkeparametre.
	 * 
	 *            <p>
	 *            Metoden konstruerer en streng basert på søkeparametrene. Hvis
	 *            parametrene er tomme(null) gjøres et generelt søk.
	 * 
	 * @return
	 * @author Petter Gjerstad
	 * @version 1.00, 14 mai 2014
	 */
	public static ResultSet querykonstruktør(String PersNrT, String NavnT,
			String AdresseT, String EmailT, String TelefonT) // Konstruerer en
																// query når man
																// trykker søk.
	{

		String and = "and";
		String where = "where ";
		Boolean blank = true;

		String[] felt = new String[5];

		if (!PersNrT.equals("")) {
			where += COLUMN_PERSONNUMMER + "='" + PersNrT + "'";
			blank = false;
		}
		if (!NavnT.equals("")) {
			if (blank) {
				where += COLUMN_NAVN + "='" + NavnT + "'";
				blank = false;
			} else {
				where += "AND " + COLUMN_NAVN + "='" + NavnT + "'";
			}
		}
		if (!AdresseT.equals("")) {
			if (blank) {
				where += COLUMN_ADRESSE + "='" + AdresseT + "'";
				blank = false;
			} else {
				where += "AND " + COLUMN_ADRESSE + "='" + AdresseT + "'";
			}
		}
		if (!EmailT.equals("")) {
			if (blank) {
				where += COLUMN_EMAIL + "='" + EmailT + "'";
				blank = false;
			} else {
				where += "AND " + COLUMN_EMAIL + "='" + EmailT + "'";
			}
		}
		if (!TelefonT.equals("")) {
			if (blank) {
				where += COLUMN_TELEFON + "='" + TelefonT + "'";
				blank = false;
			} else {
				where += "AND " + COLUMN_TELEFON + "='" + TelefonT + "'";
			}
		}
		if (blank) {
			where = "where " + COLUMN_PERSONNUMMER + " not in(select * from "
					+ TABLE_BEHANDLER + ")";
		} else {
			where += "AND " + COLUMN_PERSONNUMMER + " not in (select * from "
					+ TABLE_BEHANDLER + ")";
		}

		String Query = "select * from " + TABLE_BRUKER + " " + where
				+ " order by ABS(Personnummer)" + ";";
		try {
			return Data_Boliger.execQuery(Query);
		} catch (SQLException ex) {
			Logger.getLogger(SubPanel_Kunder.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;
	}
        
	/**
	 * Sjekker om en bruker med gitt id matcher gitt passord. Ut ifra om en
	 * bruker ble funnet eller ikke, vil metoden returnere true/false.
	 * 
	 * @param id
	 *            ID til brukeren.
	 * @param passord
	 *            Passordet til brukeren.
	 * @return true/false.
	 */
	public static boolean userMatchesPassword(String id, String passord) {

		String sql = "select * from " + TABLE_BRUKER_PASSORD_REGISTER
				+ " where " + COLUMN_BRUKER_PERSONNUMMER + " = '" + id + "'"
				+ " and " + COLUMN_PASSORD + " = password('" + passord + "')";

		try (ResultSet rs = execQuery(sql)) {
			rs.last();

			if (rs.getRow() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException ex) {
			System.out.println("Feil i userExists: " + ex);
			return false;
		} catch (NullPointerException npe) {
			System.out.println("Feil i userExists, rs er null: " + npe);
			return false;
		}

	}// end of userMatchesPassword

	/**
	 * Sjekker om bruker med gitt id finnes i gitt tabell. Ut ifra om en bruker
	 * ble funnet eller ikke, vil metoden returnere true/false.
	 * 
	 * @param table
	 *            Tabellen som skal sjekkes.
	 * @param id
	 *            ID til brukeren.
	 * @return true/false.
	 */
	public static boolean userExists(String table, String id) {

		String sql = "select * from " + table + " where "
				+ COLUMN_BRUKER_PERSONNUMMER + " = '" + id + "'";

		try (ResultSet rs = execQuery(sql)) {
			rs.last();

			if (rs.getRow() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException ex) {
			System.out.println("Feil i userExists: " + ex);
			return false;
		} catch (NullPointerException npe) {
			System.out.println("Feil i userExists, rs er null: " + npe);
			return false;
		}

	}// end of userExists

	/**
	 * Returnerer et ResultSet som inneholder informasjon om en bruker Ut ifra
	 * spesifisert id.
	 * 
	 * @param id
	 *            ID til brukeren.
	 * @return ResultSet.
	 * @throws SQLException
	 *             hvis en feil skulle oppstå under spørringen.
	 */
	public static ResultSet getBrukerResult(String id) throws SQLException {

		String sql = "select * from " + TABLE_BRUKER + " where "
				+ COLUMN_PERSONNUMMER + " = '" + id + "'";

		ResultSet rs = execQuery(sql);
		if (rs != null) {
			return rs;
		}

		return null;

	}// end of getBrukerResult
}// end of class Data_Bruker
