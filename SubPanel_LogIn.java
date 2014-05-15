package boligformidling;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Klassen inneholder GUI og metoder for å kunne logge inn en bruker.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_LogIn extends SubPanel implements ActionListener {

	private JButton login;
	private JLabel lblWelcomeText, lblPersonnummer, lblPassord;
	private JTextField personnummerField;
	private JPasswordField passord;
	private boolean søker = false;
	private boolean utleier = false;
	private boolean kundebehandler = false;

	public SubPanel_LogIn(MainPanel parent) {
		super(parent);
		setLayout(new GridBagLayout());
	
		lblWelcomeText = new JLabel("Velkommen til boligformidling!");
		lblWelcomeText.setFont(new Font("Plain", Font.BOLD, 18));

		lblPersonnummer = new JLabel("Personnummer: ");
		lblPassord = new JLabel("Passord: ");

		login = new JButton("Logg inn");
		personnummerField = new JTextField(15);
		passord = new JPasswordField(15);

		login.addActionListener(this);
				   
		JPanel outerJp = new JPanel();
		outerJp.setLayout(new BoxLayout(outerJp, BoxLayout.PAGE_AXIS));

		JPanel welcomeJp = new JPanel();
		welcomeJp.add(lblWelcomeText);

		JPanel outerLoginJp = new JPanel();

		JPanel loginJp = new JPanel();
		loginJp.setLayout(new BoxLayout(loginJp, BoxLayout.PAGE_AXIS));

		JPanel lblPNrJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPNrJp.add(lblPersonnummer);

		JPanel fieldPNrJp = new JPanel();
		fieldPNrJp.add(personnummerField);

		JPanel lblPasswordJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPasswordJp.add(lblPassord);

		JPanel fieldPasswordJp = new JPanel();
		fieldPasswordJp.add(passord);

		JPanel loginButtonJp = new JPanel();
		loginButtonJp.add(login);

		loginJp.add(lblPNrJp);
		loginJp.add(fieldPNrJp);
		loginJp.add(lblPasswordJp);
		loginJp.add(fieldPasswordJp);
		loginJp.add(loginButtonJp);

		outerLoginJp.add(loginJp);

		outerJp.add(welcomeJp);
		outerJp.add(outerLoginJp);

		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.CENTER;

		add(outerJp, c);

	}// end of constructor

	/**
	 * Sjekker om informasjonen brukeren oppga representerer en bruker i
	 * systemet, hvis dette ikke er tilfellet blir false returnert.
	 * 
	 * @return true/false.
	 */
	private boolean loggInnBruker() {

		if (!checkFields()) {
			passord.setText("");
			displayMessage("Informasjonen du oppga var ikke riktig");
			return false;
		}

		String id = this.personnummerField.getText();
		String password = String.valueOf(passord.getPassword());

		if (!Data_Login.userMatchesPassword(id, password)) {
			passord.setText("");
			displayMessage("Personnummeret eller passordet som ble oppgitt var feil!");
			return false;
		}

		if (Data_Login.userExists(Data_Login.TABLE_UTLEIER, id)) {
			utleier = true;
		} 
		
		if (Data_Login.userExists(Data_Login.TABLE_BOLIGSØKER, id)) {
			søker = true;
		} 

		if (Data_Login.userExists(Data_Login.TABLE_BEHANDLER, id)) {
			kundebehandler = true;
		} 

		if (søker || utleier || kundebehandler) {
			return true;
		} else {
			return false;
		}

	}// end of loggInnBruker

	/**
	 * Sjekker om feltene er fyllt ut med riktig informasjon. Ut ifra resultatet
	 * vil metoden returnere true/false.
	 * 
	 * @return true/false.
	 */
	private boolean checkFields() {

		if (personnummerField.getText().equals("")
				|| String.valueOf(passord.getPassword()).equals("")) {
			return false;
		} else if (!isNum(personnummerField.getText())) {
			return false;
		} else {
			return true;
		}

	}// end of checkFields

	/**
	 * Henter informasjon om en bruker Ut ifra den spesifiserte IDen, og
	 * returnerer et bruker objekt som representerer brukeren.
	 * 
	 * @param id
	 *            IDen til brukeren.
	 * @return Bruker objekt.
	 */
	private Bruker getBrukerInfo(String id) {

		String brukerID = "";
		String navn = "";
		String tlf = "";
		String email = "";
		String adresse = "";
		String postnummer = "";

		try (ResultSet rs = Data_Login.getBrukerResult(id)) {

			while (rs.next()) {
				brukerID = rs.getString(Data_Login.COLUMN_PERSONNUMMER);
				navn = rs.getString(Data_Login.COLUMN_NAVN);
				tlf = rs.getString(Data_Login.COLUMN_TELEFON);
				email = rs.getString(Data_Login.COLUMN_EMAIL);
				adresse = rs.getString(Data_Login.COLUMN_ADRESSE);
				postnummer = rs.getString(Data_Login.COLUMN_POSTNUMMER);
			}

		} catch (SQLException ex) {
			System.out.println("Feil i getBruker: " + ex);
		}

		if (søker && utleier) {
			Bruker b = new Bruker(brukerID, navn, new String[] { Bruker.SØKER,
					Bruker.UTLEIER }, tlf, adresse, email, postnummer);
			return b;
		} else if (søker) {
			Bruker b = new Bruker(brukerID, navn,
					new String[] { Bruker.SØKER }, tlf, adresse, email,
					postnummer);
			return b;
		} else if (utleier) {
			Bruker b = new Bruker(brukerID, navn,
					new String[] { Bruker.UTLEIER }, tlf, adresse, email,
					postnummer);
			return b;
		} else if (kundebehandler) {
			Bruker b = new Bruker(brukerID, navn,
					new String[] { Bruker.BEHANDLER }, tlf, adresse, email,
					postnummer);
			return b;
		}

		return null;
	}// end of getBruker
	
	/**
	 * Logger en bruker inn i applikasjonen.
	 */
	private void logIn(){
		
		Bruker b = null;
		String id = personnummerField.getText();

		if (loggInnBruker()) {
			b = getBrukerInfo(id);
		}

		if (b == null) {
			søker = false;
			utleier = false;
			kundebehandler = false;
			return;
		}

		if (kundebehandler) {
			parent.getRoot().swap(
					new MainPanel_Kundebehandler(parent.getRoot(), b));
		} else if (søker || utleier) {
			parent.getRoot().swap(new MainPanel_Kunde(parent.getRoot(), b));
		} else {
			passord.setText("");
			displayMessage("Personnummeret eller passordet som ble oppgitt var feil!");
		}

		søker = false;
		utleier = false;
		kundebehandler = false;

	}//end of logIn

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {
			
			logIn();
		}

	}// end of actionPerformed
	
}// end of class SubPanel_Login
