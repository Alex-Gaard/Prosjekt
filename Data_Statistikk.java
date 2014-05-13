package Boligformidling;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Inneholder metoder for å hente ut og bearbeide informasjon fra databasen.
 * Metodene er relatert til statistikk.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class Data_Statistikk extends Database {

	// Tabeller
	final static String TABLE_BOLIG = "bolig";
	final static String TABLE_LEIEKONTRAKT_FORESPØRSEL = "leiekontrakt_forespørsel";
	final static String TABLE_BRUKER = "bruker";

	// Kolonner
	final static String COLUMN_AVERTERT = "Avertert";
	final static String COLUMN_UTLEIE_PRIS = "Utleie_pris";
	final static String COLUMN_OPPRETTET_DATO = "Opprettet_Dato";
	final static String COLUMN_OPPRETTET = "Opprettet";

	/**
	 * Returnerer frekvens utifra SQLen som blir gitt.
	 * 
	 * @param sql
	 *            SQL setning.
	 * @return Frekvens.
	 */
	public static int getCountForMonth(String sql) {

		int count = -1;

		try (ResultSet rs = execQuery(sql)) {

			while (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("Feil i getCountForMonth: " + e);
		} catch (NullPointerException npe) {
			System.out.println("Feil i getCountForMonth, rs er null: " + npe);
		}

		return count;

	}// end of getCountForYear

	/**
	 * Returnerer antall rader fra en tabell.
	 * 
	 * @param table
	 *            Tabellnavn.
	 * @return Antall rader.
	 */
	public static int getTableCount(String table) {

		String sql = "select count(*) from " + table;

		try (ResultSet rs = execQuery(sql)) {

			while (rs.next()) {
				return rs.getInt(1);
			}

			return -1;
		} catch (SQLException ex) {
			System.out.println("Feil i getTableCount: " + ex);
			return -1;
		} catch (NullPointerException npe) {
			System.out.println("Feil i getTableCount, rs er null: " + npe);
			return -1;
		}

	}// end of getTableCount

	/**
	 * Henter ut gjennomsnittlig pris for alle boliger i tabellen bolig.
	 * 
	 * @return Gjenonnomsnittlig pris for boliger.
	 */
	public static int getAvgUtleiePris() {

		String sql = "select avg(" + COLUMN_UTLEIE_PRIS + ") from "
				+ TABLE_BOLIG;
		try (ResultSet rs = execQuery(sql)) {

			while (rs.next()) {
				return rs.getInt(1);
			}
			return -1;
		} catch (SQLException ex) {
			System.out.println("Feil i getAvgBoligPris: " + ex);
			return -1;
		} catch (NullPointerException npe) {
			System.out.println("Feil i getAvgUtleierPris, rs er null: " + npe);
			return -1;
		}

	}// end of getAvgBoligPris

}// end of class Data_Statistikk
