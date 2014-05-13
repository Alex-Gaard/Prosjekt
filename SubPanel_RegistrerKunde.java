package Boligformidling;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * Klassen inneholder GUI og metoder for å kunne registrere en
 * kunde(utleier/søker).
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_RegistrerKunde extends SubPanel implements ActionListener {

	private JButton regUtleier, regSøker;
	private JTextField personnummerField, navnField, adresseField, emailField,
			telefonField, postnummerField, firmaField;
	private JLabel lblPersonnummer, lblNavn, lblAdresse, lblPostnummer,
			lblEmail, lblTelefon, lblFirma, lblPassord, lblPassord2;
	private JPasswordField passordField, passord2Field;

	public SubPanel_RegistrerKunde(MainPanel parent) {
		super(parent);
		setLayout(new GridBagLayout());

		regUtleier = new JButton("Registrer utleier");
		regSøker = new JButton("Registrer søker");

		regUtleier.addActionListener(this);
		regSøker.addActionListener(this);

		passordField = new JPasswordField(15);
		passord2Field = new JPasswordField(15);

		personnummerField = new JTextField(15);
		navnField = new JTextField(15);
		adresseField = new JTextField(15);
		postnummerField = new JTextField(5);
		emailField = new JTextField(15);
		telefonField = new JTextField(15);
		firmaField = new JTextField(15);

		lblPersonnummer = new JLabel("Personnummer: ");
		lblNavn = new JLabel("Navn: ");
		lblAdresse = new JLabel("Adresse: ");
		lblPostnummer = new JLabel("Postnummer: ");
		lblEmail = new JLabel("Email: ");
		lblTelefon = new JLabel("Telefon: ");
		lblFirma = new JLabel("Firma: ");
		lblPassord = new JLabel("Passord: ");
		lblPassord2 = new JLabel("Gjenta passord: ");

		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.CENTER;

		JPanel outerJp = new JPanel(new GridLayout(0, 1));

		JPanel kundeJp = new JPanel();
		kundeJp.setLayout(new BoxLayout(kundeJp, BoxLayout.LINE_AXIS));
		TitledBorder title = BorderFactory.createTitledBorder("Søker/Utleier");

		kundeJp.setBorder(title);
		kundeJp.setMaximumSize(new Dimension(150, 400));

		JPanel innerKundeJp1 = new JPanel();
		innerKundeJp1.setLayout(new BoxLayout(innerKundeJp1,
				BoxLayout.PAGE_AXIS));

		innerKundeJp1.add(lblPersonnummer);
		innerKundeJp1.add(personnummerField);
		innerKundeJp1.add(Box.createVerticalStrut(5));

		innerKundeJp1.add(lblNavn);
		innerKundeJp1.add(navnField);
		innerKundeJp1.add(Box.createVerticalStrut(5));

		innerKundeJp1.add(lblAdresse);
		innerKundeJp1.add(adresseField);
		innerKundeJp1.add(Box.createVerticalStrut(5));

		innerKundeJp1.add(lblPostnummer);
		innerKundeJp1.add(postnummerField);
		innerKundeJp1.add(Box.createVerticalStrut(5));

		JPanel innerKundeJp2 = new JPanel();
		innerKundeJp2.setLayout(new BoxLayout(innerKundeJp2,
				BoxLayout.PAGE_AXIS));

		innerKundeJp2.add(lblEmail);
		innerKundeJp2.add(emailField);
		innerKundeJp2.add(Box.createVerticalStrut(5));

		innerKundeJp2.add(lblTelefon);
		innerKundeJp2.add(telefonField);
		innerKundeJp2.add(Box.createVerticalStrut(5));

		innerKundeJp2.add(lblPassord);
		innerKundeJp2.add(passordField);
		innerKundeJp2.add(Box.createVerticalStrut(5));

		innerKundeJp2.add(lblPassord2);
		innerKundeJp2.add(passord2Field);
		innerKundeJp2.add(Box.createVerticalStrut(5));

		kundeJp.add(innerKundeJp1);
		kundeJp.add(Box.createHorizontalStrut(5));
		kundeJp.add(innerKundeJp2);

		JPanel utleierJp = new JPanel();
		title = BorderFactory.createTitledBorder("Utleier");
		utleierJp.setLayout(new BoxLayout(utleierJp, BoxLayout.PAGE_AXIS));
		utleierJp.setBorder(title);
		utleierJp.setMaximumSize(new Dimension(150, 100));

		JPanel lblFirmaJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblFirmaJp.add(lblFirma);

		utleierJp.add(lblFirmaJp);
		firmaField.setMaximumSize(new Dimension(180, 20));
		utleierJp.add(firmaField);

		JPanel regUtleierJp = new JPanel();
		regUtleierJp.add(regUtleier);

		utleierJp.add(regUtleierJp);

		JPanel søkerJp = new JPanel();
		søkerJp.setLayout(new FlowLayout());
		title = BorderFactory.createTitledBorder("Søker");
		søkerJp.setBorder(title);
		søkerJp.setMaximumSize(new Dimension(150, 100));
		søkerJp.add(regSøker);
		søkerJp.add(Box.createVerticalStrut(72));

		JPanel outerRegJp = new JPanel();
		outerRegJp.setLayout(new BoxLayout(outerRegJp, BoxLayout.LINE_AXIS));

		JPanel innerRegUtleierJp = new JPanel(new BorderLayout());
		innerRegUtleierJp.add(utleierJp, BorderLayout.NORTH);

		JPanel innerRegSøkerJp = new JPanel(new BorderLayout());
		innerRegSøkerJp.add(søkerJp, BorderLayout.NORTH);

		outerRegJp.add(innerRegUtleierJp);
		outerRegJp.add(innerRegSøkerJp);

		outerJp.add(kundeJp);
		outerJp.add(outerRegJp);
		add(outerJp, c);

	}// end of constructor

	/**
	 * Registrerer en utleier(setter en ny utleier inn i databasen).
	 */
	public void registrerUtleier() {

		if (!checkFields("utleier")) {
			return;
		}

		String personnummer = this.personnummerField.getText();
		String navn = this.navnField.getText();
		String adresse = this.adresseField.getText();
		String email = this.emailField.getText();
		String telefon = this.telefonField.getText();
		String postnummer = this.postnummerField.getText();
		String firma = this.firmaField.getText();
		String passord = String.valueOf(this.passordField.getPassword());

		try {
			Data_Bruker.getConnection().setAutoCommit(false);

			if (!Data_Bruker.insertBruker(personnummer, navn, adresse, email,
					telefon, postnummer)) {
				System.out.println("Fikk ikke registret bruker");
				return;
			}

			if (!Data_Bruker.insertUtleier(personnummer, firma)) {
				System.out.println("Fikk ikke registret utleier");
				return;
			}

			if (!Data_Bruker.insertPassord(personnummer, passord)) {
				System.out.println("Fikk ikke registret passord");
				return;
			}

			Data_Bruker.getConnection().commit();
			displayMessage("Ny utleier ble registrert");
			cleanFields();
		} catch (SQLException ex) {
			System.out.println("Feil under innsetting: " + ex);
		}

	}// end of registrerUtleier

	/**
	 * Registrerer en søker(setter en ny søker inn i databasen).
	 */
	public void registrerSøker() {

		if (!checkFields("søker")) {
			return;
		}

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

			if (!Data_Bruker.insertSøker(personnummer)) {
				System.out.println("Fikk ikke registret søker");
				return;
			}

			if (!Data_Bruker.insertPassord(personnummer, passord)) {
				System.out.println("Fikk ikke registret passord");
				return;
			}

			Data_Bruker.getConnection().commit();
			displayMessage("Ny søker ble registrert");
			cleanFields();
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
				System.out.println("feil i registrerSøker: " + e);

			}
		}

	}// end of registrerSøker

	/**
	 * Nuller ut alle tekstfeltene i GUIen.
	 */
	public void cleanFields() {

		personnummerField.setText("");
		navnField.setText("");
		adresseField.setText("");
		postnummerField.setText("");
		emailField.setText("");
		telefonField.setText("");
		firmaField.setText("");

		passordField.setText("");
		passord2Field.setText("");

	}// end of cleanFields

	/**
	 * Sjekker om alle tekstfeltene i GUIen er fyllt ut med riktig informasjon.
	 * Utifra resultatet vil metoden returnere true/false.
	 * 
	 * @param gruppe
	 *            Hvilken kundegruppe(søker/utleier) kunden tilhører. Hvilke
	 *            felt som skal bli validert er avhengig av hvilken gruppe
	 *            kunden tilhører.
	 * @return true/false.
	 */
	public boolean checkFields(String gruppe) {

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

		if (gruppe.equals("utleier")) {
			if (firmaField.getText().equals("")) {
				displayMessage("Firma er ikke fyllt ut\n");
				return false;
			}

			if (firmaField.getText().length() > 45) {
				displayMessage("Firma kan ikke være lengere enn 45 tegn");
				return false;
			}
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
			displayMessage("Adresse kan ikke være lengere enn 45 tegn");
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
			displayMessage("Email kan ikke være lengere enn 45 tegn");
			return false;
		}

		if (!String.valueOf(passordField.getPassword()).equals(
				String.valueOf(passord2Field.getPassword()))) {
			displayMessage("Passordene må være like\n");
			return false;
		}

		if (passordField.getPassword().length > 45) {
			displayMessage("Passord kan ikke være lengere enn 45 tegn");
			return false;
		}

		return true;
	}// end of checkFields

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == regUtleier) {
			registrerUtleier();

		} else if (e.getSource() == regSøker) {
			registrerSøker();
		}

	}// end of actionPerformed

}// end of class SubPanel_RegistrerKunde
