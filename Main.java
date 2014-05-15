package boligformidling;

/**
 * Klassen har ansvar for å starte opp applikasjonen.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */

public class Main {
        private static final int SplashScreenTime = 2000;
        //VM options: -splash:src/boligformidling/splash.png
        /**
	 * Starter opp applikasjonen.
	 */
        private static Vindu_Main vindu;
	public static void main(String[] args) {
                vindu = new Vindu_Main();
                try{
                    Thread.sleep(SplashScreenTime);
                }catch(InterruptedException ie){
                
                }
                vindu.setVisible(true);
	}// end of main

}// end of class Main
