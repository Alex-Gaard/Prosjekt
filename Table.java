package boligformidling;

import java.util.ArrayList;

/**
 * Klassen representerer en vilkårlig tabell i databasen, og vil bli brukt av
 * klassen SubPanel_AdminTools for sikkerhetskopiering og gjenoppretting.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 10 Mai 2014
 */
public class Table {

	private String tableName;
	private String createShowTable;
	private String createTableString = null;

	private ArrayList<String> references = new ArrayList<String>();
	private ArrayList<String> column = new ArrayList<String>();
	private ArrayList<String> columnType = new ArrayList<String>();
	private ArrayList<String> insert = new ArrayList<String>();

	public Table(String name) {
		tableName = name;
		column = new ArrayList<String>();
		columnType = new ArrayList<String>();

	}// end of constructor
		// Getters

	/**
	 * Returner en ArrayList med strenger som innholder tabeller som denne
	 * tabellen refererer til(Foreign Key).
	 * 
	 * @return En ArrayList med strenger.
	 */
	public ArrayList<String> getReferences() {
		return references;
	}// end of getReferences

	/**
	 * Returnerer en streng som representerer SQL koden for å lage tabellen .
	 * 
	 * @return En streng.
	 */
	public String getCreateTableString() {
		return createTableString;
	}// end of getCreateTableString

	/**
	 * Returnerer en streng som representerer en INSERT SQL setning på den
	 * spesifiserte indexen.
	 * 
	 * @param index
	 *            Indexen til INSERT setningen.
	 * @return INSERT streng.
	 */
	public String getInsert(int index) {
		return insert.get(index);
	}// end of getInsert

	/**
	 * Returnerer tabellnavnet til tabellen.
	 * 
	 * @return Tabellnavnet.
	 */
	public String getTableName() {
		return tableName;
	}// end of getTableName

	/**
	 * Returnerer en streng som representerer strukturen til tabellen.
	 * 
	 * @return En streng.
	 */
	public String getCreateShowTable() {
		return createShowTable;
	}// end of getCreateShowTable

	/**
	 * Returnerer størrelsen til ArrayListen "insert" som inneholder alle INSERT
	 * setningene for tabellen.
	 * 
	 * @return "insert" størrelse.
	 */
	public int getInsertSize() {
		return insert.size();
	}// end of getInsertSize

	/**
	 * Returnerer en streng som lister opp alle kolonnene i tabellen
	 * 
	 * @return Streng med alle kolonner
	 */
	public String getColumns() {
		String columns = "";
		for (int i = 0; i < column.size(); i++) {
			if (columns.equals("")) {
				columns += column.get(i);
			} else {
				columns += "," + column.get(i);
			}

		}

		return columns;
	}// end of getColumns

	/**
	 * Returnerer antall kolonner i tabellen
	 * 
	 * @return Antall kolonner
	 */
	public int getNumOfColumns() {
		return column.size();
	}// end of getNumOfColumns

	/**
	 * Returnerer kolonne navnet på den spesifiserte indexen.
	 * 
	 * @param columnIndex
	 *            Indexen til det ønskede kolonne navnet.
	 * @return Kolonnenavn.
	 */
	public String getColumnName(int columnIndex) {
		return column.get(columnIndex);
	}// end of getColumnName

	/**
	 * Returnerer om kolonnen kan bli representert som en "blob" eller en
	 * "string".
	 * 
	 * @param columnIndex
	 *            Indexen til den ønskede kolonnen.
	 * @return "blob" eller "string".
	 */
	public String getType(int columnIndex) {

		if (columnType.get(columnIndex).indexOf("blob") >= 0) {
			return "blob";
		} else {
			return "string";
		}

	}// end of getType

	// Setters

	/**
	 * Setter createTableString lik parameteren tableString.
	 * 
	 * @param s
	 *            Streng representasjon av tabellstrukturen.
	 */
	public void setCreateTableString(String tableString) {
		createTableString = tableString;
	}// end of setCreateTableString

	/**
	 * Setter opp alle referansene til tabellen Ut ifra en streng som
	 * representerer tabellstrukturen.
	 * 
	 * @param createTableString
	 *            Tabellstruktur streng.
	 */
	public void setReferences(String createTableString) {

		String refString = " REFERENCES ";

		while (createTableString.indexOf(refString) >= 0) {
			int mark = 0;
			try {
				if ((mark = createTableString.indexOf(refString)) >= 0) {
					String subS = createTableString.substring(
							mark + refString.length(),
							createTableString.indexOf(" (", mark));
					createTableString = createTableString.substring(mark
							+ subS.length());
					subS = subS.trim();
					references.add(subS);
				}
			} catch (Exception ex) {
				System.out.println("feil i setReferences: " + ex);
			}

		}

	}// end of setReferencs

	/**
	 * Setter en formatert representasjon av tabellstrukturen Ut ifra en
	 * tabellstruktur streng.
	 * 
	 * @param createShowString
	 *            Uformatert tabellstruktur.
	 */
	public void setCreateShowTable(String createShowString) {
		createShowTable = formatCreateTable(createShowString);

	}// end of setCreateShowTable

	// Diverse

	/**
	 * Legger til en ny kolonne i ArrayList<String> column.
	 * 
	 * @param column
	 *            Kolonnen som skal legges til.
	 */
	public void addColumn(String column) {
		this.column.add(column);
	}// end of addColumn

	/**
	 * Legger til en kolonne-type("String"/"blob") i ArrayList<String>
	 * columnType.
	 * 
	 * @param columnType
	 *            Kolonne-typen som skal legges til.
	 */
	public void addColumnType(String columnType) {
		this.columnType.add(columnType);
	}// end of addColumnType

	/**
	 * Legger til en ny INSERT streng i ArrayList<String> insert.
	 * 
	 * @param insertString
	 *            Strengen som skal legges til.
	 */
	public void addInsert(String insertString) {
		insert.add(insertString);
	}// end of addInsert

	/**
	 * Tester om tabellen er klar for å bli opprettet, Ut ifra resultatet vil
	 * "true" eller "false" bli returnert.
	 * 
	 * @param createdTables
	 *            Liste over tabeller som allerede har blitt opprettet.
	 * @return true/false.
	 */
	public boolean readyForInsert(ArrayList<String> createdTables) {
		if (references.size() == 0) {
			return true;
		}

		for (String ref : references) {
			boolean foundRef = false;
			for (String createdTable : createdTables) {
				if (ref.equals(createdTable)) {
					foundRef = true;
				}
			}
			if (foundRef == false) {
				return false;
			}

		}
		return true;
	}// end of readyForInsert

	/**
	 * Returnerer en formatert representasjon av tabellstrukturen.
	 * 
	 * @param createTableString
	 *            Uformatert tabellstruktur.
	 * @return Formatert tabellstruktur.
	 */
	public String formatCreateTable(String createTableString) {
		String formattedString = createTableString.replace(",", ",\r\n");
		if (formattedString.indexOf("CREATE TABLE") == 0) {
			formattedString = formattedString.replace("CREATE TABLE",
					"CREATE TABLE IF NOT EXISTS");
		}
		formattedString += ";\r\n\r\n";
		return formattedString;
	}// end of formatCreateTable

}// end of class Table
