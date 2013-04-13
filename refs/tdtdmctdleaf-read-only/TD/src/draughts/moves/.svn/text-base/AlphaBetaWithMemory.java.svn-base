package draughts.moves;

import java.util.List;
import java.util.Set;

import draughts.Author;
import draughts.CheckerModel;
import draughts.Data;
import draughts.FeatureCalculations;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.MovesFinder;
import draughts.Pair;
import draughts.Player;

public class AlphaBetaWithMemory {

	private LookupTable lookup = new LookupTable();

	// public int evaluate1(Node node, int alpha, int beta, int depth, boolean
	// myMove, Player me, ITD td,
	// Checker[][] originalBoard) {
	// return evaluate(node, alpha, beta, depth, myMove, me, td, originalBoard);
	// }

	public Pair<Double, CheckerModel[][]> evaluate(Node node, double alpha, double beta, int depth, boolean myMove,
			Player me, ITD td) {

		// System.out.println("evaluate, depth = " + depth);
		Author opponent = Author.owner.equals(me.getMAuthor()) ? Author.opponent : Author.owner;

		Data data = lookup.lookup(node, depth);
		if (data != null) {
			if (data.getLower() != null && data.getLower() >= beta) {
				return new Pair<Double, CheckerModel[][]>(data.getLower(), node.getBoard());
			}
			if (data.getUpper() != null && data.getUpper() <= alpha) {
				return new Pair<Double, CheckerModel[][]>(data.getUpper(), node.getBoard());
			}
			if (data.getLower() != null) {
				alpha = Math.max(alpha, data.getLower());
			}
			if (data.getUpper() != null) {
				beta = Math.min(beta, data.getUpper());
			}
		}

		double g = 0;
		MovesFinder mf = new MovesFinder(node.getBoard(), myMove ? me.getMAuthor() : opponent);
		Set<List<MoveMessage>> legalMoves = mf.getLegalMoves();
		boolean isTerminal = legalMoves.isEmpty();
		CheckerModel[][] principalVariation = node.getBoard();
		if (isTerminal || depth <= 0) {
			FeatureCalculations featuresCalculator = new FeatureCalculations(node.getBoard(), me);
			double value = td.calculateEvaluationFunction(featuresCalculator.getEvaluationFunctionFeatures());
			return new Pair<Double, CheckerModel[][]>(value, principalVariation);
		} else if (myMove) {
			g = Double.NEGATIVE_INFINITY;
			double a = alpha;
			// MovesFinder mf = new MovesFinder(node.getBoard(), myMove ?
			// me.getMAuthor() : opponent);
			// Set<List<MoveMessage>> s = mf.getLegalMoves();
			for (List<MoveMessage> list : legalMoves) {
				if (g >= beta) {
					break;
				}
				// MoveMessage mm = list.get(list.size() - 1);
				// originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(BorderFactory.createLineBorder(Color.red));
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				// int newDepth = BoardUtils.isBeating(list) ? depth : depth -
				// 1;
				int newDepth = depth - 1;
				if (newDepth == 0 && BoardUtils.isBeating(list, node.getBoard())) {
					newDepth = 1;
				}
				Pair<Double, CheckerModel[][]> p = evaluate(newNode, a, beta, newDepth, !myMove, me, td);
				if (p.getFirst() > g) {
					g = p.getFirst();
					principalVariation = p.getSecond();
				}
				// g = Math.max(g, p.getFirst());
				a = Math.max(a, g);
				// originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(null);
			}

		} else {
			g = Double.POSITIVE_INFINITY;
			double b = beta;
			// MovesFinder mf = new MovesFinder(node.getBoard(), myMove ?
			// me.getMAuthor() : opponent);
			// Set<List<MoveMessage>> s = mf.getLegalMoves();
			for (List<MoveMessage> list : legalMoves) {
				if (g <= alpha) {
					break;
				}
				// MoveMessage mm = list.get(list.size() - 1);
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				// originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(BorderFactory.createLineBorder(Color.red));
				// int newDepth = BoardUtils.isBeating(list) ? depth : depth -
				// 1;
				int newDepth = depth - 1;
				if (newDepth == 0 && BoardUtils.isBeating(list, node.getBoard())) {
					newDepth = 1;
				}
				Pair<Double, CheckerModel[][]> p = evaluate(newNode, alpha, b, newDepth, !myMove, me, td);
				if (p.getFirst() < g) {
					g = p.getFirst();
					principalVariation = p.getSecond();
				}
				b = Math.min(b, g);
				// originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(null);
			}
		}

		if (g <= alpha) {
			lookup.storeUpper(node, depth, g);
		} else if (g > alpha && g < beta) {
			lookup.storeLower(node, depth, g);
			lookup.storeUpper(node, depth, g);
		} else {
			lookup.storeLower(node, depth, g);
		}

		return new Pair<Double, CheckerModel[][]>(g, principalVariation);
	}
}
