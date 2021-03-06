package boligformidling;

/**
 * Klassen har ansvar for å starte opp applikasjonen.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 15 Mai 2014
 */

public class Main {
	private static final int SplashScreenTime = 1000;

	/**
	 * Starter opp applikasjonen.
	 */
	private static Vindu_Main vindu;

	public static void main(String[] args) {
		vindu = new Vindu_Main();
                //Viser splsh screen en git tid
		try {
			Thread.sleep(SplashScreenTime);
		} catch (InterruptedException ie) {
			System.out.println("feil i main: " + ie);
		}
		vindu.setVisible(true);
	}// end of main

}// end of class Main