package boligformidling;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Klassen inneholder GUI og metoder for å kunne registrere en leiekontrakt.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_RegistrerKontrakt extends SubPanel implements
		ActionListener {

	private JButton regKontrakt;
	private JTextField behandlerID, boligID, utleierID, leietagerID, leiepris,
			start, slutt;
	private JLabel lblBehandlerID, lblBoligID, lblUtleier, lblLeietager,
			lblPris, lblStart, lblSlutt;
	private JComboBox avtaleStartÅr, avtaleStartMåned, avtaleStartDag,
			avtaleSluttÅr, avtaleSluttMåned, avtaleSluttDag;;

	public SubPanel_RegistrerKontrakt(MainPanel parent) {
		super(parent);

		regKontrakt = new JButton("Registrer");
		regKontrakt.addActionListener(this);

		behandlerID = new JTextField(8);
		boligID = new JTextField(8);

		utleierID = new JTextField(20);
		leietagerID = new JTextField(20);

		leiepris = new JTextField(8);

		lblBehandlerID = new JLabel("Kundebehandler personnummer: ");
		lblBoligID = new JLabel("Bolig ID: ");
		lblUtleier = new JLabel("Utleier personnummer: ");
		lblLeietager = new JLabel("Leietager personnummer: ");
		lblPris = new JLabel("Leiepris: ");
		lblStart = new JLabel("Avtale start: ");
		lblSlutt = new JLabel("Avtale slutt: ");

		int year = Calendar.getInstance().get(Calendar.YEAR);
		String[] yArray = new String[100];

		for (int i = 0; i < yArray.length; i++) {
			yArray[i] = String.valueOf(year + i);
		}

		String[] mArray = new String[12];

		for (int i = 0; i < mArray.length; i++) {
			mArray[i] = String.valueOf(i + 1);
		}

		String[] dArray = new String[31];

		for (int i = 0; i < dArray.length; i++) {
			dArray[i] = String.valueOf(i + 1);
		}

		Calendar nå = Calendar.getInstance();

		avtaleStartÅr = new JComboBox(yArray);
		avtaleStartÅr.setBackground(Color.white);

		avtaleStartMåned = new JComboBox(mArray);
		avtaleStartMåned.setSelectedIndex(nå.get(Calendar.MONTH));
		avtaleStartMåned.setBackground(Color.white);

		avtaleStartDag = new JComboBox(dArray);
		avtaleStartDag.setSelectedIndex(nå.get(Calendar.DAY_OF_MONTH) - 1);
		avtaleStartDag.setBackground(Color.white);

		avtaleSluttÅr = new JComboBox(yArray);
		avtaleSluttÅr.setBackground(Color.white);

		avtaleSluttMåned = new JComboBox(mArray);
		avtaleSluttMåned.setSelectedIndex(nå.get(Calendar.MONTH));
		avtaleSluttMåned.setBackground(Color.white);

		avtaleSluttDag = new JComboBox(dArray);
		avtaleSluttDag.setSelectedIndex(nå.get(Calendar.DAY_OF_MONTH));
		avtaleSluttDag.setBackground(Color.white);

		JPanel outerJp = new JPanel();
		outerJp.setLayout(new BoxLayout(outerJp, BoxLayout.PAGE_AXIS));

		JPanel lblBehandlerIDJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblBehandlerIDJp.add(lblBehandlerID);

		outerJp.add(lblBehandlerIDJp);
		outerJp.add(behandlerID);

		JPanel lblBoligIDJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblBoligIDJp.add(lblBoligID);

		outerJp.add(lblBoligIDJp);
		outerJp.add(boligID);

		JPanel lblUtleierJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblUtleierJp.add(lblUtleier);

		outerJp.add(lblUtleierJp);
		outerJp.add(utleierID);

		JPanel lblLeietagerJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblLeietagerJp.add(lblLeietager);

		outerJp.add(lblLeietagerJp);
		outerJp.add(leietagerID);

		JPanel lblPrisJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPrisJp.add(lblPris);

		outerJp.add(lblPrisJp);
		outerJp.add(leiepris);

		JPanel lblStartJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblStartJp.add(lblStart);

		outerJp.add(lblStartJp);
		outerJp.add(avtaleStartDag);
		outerJp.add(avtaleStartMåned);
		outerJp.add(avtaleStartÅr);

		JPanel lblSluttJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblSluttJp.add(lblSlutt);

		outerJp.add(lblSluttJp);
		outerJp.add(avtaleSluttDag);
		outerJp.add(avtaleSluttMåned);
		outerJp.add(avtaleSluttÅr);

		JPanel regButtonJp = new JPanel(new FlowLayout(FlowLayout.CENTER));
		regButtonJp.add(regKontrakt);

		outerJp.add(regButtonJp);
		add(outerJp);
		
	}// end of constructor

	/**
	 * Sjekker om kombinasjonen av dag/måned/år er en gyldig dato. Ut ifra
	 * resultatet vil metoden returnere true/false.
	 * 
	 * @param dag
	 *            Dagen i datoen.
	 * @param Måned
	 *            Måneden i datoen.
	 * @param år
	 *            Året i datoen.
	 * @return true/false.
	 */
	private boolean checkDate(String dag, String Måned, String år) {
		String date = dag + "-" + Måned + "-" + år;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			sdf.setLenient(false);
			sdf.parse(date);
		} catch (ParseException pe) {
			return false;
		}

		return true;
	}// End of checkDate

	/**
	 * Sjekker om en utleier med det spesifiserte personnummeret eksisterer i
	 * databasen. Ut ifra resultatet vil metoden returnere true/false.
	 * 
	 * @param utleierPersonnummer
	 *            Personnummmeret til utleieren.
	 * @return true/false.
	 */
	private boolean checkUtleierPersonnummer(String utleierPersonnummer) {

		return Data_Login.userExists(Data_Login.TABLE_UTLEIER,
				utleierPersonnummer);

	}// end of checkUtleierPersonnummer

	/**
	 * Sjekker om en boligsøker med det spesifiserte personnummeret eksisterer i
	 * databasen. Ut ifra resultatet vil metoden returnere true/false.
	 * 
	 * @param boligsøkerPersonnummer
	 *            Personnummmeret til boligsøkeren.
	 * @return true/false.
	 */
	private boolean checkBoligsøkerPersonnummer(String boligsøkerPersonnummer) {

		return Data_Login.userExists(Database.TABLE_BOLIGSØKER,
				boligsøkerPersonnummer);

	}// end of checkBoligsøkerPersonnummer

	/**
	 * Sjekker om en behandler med det spesifiserte personnummeret eksisterer i
	 * databasen. Ut ifra resultatet vil metoden returnere true/false.
	 * 
	 * @param behandlerPersonnummer
	 *            Personnummmeret til behandleren.
	 * @return true/false.
	 */
	private boolean checkBehandlerPersonnummer(String behandlerPersonnummer) {

		return Data_Login.userExists(Data_Login.TABLE_BEHANDLER,
				behandlerPersonnummer);

	}// end of checkBehandlerPersonnummer

	/**
	 * Sjekker om en bolig med den spesifiserte IDen eksisterer i databasen.
	 * Ut ifra resultatet vil metoden returnere true/false.
	 * 
	 * @param boligID
	 *            IDen til boligen.
	 * @return true/false.
	 */
	private boolean checkBoligID(String boligID) {

		return Data_Kontrakter.boligExists(boligID);

	}// end of checkBoligID

	/**
	 * Sjekker om det eksisterer en aktiv kontrakt for boligen med den
	 * spesifiserte IDen. Ut ifra resultatet vil metoden returnere true/false.
	 * 
	 * @param boligID
	 *            IDen til boligen.
	 * @return true/false.
	 */
	private boolean checkKontrakt(String boligID) {

		return !Data_Kontrakter.aktivKontraktExists(boligID);

	}// end of checkKontrakt

	/**
	 * Sjekker om alle feltene i GUIen er fyll inn med riktig informasjon.
	 * Ut ifra resultatet vil metoden returnere true/false.
	 * 
	 * @return true/false.
	 */
	private boolean checkFields() {
		String behandlerPersonnummer = this.behandlerID.getText();
		String boligID = this.boligID.getText();
		String utleierPersonnummer = this.utleierID.getText();
		String boligsøkerPersonnummer = this.leietagerID.getText();
		String leiepris = this.leiepris.getText();

		if (behandlerID.equals("") || boligID.equals("")
				|| utleierPersonnummer.equals("")
				|| boligsøkerPersonnummer.equals("") || leiepris.equals("")) {

			displayMessage("Et eller flere av feltene mangler informasjon");
			return false;
		}

		if (!isNum(behandlerPersonnummer)) {
			displayMessage("Behandler personnummer er feil");
			return false;
		}

		if (!isNum(boligID)) {
			displayMessage("Bolig ID er feil");
			return false;
		}

		if (!isNum(leiepris)) {
			displayMessage("Leiepris er feil");
			return false;
		}

		if (Long.parseLong(leiepris) > Integer.MAX_VALUE) {
			displayMessage("Verdien til leiepris er for stor");
			return false;
		}

		if (!checkBoligID(boligID)) {
			displayMessage("Det finnes ikke en bolig med ID: " + boligID);
			return false;
		}

		if (!checkUtleierPersonnummer(utleierPersonnummer)) {
			displayMessage("Det finnes ikke en utleier med personnummer: "
					+ utleierPersonnummer);
			return false;
		}

		if (!checkBoligsøkerPersonnummer(boligsøkerPersonnummer)) {
			displayMessage("Det finnes ikke en boligsøker med personnummer: "
					+ boligsøkerPersonnummer);
			return false;
		}

		if (!checkBehandlerPersonnummer(behandlerPersonnummer)) {
			displayMessage("Det finnes ikke en behandler med personnummer: "
					+ behandlerPersonnummer);
			return false;
		}

		String startDag = (String) avtaleStartDag.getSelectedItem();
		String startMåned = (String) avtaleStartMåned.getSelectedItem();
		String startÅr = (String) avtaleStartÅr.getSelectedItem();

		if (!checkDate(startDag, startMåned, startÅr)) {
			displayMessage("Start datoen var ikke gyldig\n");
			return false;
		}

		String sluttDag = (String) avtaleSluttDag.getSelectedItem();
		String sluttMåned = (String) avtaleSluttMåned.getSelectedItem();
		String sluttÅr = (String) avtaleSluttÅr.getSelectedItem();

		if (!checkDate(sluttDag, sluttMåned, sluttÅr)) {
			displayMessage("Slutt datoen var ikke gyldig\n");
			return false;
		}		
		
		Calendar startDato = Calendar.getInstance();
		startDato.set(Integer.parseInt(startÅr), Integer.parseInt(startMåned),Integer.parseInt(startDag));
		
		Calendar sluttDato = Calendar.getInstance();
		sluttDato.set(Integer.parseInt(sluttÅr), Integer.parseInt(sluttMåned),Integer.parseInt(sluttDag));

		if(!startDato.before(sluttDato)){
			displayMessage("Slutt datoen må være etter start datoen!");
			return false;
		}
		
		if (!checkKontrakt(boligID)) {
			displayMessage("Kontrakten for bolig ID " + boligID
					+ " er fortsatt aktiv");
			return false;
		}

		return true;
	}// end of checkFields

	/**
	 * Setter alle feltene i GUIen tilbake til sin opprinnelige verdi.
	 */
	private void resetFields() {

		behandlerID.setText("");
		boligID.setText("");
		utleierID.setText("");
		leietagerID.setText("");
		leiepris.setText("");

		Calendar nå = Calendar.getInstance();

		avtaleStartÅr.setSelectedIndex(0);
		avtaleStartMåned.setSelectedIndex(nå.get(Calendar.MONTH));
		avtaleStartDag.setSelectedIndex(nå.get(Calendar.DAY_OF_MONTH) - 1);

		avtaleSluttÅr.setSelectedIndex(0);
		avtaleSluttMåned.setSelectedIndex(nå.get(Calendar.MONTH));
		avtaleSluttDag.setSelectedIndex(nå.get(Calendar.DAY_OF_MONTH));
	}// end of cleanFields

	/**
	 * Registrerer en kontrakt. Ut ifra om registreringen var vellykket eller
	 * mislykket, vil metoden returnere true/false.
	 * 
	 * @return true/false.
	 */
	private boolean registrerKontrakt() {

		String behandlerPersonnummer = this.behandlerID.getText();
		int boligID = Integer.parseInt(this.boligID.getText());
		String utleierPersonnummer = this.utleierID.getText();
		String søkerPersonnummer = this.leietagerID.getText();
		int leiepris = Integer.parseInt(this.leiepris.getText());

		String startDag = (String) avtaleStartDag.getSelectedItem();
		String startMåned = (String) avtaleStartMåned.getSelectedItem();
		String StartÅr = (String) avtaleStartÅr.getSelectedItem();

		String sluttDag = (String) avtaleSluttDag.getSelectedItem();
		String sluttMåned = (String) avtaleSluttMåned.getSelectedItem();
		String sluttÅr = (String) avtaleSluttÅr.getSelectedItem();

		String startDato = StartÅr + "-" + startMåned + "-" + startDag;
		String sluttDato = sluttÅr + "-" + sluttMåned + "-" + sluttDag;

		if (Data_Kontrakter.insertKontrakt(boligID, behandlerPersonnummer,
				utleierPersonnummer, søkerPersonnummer, leiepris, startDato,
				sluttDato)) {
			return true;
		} else {
			return false;
		}

	}// end of registrerKontrakt

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == regKontrakt) {
			if (checkFields()) {
				if (registrerKontrakt()) {
					resetFields();
					displayMessage("Registreringen var vellykket");

				} else {
					displayMessage("Registreringen var mislykket");
				}

			}

		}

	}// end of actionPerformed

}// end of class SubPanel_RegistrerKontrakt
