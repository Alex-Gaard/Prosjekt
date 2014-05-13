package Boligformidling;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

/**
 * Klassen inneholder et label som viser at brukeren ikke er koblet til
 * databasen. Vinduet vil bli vist hvis koblingen til databasen skulle få
 * problemer.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class MainPanel_NotConnected extends MainPanel {

	private JLabel lblNotConnected;

	public MainPanel_NotConnected(Vindu_Main parent, Bruker bruker) {
		super(parent, bruker);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.CENTER;

		lblNotConnected = new JLabel(
				"En feil oppstod under tilkoblingen opp mot databasen...\n");
		add(lblNotConnected, c);
	}// end of constructor

}// end of class MainPanel_NotConnected
