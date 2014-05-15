package boligformidling;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Inneholder metoder for å hente ut og bearbeide informasjon fra databasen.
 * Metodene er relatert til "logg inn" funksjonaliteten.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class Data_Login extends Database {

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

}// end of class Data_Login

