package Boligformidling;
import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Klassen fungerer som et rammeverk og holder alle SubPanelene som vil bli
 * brukt av kundene.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class MainPanel_Kunde extends MainPanel{
	private JTabbedPane tabPane;

	public MainPanel_Kunde(Vindu_Main parent,Bruker bruker){
		super(parent,bruker);
		tabPane = new JTabbedPane();
		setLayout(new BorderLayout());


		JPanel subPanel_Boliger = new SubPanel_Boliger(this);
		
		JPanel subPanel_KundeProfil = null;
		try {
			subPanel_KundeProfil = new SubPanel_KundeProfil(this, bruker.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tabPane.addTab("Vis boliger", subPanel_Boliger);
		
		tabPane.addTab("Kunde profil", subPanel_KundeProfil);

		if(bruker.is(Bruker.UTLEIER)){
			 JPanel subPanel_RegistrerBoliger = new SubPanel_RegistrerBoliger(this);
			 tabPane.addTab("Registrer boliger",subPanel_RegistrerBoliger);

			 JPanel subPanel_Boligeier = new SubPanel_Boligeier(this);
			 tabPane.addTab("Vis egne boliger",subPanel_Boligeier);
		}


	    JPanel logOutPanel = new SubPanel_LogOut(bruker,this);

	    add(logOutPanel,BorderLayout.NORTH);
		add(tabPane,BorderLayout.CENTER);

	}//end of constructor
}//end of MainPanel_Kunde