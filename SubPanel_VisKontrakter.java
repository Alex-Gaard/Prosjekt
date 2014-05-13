package boligformidling;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Klassen inneholder GUI og metoder for å kunne vise fram kontrakter.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_VisKontrakter extends SubPanel implements ActionListener {

	private JButton personnummerButton, idButton;
	private JLabel lblPersonnummer, lblID;
	private JTextField personnummerField, idField;
	private JTable table;
	private DefaultTableModel model;
	private ArrayList<Leiekontrakt> kontrakter;
	private final String[] kolonner = { "Kundebehandler personnummer",
			"Bolig ID" };
	private JPanel visKontraktInfoJp;

	public SubPanel_VisKontrakter(MainPanel parent) {
		super(parent);

		setLayout(new FlowLayout(FlowLayout.CENTER));

		personnummerButton = new JButton("Vis kontrakter");
		personnummerButton.addActionListener(this);
		idButton = new JButton("Vis kontrakter");
		idButton.addActionListener(this);

		lblPersonnummer = new JLabel("Personnummer: ");
		lblID = new JLabel("Bolig ID:");

		personnummerField = new JTextField(10);
		idField = new JTextField(10);

		visKontraktInfoJp = new JPanel();
		visKontraktInfoJp.setLayout(new BoxLayout(visKontraktInfoJp,
				BoxLayout.PAGE_AXIS));
		TitledBorder title = BorderFactory.createTitledBorder("Kontrakt info");
		visKontraktInfoJp.setBorder(title);
		visKontraktInfoJp.setPreferredSize(new Dimension(300, 200));

		model = new DefaultTableModel(null, kolonner) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		table = new JTable(model);
		table.setEnabled(true);
		table.setFillsViewportHeight(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int rad = table.getSelectedRow();

					if (kontrakter.size() > 0) {
						ArrayList<String> infoList = kontrakter.get(rad)
								.getKontraktInfo();
						displayInfo(infoList);
					}

				}
			}// end of mouseClicked
		});

		JPanel outerJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		outerJp.setPreferredSize(new Dimension(350, 300));

		JPanel nrAndIdJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nrAndIdJp.setPreferredSize(new Dimension(350, 90));

		JPanel pNrJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pNrJp.setPreferredSize(new Dimension(350, 40));
		pNrJp.add(lblPersonnummer);
		pNrJp.add(personnummerField);
		pNrJp.add(personnummerButton);

		JPanel idJp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		idJp.setPreferredSize(new Dimension(350, 40));
		idJp.add(lblID);
		idJp.add(idField);
		idJp.add(idButton);

		nrAndIdJp.add(pNrJp);
		nrAndIdJp.add(idJp);

		outerJp.add(nrAndIdJp);
		outerJp.add(visKontraktInfoJp);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(450, 300));
		add(scroll);
		add(outerJp);

	}// end of constructor

	/**
	 * Skriver ut infoen om en kontrakt til visKontraktInfoJp.
	 * 
	 * @param infoList
	 *            Liste over kontrakt info.
	 */
	private void displayInfo(ArrayList<String> infoList) {
		visKontraktInfoJp.removeAll();

		for (String info : infoList) {
			visKontraktInfoJp.add(new JLabel(info));
			visKontraktInfoJp.add(Box.createVerticalStrut(5));
		}

		visKontraktInfoJp.revalidate();

	}// end of displayInfo

	/**
	 * Viser fram kontrakter hvor personnummeret som er oppgitt inngår i GUIen.
	 */
	public void displayKontraktForPersonnummer() {

		String personnummer = this.personnummerField.getText();
		kontrakter = new ArrayList<Leiekontrakt>();

		try {

			kontrakter = Data_Kontrakter
					.getKontrakterForPersonnummer(personnummer);
			if (kontrakter == null) {
				displayMessage("En feil oppstod\n");
				return;
			}

			if (kontrakter.size() > 0) {

				while (model.getRowCount() != 0) {
					model.removeRow(0);
				}

				for (int i = 0; i < kontrakter.size(); i++) {
					String[] rad = {
							kontrakter.get(i).getBehandlerPersonnummer(),
							kontrakter.get(i).getBoligID() };
					model.addRow(rad);
				}

			} else {

				displayMessage("Fant ingen kontrakter hvor personnummer: "
						+ personnummer + " inngår\n");
			}

		} catch (Exception ex) {
			System.out.println("Feil i displayKontraktForPersonnummer: " + ex);
			ex.printStackTrace();
		}

	}// end of displayKontraktForPersonnummer

	/**
	 * Viser fram kontrakter for bolig IDen som er fyllt inn i GUIen.
	 */
	public void displayKontraktForID() {

		String id = this.idField.getText();
		kontrakter = new ArrayList<Leiekontrakt>();

		try {

			kontrakter = Data_Kontrakter.getKontrakterForBoligID(id);
			if (kontrakter == null) {
				displayMessage("En feil oppstod\n");
				return;
			}

			if (kontrakter.size() > 0) {

				while (model.getRowCount() != 0) {
					model.removeRow(0);
				}

				for (int i = 0; i < kontrakter.size(); i++) {
					String[] rad = {
							kontrakter.get(i).getBehandlerPersonnummer(),
							kontrakter.get(i).getBoligID() };
					model.addRow(rad);
				}

			} else {
				displayMessage("Fant ingen kontrakter hvor ID: " + id
						+ " inngår\n");
			}

		} catch (Exception ex) {
			System.out.println("Feil i displayKontraktForID: " + ex);
			ex.printStackTrace();
		}

	}// end of displayKontraktForID

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == personnummerButton) {
			displayKontraktForPersonnummer();

		} else if (e.getSource() == idButton) {
			displayKontraktForID();
		}

	}// end of actionPerformed

}// end of class SubPanel_VisKontrakter
