package draughts;



import javax.swing.SwingUtilities;
import javax.swing.UIManager;




/**
 * Główna klasa aplikacji, która uruchamia okno gry.
 */
/**
 * @author Kamil
 * 
 */
public class Main {

	/**
	 * Otwiera główne okno gry.
	 * 
	 * @param args
	 *            Argumenty wywołania.
	 */

	public static void main(String[] args) {
		/*
		 * double[] weights = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,
		 * 0.5, 0.5, 0.5 }; ArrayList<double[]> features = new
		 * ArrayList<double[]>(); TDMC tdmc = new TDMC(weights, 0.5, 0.1, 0.1,
		 * 0.5, 0.5); GameData gd = new GameData();
		 * 
		 * double[] features1 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features2 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features3 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features4 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features5 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * 
		 * gd.evaluationFunctionFeatures.add(features1);
		 * gd.evaluationFunctionFeatures.add(features2);
		 * gd.evaluationFunctionFeatures.add(features3);
		 * gd.evaluationFunctionFeatures.add(features4);
		 * gd.evaluationFunctionFeatures.add(features5);
		 * 
		 * // gd.m_board = null; gd.playerCheckersSort = Author.opponent;
		 * gd.startingCheckersSort = Author.opponent;
		 * 
		 * tdmc.updateWeights(gd);
		 */
		final String[] argsFinal = args;
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				Frame f = new Frame();
				if (argsFinal.length > 0) {
					double[] weights = { 1.3431066562013678,
							-0.6200605215485304, -1.9229043932417471,
							-3.362370613999268, 3.8764701933311523,
							2.0950764879054082, -3.0220559224093093,
							-1.4920617272925127, -1.7673224117739434,
							0.9082276129469169, -3.4043693801647783,
							2.1537442439203023 };

					ITD tdmc = new TDMC(weights, 0.9, 0.9, 0.001, 99, 0.027);
					f.setArtificialGame(true);
					f.setItd(tdmc);
				}
				f.setVisible(true);
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(f);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		});
	}
}
