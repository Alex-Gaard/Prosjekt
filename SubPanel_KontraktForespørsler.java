package boligformidling;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Klassen inneholder GUI og metoder for å kunne behandle kontrakt forespørsler.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_KontraktForespørsler extends SubPanel implements
		ActionListener {

	private JTable forespørselTable, påtattTable;
	private JButton refresh, påtaForespørsel, slettPåtattKontrakt;
	private JLabel lblForespørsler, lblPåtatt;
	private final String[] forespørselKolonner = { "Boligsøker", "Bolig ID",
			"Opprettet" };
	private final String[] påtattKolonner = { "Kundebehandler", "Boligsøker",
			"Bolig ID", "Opprettet" };
	private DefaultTableModel forespørselModel, påtattModel;
	private String behandlerID;

	public SubPanel_KontraktForespørsler(Bruker b, MainPanel parent) {
		super(parent);

		behandlerID = b.getId();

		lblForespørsler = new JLabel("Forespørsler");
		lblPåtatt = new JLabel("Påtatt");

		refresh = new JButton("Oppdater");
		refresh.addActionListener(this);

		påtaForespørsel = new JButton("Påta forespørsel");
		påtaForespørsel.addActionListener(this);

		slettPåtattKontrakt = new JButton("Slett påtatt kontrakt");
		slettPåtattKontrakt.addActionListener(this);

		forespørselModel = new DefaultTableModel(null, forespørselKolonner) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		forespørselTable = new JTable(forespørselModel);
		forespørselTable.setFillsViewportHeight(false);
		forespørselTable.setEnabled(true);
		forespørselTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		påtattModel = new DefaultTableModel(null, påtattKolonner) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}// end of isCellEditable

			@Override
			public String getColumnName(int column) {
				return påtattKolonner[column];
			}// end of getColumnName
		};

		påtattTable = new JTable(påtattModel);
		påtattTable.setFillsViewportHeight(false);
		påtattTable.setEnabled(true);
		påtattTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel outer = new JPanel(new GridLayout(0, 1));

		JPanel upper = new JPanel();
		upper.setLayout(new BoxLayout(upper, BoxLayout.LINE_AXIS));

		JPanel upperLeft = new JPanel();
		upperLeft.setPreferredSize(new Dimension(400, 300));
		upperLeft.setLayout(new BoxLayout(upperLeft, BoxLayout.PAGE_AXIS));
		JPanel lblForespørslerJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblForespørslerJp.add(lblForespørsler);

		upperLeft.add(lblForespørslerJp);
		upperLeft.add(new JScrollPane(forespørselTable));

		JPanel upperRight = new JPanel();
		upperRight.setPreferredSize(new Dimension(400, 300));
		upperRight.setLayout(new BoxLayout(upperRight, BoxLayout.PAGE_AXIS));
		JPanel lblPåtattJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblPåtattJp.add(lblPåtatt);

		upperRight.add(lblPåtattJp);
		upperRight.add(new JScrollPane(påtattTable));

		upper.add(upperLeft);
		upper.add(Box.createHorizontalStrut(10));
		upper.add(upperRight);

		outer.add(upper);

		JPanel lower = new JPanel(new BorderLayout());
		JPanel lowerNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));

		lowerNorth.add(påtaForespørsel);
		lowerNorth.add(slettPåtattKontrakt);
		lowerNorth.add(refresh);

		lower.add(lowerNorth, BorderLayout.NORTH);

		outer.add(lower);
		add(outer);

		showRequests();
		showUndertakenRequests();
	}// end of constructor

	/**
	 * Viser forespørsler som ikke har blitt påtatt.
	 */
	private void showRequests() {

		while (forespørselModel.getRowCount() > 0) {
			forespørselModel.removeRow(0);
		}

		Data_Kontrakter.visKontraktForespørsler(forespørselModel);

	}// end of showRequests

	/**
	 * Påtar en forespørsel for kundebehandleren.
	 */
	private void takeOnRequest() {

		int rad = forespørselTable.getSelectedRow();
		if (rad == -1) {
			displayMessage("Du må velge en kontrakt fra tabellen får du kan påta deg den\n");
			return;
		}
		String søkerPersonnummer = (String) forespørselTable.getValueAt(rad,
				forespørselTable.getColumnModel().getColumnIndex("Boligsøker"));
		String boligID = (String) forespørselTable.getValueAt(rad,
				forespørselTable.getColumnModel().getColumnIndex("Bolig ID"));
		String dato = (String) forespørselTable.getValueAt(rad,
				forespørselTable.getColumnModel().getColumnIndex("Opprettet"));

		if (Data_Kontrakter.påtaKontrakt(behandlerID, søkerPersonnummer, dato,
				boligID)) {
			displayMessage("Påtagelsen var vellykket\n");
			showRequests();
			showUndertakenRequests();
		} else {

			displayMessage("Påtagelsen var mislykket\n");
		}

	}// end of takeOnRequest

	/**
	 * Viser forespørsler som kundebehandler har påtatt seg.
	 */
	private void showUndertakenRequests() {

		while (påtattModel.getRowCount() > 0) {
			påtattModel.removeRow(0);
		}

		if (!Data_Kontrakter.visPåtatteKontrakter(påtattModel, behandlerID)) {
			displayMessage("En feil oppstod\n");
		}

	}// end of showUndertakenRequests

	/**
	 * Fjerner en forespørsel som kundebehandler har påtatt seg.
	 */
	private void fjernPåtattKontrakt() {

		int rad = påtattTable.getSelectedRow();
		if (rad == -1) {
			displayMessage("Du må velge en kontrakt fra tabellen får du kan påta deg den\n");
			return;
		}

		String søkerPersonnummer = (String) påtattTable.getValueAt(rad,
				påtattTable.getColumnModel().getColumnIndex("Boligsøker"));
		String boligID = (String) påtattTable.getValueAt(rad, påtattTable
				.getColumnModel().getColumnIndex("Bolig ID"));
		String dato = (String) påtattTable.getValueAt(rad, påtattTable
				.getColumnModel().getColumnIndex("Opprettet"));

		if (Data_Kontrakter.fjernForespørsel(behandlerID, søkerPersonnummer,
				boligID, dato)) {
			displayMessage("Forespørselen ble fjernet\n");
			showRequests();
			showUndertakenRequests();
		} else {
			displayMessage("En feil oppstod under fjerningen av forespørselen\n");
		}

	}// end of fjernPåtattKontrakt

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == refresh) {
			showRequests();
			showUndertakenRequests();
		} else if (e.getSource() == påtaForespørsel) {
			takeOnRequest();

		} else if (e.getSource() == slettPåtattKontrakt) {
			fjernPåtattKontrakt();
		}

	}// end of actionPerformed

}// end of SubPanel_KontraktForespørsler
