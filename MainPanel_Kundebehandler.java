package boligformidling;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Klassen fungerer som et rammeverk og holder alle SubPanelene som vil bli
 * brukt av kundebehandleren.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class MainPanel_Kundebehandler extends MainPanel {

	private JTabbedPane tabPane;

	public MainPanel_Kundebehandler(Vindu_Main parent, Bruker bruker) {
		super(parent, bruker);
		tabPane = new JTabbedPane();

		setLayout(new BorderLayout());

		JPanel subPanel_AdminTools = new SubPanel_AdminTools(this);
		JPanel subPanel_KontraktForespørsler = new SubPanel_KontraktForespørsler(
				bruker, this);
		JPanel subPanel_VisKontrakter = new SubPanel_VisKontrakter(this);
		JPanel subPanel_RegistrerKontrakt = new SubPanel_RegistrerKontrakt(this);
		JPanel subPanel_Boliger = new SubPanel_Boliger(this);
		JPanel subPanel_Statistikk = new SubPanel_Statistikk(this);
		JPanel subPanel_Kunde = new SubPanel_Kunder(this);
		JPanel subPanel_RegistrerBehandler = new SubPanel_RegistrerBehandler(
				this);

		tabPane.addTab("Vis leiekontrakter", subPanel_VisKontrakter);
		tabPane.addTab("Registrer kontrakter", subPanel_RegistrerKontrakt);
		tabPane.addTab("Kontrakt forespørsler", subPanel_KontraktForespørsler);
		tabPane.addTab("Kunder", subPanel_Kunde);
		tabPane.addTab("Vis boliger", subPanel_Boliger);
		tabPane.addTab("Registrer behandler", subPanel_RegistrerBehandler);
		tabPane.addTab("Statistikk", subPanel_Statistikk);
		tabPane.addTab("Admin verktøy", subPanel_AdminTools);

		/*
		 * tabPane.addChangeListener(new ChangeListener() { public void
		 * stateChanged(ChangeEvent e) { System.out.println("Tab: " +
		 * tabPane.getSelectedIndex());
		 * 
		 * if(tabPane.getSelectedIndex() == 1){ vm.setSize(500,450);
		 * vm.setLocationRelativeTo(null); //vm.repaint(); }else
		 * if(tabPane.getSelectedIndex() == 2){ vm.setSize(1200,600);
		 * vm.setLocationRelativeTo(null); } } });
		 */

		JPanel logOutPanel = new SubPanel_LogOut(bruker, this);
		add(logOutPanel, BorderLayout.NORTH);
		add(tabPane, BorderLayout.CENTER);

	}// end of constructor

}// end of class MainPanel_Kundebehandler
