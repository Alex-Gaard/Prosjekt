package boligformidling;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

/**
 * Klassen fungerer som et rammeverk og holder alle SubPanelene som vil bli
 * brukt av kundebehandleren.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 15 Mai 2014
 */
public class MainPanel_Kundebehandler extends MainPanel {

	private JTabbedPane tabPane;

	public MainPanel_Kundebehandler(Vindu_Main parent, Bruker bruker) {
		super(parent, bruker);
		tabPane = new JTabbedPane();

		setLayout(new BorderLayout());

		tabPane.addTab("Vis leiekontrakter", new SubPanel_VisKontrakter(this));
		tabPane.addTab("Registrer kontrakter", new SubPanel_RegistrerKontrakt(this));
		tabPane.addTab("Kontrakt forespørsler", new SubPanel_KontraktForespørsler(
				bruker, this));
		tabPane.addTab("Kunder", new SubPanel_Kunder(this));
		tabPane.addTab("Vis boliger", new SubPanel_Boliger(this));
		tabPane.addTab("Registrer behandler", new SubPanel_RegistrerBehandler(
				this));
		tabPane.addTab("Statistikk", new SubPanel_Statistikk(this));
		tabPane.addTab("Admin verktøy", new SubPanel_AdminTools(this));

		add(new SubPanel_LogOut(bruker, this), BorderLayout.NORTH);
		add(tabPane, BorderLayout.CENTER);

	}// end of constructor

}// end of class MainPanel_Kundebehandler
