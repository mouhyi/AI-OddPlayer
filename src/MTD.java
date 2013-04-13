import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import odd.OddBoard;
import odd.OddMove;
import odd.OddBoard.Piece;

public class MTD {

	static int MAX_SEARCH_DEPTH = 10;
	static HashMap<Long, State> TranspositionTable;
	// static OddMove bestMove;
	// TODO AIPLAYEr -> dynamic id
	static int AIPLAYER = 1;
	static final int SIZE_DATA = 2 * 4 + 1;

	static Zobrist zob = new Zobrist();
	
	static OddMove globalbestMove;

	public static int iterativeDeepening(OddBoard root) {
		
		MoveScore res;
		long hash = hash(root);

		// MTD(f) needs a “first guess” as to where the minimax value will turn
		// out to be
		int firstguess = 0;
		for (int d = 1; d < MAX_SEARCH_DEPTH; d++) {
			res = MTDF(root, firstguess, d, hash);
			firstguess = res.score;
			globalbestMove = res.move;
		}
		// if times_up() then break;

		return firstguess;
	}

	public static MoveScore MTDF(OddBoard root, int f, int d, long hash) {
		int g = f;
		int beta;
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		MoveScore res=null;

		while (lowerBound < upperBound) {
			if (g == lowerBound) {
				beta = g + 1;
			} else {
				beta = g;
			}
			res = AlphaBetaWithMemory(root, beta - 1, beta, d, hash);
			g = res.score;
			if (g < beta) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		return res;
	}

	private static MoveScore AlphaBetaWithMemory(OddBoard board, int alpha,
			int beta, int d, long curHash) {

		MoveScore res;
		OddMove bestMove = null;
		int g, a, b;
		State n = null;
		// TODO depth? -- OK
		OddBoard c;

		// TODO ZOBric Keys -- OK
		long h = curHash;

		// TODO transTable.lookup(node, depth) -- OK
		/* Transposition table lookup */
		if (TranspositionTable.containsKey(h)) {
			n = TranspositionTable.get(h);
			if (n.depth >= d || n.terminal) {

				if (n.lowerbound >= beta) {
					return (new MoveScore(n.bestMove, n.lowerbound));
				}
				if (n.upperbound <= alpha) {
					return (new MoveScore(n.bestMove, n.upperbound));
				}
			}
		} else {
			n = new State(board, h);
			TranspositionTable.put(h, n);
		}

		alpha = Math.max(alpha, n.lowerbound);
		beta = Math.min(beta, n.upperbound);

		// //////

		LinkedList<OddMove> moves = board.getValidMoves();

		// TODO isterminal
		if (d == 0 || isTerminal(board)) {
			g = evaluate(n); /* leaf node */
		}

		else if (n.MAXNODE) {

			g = Integer.MIN_VALUE;
			a = alpha; /* save original alpha value */

			for (OddMove m : moves) {
				if (g >= beta) {
					break;
				}
				c = (OddBoard) board.clone();
				c.move(m);
				
				res = AlphaBetaWithMemory(c, a, beta, d - 1, incrementalHash(h, m));
				if(res.score > g){
					bestMove = res.move;
				}

				g = Math.max(g,res.score);
						
				a = Math.max(a, g);
			}
		}

		else { /* n is a MINNODE */

			g = Integer.MAX_VALUE;
			b = beta; /* save original beta value */

			for (OddMove m : moves) {
				if (g <= alpha) {
					break;
				}
				c = (OddBoard) board.clone();
				c.move(m);
				
				res = AlphaBetaWithMemory(c, alpha, b, d - 1, incrementalHash(h, m));
				
				if(res.score < g){
					bestMove = res.move;
				}

				g = Math.min(res.score, g);
						
				b = Math.min(b, g);

			}
		}

		/* Traditional transposition table storing of bounds */
		n.depth = d;
		/* Fail low result implies an upper bound */
		if (g <= alpha) {
			n.upperbound = g;
			// store n.upperbound;
		}
		/*
		 * Found an accurate minimax value – will not occur if called with zero
		 * window
		 */
		if (g > alpha && g < beta) {
			n.lowerbound = g;
			n.upperbound = g;
			// store n.lowerbound, n.upperbound;
		}
		/* Fail high result implies a lower bound */
		if (g >= beta) {
			n.lowerbound = g;
			// store n.lowerbound;
		}
		return new MoveScore(bestMove, g);
	}

	private static int evaluate(State n) {
		// TODO Heuristic
		return 0;
	}

	// TODO remove
	static OddMove getBestMove(OddBoard b) {
		OddBoard c;
		OddMove bestM = null;
		int s;
		int bestScore = Integer.MIN_VALUE;
		for (OddMove m : b.getValidMoves()) {
			c = (OddBoard) b.clone();
			c.move(m);
			s = iterativeDeepening(c);
			if (s > bestScore) {
				bestM = m;
				bestScore = s;
			}
		}
		return bestM;
	}

	static boolean isTerminal(OddBoard node) {
		return (node.countEmptyPositions() < 1);
	}

	static boolean isMaximizing(OddBoard b) {
		return (b.getTurn() == AIPLAYER);
	}

	/*
	 * static State looku1p(OddBoard b, int d) { long hashValue = hash(b); State
	 * n = TranspositionTable.get(hashValue); if (n == null) { return null; } if
	 * (n.depth >= d) { return n; } if (n.terminal) { return n; } return null; }
	 */

	static long hash(OddBoard board) {
		long result = 0l;
		for (int row = 0; row < SIZE_DATA; row++) {
			for (int col = 0; col < SIZE_DATA; col++) {
				if (board.getBoardData()[row][col] == Piece.WP) {
					result ^= zob.getKey(row, col, 0);
				} else if (board.getBoardData()[row][col] == Piece.BP) {
					result ^= zob.getKey(row, col, 1);
				}
			}
		}
		return result;
	}

	static long incrementalHash(long hash, OddMove move) {
		int color = move.getColor() == Piece.WP ? 0 : 1;
		hash ^= zob.getKey(move.getDestRow(), move.getDestCol(), color);
		hash ^= zob.zobMyTurn;
		return hash;

	}
}

class State {
	// OddBoard board;
	long hashValue;
	int lowerbound;
	int upperbound;
	int depth;
	boolean MAXNODE;
	boolean terminal;
	OddMove bestMove;

	public State(OddBoard board, long hash) {
		this.hashValue = hash;
		this.lowerbound = Integer.MIN_VALUE;
		this.upperbound = Integer.MAX_VALUE;
		this.depth = -1;
		terminal = MTD.isTerminal(board);
		MAXNODE = MTD.isMaximizing(board);
	}

}

class MoveScore {
	OddMove move;
	int score;

	public MoveScore(OddMove m, int score) {
		super();
		this.move = m;
		this.score = score;
	}
}

class Zobrist {
	long[][][] zobristKey;
	long zobMyTurn;
	int wp, bp;

	static final int size = MTD.SIZE_DATA * MTD.SIZE_DATA * 2 + 1;

	Zobrist() {
		zobristKey = new long[MTD.SIZE_DATA][MTD.SIZE_DATA][2];
		Random r = new Random();
		for (int i = 0; i < MTD.SIZE_DATA; i++) {
			for (int j = 0; j < MTD.SIZE_DATA; j++) {
				// 2= card {wp, bp}
				for (int k = 0; k < 2; k++) {
					zobristKey[i][j][k] = r.nextLong();
				}
			}
		}
		zobMyTurn = r.nextLong();
	}

	long getKey(int i, int j, int k) {
		return zobristKey[i][j][k];
	}

}
