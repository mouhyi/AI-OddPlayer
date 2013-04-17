package draughts;

import java.util.ArrayList;
import java.util.List;

/*
 * Information about game played.
 */
public class GameData {

	public GameData() {
		evaluationFunctionFeatures = new ArrayList<double[]>();
		m_board = new ArrayList<CheckerModel[][]>();
	}

	/**
	 * Holds evaluation function features values for every time moment t = 1,2.....T.
	 */
	public List<double[]> evaluationFunctionFeatures = new ArrayList<double[]>();

	/**
	 * Holds board situations after each move.
	 */
	public List<CheckerModel[][]> m_board = new ArrayList<CheckerModel[][]>();

	/**
	 * Holds principal variation nodes for each move
	 */
	public List<CheckerModel[][]> principalVariation = new ArrayList<CheckerModel[][]>();

	/**
	 * Holds evaluations of board situations after each move
	 */
	public List<Double> statesEvaluations = new ArrayList<Double>();

	/**
	 * Holds information what kind of checkers was the algorithm using.
	 */
	public Author playerCheckersSort;

	/**
	 * Specifies the kind of Checkers which started the game
	 */
	public Author startingCheckersSort;
}
