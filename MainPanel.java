package boligformidling;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * MainPanel'er har i oppgave å 'hoste' SubPanel'er.
 * @author Arlen Syver Wasserman, s193956, IT 1 år
 * @version 1.0 13 Mai 2014
 */
public abstract class MainPanel extends JPanel{
    private Vindu_Main parent;
    private Bruker bruker;
    JTabbedPane tabPane;
    /**
     * Konstruktør som tar imot et Vindu_Main og en bruker
     * @param Parent Denne sendes med for å kunne utføre operasjoner på den.
     * @param Bruker Holder brukeren som benytter seg av funksjonene
     */
    public MainPanel(Vindu_Main Parent, Bruker Bruker) {
        parent = Parent;
        bruker = Bruker;
    }
    Vindu_Main getRoot() {
        return parent;
    }
    Bruker getBruker() {
        return bruker;
    }
}//End class MainPanel