package odd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import odd.OddBoard.Piece;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

/**
 * MTD(f) with MCST as the static evaluation function
 * @author mouhyi
 *
 */
public class MTDPlayer extends Player {

	static int MAX_SEARCH_DEPTH = 20;
	HashMap<Long, State> TranspositionTable;
	static final int SIZE = 4;
	static final int SIZE_DATA = 2 * SIZE + 1;
	static final int VALID_ENTRIES = SIZE_DATA * SIZE_DATA - SIZE * (SIZE + 1);

	static final int MAX_SCORE = 100;
	static final int MIN_SCORE = -MAX_SCORE;
	static final int ERROR = -1;

	OddBoard myboard;
	private boolean stop;

	private Zobrist zob = new Zobrist();

	private OddMove globalbestMove;

	protected static final int DEFAULT_TIMEOUT = 4000;
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
		return MAX_SEARCH_DEPTH/2 - b.countEmptyPositions() / 10;

		// return Math.min(MAX_SEARCH_DEPTH, b.countEmptyPositions());
	}

	/*
	 * MTD algorithm with iterative deepening, MTD-f
	 */
	public int iterativeDeepening(OddBoard root) {

		MoveScore res;
		long hash = hash(root);

		int maxDepth = MAX_SEARCH_DEPTH;

		// MTD(f) needs a “first guess” as to where the minimax value will
		// turn out to be -> Heuristic
		int firstguess = MTCSimulation((OddBoard)root.clone(), 300);

		for (int d = 1; d < maxDepth; d++) {	
			firstguess = MTDF(root, firstguess, d, hash);
			if (stop){
				return ERROR;
			}
		}
		return firstguess;
	}
	
	void getBestMove(OddBoard board) {

		OddMove best = null;
		int score = Integer.MIN_VALUE;
		OddBoard b;
		LinkedList<OddMove> moves = board.getValidMoves();
		long h = hash(board);
		int firstguess[] = new int[moves.size()];
		long hash[] = new long[moves.size()];
		int dMax = MAX_SEARCH_DEPTH;
		
		// in case timout
		updateBestMove( moves.get(R.nextInt(moves.size())));
		
		// init
		for (int i = 0; i < moves.size(); i++) {
			hash[i] = incrementalHash(h, moves.get(i));
			firstguess[i]=0;
		}

		// start d=1
		for (int d = 1; d < dMax; d++) {
			for (int i = 0; i < moves.size(); i++) {
				b = (OddBoard) board.clone();
				b.move(moves.get(i));
				firstguess[i] = MTDF(b, firstguess[i], d, hash[i]);
				if (stop){
					return;
				}
				if (firstguess[i] > score) {
					score = firstguess[i];
					best = moves.get(i);
				}

			}
			if (stop){
				return;
			}
			updateBestMove(best);
		}
	}

	public int MTDF(OddBoard root, int f, int d, long hash) {
		int g = f;
		int beta;
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;

		while (lowerBound < upperBound) {
			if (stop){
				return ERROR;
			}

			if (g == lowerBound) {
				beta = g + 1;
			} else {
				beta = g;
			}
			g = AlphaBetaWithMemory(root, beta - 1, beta, d, hash);

			if (g < beta) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		if (stop){
			return ERROR;
		}
		return g;
	}

	private int AlphaBetaWithMemory(OddBoard board, int alpha, int beta, int d,
			long h) {

		if (stop)
			return ERROR;

		int g, a, b;
		State n = null;
		OddBoard c;

		/* Transposition table lookup */
		if (TranspositionTable.containsKey(h)) {
			n = TranspositionTable.get(h);
			if (n.depth >= d || n.terminal) {
				if (n.lowerbound >= beta) {
					return n.lowerbound;
				}
				if (n.upperbound <= alpha) {
					return n.upperbound;
				}
			}
		} else {
			n = new State(board, h, getColor() == board.getTurn());
			TranspositionTable.put(h, n);
		}

		alpha = Math.max(alpha, n.lowerbound);
		beta = Math.min(beta, n.upperbound);
		LinkedList<OddMove> moves = board.getValidMoves();

		if (isTerminal(board) || d == 0) {
			g = evaluate(n); /* leaf node */
			n.lowerbound = g;
			n.upperbound = g;
		}

		else if (n.MAXNODE) {
			g = Integer.MIN_VALUE;
			a = alpha; /* save original alpha value */

			for (OddMove m : moves) {
				if (stop){
					return ERROR;
				}
				if (g >= beta) {
					break;
				}
				c = (OddBoard) board.clone();
				c.move(m);
				g = Math.max( g, AlphaBetaWithMemory(c, a, beta, d - 1, incrementalHash(h, m)));
				a = Math.max(a, g);
			}
		}

		else { /* n is a MINNODE */

			g = Integer.MAX_VALUE;
			b = beta; /* save original beta value */

			for (OddMove m : moves) {

				if (stop){
					return ERROR;
				}

				if (g <= alpha) {
					break;
				}
				c = (OddBoard) board.clone();
				c.move(m);

				g = Math.min( AlphaBetaWithMemory(c, alpha, b, d - 1,
								incrementalHash(h, m)), g);
				b = Math.min(b, g);
			}
		}

		/* Traditional transposition table storing of bounds */
		n.depth = d;
		/* Fail low result implies an upper bound */
		if (g <= alpha) {
			n.upperbound = g;
		}
		 // Found an accurate minimax value – will not occur if called with zero window
		if (g > alpha && g < beta) {
			System.out.println("Never");
			n.lowerbound = g;
			n.upperbound = g;
		}
		/* Fail high result implies a lower bound */
		if (g >= beta) {
			n.lowerbound = g;
		}

		return g;
	}
	
	// Static Evaluation Function
	private int evaluate(State n) {
		if (n.terminal) {
			return (n.board.getWinner() == this.getColor()) ? MAX_SCORE
					: MIN_SCORE;
		}
		return MTCSimulation(n.board, getSimNumber(n.board));
	}
	
	// Number of MC Simulations to run
	private int getSimNumber(OddBoard board) {
		return (100 * board.countEmptyPositions()) / 61;
	}

	private int MTCSimulation(OddBoard board, int simulations) {
		int score = 0;
		OddBoard b;
		LinkedList<OddMove> moves = board.getValidMoves();
		int i = simulations;

		while (i-- > 0) {
			b = (OddBoard) board.clone();

			while (!isTerminal(b)) {
				moves = b.getValidMoves();
				b.move(moves.get(R.nextInt(moves.size())));
			}
			if (b.getWinner() == this.getColor()) {
				score++;
			} else {
				score--;
			}
		}
		return (2 * score * MAX_SCORE) / (3 * simulations);
	}

	static boolean isTerminal(OddBoard node) {
		return (node.countEmptyPositions() < 1);
	}

	boolean isMaximizing(OddBoard b) {
		return (b.getTurn() == this.getColor());
	}

	// Zobrist Hashing	
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
		globalbestMove = OddMove.copy(m);;
	}

	private OddMove getBestMove() {
		return globalbestMove;
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
			return getBestMove();
		}
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
	}

	private void initLog() {
		File logFile = new File("logs/MTDout.txt");
		try {
			bw = new PrintStream(new FileOutputStream(logFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void log(String str) {
//		if (bw != null) bw.println(str);
	}

	public Move chooseRandomMove(Board board) {
		OddBoard pb = (OddBoard) board;
		LinkedList<OddMove> validMoves = pb.getValidMoves();
		return validMoves.get(R.nextInt(validMoves.size()));
	}

	public int playRandomly(OddBoard b) {
		while (!isTerminal(b)) {
			b.move(chooseRandomMove(b));
		}
		return (b.getWinner() == this.getColor()) ? MAX_SCORE
				: MIN_SCORE ;
	}
	
	// check if two boards are equivalent 
	public static boolean equivalent(OddBoard b1, OddBoard b2) {
		for (int i = -SIZE; i <= SIZE; i++) {
			for (int j = -SIZE; j <= SIZE; j++) {
				if (b1.getPieceAt(i, j) != b2.getPieceAt(i, j)) {
					return false;
				}
			}
		}
		if(b1.getTurn() != b2.getTurn()){
			return false;
		}
		return true;
	}
}

class State {
	long hashValue;
	int lowerbound;
	int upperbound;
	int depth;
	boolean MAXNODE;
	boolean terminal;
	OddBoard board;


	public State(OddBoard board, long hash, boolean max) {
		this.hashValue = hash;
		this.lowerbound = Integer.MIN_VALUE;
		this.upperbound = Integer.MAX_VALUE;
		this.depth = -1;
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
		p.getBestMove(p.myboard);
	}
}
