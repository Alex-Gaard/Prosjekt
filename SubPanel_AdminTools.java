package boligformidling;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Klassen inneholder GUI og metoder for å kunne ta sikkerhetskopiering og
 * gjennoppretting av en gitt database
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_AdminTools extends SubPanel implements ActionListener {

	private JButton restore, backup, backupSqlButton, backupImageButton,
			restoreSqlButton, restoreImageButton;
	private JPasswordField restorePasswordField, backupPasswordField;

	private JTextField backupSqlField, backupImageField, backupDatabaseField,
			backupUserNameField, restoreSqlField, restoreImageField,
			restoreDatabaseField, restoreUserNameField;

	private JLabel lblBackupDatabase, lblBackupSql, lblBackupImage,
			lblBackupUserName, lblBackupPassord, lblRestoreDatabase,
			lblRestoreSql, lblRestoreImage, lblRestoreUserName,
			lblRestorePassord;

	public SubPanel_AdminTools(MainPanel parent) {
		super(parent);

		restore = new JButton("Gjenopprett");
		backup = new JButton("Sikkerhetskopier");

		backupSqlButton = new JButton("Sett SQL sti");
		backupImageButton = new JButton("Sett bilde sti");

		restoreSqlButton = new JButton("Finn SQL sti");
		restoreImageButton = new JButton("Finn bilde sti");

		backup.addActionListener(this);
		backupSqlButton.addActionListener(this);
		backupImageButton.addActionListener(this);

		restore.addActionListener(this);
		restoreSqlButton.addActionListener(this);
		restoreImageButton.addActionListener(this);

		backupSqlField = new JTextField(15);
		backupImageField = new JTextField(15);
		backupDatabaseField = new JTextField(15);

		backupUserNameField = new JTextField(15);
		backupPasswordField = new JPasswordField(15);

		restoreSqlField = new JTextField(15);
		restoreImageField = new JTextField(15);
		restoreDatabaseField = new JTextField(15);

		restoreUserNameField = new JTextField(15);
		restorePasswordField = new JPasswordField(15);

		lblBackupSql = new JLabel("Lagre backup på: ");
		lblBackupImage = new JLabel("Lagre bilder på : ");
		lblBackupDatabase = new JLabel("Database sti eks(localhost/mydb): ");

		lblBackupUserName = new JLabel("Brukernavn: ");
		lblBackupPassord = new JLabel("Passord: ");

		lblRestoreSql = new JLabel("Hent sql fra: ");
		lblRestoreImage = new JLabel("Hent bilder fra: ");
		lblRestoreDatabase = new JLabel("Database sti eks(localhost/mydb): ");

		lblRestoreUserName = new JLabel("Brukernavn: ");
		lblRestorePassord = new JLabel("Passord: ");

		// Backup Jp

		JPanel backupJp = new JPanel();
		TitledBorder title = BorderFactory
				.createTitledBorder("Sikkerhetskopiering");
		backupJp.setLayout(new BoxLayout(backupJp, BoxLayout.PAGE_AXIS));
		backupJp.setBorder(title);
		backupJp.setPreferredSize(new Dimension(400, 220));

		JPanel backupSQLJp = new JPanel();
		backupSQLJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		backupSQLJp.add(lblBackupSql);
		backupSQLJp.add(backupSqlField);
		backupSQLJp.add(backupSqlButton);

		backupJp.add(backupSQLJp);

		JPanel backupImageJp = new JPanel();
		backupImageJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		backupImageJp.add(lblBackupImage);
		backupImageJp.add(backupImageField);
		backupImageJp.add(backupImageButton);

		backupJp.add(backupImageJp);

		JPanel backupDatabaseJp = new JPanel();
		backupDatabaseJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		backupDatabaseJp.add(lblBackupDatabase);
		backupDatabaseJp.add(backupDatabaseField);

		backupJp.add(backupDatabaseJp);

		JPanel backupUserNameJp = new JPanel();
		backupUserNameJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		backupUserNameJp.add(lblBackupUserName);
		backupUserNameJp.add(backupUserNameField);

		backupJp.add(backupUserNameJp);

		JPanel backupPassordJp = new JPanel();
		backupPassordJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		backupPassordJp.add(lblBackupPassord);
		backupPassordJp.add(backupPasswordField);

		backupJp.add(backupPassordJp);

		JPanel backupButtonJp = new JPanel();
		backupButtonJp.setLayout(new FlowLayout(FlowLayout.CENTER));
		backupButtonJp.add(backup);

		backupJp.add(backupButtonJp);

		// Restore JP

		JPanel restoreJp = new JPanel();
		title = BorderFactory.createTitledBorder("Gjenoppretting");
		restoreJp.setBorder(title);
		restoreJp.setLayout(new BoxLayout(restoreJp, BoxLayout.PAGE_AXIS));
		restoreJp.setPreferredSize(new Dimension(400, 220));

		JPanel restoreSQLJp = new JPanel();
		restoreSQLJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		restoreSQLJp.add(lblRestoreSql);
		restoreSQLJp.add(restoreSqlField);
		restoreSQLJp.add(restoreSqlButton);

		restoreJp.add(restoreSQLJp);

		JPanel restoreImageJp = new JPanel();
		restoreImageJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		restoreImageJp.add(lblRestoreImage);
		restoreImageJp.add(restoreImageField);
		restoreImageJp.add(restoreImageButton);

		restoreJp.add(restoreImageJp);

		JPanel restoreDatabaseJp = new JPanel();
		restoreDatabaseJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		restoreDatabaseJp.add(lblRestoreDatabase);
		restoreDatabaseJp.add(restoreDatabaseField);

		restoreJp.add(restoreDatabaseJp);

		JPanel restoreUserNameJp = new JPanel();
		restoreUserNameJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		restoreUserNameJp.add(lblRestoreUserName);
		restoreUserNameJp.add(restoreUserNameField);

		restoreJp.add(restoreUserNameJp);

		JPanel restorePassordJp = new JPanel();
		restorePassordJp.setLayout(new FlowLayout(FlowLayout.LEFT));

		restorePassordJp.add(lblRestorePassord);
		restorePassordJp.add(restorePasswordField);

		restoreJp.add(restorePassordJp);

		JPanel restoreButtonJp = new JPanel();
		restoreButtonJp.setLayout(new FlowLayout(FlowLayout.CENTER));
		restoreButtonJp.add(restore);

		restoreJp.add(restoreButtonJp);

		add(backupJp);
		add(restoreJp);

	}// end of constructor

	/**
	 * Tar backup av databasen Ut ifra informasjonen som er blitt fyllt ut i
	 * tekstfeltene Dataen vil bli lagret lokalt på maskinen
	 */
	private void backup() {

		if (!checkBackupFields()) {
			displayMessage("Fyll inn alle de nødvendige feltene\n");
			return;
		}

		Connection connect = null;
		Statement statement = null;
		ResultSet rs = null;

		String databasePath = backupDatabaseField.getText();
		String sqlPath = backupSqlField.getText();
		String imagePath = backupImageField.getText();

		int end = databasePath.lastIndexOf("/") + 1;
		String databaseName = databasePath.substring(end);
		String databaseUserName = backupUserNameField.getText();
		String databasePassord = String.valueOf(backupPasswordField
				.getPassword());

		if (!checkFilePath(sqlPath)) {
			displayMessage("Filstien til SQL lagringsplassen er ikke korrekt");
			return;
		}

		if (!checkFilePath(imagePath)) {
			displayMessage("Filstien til bilde lagringsplassen er ikke korrekt");
			return;
		}

		ArrayList<Table> table = new ArrayList<Table>();
		PrintWriter out = null;

		try {

			out = new PrintWriter(new BufferedWriter(new FileWriter(sqlPath
					+ File.separator + "backup.sql", true)));

			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://"
					+ databasePath, databaseUserName, databasePassord);
			statement = connect.createStatement();

			displayMessage("Starter backup av database...\n");

			String sql = "show tables";
			rs = statement.executeQuery(sql);

			// Henter tabeller
			while (rs.next()) {
				table.add(new Table(rs.getString("Tables_in_" + databaseName)));
			}

			// Henter info om tabellene
			for (int i = 0; i < table.size(); i++) {
				sql = "show create table " + table.get(i).getTableName();
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					String s = rs.getString("Create Table");
					table.get(i).setCreateShowTable(s);
				}

				rs.close();

				sql = "describe " + table.get(i).getTableName();
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					table.get(i).addColumn(rs.getString("Field"));
					table.get(i).addColumnType(rs.getString("Type"));
				}

			}// end of for

			// Skriver ut info til fil
			for (int i = 0; i < table.size(); i++) {

				String tableName = table.get(i).getTableName();

				sql = "select * from " + tableName;
				rs = statement.executeQuery(sql);

				out.append("--\r\n-- Table structure for table " + tableName
						+ " \r\n--\r\n\r\n");
				out.append(table.get(i).getCreateShowTable());

				if (!rs.first()) {
					continue;
				} else {
					rs.previous();
				}

				out.append("--\r\n-- Dumping data for table " + tableName
						+ "\r\n--\r\n\r\n");

				while (rs.next()) {

					String value = "insert into " + tableName + " ("
							+ table.get(i).getColumns() + ") values\r\n";
					value += "(";
					for (int c = 0; c < table.get(i).getNumOfColumns(); c++) {

						if (table.get(i).getType(c).equals("string")) {
							value += "'"
									+ rs.getString(table.get(i)
											.getColumnName(c)) + "'";

						} else if (table.get(i).getType(c).equals("blob")) {
							downloadImage(rs.getBinaryStream(table.get(i)
									.getColumnName(c)), rs.getString(table.get(
									i).getColumnName(c - 1)));
							value += "'Bilde'";
						}

						if (c != (table.get(i).getNumOfColumns() - 1)) {
							value += ",";
						} else {
							value += ");\r\n";
						}

					}// end of nested for
					out.append(value);

				}// end of while

			}// end of for

			displayMessage("Backup er ferdig\n");
		} catch (
				com.mysql.jdbc.exceptions.jdbc4.CommunicationsException
				| com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException ex) {

			displayMessage("En feil oppstod under koblingen til databasen,\n "
					+ "sjekk om alle feltene er fyllt inn med riktig informasjon");

		} catch (SQLException ex) {
			displayMessage("En feil oppstod under koblingen til databasen,\n "
					+ "sjekk om alle feltene er fyllt inn med riktig informasjon");
		} catch (Exception ex) {
			System.out.println("Feil i backup: " + ex);
			displayMessage("Backup var mislykket\n");
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connect.close();
			} catch (Exception e) {
			}
			try {
				out.close();
			} catch (Exception e) {
			}
		}

	}// end of backup

	/**
	 * Sjekker om en filsti eksisterer på maskinen.
	 * Ut ifra om en gyldig filsti ble funnet eller ikke, vil metoden returnere true/false;
	 * 
	 * @param absoluteFilePath Filstien som skal sjekkes.
	 * @return true/false;
	 */
	private boolean checkFilePath(String absoluteFilePath) {
		File f = new File(absoluteFilePath);
		return f.exists();

	}// end of checkFilePath

	/**
	 * Laster ned et bilde og lagrer det lokalt på maskinen.
	 * 
	 * @param is
	 *            InputStreamen til bildet.
	 * @param name
	 *            Navnet til bildet.
	 */
	private void downloadImage(InputStream is, String name) {

		String path = backupImageField.getText();

		File f = new File(path + File.separator + "Database bilder");
		if (!f.exists()) {
			f.mkdir();
		}

		try {

			BufferedInputStream bis = new BufferedInputStream(is);
			FileOutputStream fos = new FileOutputStream(f.getAbsolutePath()
					+ File.separator + name + ".jpg");

			int data;
			while ((data = bis.read()) != -1) {
				fos.write(data);
			}

			bis.close();
			fos.close();

		} catch (Exception ex) {
			System.out.println("Feil i downloadImage: " + ex);

		}

	}// end of downloadImage

	/**
	 * Gjenoppretter en database fra innholdet til en spesifisert SQL fil, og
	 * mappe med bilder.
	 */
	private void restore() {

		if (!checkRestoreFields()) {
			displayMessage("Fyll inn alle de nødvendige feltene\n");
			return;
		}

		Connection connect = null;
		PreparedStatement prepStatement = null;
		Statement statement = null;

		String databasePath = restoreDatabaseField.getText();
		String sqlPath = restoreSqlField.getText();
		String imagePath = restoreImageField.getText();
		String databaseUserName = restoreUserNameField.getText();
		String databasePassord = String.valueOf(restorePasswordField
				.getPassword());

		if (!checkFilePath(sqlPath)) {
			displayMessage("Filstien til SQL lagringsplassen er ikke korrekt");
			return;
		}

		if (!checkFilePath(imagePath)) {
			displayMessage("Filstien til bilde lagringsplassen er ikke korrekt");
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(sqlPath));) {

			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://"
					+ databasePath, databaseUserName, databasePassord);

			String sql = "";

			StringBuffer sb = new StringBuffer();
			String s;
			while ((s = br.readLine()) != null) {
				if (s.indexOf("--") == -1) {
					sb.append(s);
				}

			}

			ArrayList<String> queries = new ArrayList(Arrays.asList(sb
					.toString().split(";")));

			ArrayList<String> createdTables = new ArrayList<String>();

			ArrayList<Table> table = new ArrayList<Table>();

			statement = connect.createStatement();
            //Lagrer info om tabeller i Table objekter
			for (int i = 0; i < queries.size(); i++) {

				if (queries.get(i).indexOf("insert into") >= 0) {
					String tableName = parseInsert(queries.get(i));
					boolean exists = false;
					int index = 0;
					for (int c = 0; c < table.size(); c++) {
						if (table.get(c).getTableName().indexOf(tableName) >= 0) {
							exists = true;
							index = c;
						}
					}

					if (exists) {
						table.get(index).addInsert(queries.get(i));
					} else {
						table.add(new Table(tableName));
						table.get(table.size() - 1).addInsert(queries.get(i));
					}

				} else if (queries.get(i).indexOf("CREATE TABLE IF NOT EXISTS") >= 0) {

					String tableName = parseCreateTable(queries.get(i));
					boolean exists = false;
					int index = 0;

					for (int c = 0; c < table.size(); c++) {
						if (table.get(c).getTableName().equals(tableName)) {
							exists = true;
							index = c;
						}
					}

					if (exists) {
						table.get(index).setCreateTableString(
								queries.get(index));
					} else {
						table.add(new Table(tableName));
						table.get(table.size() - 1).setCreateTableString(
								queries.get(i));
					}

				}

			}// end of for

			// Setter opp alle referanser til tabellene i databasen
			for (int i = 0; i < table.size(); i++) {
				table.get(i).setReferences(table.get(i).getCreateTableString());
			}

			for (Table t : table) {
				System.out.println(t.getTableName());
				for (int i = 0; i < t.getInsertSize(); i++) {
					System.out.println(t.getInsert(i));
				}

			}
            
			//Skriver ut SQL setninger til databasen
			while (table.size() > 0) {
				for (int i = 0; i < table.size(); i++) {
					if (table.get(i).readyForInsert(createdTables)) {
						statement.execute(table.get(i).getCreateTableString());

						if (table.get(i).getInsertSize() > 0) {
							for (int c = 0; c < table.get(i).getInsertSize(); c++) {
								statement.execute(table.get(i).getInsert(c));
							}

						}
						createdTables.add(parseCreateTable(table.get(i)
								.getCreateTableString()));
						table.remove(i);
					}// end of if
				}// end of for
			}// end of while

			// Laster opp bilder til databasen
			File f = new File(imagePath);
			File[] bilder = null;
			if (f.exists()) {
				bilder = f.listFiles();
				if (bilder == null) {
					System.out.println("bilder[] er null");
					displayMessage("Gjenoppreting var vellykket, men bilder ble ikke satt inn");
					return;
				}
				if (bilder.length == 0) {
					System.out.println("Fant ingen bilder");
					displayMessage("Gjenoppreting var vellykket, men ingen bilder ble funnet");
					return;
				}

				for (int i = 0; i < bilder.length; i++) {
					if (!bilder[i].getName().endsWith(".jpeg")) {
						bilder[i] = null;
					}
				}
			}

			for (int i = 0; i < bilder.length; i++) {
				if (bilder[i] == null) {
					continue;
				}

				int seperator = bilder[i].getName().indexOf(".");
				String filename = bilder[i].getName().substring(0, seperator);
				sql = "update bolig_bilde set Bilde = ? where Bolig_BoligID = "
						+ filename;
				prepStatement = connect.prepareStatement(sql);

				InputStream is = new FileInputStream(
						bilder[i].getAbsolutePath());
				prepStatement.setBinaryStream(1, is, bilder[i].length());

				prepStatement.execute();

			}
			displayMessage("Databasen er blitt gjenopprettet\n");

		} catch (
				com.mysql.jdbc.exceptions.jdbc4.CommunicationsException
				| com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException ex) {
			displayMessage("En feil oppstod under koblingen til databasen,\n "
					+ "sjekk om alle feltene er fyllt inn med riktig informasjon");

		} catch (SQLException ex) {
			displayMessage("En feil oppstod under koblingen til databasen,\n "
					+ "sjekk om alle feltene er fyllt inn med riktig informasjon");

		} catch (Exception ex) {
			System.out.println("Feil i restore: " + ex);
			displayMessage("Feil under gjenoppretting av database\n");
		} finally {
			try {
				prepStatement.close();
			} catch (Exception e) {};
			try {
				statement.close();
			} catch (Exception e) {};
			try {
				connect.close();
			} catch (Exception e) {};

		}

	}// end of restore

	/**
	 * Parser tabellstrukturen og returnerer navnet til tabellen.
	 * 
	 * @param createTableString
	 *            Tabellstrukturen.
	 * @return Navnet til tabellen.
	 */
	private String parseCreateTable(String createTableString) {
		String start = createTableString.replace("CREATE TABLE IF NOT EXISTS ",
				"");
		int end = start.indexOf("(");
		String tableName = start.substring(0, end);
		tableName = tableName.trim();
		return tableName;
	}// end of parseCreateTable

	/**
	 * Parser en INSERT setning og returnerer navnet til tabellen.
	 * 
	 * @param insertString
	 *            INSERT setning.
	 * @return Navnet til tabellen.
	 */
	private String parseInsert(String insertString) {

		String start = insertString.replace("insert into ", "");
		int end = start.indexOf(" (");
		String tableName = "`" + start.substring(0, end) + "`";
		return tableName;
	}// end of parseInsert

	/**
	 * Setter stien hvor SQL koden til databasen vil bli lagret.
	 */
	private void setBackupPath() {

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setCurrentDirectory(new File("."));

		int resultat = jfc.showOpenDialog(this);
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			backupSqlField.setText(f.getAbsolutePath());
		} else {
			return;
		}

	}// end of setBackup

	/**
	 * Setter stien hvor bildene fra databasen vil bli lagret.
	 */
	private void setImagePath() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setCurrentDirectory(new File("."));

		int resultat = jfc.showOpenDialog(this);
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			backupImageField.setText(f.getAbsolutePath());
		} else {
			return;
		}

	}// End of setImagePath

	/**
	 * Setter stien hvor SQL koden vil bli hentet fra under gjenopprettingen av
	 * databasen.
	 */
	private void findBackupPath() {

		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"SQL filer", "sql");
		jfc.setFileFilter(filter);
		jfc.setCurrentDirectory(new File("."));

		int resultat = jfc.showOpenDialog(this);
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			restoreSqlField.setText(f.getAbsolutePath());
		} else {
			return;
		}

	}// end of findBackupPath

	/**
	 * Setter stien hvor bildene vil bli hentet fra under gjenopprettingen av
	 * databasen.
	 */
	private void findImagePath() {

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setCurrentDirectory(new File("."));

		int resultat = jfc.showOpenDialog(this);
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			restoreImageField.setText(f.getAbsolutePath());
		} else {
			return;
		}

	}// end of findImagePath

	/**
	 * Sjekker om feltene for sikkerhetskopieing-funksjonen er gyldige, Ut ifra
	 * resultatet vil metoden returnere true/false.
	 * 
	 * @return true/false.
	 */
	private boolean checkBackupFields() {
		if (backupSqlField.getText().equals("")
				|| backupImageField.getText().equals("")
				|| backupDatabaseField.getText().equals("")
				|| backupUserNameField.getText().equals("")) {
			return false;
		}

		return true;
	}// end of checkBackupFields

	/**
	 * Sjekker om feltene for gjenopprettings-funksjonen er gyldige, Ut ifra
	 * resultatet vil metoden returnere true/false.
	 * 
	 * @return true/false.
	 */
	private boolean checkRestoreFields() {

		if (restoreSqlField.getText().equals("")
				|| restoreImageField.getText().equals("")
				|| restoreDatabaseField.getText().equals("")
				|| restoreUserNameField.getText().equals("")) {
			return false;
		}

		return true;

	}// end of checkRestoreFields

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == backup) {
			backup();

		} else if (e.getSource() == restore) {

			restore();
		} else if (e.getSource() == backupSqlButton) {
			setBackupPath();
		} else if (e.getSource() == backupImageButton) {
			setImagePath();
		} else if (e.getSource() == restoreSqlButton) {
			findBackupPath();
		} else if (e.getSource() == restoreImageButton) {
			findImagePath();
		}

	}// end of actionPerformed

}// end of class SubPanel_AdminTools
