package boligformidling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

import javax.swing.table.DefaultTableModel;

/**
 * Inneholder metoder for å hente ut og bearbeide informasjon fra databasen.
 * Metodene er relatert til kontrakter.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class Data_Kontrakter extends Database {

	// Tabeller
	final static String TABLE_LEIEKONTRAKT = "leiekontrakt";
	final static String TABLE_BOLIG = "bolig";
	final static String TABLE_LEIEKONTRAKT_FORESPØRSEL = "leiekontrakt_forespørsel";

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

	/**
	 * Returnerer en liste med kontrakt objekter hvor det spesifiserte
	 * personnummeret inngår. Personnummeret kan tilhøre en søker,utleier eller
	 * en behandler.
	 * 
	 * @param personnummer
	 *            Personnummer til bruker.
	 * @return Liste med kontrakter.
	 */
	public static ArrayList<Leiekontrakt> getKontrakterForPersonnummer(
			String personnummer) {

		String sql = "select * from " + TABLE_LEIEKONTRAKT + " where "
				+ COLUMN_UTLEIER_PERSONNUMMER + "= '" + personnummer + "' or "
				+ COLUMN_BOLIGSØKER_PERSONNUMMER + " = '" + personnummer
				+ "' or " + COLUMN_BEHANDLER_PERSONNUMMER + " = '"
				+ personnummer + "'";

		try (ResultSet rs = execQuery(sql)) {

			ArrayList<Leiekontrakt> lkList = new ArrayList<Leiekontrakt>();

			while (rs.next()) {
				int boligID = rs.getInt(COLUMN_BOLIG_BOLIG_ID);
				String behandlerPersonnummer = rs
						.getString(COLUMN_BEHANDLER_PERSONNUMMER);
				String utleierPersonnummer = rs
						.getString(COLUMN_UTLEIER_PERSONNUMMER);
				String boligsøkerPersonnummer = rs
						.getString(COLUMN_BOLIGSØKER_PERSONNUMMER);
				int leiepris = rs.getInt(COLUMN_LEIEPRIS);
				Date avtaleStart = rs.getDate(COLUMN_AVTALE_START);
				Date avtaleSlutt = rs.getDate(COLUMN_AVTALE_SLUTT);

				Leiekontrakt lk = new Leiekontrakt(boligID,
						behandlerPersonnummer, utleierPersonnummer,
						boligsøkerPersonnummer, leiepris, avtaleStart,
						avtaleSlutt);
				lkList.add(lk);
			}

			return lkList;
		} catch (SQLException ex) {
			System.out.println("Feil i getKontrakterForPersonnummer: " + ex);
			return null;
		} catch (NullPointerException npe) {
			System.out
					.println("Feil i getKontrakterForPersonnummer, rs er null: "
							+ npe);
			return null;
		}

	}// end of getKontrakterForPersonnummer

	/**
	 * Returnerer en liste med kontrakt objekter hvor den spesifiserte bolig
	 * IDen inngår.
	 * 
	 * @param boligId
	 *            IDen til boligen.
	 * @return Liste med kontrakter.
	 */
	public static ArrayList<Leiekontrakt> getKontrakterForBoligID(String boligId) {

		String sql = "select * from " + TABLE_LEIEKONTRAKT + " where "
				+ COLUMN_BOLIG_BOLIG_ID + "= '" + boligId + "'";

		try (ResultSet rs = execQuery(sql)) {

			ArrayList<Leiekontrakt> lkList = new ArrayList<Leiekontrakt>();

			while (rs.next()) {
				int boligID = rs.getInt(COLUMN_BOLIG_BOLIG_ID);
				String behandlerPersonnummer = rs
						.getString(COLUMN_BEHANDLER_PERSONNUMMER);
				String utleierPersonnummer = rs
						.getString(COLUMN_UTLEIER_PERSONNUMMER);
				String boligsøkerPersonnummer = rs
						.getString(COLUMN_BOLIGSØKER_PERSONNUMMER);
				int leiepris = rs.getInt(COLUMN_LEIEPRIS);
				Date avtaleStart = rs.getDate(COLUMN_AVTALE_START);
				Date avtaleSlutt = rs.getDate(COLUMN_AVTALE_SLUTT);

				Leiekontrakt lk = new Leiekontrakt(boligID,
						behandlerPersonnummer, utleierPersonnummer,
						boligsøkerPersonnummer, leiepris, avtaleStart,
						avtaleSlutt);
				lkList.add(lk);
			}

			return lkList;
		} catch (SQLException ex) {
			System.out.println("Feil i getKontrakterForBoligID: " + ex);
			return null;
		} catch (NullPointerException npe) {
			System.out.println("Feil i getKontrakterForBoligID, rs er null: "
					+ npe);
			return null;
		}

	}// end of getKontrakterForBoligID

	/**
	 * Sjekker om en bolig med den spesifiserte IDen eksisterer i databasen.
	 * Utifra om en bolig ble funnet eller ikke vil metoden returnere
	 * true/false.
	 * 
	 * @param id
	 *            Bolig ID.
	 * @return true/false.
	 */
	public static boolean boligExists(String id) {

		String sql = "select * from " + TABLE_BOLIG + " where "
				+ COLUMN_BOLIG_ID + " = '" + id + "'";

		try (ResultSet rs = execQuery(sql)) {

			rs.last();

			if (rs.getRow() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException ex) {
			System.out.println("Feil i boligExists: " + ex);
			return false;
		} catch (NullPointerException npe) {
			System.out.println("Feil i boligExists, rs er null: " + npe);
			return false;
		}

	}// end of boligExists

	/**
	 * Sjekker om det eksisterer en aktiv kontrakt for en bolig med den
	 * spesifiserte bolig IDen. Utifra om en aktiv kontrakt ble funnet eller
	 * ikke, vil metoden returnere true/false.
	 * 
	 * @param boligID
	 *            IDen til boligen.
	 * @return true/false.
	 */
	public static boolean aktivKontraktExists(String boligID) {

		String sql = "select * from " + TABLE_LEIEKONTRAKT + " where "
				+ COLUMN_BOLIG_BOLIG_ID + " = '" + boligID
				+ "' and curdate() < " + COLUMN_AVTALE_SLUTT;

		try (ResultSet rs = execQuery(sql)) {
			rs.last();

			if (rs.getRow() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException ex) {
			System.out.println("Feil i kontraktExists: " + ex);
			return true;
		} catch (NullPointerException npe) {
			System.out.println("Feil i kontraktExists, rs er null: " + npe);
			return true;
		}

	}// end of kontraktExists

	/**
	 * Setter inn en ny kontrakt i databasen med de spesifiserte parameterene.
	 * Utifra om en ny kontrakt ble satt inn eller ikke, vil metoden returnere
	 * true/false.
	 * 
	 * @param boligID
	 *            ID til boligen.
	 * @param behandlerPersonnummer
	 *            Personnummeret til behandleren.
	 * @param utleierPersonnummer
	 *            Personnummmeret til utleieren.
	 * @param søkerPersonnummer
	 *            Personnummeret til søkeren.
	 * @param leiepris
	 *            Leiepris på boligen.
	 * @param startDato
	 *            Start dato for kontrakten.
	 * @param sluttDato
	 *            Slutt dato for kontrakten.
	 * @return true/false.
	 */
	public static boolean insertKontrakt(int boligID,
			String behandlerPersonnummer, String utleierPersonnummer,
			String søkerPersonnummer, int leiepris, String startDato,
			String sluttDato) {

		String sql = "insert into " + TABLE_LEIEKONTRAKT + " ("
				+ COLUMN_BEHANDLER_PERSONNUMMER + "," + COLUMN_BOLIG_BOLIG_ID
				+ "," + COLUMN_UTLEIER_PERSONNUMMER + ","
				+ COLUMN_BOLIGSØKER_PERSONNUMMER + "," + COLUMN_LEIEPRIS + ","
				+ COLUMN_AVTALE_START + "," + COLUMN_AVTALE_SLUTT + ") "
				+ "values ('" + behandlerPersonnummer + "','" + boligID + "','"
				+ utleierPersonnummer + "','" + søkerPersonnummer + "',"
				+ leiepris + ",'" + startDato + "','" + sluttDato + "')";

		try {
			execUpdate(sql);
			return true;
		} catch (SQLException ex) {
			System.out.println("Feil i insertKontrakt: " + ex);
			return false;
		}

	}// End of insertKontrakt

	/**
	 * Setter inn forespørsler for et JTable gjennom en DefaultTableModel.
	 * 
	 * @param dtm
	 *            Modellen til JTable.
	 */
	public static void visKontraktForespørsler(DefaultTableModel dtm) {

		String sql = "select * from " + TABLE_LEIEKONTRAKT_FORESPØRSEL
				+ " where " + COLUMN_PÅTATT + " = " + 0;
		ArrayList<LeiekontraktForespørsel> lkfList = new ArrayList<LeiekontraktForespørsel>();

		try (ResultSet rs = execQuery(sql)) {

			while (rs.next()) {
				String søkerPersonnummer = rs
						.getString(COLUMN_BOLIGSØKER_PERSONNUMMER);
				int boligID = rs.getInt(COLUMN_BOLIG_BOLIG_ID);
				Date dato = rs.getDate(COLUMN_OPPRETTET_DATO);

				LeiekontraktForespørsel lkf = new LeiekontraktForespørsel(
						søkerPersonnummer, boligID, dato);
				lkfList.add(lkf);
			}
			if (lkfList.size() > 0) {
				for (LeiekontraktForespørsel lkf : lkfList) {
					dtm.addRow(lkf.getInfo());
				}
			}

		} catch (SQLException ex) {
			System.out.println("Feil i visKontraktForespørsler: " + ex);
		} catch (NullPointerException npe) {
			System.out.println("Feil i visKontraktForespørsler, rs er null: "
					+ npe);
		}

	}// end of visKontraktForespørseler

	/**
	 * Setter inn påtatte kontrakter for et JTable gjennom en DefaultTableModel.
	 * Kontrakene som blir satt inn er spesifisert av behandlerIDen. Utifra om
	 * metoden fant noen kontrakter eller ikke, vil metoden returnere
	 * true/false.
	 * 
	 * @param dtm
	 *            Modellen til JTable.
	 * @param behandlerID
	 *            IDen til behandleren som opprettet forespørselen.
	 * @return true/false.
	 */
	public static boolean visPåtatteKontrakter(DefaultTableModel dtm,
			String behandlerID) {

		String sql = "select * from " + TABLE_LEIEKONTRAKT_FORESPØRSEL
				+ " where " + COLUMN_BEHANDLER_BRUKER_PERSONNUMMER + " = "
				+ behandlerID + " and " + COLUMN_PÅTATT + " = " + 1;

		try (ResultSet rs = execQuery(sql)) {

			while (rs.next()) {
				String behandlerPersonnummer = String.valueOf(rs
						.getInt(COLUMN_BEHANDLER_BRUKER_PERSONNUMMER));
				String søkerPersonnummer = rs
						.getString(COLUMN_BOLIGSØKER_PERSONNUMMER);
				String boligID = rs.getString(COLUMN_BOLIG_BOLIG_ID);
				Date dato = rs.getDate(COLUMN_OPPRETTET_DATO);

				String[] rad = { behandlerPersonnummer, søkerPersonnummer,
						boligID, dato.toString() };
				dtm.addRow(rad);
			}
			return true;

		} catch (SQLException ex) {
			System.out.println("Feil i visPåtatteKontrakter: " + ex);
			return false;
		} catch (NullPointerException npe) {
			System.out.println("Feil i visPåtatteKontrakter, rs er null: "
					+ npe);
			return false;
		}

	}// end of visPåttateKontrakter

	/**
	 * Kontrakten hvor de spesifiserte parameterene
	 * søkerPersonnummer,opprettetDato og boligID inngår vil bli påtatt av
	 * behandleren med personnummer lik behandlerPersonnummer. Utifra om
	 * påtagelsen var vellykket eller ikke, vil metoden returnere true/false.
	 * 
	 * @param behandlerPersonnummer
	 *            Personnummeret til behandleren.
	 * @param søkerPersonnummer
	 *            Personnummeret til søkeren.
	 * @param opprettetDato
	 *            Datoen forespørselen ble opprettet.
	 * @param boligID
	 *            IDen til boligen.
	 * @return true/false
	 */
	public static boolean påtaKontrakt(String behandlerPersonnummer,
			String søkerPersonnummer, String opprettetDato, String boligID) {

		String sql = "update " + TABLE_LEIEKONTRAKT_FORESPØRSEL + " set "
				+ COLUMN_BEHANDLER_PERSONNUMMER + " = '"
				+ behandlerPersonnummer + "'," + COLUMN_PÅTATT + " = " + 1
				+ " where " + COLUMN_OPPRETTET_DATO + " = '" + opprettetDato
				+ "' and " + COLUMN_BOLIGSØKER_PERSONNUMMER + " = '"
				+ søkerPersonnummer + "'" + " and " + COLUMN_BOLIG_BOLIG_ID
				+ " = " + boligID + " limit 1";

		try {
			execUpdate(sql);
			return true;

		} catch (SQLException ex) {
			System.out.println("Feil i påtaKontrakt: " + ex);
			return false;
		}

	}// end of påtaKontrakt

	/**
	 * Fjerner en forespørsel fra databasen hvor de spesifiserte parameterene
	 * inngår. Utifra om fjerningen av forespørselen var vellykket eller ikke,
	 * vil metoden returnere true/false.
	 * 
	 * @param behandlerPersonnummer
	 *            Personnummeret til behandleren.
	 * @param søkerPersonnummer
	 *            Personnummeret til søkeren.
	 * @param boligID
	 *            IDen til boligen.
	 * @param dato
	 *            Datoen når forespørselen ble opprettet.
	 * @return true/false.
	 */
	public static boolean fjernForespørsel(String behandlerPersonnummer,
			String søkerPersonnummer, String boligID, String dato) {

		String sql = "delete from " + TABLE_LEIEKONTRAKT_FORESPØRSEL
				+ " where " + COLUMN_BOLIGSØKER_PERSONNUMMER + " = '"
				+ søkerPersonnummer + "' and " + COLUMN_BEHANDLER_PERSONNUMMER
				+ " = '" + behandlerPersonnummer + "'" + " and "
				+ COLUMN_BOLIG_BOLIG_ID + " = " + boligID + " and "
				+ COLUMN_OPPRETTET_DATO + " = '" + dato + "' limit 1";

		try {
			execUpdate(sql);
			return true;
		} catch (SQLException ex) {
			System.out.println("Feil i fjernForespørsel: " + ex);
			return false;
		}

	}// end of fjernForespørsel

	/**
	 * Sjekker om en forespørsel med den spesifiserte bolig IDen eksisterer i
	 * databasen. Utifra om en forespørsel ble funnet eller ikke, vil metoden
	 * returnere true/false.
	 * 
	 * @param boligID
	 *            IDen til boligen.
	 * @return true/false.
	 */
	public static boolean forespørselExists(int boligID) {
		String sql = "select * from " + TABLE_LEIEKONTRAKT_FORESPØRSEL
				+ " where " + COLUMN_BOLIG_BOLIG_ID + " = '" + boligID + "'";

		try (ResultSet rs = execQuery(sql)) {
			rs.last();

			if (rs.getRow() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException ex) {
			System.out.println("Feil i boligExists: " + ex);
			return true;
		}
	}// end of forespørselExists

	/**
	 * Setter inn en ny kontrakt forespørsel. Utifra om en ny forespørsel ble
	 * satt inn eller ikke, vil metoden returnere true/false.
	 * 
	 * @param bolig
	 *            Bolig objekt som representerer en bolig i databasen.
	 * @param bruker
	 *            Bruker objekt som representerer en bruker i databasen.
	 * @return true/false.
	 */
	public static boolean nyKontraktForespørsel(Bolig bolig, Bruker bruker) {
		if (!bruker.is(bruker.SØKER)) {
			return false;
		}
		if (getKontraktForesørsler(bruker.getId(), 0, null, null, 0).isEmpty()
				&& getKontraktForesørsler(null, bolig.getId(), null, null, 0)
						.isEmpty()) {
			String sql = "INSERT INTO " + TABLE_LEIEKONTRAKT_FORESPØRSEL + " ("
					+ COLUMN_BOLIGSØKER_PERSONNUMMER + ", "
					+ COLUMN_BOLIG_BOLIG_ID + ", " + COLUMN_OPPRETTET_DATO
					+ ") ";
			sql += "VALUES(" + bruker.getId() + ", " + bolig.getId() + ", "
					+ "curdate())";
			try {
				return execUpdate(sql);
			} catch (SQLException e) {
				System.out.println("ny leiekontrakt feil: " + e);
				return false;
			}
		} else {
			return false;
		}
	}// end of nyKontraktForespørsel

	/**
	 * Returnerer en liste med forespørsler utifra de spesifiserte parameterene.
	 * Hvis en feil skulle oppstå under hentingen av kontrakter vil metoden
	 * returnere null.
	 * 
	 * @param kundeId
	 *            Personnummeret til kunden.
	 * @param boligId
	 *            IDen til boligen.
	 * @param dato
	 *            Dato for opprettelse.
	 * @param behandlerId
	 *            Personnummeret til behandleren.
	 * @param påtatt
	 *            Om kontrakten er påtatt eller ikke.
	 * @return Liste med kontrakter/null.
	 */
	public static ArrayList<LeiekontraktForespørsel> getKontraktForesørsler(
			String kundeId, int boligId, String dato, String behandlerId,
			int påtatt) {
		String sql = "SELECT * FROM " + TABLE_LEIEKONTRAKT_FORESPØRSEL;
		if (kundeId != null || boligId != 0 || dato != null
				|| behandlerId != null || påtatt != 0) {
			sql += " WHERE ";
		}
		int ANDhelp = 0;
		if (kundeId != null) {
			sql += COLUMN_BOLIGSØKER_PERSONNUMMER + " = " + kundeId;
			ANDhelp++;
		}
		if (boligId != 0) {
			if (ANDhelp > 0) {
				sql += " AND ";
			}
			sql += COLUMN_BOLIG_BOLIG_ID + " = " + boligId;
		}
		if (dato != null) {
			if (ANDhelp > 0) {
				sql += " AND ";
			}
			sql += COLUMN_OPPRETTET_DATO + " = " + dato;
		}
		if (behandlerId != null) {
			if (ANDhelp > 0) {
				sql += " AND ";
			}
			sql += COLUMN_BEHANDLER_PERSONNUMMER + " = " + behandlerId;
		}
		if (påtatt != 0) {
			if (ANDhelp > 0) {
				sql += " AND ";
			}
			String bool = påtatt == 2 ? "true" : "false";
			sql += COLUMN_PÅTATT + " = " + bool;
		}
		try (ResultSet rs = execQuery(sql)) {

			ArrayList<LeiekontraktForespørsel> lkList = new ArrayList<LeiekontraktForespørsel>();
			while (rs.next()) {
				LeiekontraktForespørsel lk = new LeiekontraktForespørsel(
						rs.getString(COLUMN_BOLIGSØKER_PERSONNUMMER),
						rs.getString(COLUMN_BEHANDLER_PERSONNUMMER),
						rs.getInt(COLUMN_BOLIG_BOLIG_ID),
						rs.getDate(COLUMN_BOLIG_BOLIG_ID));
				lkList.add(lk);
			}
			return lkList;
		} catch (SQLException ex) {
			System.out.println("Feil i getKontrakterForID: " + ex);
			return null;
		}
	}
}// end of Data_Kontrakter