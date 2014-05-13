package Boligformidling;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public abstract class MainPanel extends JPanel{
    private Vindu_Main parent;
    private Bruker bruker;
    
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
