package boligformidling;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Klassen fungerer som rammeverk for GUIen gjennom hele applikasjonen, og paneler
 * vil bli fjernet/lagt til med dette rammeverket som utgangspunkt.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 15 Mai 2014
 */
public class Vindu_Main extends JFrame{

    public Vindu_Main(){
    	super("Boligformidling");
    	setLayout(new BorderLayout());
        JPanel mainPanel = null;
        addWindowListener(new Vinduslytter());
            
        if(Database.setupDatabaseConnection(this)){
            mainPanel =  new MainPanel_LogIn(this,null);
            add(mainPanel, BorderLayout.CENTER);
            revalidate();
        }else{
            mainPanel = new MainPanel_NotConnected(this,null);
            add(mainPanel, BorderLayout.CENTER);
            revalidate();
            checkForConnection();
            }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);
    }//end of constructor
    
    /**
     * Bytter ut panelet med et nytt MainPanel.
     * 
     * @param mp Nytt MainPanel.
     */
    public void swap(MainPanel mp){
            getContentPane().removeAll();
            getContentPane().add(mp,BorderLayout.CENTER);
            revalidate();
    }//end of swap
    
    /**
     * Sjekker om det er mulig å opprette kobling mot databasen.
     * Når metoden oppretter en vellykket tilkobling, vil hoved panelet bli byttet til MainPanel_LogIn
     */
    public void checkForConnection(){
    	
    	final Vindu_Main vm = this;
    	
    	Thread t = new Thread(new Runnable(){
    		public void run(){
    			
    	    	while(!Database.setupDatabaseConnection(vm)){
    	    		
    	    		try{
    	    			Thread.sleep(1000);
    	    		}catch(InterruptedException ie){
    	    			System.out.println("feil i checkForConnection: " + ie);
    	    		}
    	    	}
    	    	vm.swap(new MainPanel_LogIn(vm,null));
    		}
    		
    	});
    	
    	t.setDaemon(true);
    	t.start();
   
    }//end of checkForConnection
    /**
     * Klassen sjekker når applikasjonen blir lukket.
     * 
     * @author Alexander Gård, s198585, 1. år IT
     * @version 1.00, 12 Mai 2014
     */
    public class Vinduslytter extends WindowAdapter{

    	@Override
    	/**
    	 * Sørger for å lukke database tilkoblingen før applikasjonen blir lukket.
    	 */
		public void windowClosing(WindowEvent e){
    		Database.closeConnection();
			System.exit(0);

		}//end of windowClosing
  		
	}//end of class Vinduslytter
        
}//end of class Vindu_Main