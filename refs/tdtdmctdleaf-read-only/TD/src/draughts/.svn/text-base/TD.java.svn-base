package draughts;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import draughts.moves.BoardUtils;

/*
 * Interface used by every TD implementation
 */
public abstract class TD implements ITD {

	protected double[] weights = null;
	private double a = 0;
	private double b = 0;
	private Random random = new Random();

	public TD(double[] initialWeights, double a, double b) {
		setWeights(initialWeights);
		this.a = a;
		this.b = b;
	}

	/**
	 * Calculates value of the evaluation function for given evaluation function
	 * features;
	 */
	public double calculateEvaluationFunction(double[] features) {
		if (features == null || features.length != getWeights().length)
			return 0;
		double sum = 0.0;
		for (int i = 0; i < getWeights().length; i++) {
			sum += features[i] * getWeights()[i];
		}
		return a * Math.tanh(b * sum);
		//return sum;
	}

	/**
	 * Provides single game data, that should be used in the learning process.
	 */
	public abstract void updateWeights(GameData gameData);

	/**
	 * Calculates evaluation function values for every time moment
	 */
	protected double[] calculateEvaluationFunctionValue(List<double[]> features) {
		double[] functionValues = new double[features.size()];
		for (int i = 0; i < features.size() - 1; i++) {
			functionValues[i] = calculateEvaluationFunction(features.get(i));
		}
		return functionValues;
	}

	/**
	 * Simulates game for every time moment, and returns probability of winning
	 * for the player in each situation on the board.
	 * 
	 * @param gameData
	 * @param simulationsNumber
	 * @return
	 */
	protected double[] calculateMonteCarloWinningProbabilities(
			GameData gameData, int simulationsNumber) {
		// TODO napisa� kod symulacji MonteCarlo
		double[] probabilities = new double[gameData.evaluationFunctionFeatures
				.size() - 1];
		for (int i = 0; i < probabilities.length; i++) {
			int winns = 0;
			int losses = 0;
			for (int j = 0; j < simulationsNumber; j++) {

				Boolean result = calculateMonteCarloWinningProbablitiy(
						gameData.m_board.get(i), gameData.playerCheckersSort);

				if (!result) {
					losses++;
				} else {
					winns++;
				}
			}
			if (losses != 0)
				probabilities[i] = (double) winns / (double) (losses + winns);
			else
				probabilities[i] = 1;
		}
		return probabilities;
	}

	private Boolean calculateMonteCarloWinningProbablitiy(
			CheckerModel[][] board, Author nextPlayer) {
		Author player = nextPlayer;
		Boolean ended = false;
		int sim = 0;
		while (!ended) {
			// find possible moves
			MovesFinder movesFinder = new MovesFinder(board, nextPlayer);
			Set<List<MoveMessage>> allPossibleMoves = movesFinder
					.getLegalMoves();

			// game ended
			if (allPossibleMoves.size() == 0) {
				return !(nextPlayer == player);
			}

			// change player
			if (nextPlayer == Author.owner)
				nextPlayer = Author.opponent;
			else
				nextPlayer = Author.owner;

			// choose random move (f'ckin Java)

			int moveChoseen = random.nextInt(allPossibleMoves.size());

			int element = 0;
			Iterator<List<MoveMessage>> iterator = allPossibleMoves.iterator();
			List<MoveMessage> choosenMove = null;
			while (iterator.hasNext()) {

				if (element == moveChoseen)
					choosenMove = iterator.next();
				else
					iterator.next();
				element++;
			}
			board = BoardUtils.performMoves(board, choosenMove);

			sim++;
		}
		return true;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double[] getWeights() {
		return weights;
	}
}
