package boligformidling;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * Klassen inneholder metoder for å hente ut og vise frem statistikk om
 * databasen.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_Statistikk extends SubPanel implements ActionListener {

	private String[] tables = { "bolig", "enebolig_og_rekkehus", "leilighet",
			"bruker", "utleier", "boligsøker", "leiekontrakt",
			"leiekontrakt_forespørsel" };

	private String[] info = { "Antall boliger: ", "Antall eneboliger: ",
			"Antall leiligeheter: ", "Antall kunder: ", "Antall utleiere: ",
			"Antall boligsøkere: ", "Leiekontrakter: ",
			"Kontrakt forespørsler: " };

	private String[] forslag = { "Averterte boliger",
			"Opprettede forespørsler", "Opprettede brukere" };

	private JButton hentStatistikk, hentInfo;
	private JTextField yearField;
	private JLabel lblYear;
	private JComboBox jcb;
	private JPanel graphJp, infoJp;

	public SubPanel_Statistikk(MainPanel parent) {
		super(parent);

		hentStatistikk = new JButton("Tegn graf");
		hentStatistikk.addActionListener(this);

		hentInfo = new JButton("Vis info");
		hentInfo.addActionListener(this);

		lblYear = new JLabel("År: ");
		yearField = new JTextField(5);

		jcb = new JComboBox(forslag);
		jcb.setSelectedIndex(0);

		infoJp = new JPanel();
		infoJp.setLayout(new BoxLayout(infoJp, BoxLayout.PAGE_AXIS));

		graphJp = new JPanel(new BorderLayout());
		TitledBorder title = BorderFactory.createTitledBorder("Graf/Info");
		graphJp.setBorder(title);
		graphJp.setPreferredSize(new Dimension(550, 420));
		
		add(lblYear);
		add(yearField);
		add(jcb);
		add(hentStatistikk);
		add(hentInfo);
		add(graphJp);

	}// end of constructor

	/**
	 * Tegner en ny graf på SubPanel_Statistikk Ut ifra informasjonen som er
	 * fyllt inn i feltene.
	 */
	private void drawGraph() {

		String år = yearField.getText();
		String forslag = (String) jcb.getItemAt(jcb.getSelectedIndex());
		String table = "";
		String column = "";

		if (forslag.equals("Averterte boliger")) {
			table = Data_Statistikk.TABLE_BOLIG;
			column = Data_Statistikk.COLUMN_AVERTERT;
		} else if (forslag.equals("Opprettede forespørsler")) {
			table = Data_Statistikk.TABLE_LEIEKONTRAKT_FORESPØRSEL;
			column = Data_Statistikk.COLUMN_OPPRETTET_DATO;
		} else if (forslag.equals("Opprettede brukere")) {
			table = Data_Statistikk.TABLE_BRUKER;
			column = Data_Statistikk.COLUMN_OPPRETTET;
		}

		swapGraph(new GraphPanel(år, table, column).getPanel());

	}// end of drawGraph

	/**
	 * Bytter ut graf panelet i SubPanel_Statistikk med et nytt graf panel.
	 * 
	 * @param graphPanel
	 *            Det nye graf panelet.
	 */
	private void swapGraph(JPanel graphPanel) {

		graphJp.removeAll();
		graphJp.add(graphPanel, BorderLayout.CENTER);
		graphJp.repaint();
		revalidate();

	}// end of swapGraph

	/**
	 * Henter ut info om databasen og skriver det ut på skjermen.
	 */
	private void getInfo() {

		infoJp.removeAll();

		for (int i = 0; i < tables.length; i++) {
			infoJp.add(new JLabel(getTableInfo(info[i], tables[i])));
			infoJp.add(Box.createVerticalStrut(5));
		}

		infoJp.add(new JLabel(getAvgBoligPrisInfo()));
		infoJp.revalidate();

	}// end of getInfo

	
	/**
	 * Returnerer en string med tekst som beskriver en tabell i databasen.
	 * 
	 * @param text
	 *            Teksten som skal beskrive tabellen. Eks: "Antall boliger: ".
	 * @param tableName
	 *            Tabellen som blir brukt.
	 * @return Tekst som beskriver en tabell.
	 */
	private String getTableInfo(String text, String tableName) {

		int count = Data_Statistikk.getTableCount(tableName);

		if (count != -1) {
			return text + count;
		} else {
			return "Kunne ikke hente informasjon fra: " + tableName;
		}

	}// End of getTableCount

	/**
	 * Returnerer den gjennomsnittlige boligprisen for alle boliger i databasen.
	 * 
	 * @return Gjennomsnittlig boligpris.
	 */
	private String getAvgBoligPrisInfo() {

		int avg = Data_Statistikk.getAvgUtleiePris();

		if (avg != -1) {
			return "Gjennomsnittlig pris på boliger: " + avg;
		} else {
			return "Kunne ikke hente informasjon om gjennomsnittlig boligpris";
		}

	}// End of getAvgBoligPrisInfo

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == hentStatistikk) {
			if (!isNum(yearField.getText())) {
				JOptionPane.showMessageDialog(null,
						"Året du oppga var ikke korrekt");
				return;
			}

			drawGraph();

		} else if (e.getSource() == hentInfo) {
			System.out.println("hentInfo ble kjørt");
			getInfo();
			swapGraph(infoJp);
		}

	}// end of actionPerformed

}// end of class subPanel_Statistikk
