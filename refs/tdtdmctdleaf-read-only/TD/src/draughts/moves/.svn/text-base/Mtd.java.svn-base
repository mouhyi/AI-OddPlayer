package draughts.moves;

import java.util.List;
import java.util.Set;

import draughts.Author;
import draughts.Checker;
import draughts.CheckerModel;
import draughts.FeatureCalculations;
import draughts.GameData;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.MovesFinder;
import draughts.Pair;
import draughts.Player;

public class Mtd {

	private static final int F = 0;
	private static final int MAX_DEPTH = 5;

	// private Checker[][] originalBoard;
	private GameData gameData = new GameData();

	private AlphaBetaWithMemory alphaBetaWithMemory = new AlphaBetaWithMemory();

	public Mtd(Author player) {
		gameData.playerCheckersSort = player;
		gameData.startingCheckersSort = Author.owner;
	}

	public GameData getGameData() {
		return gameData;
	}

	public List<MoveMessage> getBestMoveDefault(Checker[][] board, Player me,
			ITD td) {
		return getBestMove(board, F, MAX_DEPTH, me, td);
	}

	public List<MoveMessage> getBestMove(Checker[][] bboard, int f,
			int maxDepth, Player me, ITD td) {
		// this.originalBoard = bboard;
		CheckerModel[][] board = BoardUtils.fromChecker(bboard);

		return getBestMove(board, f, maxDepth, me, td);
	}

	public List<MoveMessage> getBestMove(CheckerModel[][] board, int f,
			int maxDepth, Player me, ITD td) {

		MovesFinder mf = new MovesFinder(board, me.getMAuthor());
		Set<List<MoveMessage>> legalMoves = mf.getLegalMoves();
		List<MoveMessage> bestMove = null;
		CheckerModel[][] bestVariation = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		for (List<MoveMessage> move : legalMoves) {
			CheckerModel[][] copy = BoardUtils.performMoves(board, move);
			Pair<Double, CheckerModel[][]> p = evaluate(copy, f, maxDepth, me,
					td);
			double currentValue = p.getFirst();
			if (currentValue > bestValue) {
				bestMove = move;
				bestValue = currentValue;
				bestVariation = p.getSecond();
			}
		}
		if (bestMove != null) {
			CheckerModel[][] newBoard = BoardUtils
					.performMoves(board, bestMove);
			FeatureCalculations calculations = new FeatureCalculations(
					newBoard, me);
			gameData.evaluationFunctionFeatures.add(calculations
					.getEvaluationFunctionFeatures());
			gameData.m_board.add(newBoard);
			gameData.statesEvaluations.add(bestValue);
			gameData.principalVariation.add(bestVariation);
		}
		return bestMove;
	}

	private Pair<Double, CheckerModel[][]> evaluate(CheckerModel[][] board,
			double f, int maxDepth, Player me, ITD td) {
		double g = f;
		CheckerModel[][] principalVariation = null;
		double upperBound = Double.POSITIVE_INFINITY;
		double lowerBound = Double.NEGATIVE_INFINITY;
		while (lowerBound < upperBound) {
			double beta;
			if (g == lowerBound) {
				beta = g + 1;
			} else {
				beta = g;
			}
			Node node = new Node(board);
			Pair<Double, CheckerModel[][]> p = alphaBetaWithMemory.evaluate(
					node, beta - 1, beta, maxDepth, false, me, td);
			g = p.getFirst();
			principalVariation = p.getSecond();
			if (g < beta) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		return new Pair<Double, CheckerModel[][]>(g, principalVariation);
	}
}
