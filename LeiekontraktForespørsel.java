package Boligformidling;

import java.sql.Date;

/**
 * Klassen representerer en leiekontrakt forespørsel i databasen.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class LeiekontraktForespørsel {

	private String søkerPersonnummer;
	private String behandlerPersonnummer;
	private int boligID;
	private Date opprettetDato;
    
	/**
	 * Oppretter en leiekontrakt forespørsel med behandler personnummer, dvs at forespørselen er påtatt.
	 * 
	 * @param søkerPersonnummer Boligsøker personnummer.
	 * @param behandlerPersonnummer Behandler personnummer.
	 * @param boligID Bolig ID.
	 * @param opprettetDato Dato for opprettelse.
	 */
	public LeiekontraktForespørsel(String søkerPersonnummer,
			String behandlerPersonnummer, int boligID, Date opprettetDato) {
		this.søkerPersonnummer = søkerPersonnummer;
		this.behandlerPersonnummer = behandlerPersonnummer;
		this.boligID = boligID;
		this.opprettetDato = opprettetDato;

	}// end of constructor
	
	/**
	 * Oppretter en leiekontrakt forespørsel uten behandler personnummer,dvs at forespørselen ikke er påtatt.
	 * 
	 * @param søkerPersonnummer
	 * @param boligID
	 * @param dato
	 */
	public LeiekontraktForespørsel(String søkerPersonnummer, int boligID,
			Date dato) {
		this.søkerPersonnummer = søkerPersonnummer;
		this.boligID = boligID;
		this.opprettetDato = dato;

	}// end of overloaded constructor

	/**
	 * Returnerer personnummeret til boligsøkeren som sendte forespørselen.
	 * 
	 * @return Personnummer til boligsøker.
	 */
	public String getSøkerPersonnummer() {

		return søkerPersonnummer;
	}// end of getSøkerPersonnummer

	/**
	 * Returnerer personnummeret til kundebehandleren som har påtatt seg
	 * kontrakten.
	 * 
	 * @return Personnummer til kundebehandler.
	 */
	public String getBehandlerPersonnummer() {
		return behandlerPersonnummer;
	}// end of getBehandlerPersonnummer

	/**
	 * Returnerer boligIDen til forespørselen.
	 * 
	 * @return BoligID.
	 */
	public int getBoligID() {

		return boligID;
	}// end of getBoligID

	/**
	 * Returnerer datoen når forespørselen ble opprettet.
	 * 
	 * @return Opprettelses dato.
	 */
	public Date getDato() {
		return opprettetDato;
	}// end of getDato

	/**
	 * Returnerer info om forespørselen.
	 * 
	 * @return Info om forespørselen.
	 */
	public String[] getInfo() {
		String[] s = { søkerPersonnummer, String.valueOf(boligID),
				opprettetDato.toString() };
		return s;
	}// end of getInfo

}// end of class LeiekontraktForespørsel
