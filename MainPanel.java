package boligformidling;

import java.awt.GridLayout;
import javax.swing.JPanel;

public abstract class MainPanel extends JPanel{
    private Vindu_Main parent;
    private Bruker bruker;
    /**
     * MainPanel opptar hele Vindu_Main og har som hovedfunksjon å hoste SubPanel'er, men kan også brukes til annet.
     * @param parent
     * @param bruker 
     */
    public MainPanel(Vindu_Main parent,Bruker bruker){
    	super(new GridLayout(0,1));
        this.parent = parent;
        this.bruker = bruker;
    }
    
   public Bruker getBruker(){
        return bruker;
    }
    
   public Vindu_Main getRoot(){
        return parent;
    }
}
