package boligformidling;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Klassen fungerer som et rammeverk og holder alle SubPanelene som er med i
 * "logg inn" funksjonaliteten.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class MainPanel_LogIn extends MainPanel {

	private JTabbedPane tabPane;

	public MainPanel_LogIn(Vindu_Main vm, Bruker bruker) {
		super(vm, null);

		tabPane = new JTabbedPane();
		JPanel subPanelLogin = new SubPanel_LogIn(this);
		JPanel subPanelRegistrerKunde = new SubPanel_RegistrerKunde(this);

		tabPane.addTab("Login", subPanelLogin);
		tabPane.addTab("Registrer kunde", subPanelRegistrerKunde);

		add(tabPane);

	}// end of constructor

}// end of class MainPanel_Login
