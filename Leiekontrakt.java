package boligformidling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Klassen representerer en leiekontrakt i databasen.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 7 Mai 2014
 */
public class Leiekontrakt {

	private int boligID;
	private String behandlerPersonnummer;
	private String utleierPersonnummer;
	private String boligsøkerPersonnummer;
	private int leiepris;
	private Date avtaleStart;
	private Date avtaleSlutt;

	public Leiekontrakt(int boligID, String behandlerPersonnummer,
			String utleierPersonnummer, String boligsøkerPersonnummer,
			int leiepris, Date avtaleStart, Date avtaleSlutt) {

		this.behandlerPersonnummer = behandlerPersonnummer;
		this.boligID = boligID;
		this.utleierPersonnummer = utleierPersonnummer;
		this.boligsøkerPersonnummer = boligsøkerPersonnummer;
		this.leiepris = leiepris;
		this.avtaleStart = avtaleStart;
		this.avtaleSlutt = avtaleSlutt;

	}// end of constructor

	/**
	 * Returnerer personnummeret til kundebehandleren som opprettet
	 * leiekontrakten.
	 * 
	 * @return Personnummer til kundebehandler.
	 */
	public String getBehandlerPersonnummer() {

		return String.valueOf(behandlerPersonnummer);
	}// end of getBehandlerPersonnummer

	/**
	 * Returnerer IDen til boligen som leiekontrakten er opprettet for.
	 * 
	 * @return BoligID.
	 */
	public String getBoligID() {

		return String.valueOf(boligID);
	}

	/**
	 * Returnerer en streng som viser tidspunktet leiekontrakten ble opprettet
	 * og når kontrakten går ut.
	 * 
	 * @return Dato streng.
	 */
	public String displayThePeriod() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);
		String output = sdf.format(avtaleStart).toString() + " - ";
		output += sdf.format(avtaleSlutt).toString();
		return output;
	}// end of displayThePeriod

	/**
	 * Returnerer en ArrayList<String> som inneholder informasjon om
	 * leiekontrakten.
	 * 
	 * @return ArrayList<String> med leiekontrakt data.
	 */
	public ArrayList<String> getKontraktInfo() {

		ArrayList<String> infoList = new ArrayList<String>();

		infoList.add("Bolig ID: " + boligID);
		infoList.add("Behandler personnummer: " + behandlerPersonnummer);
		infoList.add("Utleier personnummer: " + utleierPersonnummer);
		infoList.add("Leietager personnummer: " + boligsøkerPersonnummer);
		infoList.add("Leiepris pr måned: " + leiepris);
		infoList.add("Avtale start: " + avtaleStart.toString());
		infoList.add("Avtale slutt: " + avtaleSlutt.toString());

		return infoList;

	}// End of visInfo

}// end of class Leiekontrakt