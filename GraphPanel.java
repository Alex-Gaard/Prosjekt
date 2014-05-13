package boligformidling;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klassen tegner en graf utifra parameterene som blir oppgitt i konstruktøren.
 * 
 * @author Alexander Gård, s198585, 1. år IT
 * @version 1.00, 12 Mai 2014
 */
public class GraphPanel {

	private JPanel graphPanel;
	private final int GRAPH_START = 4;
	private final int GRAPH_MAGNITUDE = 5;

	// Lables
	private final int LABEL_X_START = 20;
	private final int LABEL_Y_START = 10;
	private final int LABEL_Y_OFFSET = 65;

	// Bars
	private final int BAR_X_START = 60;
	private final int BAR_X_OFFSET = 40;
	private final int BAR_Y_START = 330;
	private final int BAR_WIDTH = 30;

	/**
	 * Tar imot parameterene som skal til for å tegne grafen.
	 * 
	 * @param year
	 *            Hvilket år skal grafen omhandle.
	 * @param table
	 *            Hvilken tabell skal brukes.
	 * @param column
	 *            Hvilken kolonne skal grafen tegnes med hensyn på.
	 */
	public GraphPanel(final String year, final String table, final String column) {

		graphPanel = new JPanel() {
			@Override
			/**
			 * Tegner ut grafen til graphPanel.
			 */
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				File f = new File("C:\\Users\\Alex\\Desktop\\gut.png");
				Image img = null;

				try {
					img = ImageIO.read(f);
				} catch (IOException e) {
					System.out.println("feil i paintComponent: " + e);
				}

				img = img.getScaledInstance(500, 400, Image.SCALE_SMOOTH);
				g.drawImage(img, 50, 0, null);

				int[] counts = getCountForYear(year, table, column);
				int highestCount = getHighestCount(counts);
				int roundedHighest = getRoundedToNearest(highestCount,
						getGraphMagnitude(highestCount));

				String[] sArr = getGraphLables(highestCount);

				for (int i = 0; i < sArr.length; i++) {
					g.drawString(sArr[i], LABEL_X_START, LABEL_Y_START
							+ LABEL_Y_OFFSET * i);
				}

				for (int i = 0; i < 12; i++) {
					g.fillRect(BAR_X_START + (i * BAR_X_OFFSET), BAR_Y_START,
							BAR_WIDTH, getBarHeight(counts[i], roundedHighest));
				}

			}// end of paintComponent

		};

		graphPanel.setPreferredSize(new Dimension(550, 400));

	}// End of constructor

	/**
	 * Returnerer graf panelet.
	 * 
	 * @return Graf panel.
	 */
	public JPanel getPanel() {

		return graphPanel;

	}// end of getPanel

	/**
	 * Returnerer Y kordinaten som vil bli brukt for å tegne baren på grafen.
	 * 
	 * @param yValue
	 *            Y verdi.
	 * @param roundedHighest
	 *            Høyeste verdi som grafen kan ha.
	 * @return Y kordinat for bar.
	 */
	public int getBarHeight(int yValue, int roundedHighest) {

		double yCord = (yValue * 100) / roundedHighest;
		yCord = Math.ceil((yCord * -3.25));
		return (int) yCord;

	}// End of getBarHeight

	/**
	 * Returnerer det høyeste elementet i et int array.
	 * 
	 * @param counts
	 *            Int array.
	 * @return Høyeste element.
	 */
	public int getHighestCount(int[] counts) {
		int highest = -1;
		for (int i : counts) {
			if (i > highest) {
				highest = i;
			}
		}
		return highest;

	}// end of getHighestCount

	/**
	 * Reverserer et String array og returnerer det tilbake.
	 * 
	 * @param arr
	 *            Arrayet som skal bli reversert.
	 * @return Reversert array.
	 */
	public String[] reverse(String[] arr) {
		List<String> list = Arrays.asList(arr);
		Collections.reverse(list);
		return (String[]) list.toArray();

	}// end of reverse

	/**
	 * Returnerer en array med hvor mange forekomster av et objekt som finnes i
	 * databasen gjennom et helt år. Hver index i arrayen som blir returnert
	 * representerer en måned.
	 * 
	 * @param year
	 *            Hvilket år.
	 * @param table
	 *            Hvilken tabell.
	 * @param column
	 *            Hvilken kolonne. Kolonnen i databasen som blir referert til må
	 *            være av type Date.
	 * @return Array med antall forekomster.
	 */
	public int[] getCountForYear(String year, String table, String column) {

		int[] counts = new int[12];

		for (int i = 0; i < 12; i++) {
			String sql = "select count(*) from " + table + " where month("
					+ column + ") = " + (i + 1) + " and year(" + column
					+ ") = " + year;
			counts[i] = Data_Statistikk.getCountForMonth(sql);
		}
		return counts;

	}// end of getCountForYear

	/**
	 * Returnerer skaleringen som grafen er basert på.
	 * 
	 * @param highest
	 *            Høyeste verdi som forekommer i grafen.
	 * @return Skaleringn til grafen.
	 */
	public int getGraphMagnitude(int highest) {
		int endMagnitude = GRAPH_START;

		do {
			endMagnitude *= GRAPH_MAGNITUDE;

			if (highest < endMagnitude) {

				return endMagnitude;
			}

		} while (highest > endMagnitude);

		return -1;
	}// end of getLabelScaling

	/**
	 * Returnerer et array med strings som representerer teksten som vil bli
	 * skrevet til venstre for grafen.
	 * 
	 * @param highest
	 *            Høyeste verdi i grafen.
	 * @return Array med tekst.
	 */
	public String[] getGraphLables(int highest) {

		int graphLabels = 5;

		int rounded = getRoundedToNearest(highest, getGraphMagnitude(highest));

		String[] strArr = new String[graphLabels];
		int inst = rounded / graphLabels;

		for (int i = 1; i <= strArr.length; i++) {
			strArr[i - 1] = String.valueOf(inst * i);
		}

		return reverse(strArr);

	}// end of getGraphLabels

	/**
	 * Runder et tall opp eller ned til nærmeste spesifiserte tall.
	 * 
	 * @param num
	 *            Tallet som skal bli avrundet.
	 * @param nearest
	 *            Avrund til.
	 * @return Avrundet tall.
	 */
	public int getRoundedToNearest(int num, int nearest) {

		int rounded = num + (nearest - (num % nearest));
		return rounded;

	}// end of getRoundedToNearest

}// end of class GraphPanel
