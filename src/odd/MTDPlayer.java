package odd;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import odd.OddBoard.Piece;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class MTDPlayer extends Player {

	static int MAX_SEARCH_DEPTH = 13;
	HashMap<Long, State> TranspositionTable;
	// static OddMove bestMove;
	// TODO AIPLAYEr -> dynamic player.color
	static int AIPLAYER = 2;
	static final int SIZE = 4;
	static final int SIZE_DATA = 2 * SIZE + 1;
	static final int VALID_ENTRIES = SIZE_DATA * SIZE_DATA - SIZE * (SIZE + 1);

	static final int MAX_SCORE = 100;
	static final int MIN_SCORE = -100;

	private static Zobrist zob = new Zobrist();

	private static OddMove globalbestMove;

	protected static final int DEFAULT_TIMEOUT = 5000;
	private TimerTask timeoutTask;
	private Timer timer = new Timer();
	private int timeout = DEFAULT_TIMEOUT;

	public MTDPlayer() {
		super("MTDPlayer");
		TranspositionTable = new HashMap<Long, State>();
	}

	private int getMaxDepth(OddBoard b) {
		return MAX_SEARCH_DEPTH;
		// return Math.min(MAX_SEARCH_DEPTH, b.countEmptyPositions());
	}

	/*
	 * MTD algorithm with iterative deepening, MTD-f
	 */
	public int iterativeDeepening(OddBoard root) {

		MoveScore res;
		long hash = hash(root);

		int maxDepth = getMaxDepth(root);

		System.out.println("Iterative Deepening");

		// TODO MTD(f) needs a “first guess” as to where the minimax value will
		// turn
		// out to be -> Heuristic
		int firstguess = 0;

		for (int d = 1; d < maxDepth; d++) {

			System.out.println("MTD, d=" + d);

			res = MTDF(root, firstguess, d, hash);
			firstguess = res.score;
			globalbestMove = res.move;

			// System.out.println(globalbestMove.toPrettyString());
		}
		// TODO if times_up() then break;

		return firstguess;
	}

	public MoveScore MTDF(OddBoard root, int f, int d, long hash) {
		int g = f;
		int beta;
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		MoveScore res = null;

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

	private MoveScore AlphaBetaWithMemory(OddBoard board, int alpha, int beta,
			int d, long h) {

		// System.out.println("-----  AB --------");

		MoveScore res;
		OddMove bestMove = null;
		int g, a, b;
		State n = null;
		// TODO depth? -- OK
		OddBoard c;

		// TODO ZOBric Keys -- OK
		// long h = curHash;

		// TODO transTable.lookup(node, depth) -- OK
		/* Transposition table lookup */
		if (TranspositionTable.containsKey(h)) {
			n = TranspositionTable.get(h);
			if (n.depth >= d || n.terminal) {
				System.out.println("LOOKUP -> YES");

				if (n.lowerbound >= beta) {
					return (new MoveScore(n.bestMove, n.lowerbound));
				}
				if (n.upperbound <= alpha) {
					return (new MoveScore(n.bestMove, n.upperbound));
				}
			}
		} else {
			// System.out.println("LOOKUP -> NO");
			n = new State(board, h, getColor() == board.getTurn());
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

				res = AlphaBetaWithMemory(c, a, beta, d - 1,
						incrementalHash(h, m));
				if (res.score > g) {
					bestMove = m;
					// System.out.println("update- g="+g+"  - new="+res.score);

				}
				g = Math.max(g, res.score);
				a = Math.max(a, g);

				if (res.score == MAX_SCORE) {
					break;
				}
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

				res = AlphaBetaWithMemory(c, alpha, b, d - 1,
						incrementalHash(h, m));

				if (res.score < g) {
					bestMove = m;
					// System.out.println("update 222- g="+g+"  - new="+res.score);

				}

				g = Math.min(res.score, g);

				b = Math.min(b, g);

				if (res.score == MIN_SCORE) {
					break;
				}

			}
		}

		/* Traditional transposition table storing of bounds */
		n.depth = d;
		n.bestMove = bestMove;
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

	private int evaluate(State n) {
		int k = (n.terminal) ? 1 : 2;
		return (n.board.getWinner() == this.getColor()) ? MAX_SCORE/k : MIN_SCORE/k;

	}

	/*
	 * // TODO remove static OddMove getBestMove(OddBoard b) { OddBoard c;
	 * OddMove bestM = null; int s; int bestScore = Integer.MIN_VALUE; for
	 * (OddMove m : b.getValidMoves()) { c = (OddBoard) b.clone(); c.move(m); s
	 * = MTDF(c); if (s > bestScore) { bestM = m; bestScore = s; } } return
	 * bestM; }
	 */

	static boolean isTerminal(OddBoard node) {
		return (node.countEmptyPositions() < 1);
	}

	boolean isMaximizing(OddBoard b) {
		return (b.getTurn() == this.getColor());
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
		hash ^= zob.getKey(move.getDestRow() + SIZE, move.getDestCol() + SIZE,
				color);
		// Encode player's turn in state
		hash ^= zob.zobMyTurn;
		return hash;

	}

	@Override
	public Move chooseMove(Board board) {
		System.out.println("Choose Move");
		OddBoard b = (OddBoard) board;
		OddMove m;
		// TODO while time is not up call dtd
		iterativeDeepening(b);
		// if time up
		return globalbestMove;
	}

	private void resetTimer() {
		cancelTimeout();
		timeoutTask = new TimerTask() {
			public void run() {
				timeOut();
			}
		};
		timer.schedule(timeoutTask, timeout);
	}

	// So the GUI can cancel the timeout
	synchronized void cancelTimeout() {
		if (timeoutTask != null)
			timeoutTask.cancel();
		timeoutTask = null;
	}

	private synchronized void timeOut() {

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
	OddBoard board;
	OddMove bestMove;

	public State(OddBoard board, long hash, boolean max) {
		this.hashValue = hash;
		this.lowerbound = Integer.MIN_VALUE;
		this.upperbound = Integer.MAX_VALUE;
		this.depth = -1;
		this.bestMove = null;
		terminal = MTDPlayer.isTerminal(board);
		MAXNODE = max;
		this.board = board;
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

	static final int size = MTDPlayer.SIZE_DATA * MTDPlayer.SIZE_DATA * 2 + 1;

	Zobrist() {
		zobristKey = new long[MTDPlayer.SIZE_DATA][MTDPlayer.SIZE_DATA][2];
		Random r = new Random();
		for (int i = 0; i < MTDPlayer.SIZE_DATA; i++) {
			for (int j = 0; j < MTDPlayer.SIZE_DATA; j++) {
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
