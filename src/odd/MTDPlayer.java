package odd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import odd.OddBoard.Piece;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class MTDPlayer extends Player {

	static int MAX_SEARCH_DEPTH = 61;
	HashMap<Long, State> TranspositionTable;
	// static OddMove bestMove;
	// TODO AIPLAYEr -> dynamic player.color
	static int AIPLAYER = 2;
	static final int SIZE = 4;
	static final int SIZE_DATA = 2 * SIZE + 1;
	static final int VALID_ENTRIES = SIZE_DATA * SIZE_DATA - SIZE * (SIZE + 1);

	static final int MAX_SCORE = 100;
	static final int MIN_SCORE = -MAX_SCORE;

	OddBoard myboard;
	private boolean stop;

	private Zobrist zob = new Zobrist();

	private OddMove globalbestMove;

	protected static final int DEFAULT_TIMEOUT = 4500;
	private TimerTask timeoutTask;
	private Timer timer = new Timer();
	private int timeout = DEFAULT_TIMEOUT;
	private Semaphore sem;
	Thread curT;
	PrintStream bw;
	Random R;
	private Object monitor = new Object();

	public MTDPlayer() {
		super("MTDPlayer");
		TranspositionTable = new HashMap<Long, State>();
		stop = false;
		sem = new Semaphore(1);
		initLog();
		R = new Random();

	}

	private int getMaxDepth(OddBoard b) {
		return 8 - b.countEmptyPositions()/10;

		// return Math.min(MAX_SEARCH_DEPTH, b.countEmptyPositions());
	}

	/*
	 * MTD algorithm with iterative deepening, MTD-f
	 */
	public int iterativeDeepening(OddBoard root) {

		MoveScore res;
		long hash = hash(root);

		int maxDepth = MAX_SEARCH_DEPTH;

		// TODO MTD(f) needs a “first guess” as to where the minimax value will turn
		// out to be -> Heuristic
		int firstguess = MTCSimulation(root, 50);

		for (int d = 1; d < maxDepth; d++) {

			if (stop)
				return -22;

			log("****************** d=" + d);

			res = MTDF(root, firstguess, d, hash);

			if (stop)
				return -22;

			firstguess = res.score;

			updateBestMove(res.move);

			log("IDeepening Result Score: " + res.score + "  - Rem:"
					+ root.countEmptyPositions() + "Max depth=" + d
					+ "  **********");
		}
		// TODO if times_up() then break;

		if (stop)
			return -22;

		return firstguess;
	}
	
	public void MTDCutOff(OddBoard root){
		MoveScore res;
		long hash = hash(root);

		int d = getMaxDepth(root);
		int firstguess = MTCSimulation(root, 500);
		
		log("****************** d=" + d);
		res = MTDF(root, firstguess, d, hash);
		firstguess = res.score;
		updateBestMove(res.move);

		log("MTDCutOff Score: " + res.score + "  - Rem:"
				+ root.countEmptyPositions() + "Max depth=" + d
				+ "  **********");
	}

	public MoveScore MTDF(OddBoard root, int f, int d, long hash) {
		int g = f;
		int beta;
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		MoveScore res = null;

		while (lowerBound < upperBound) {
			if (stop)
				return null;

			if (g == lowerBound) {
				beta = g + 1;
			} else {
				beta = g;
			}
			res = AlphaBetaWithMemory(root, beta - 1, beta, d, hash);
			log("AB, value="+beta);

			if (stop)
				return null;

			g = res.score;
			if (g < beta) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		if (stop)
			return null;

		log("MTD returns=" + res.score);
		return res;
	}

	private MoveScore AlphaBetaWithMemory(OddBoard board, int alpha, int beta,
			int d, long h) {

		// System.out.println("-----  AB --------");

		if (stop)
			return null;

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
				// System.out.println("LOOKUP -> YES, d="+d);

				// System.out.println("n.empty:"+n.board.countEmptyPositions()+" -- n.depth="+n.depth);

				if (!OddBoard.equivalent(board, n.board)) {
					System.out.println("COLLISION************************");
				}
				
				if(n.depth>n.board.countEmptyPositions() && n.lowerbound!=MIN_SCORE && n.upperbound!=MAX_SCORE ){
					log2("T rem<depth "+ " ** lower=" + n.lowerbound+ " ** upper=" + n.upperbound);
				}

				if (n.lowerbound >= beta) {
					// System.out.println("n.lower="+n.lowerbound);
					log2("A=" + alpha + "*** B=" + beta + " TABLE up - d=" + d
							+ " - score=" + n.lowerbound + "- depth=" + n.depth);
					if (n.bestMove != null) {
						log2(" ** Move=" + n.bestMove.toPrettyString());
					}
					return (new MoveScore(n.bestMove, n.lowerbound));
				}
				if (n.upperbound <= alpha) {
					// System.out.println("n.upper="+n.upperbound);
					log2("A=" + alpha + "*** B=" + beta + "  TABLE down - d="
							+ d + " - score=" + n.upperbound + "- depth="
							+ n.depth);
					if (n.bestMove != null) {
						log2(" ** Move=" + n.bestMove.toPrettyString());
					}
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
		if (isTerminal(board) || d == 0) {
			g = evaluate(n); /* leaf node */
			n.lowerbound = g;
			n.upperbound = g;
		}

		else if (n.MAXNODE) {

			g = Integer.MIN_VALUE;
			a = alpha; /* save original alpha value */

			for (OddMove m : moves) {

				if (stop)
					return null;

				if (g >= beta) {
					break;
				}
				c = (OddBoard) board.clone();
				c.move(m);

				res = AlphaBetaWithMemory(c, a, beta, d - 1,
						incrementalHash(h, m));

				if (stop)
					return null;

				if (res.score > g) {
					bestMove = m;
				}
				g = Math.max(g, res.score);
				a = Math.max(a, g);

				/*if (g == MAX_SCORE) {
					break;
				}*/
			}
		}

		else { /* n is a MINNODE */

			g = Integer.MAX_VALUE;
			b = beta; /* save original beta value */

			for (OddMove m : moves) {

				if (stop)
					return null;

				if (g <= alpha) {
					break;
				}
				c = (OddBoard) board.clone();
				c.move(m);

				res = AlphaBetaWithMemory(c, alpha, b, d - 1,
						incrementalHash(h, m));

				if (stop)
					return null;

				if (res.score < g) {
					bestMove = m;
				}

				g = Math.min(res.score, g);
				b = Math.min(b, g);

				/*if (g == MIN_SCORE) {
					break;
				}*/

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
			System.out.println("Never");
			n.lowerbound = g;
			n.upperbound = g;
			// store n.lowerbound, n.upperbound;
		}
		/* Fail high result implies a lower bound */
		if (g >= beta) {
			n.lowerbound = g;
			// store n.lowerbound;
		}

		/*
		 * if (n.terminal) { log("HHHHH  n.upperbound=" + n.upperbound +
		 * " *** n.lowerbound=" + n.lowerbound); }
		 */
		log2("A=" + alpha + "*** B=" + beta + " ** d=" + d + " *** score=" + g
				+ " ** Move="
				+ ((bestMove != null) ? bestMove.toPrettyString() : "NULL"));
		
		if(n.upperbound < n.lowerbound){
			log2("n.depth="+n.depth+ "** n.rem="+n.board.countEmptyPositions()+ " ** lower=" + n.lowerbound+ " ** upper=" + n.upperbound);
		}

		return new MoveScore(bestMove, g);
	}

	private int evaluate(State n) {
		if (n.terminal) {
			return (n.board.getWinner() == this.getColor()) ? MAX_SCORE
					: MIN_SCORE;
		}
		return MTCSimulation(n.board, getSimNumber(n.board));

		/*
		 * int k = (n.terminal) ? 1 : 2; return (n.board.getWinner() ==
		 * this.getColor()) ? MAX_SCORE / k : MIN_SCORE / k;
		 */

	}
	
	private int getSimNumber(OddBoard board){
		return (50 * board.countEmptyPositions())/8;
	}

	private int MTCSimulation(OddBoard board, int simulations) {
		int score = 0;
		OddBoard b;
		LinkedList<OddMove> moves = board.getValidMoves();
		int i=simulations;

		while (i-- > 0) {
			b = (OddBoard) board.clone();

			while (!isTerminal(b)) {
				moves = b.getValidMoves();
				b.move(moves.get(R.nextInt(moves.size())));
			}
			if (b.getWinner() == this.getColor()){
				score++;
			}else{
				score--;
			}
		}
		return (2*score*MAX_SCORE)/(3*simulations);
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

	long hash(OddBoard board) {
		long result = 0l;
		for (int i = -SIZE; i <= SIZE; i++) {
			for (int j = -SIZE; j <= SIZE; j++) {
				if (board.getPieceAt(i, j) == Piece.WP) {
					result ^= zob.getKey(i + SIZE, j + SIZE, 0);
				} else if (board.getPieceAt(i, j) == Piece.BP) {
					result ^= zob.getKey(i + SIZE, j + SIZE, 1);
				}
			}
		}
		return result;
	}

	long incrementalHash(long hash, OddMove move) {
		int color = move.getColor() == Piece.WP ? 0 : 1;
		hash ^= zob.getKey(move.getDestRow() + SIZE, move.getDestCol() + SIZE,
				color);
		// Encode player's turn in state
		hash ^= zob.zobMyTurn;
		return hash;

	}

	private void updateBestMove(OddMove m) {
		globalbestMove = m;
	}

	private OddMove getBestMove() {
		return OddMove.copy(globalbestMove);
	}

	public void iterativeDeepening() {
		iterativeDeepening(this.myboard);
	}

	@Override
	public Move chooseMove(Board board) {

		try {
			sem.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		OddBoard b = (OddBoard) board;
		this.myboard = b;

		stop = false;
		resetTimer();

		try {
			synchronized (monitor) {
				monitor.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (b.countEmptyPositions() > 20)
				return chooseRandomMove(b);
			return globalbestMove;
		}

		// int sc = iterativeDeepening(b);
		// log(globalbestMove.toPrettyString());
		// if sem.signal -- while timer notify
		// System.out.println("Score:"+sc + " ----- d="+
		// (61-b.countEmptyPositions()));
		// if (b.countEmptyPositions() > getMaxDepth(b)) return
		// chooseRandomMove(b);
		// return globalbestMove;
	}

	private void resetTimer() {
		cancelTimeout();
		stop = false;

		timeoutTask = new TimerTask() {
			public void run() {
				timeOut();
			}
		};
		timer.schedule(timeoutTask, timeout);

		this.curT = (new Thread(new IterativeDeepening(this)));
		this.curT.start();
	}

	// So the GUI can cancel the timeout
	synchronized void cancelTimeout() {
		if (timeoutTask != null)
			timeoutTask.cancel();
		timeoutTask = null;
	}

	private synchronized void timeOut() {
		stop = true;
		synchronized (monitor) {
			monitor.notify();
		}
		this.curT.interrupt();
		while (curT.isAlive())
			;
		sem.release();

		log("Timeout");
	}

	private void initLog() {
		File logFile = new File("logs/myOut0.txt");
		try {
			bw = new PrintStream(new FileOutputStream(logFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void log(String str) {
		if (bw != null) bw.println(str);
	}
	private void log2(String str) {
//		if (bw != null) bw.println(str);
	}

	public Move chooseRandomMove(Board board) {
		OddBoard pb = (OddBoard) board;
		LinkedList<OddMove> validMoves = pb.getValidMoves();
		// if (validMoves.isEmpty()) return null;
		// else
		return validMoves.get(R.nextInt(validMoves.size()));
	}

	public int playRandomly(OddBoard b) {
		while (!isTerminal(b)) {
			b.move(chooseRandomMove(b));
		}
		return (b.getWinner() == this.getColor()) ? MAX_SCORE / 2
				: MIN_SCORE / 2;
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

class IterativeDeepening implements Runnable {

	private MTDPlayer p;

	public IterativeDeepening(MTDPlayer p) {
		super();
		this.p = p;
	}

	public synchronized void run() {
		p.iterativeDeepening();
	}
}
