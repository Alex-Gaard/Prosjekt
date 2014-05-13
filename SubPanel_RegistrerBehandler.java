package boligformidling;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Klassen inneholder GUI og metoder for å kunne registrere en kundebehandler.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_RegistrerBehandler extends SubPanel implements
		ActionListener {

	private JButton regBehandler;
	private JTextField personnummerField, navnField, adresseField, emailField,
			telefonField, postnummerField;
	private JLabel lblPersonnummer, lblNavn, lblAdresse, lblPostnummer,
			lblEmail, lblTelefon, lblPassord, lblPassord2;
	private JPasswordField passordField, passord2Field;

	public SubPanel_RegistrerBehandler(MainPanel parent) {
		super(parent);

		regBehandler = new JButton("Registrer behandler");
		regBehandler.addActionListener(this);

		lblPersonnummer = new JLabel("Personnummer: ");
		lblNavn = new JLabel("Navn: ");
		lblAdresse = new JLabel("Adresse: ");
		lblPostnummer = new JLabel("Postnummer: ");
		lblEmail = new JLabel("Email: ");
		lblTelefon = new JLabel("Telefon: ");
		lblPassord = new JLabel("Passord: ");
		lblPassord2 = new JLabel("Gjenta passord: ");

		passordField = new JPasswordField(15);
		passord2Field = new JPasswordField(15);

		personnummerField = new JTextField(15);
		navnField = new JTextField(15);
		adresseField = new JTextField(15);
		postnummerField = new JTextField(5);
		emailField = new JTextField(15);
		telefonField = new JTextField(15);

		JPanel outerJp = new JPanel();
		outerJp.setLayout(new BoxLayout(outerJp, BoxLayout.PAGE_AXIS));

		JPanel lblPersonnummerJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPersonnummerJp.add(lblPersonnummer);

		outerJp.add(lblPersonnummerJp);
		outerJp.add(personnummerField);

		JPanel lblNavnJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblNavnJp.add(lblNavn);

		outerJp.add(lblNavnJp);
		outerJp.add(navnField);

		JPanel lblAdresseJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblAdresseJp.add(lblAdresse);

		outerJp.add(lblAdresseJp);
		outerJp.add(adresseField);

		JPanel lblPostnummerJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPostnummerJp.add(lblPostnummer);

		outerJp.add(lblPostnummerJp);
		outerJp.add(postnummerField);

		JPanel lblEmailJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblEmailJp.add(lblEmail);

		outerJp.add(lblEmailJp);
		outerJp.add(emailField);

		JPanel lblTelefonJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblTelefonJp.add(lblTelefon);

		outerJp.add(lblTelefonJp);
		outerJp.add(telefonField);

		JPanel lblPassordJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPassordJp.add(lblPassord);

		outerJp.add(lblPassordJp);
		outerJp.add(passordField);

		JPanel lblPassord2Jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPassord2Jp.add(lblPassord2);

		outerJp.add(lblPassord2Jp);
		outerJp.add(passord2Field);

		JPanel regButtonJp = new JPanel();
		regButtonJp.add(regBehandler);

		outerJp.add(regButtonJp);

		add(outerJp);

	}// end of constructor

	/**
	 * Sjekker om feltene er gyldige før kundebehandleren blir registrert,
	 * utifra resultatet vil metoden returnere true/false.
	 * 
	 * @return true/false
	 */
	public boolean checkFields() {

		if (personnummerField.getText().equals("")
				|| navnField.getText().equals("")
				|| adresseField.getText().equals("")
				|| postnummerField.getText().equals("")
				|| emailField.getText().equals("")
				|| telefonField.getText().equals("")) {
			displayMessage("Et eller flere av feltene er ikke fyllt ut\n");
			return false;
		}

		if (String.valueOf(passordField.getPassword()).equals("")
				|| String.valueOf(passord2Field.getPassword()).equals("")) {
			displayMessage("Fyll inn begge passordfeltene\n");
			return false;
		}

		if (!isNum(personnummerField.getText())
				|| personnummerField.getText().length() != 11) {
			displayMessage("Personnummer er ikke riktig\n");
			return false;
		}

		if (navnField.getText().length() > 45) {
			displayMessage("Navnet kan ikke være lengere enn 45 tegn");
			return false;
		}

		if (adresseField.getText().length() > 45) {
			displayMessage("Adressen kan ikke være lengere enn 45 tegn");
			return false;
		}

		if (!isNum(telefonField.getText())
				|| telefonField.getText().length() > 8) {
			displayMessage("Telefonnummeret er ikke riktig\n");
			return false;
		}

		if (!isNum(postnummerField.getText())
				|| postnummerField.getText().length() != 4) {
			displayMessage("Postnummeret er ikke riktig\n");
			return false;
		}

		if (emailField.getText().indexOf("@") == -1) {
			displayMessage("Email er ikke riktig\n");
			return false;
		}

		if (emailField.getText().length() > 45) {
			displayMessage("Emailen kan ikke være lengere enn 45 tegn");
			return false;
		}

		if (!String.valueOf(passordField.getPassword()).equals(
				String.valueOf(passord2Field.getPassword()))) {
			displayMessage("Passordene må være like\n");
			return false;
		}

		if (passordField.getPassword().length > 45) {
			displayMessage("Passordet kan ikke være lengere enn 45 tegn");
			return false;
		}

		return true;
	}// end of checkFields

	/**
	 * Registrerer en kundebehandler utifra informasjonen som er fyllt inn i
	 * feltene.
	 */
	public void registrerBehandler() {

		if (!checkFields())
			return;

		String personnummer = this.personnummerField.getText();
		String navn = this.navnField.getText();
		String adresse = this.adresseField.getText();
		String email = this.emailField.getText();
		String telefon = this.telefonField.getText();
		String postnummer = this.postnummerField.getText();
		String passord = String.valueOf(this.passordField.getPassword());

		try {
			Data_Bruker.getConnection().setAutoCommit(false);

			if (!Data_Bruker.insertBruker(personnummer, navn, adresse, email,
					telefon, postnummer)) {
				System.out.println("Fikk ikke registret bruker");
				return;
			}

			if (!Data_Bruker.insertBehandler(personnummer)) {
				System.out.println("Fikk ikke registret beahndler");
				return;
			}

			if (!Data_Bruker.insertPassord(personnummer, passord)) {
				System.out.println("Fikk ikke registret passord");
				return;
			}

			Data_Bruker.getConnection().commit();
			displayMessage("Ny behandler ble registrert");
			resetFields();
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ex) {
			displayMessage("En bruker med dette personnummeret eksisterer allerede!");

		} catch (SQLException ex) {
			try {
				Data_Bruker.getConnection().rollback();
			} catch (SQLException e) {
				System.out.println("feil ved rollback: " + e);
			}

			System.out.println("Feil under innsetting: " + ex);
		} finally {
			try {
				Data_Bruker.getConnection().setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println("feil i registrerBehandler: " + e);

			}
		}

	}// end of registrerBehandler

	/**
	 * Nuller ut alle tekstfeltene i GUIen.
	 */
	public void resetFields() {

		personnummerField.setText("");
		navnField.setText("");
		adresseField.setText("");
		postnummerField.setText("");
		emailField.setText("");
		telefonField.setText("");

		passordField.setText("");
		passord2Field.setText("");

	}// end of resetFields

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == regBehandler) {

			registrerBehandler();
		}

	}

}// end of class SubPanel_RegistrerBehandler
