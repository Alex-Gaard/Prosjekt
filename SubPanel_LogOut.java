package Boligformidling;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Klassen viser hvem brukeren er logget inn som og inneholder en "logg ut"
 * knapp for å logge ut brukeren.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class SubPanel_LogOut extends SubPanel implements ActionListener {

	private Bruker bruker;
	private JLabel id;
	private JButton logOut;

	public SubPanel_LogOut(Bruker b, MainPanel parent) {
		super(parent);

		bruker = b;
		id = new JLabel("Logget inn som: " + b.getNavn());
		logOut = new JButton("Logg ut");
		logOut.addActionListener(this);

		setLayout(new BorderLayout());
		add(id, BorderLayout.WEST);
		JPanel buttonJp = new JPanel();
		buttonJp.add(logOut);

		add(buttonJp, BorderLayout.EAST);

	}// end of constructor

	/**
	 * Logger brukeren ut av programmet.
	 */
	public void logOut() {
		bruker = null;
		getMainPanel().getRoot().swap(
				new MainPanel_LogIn(getMainPanel().getRoot(), bruker));
	}// end of logOut

	@Override
	/**
	 * Sjekker når brukeren har trykket på en av knappene i GUIen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == logOut) {
			logOut();

		}

	}// end of actionPerformed

}// end of class SubPanel_LogOut
