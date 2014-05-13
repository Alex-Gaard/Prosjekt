package boligformidling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klassen tar av seg alle koblinger og spørringer opp mot databasen.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public abstract class Database {

	// SQL objekter
	private static Connection connect = null;
	private static Statement statement = null;

	// Databasekobling variabler
	private final static String server = "localhost";
	private final static String database = "mydb";
	private final static String brukernavn = "root";
	private final static String passord = "";

	// Vindu
	private static Vindu_Main vindu = null;

	/**
	 * Setter opp databasetilkobling, utifra resultatet vil metoden returnere
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
	 * Sender en spørring til databasen for oppdatering av tabeller. Utifra
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
