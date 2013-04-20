package odd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import odd.OddBoard.Piece;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

/*
 * Used the MTC implementation from http://mcts.ai/code/java.html
 */

public class UCT11 extends Player {

	boolean stop = false;

	private OddMove bestMove;
	int[][][] moveFreq;

	protected static final int DEFAULT_TIMEOUT = 3000;
	private TimerTask timeoutTask;
	private Timer timer = new Timer();
	private int timeout = DEFAULT_TIMEOUT;
	Thread curT;
	PrintStream bw;
	Random R;
	private Object monitor;
	OddBoard myboard;

	TreeNode11 tn;

	public UCT11() {
		super("UCT11");
		monitor = new Object();
		initLog();
		moveFreq = new int[TreeNode11.SIZE_DATA][TreeNode11.SIZE_DATA][2];
	}

	@Override
	public Move chooseMove(Board board) {

		// init vars
		this.myboard = (OddBoard) board;
		stop = false;
		TreeNode11.totalSims = 0;

		resetTimer();
		// System.out.println("------------Choose Move-------");

		try {
			synchronized (monitor) {
				monitor.wait();
			}
			// System.out.println("******************");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			return getBestMove();
		}
	}

	void updateBestMove(OddMove m) {
		bestMove = m;
	}

	OddMove getBestMove() {
		return bestMove;
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

		tn = new TreeNode11(this.myboard, this, null, null);
		this.curT = (new Thread(tn));
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

		// log("Interrupt");

		// find best move
		int max = -1;
		if(tn.children.values().isEmpty()) System.out.println("Empty childs");
		for (Action11 child : tn.children.values()) {
			if (child.nextState.totalVisits > max) {
				max = child.nextState.totalVisits;
				updateBestMove(child.move);
			}
		}
		// log("Done");
		synchronized (monitor) {
			monitor.notify();
		}

		// this.curT.interrupt();

		log("Sims= " + TreeNode11.totalSims);
	}

	private void initLog() {
		File logFile = new File("logs/UCT11.txt");
		try {
			bw = new PrintStream(new FileOutputStream(logFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void log(String str) {
		if (bw != null)
			bw.println(str);
	}

}

class TreeNode11 implements Runnable {

	static Random r = new Random();
	static double epsilon = 1e-6;
	static int WIN = 1;
	static int LOSS = 0;
	static int totalSims = 0;
	static double EXPLORATION = 0.4;

	HashMap<Integer, Action11> children;

	OddBoard board;
	UCT11 player;
	OddMove parentMove;
	TreeNode11 parent;
	int totalVisits;
	
	// parameters
	private static double b=5;
	private static final double k=10000;

	static final int SIZE = 4;
	static final int SIZE_DATA = 2 * SIZE + 1;

	public TreeNode11(OddBoard board, UCT11 player, OddMove parentMove,
			TreeNode11 parent) {
		super();
		this.totalVisits = 0;
		this.board = board;
		this.player = player;
		this.parentMove = parentMove;
		this.parent = parent;
	}

	public synchronized void run() {
		while (!player.stop) {
			selectAction11();
		}
	}

	public void selectAction11() {
		double value;
		// selection
		List<TreeNode11> visited = new LinkedList<TreeNode11>();
		List<OddMove> moves = new LinkedList<OddMove>();
		TreeNode11 cur = this;
		visited.add(this);
		while (!cur.isLeaf()) {
			cur = cur.select();
			// System.out.println("Adding: " + cur);
			visited.add(cur);
			moves.add(cur.parentMove);
		}

		if (isTerminal(cur.board)) {
			value = evaluateTerminal(cur.board);
		} else {

			// expansion
			cur.expand();
			TreeNode11 newNode = cur.select();
			visited.add(newNode);
			moves.add(newNode.parentMove);

			// simulation
			value = newNode.rollOut(moves);
		}
		// propagation
		TreeNode11 node;
		for (int i = 0; i < visited.size()-1; i++) {
			node = visited.get(i);
			node.updateStats(value, moves.get(i));
			node.updateAMAF(value, moves, i);
		}
	}

	public void expand() {
		OddBoard b;
		LinkedList<OddMove> moves = this.board.getValidMoves();
		children = new HashMap<Integer, Action11>();
		for (OddMove m: moves) {
			b = (OddBoard) this.board.clone();
			b.move(m);
			children.put(hash(m), new Action11(new TreeNode11(b, this.player, m, this)));
		}
	}

	private TreeNode11 select() {
		return (this.board.getTurn() == this.player.getColor()) ? selectMax()
				: selectMin();
	}

	private TreeNode11 selectMax() {
		TreeNode11 selected = null;
		double bestValue = Double.MIN_VALUE;
		for (Action11 a : children.values()) {
			double uctValue = this.getRAVEvalue(a) + EXPLORATION
					* Math.sqrt(Math.log(totalVisits + 1) / (a.MCVisits + epsilon))
					+ r.nextDouble() * epsilon;
			// small random number to break ties randomly in unexpanded nodes
			// System.out.println("UCT value = " + uctValue);
			if (uctValue > bestValue) {
				selected = a.nextState;
				bestValue = uctValue;
			}
		}
		// incFreq(selected.parentMove);
		return selected;
	}

	private TreeNode11 selectMin() {
		TreeNode11 selected = null;
		double bestValue = Double.MAX_VALUE;
		for (Action11 a : children.values()) {
			double uctValue = this.getRAVEvalue(a) - EXPLORATION
					* Math.sqrt(Math.log(totalVisits + 1) / (a.MCVisits + epsilon))
					+ r.nextDouble() * epsilon;
			// small random number to break ties randomly in unexpanded nodes
			// System.out.println("UCT value = " + uctValue);
			if (uctValue < bestValue) {
				selected = a.nextState;
				bestValue = uctValue;
			}
		}
		// incFreq(selected.parentMove);
		return selected;
	}

	static int hash(OddMove m) {
		return ((m.getDestRow() + SIZE) * SIZE_DATA + (m.getDestCol() + SIZE))
				* SIZE_DATA + ((m.getColor() == Piece.WP) ? 0 : 1);

	}

	/*
	 * private void incFreq(OddMove m) {
	 * player.moveFreq[m.getDestRow()+SIZE][m.getDestCol
	 * ()+SIZE][(m.getColor()==Piece.WP) ? 0 : 1] ++;
	 * 
	 * }
	 * 
	 * private int getMoveFreq(OddMove m){ return
	 * player.moveFreq[m.getDestRow()+
	 * SIZE][m.getDestCol()+SIZE][(m.getColor()==Piece.WP) ? 0 : 1]; }
	 * 
	 * private OddMove firstOrderHeuristic(OddBoard b){ OddMove best =null; int
	 * f=-1; for(OddMove m: b.getValidMoves()){ if(getMoveFreq(m) > f){ best =
	 * m; } } return best; }
	 */

	private OddMove randomMove(OddBoard b) {
		return b.getValidMoves().get(r.nextInt(b.getValidMoves().size()));
	}

	public boolean isLeaf() {
		return children == null;
	}

	static boolean isTerminal(OddBoard node) {
		return (node.countEmptyPositions() < 1);
	}

	public double rollOut( List<OddMove> Action11s) {
		totalSims++;
		OddMove m;
		OddBoard b = (OddBoard) this.board.clone();

		while (!isTerminal(b)) {
			m = randomMove(b);
			b.move(m);
			Action11s.add(m);
		}
		return evaluateTerminal(b);
	}

	public int evaluateTerminal(OddBoard b) {
		return (b.getWinner() == player.getColor()) ? WIN : LOSS;
	}

	public void updateStats(double value, OddMove m) {
		if (!player.stop) {
			totalVisits++;
			Action11 a = children.get(hash(m));
			a.MCValue += value;
			a.MCVisits++;
		}
	}

	private void updateAMAF(double value, List<OddMove> moves, int level) {
		Action11 a;
		OddMove m;
		if (!player.stop) {
			// update very subsequent Action11 of the simulation with the same colour to play
			for (int i = level; i < moves.size(); i++) {
				if (i % 2 == level % 2) {
					m = moves.get(i);
					a = children.get(hash(m));
					if (a != null) {
						a.AMAFvisits++;
						a.AMAFvalue += value;
					}
				}
			}
		}
	}
	
	private double getRAVEvalue(Action11 a){
		double b = getBeta(a);
		double MCscore = (a.MCValue) / (a.MCVisits + epsilon);
		double AMAFscore = (a.AMAFvalue) / (a.AMAFvisits + epsilon);
		
		return ( (1-b)*MCscore + b*AMAFscore);
	}
	
	private double getBeta2(Action11 a){
		return (a.AMAFvalue/ (a.MCValue + a.AMAFvalue + 4*a.MCValue*a.AMAFvalue*b*b));
	}
	
	private double getBeta(Action11 a){
		return Math.sqrt(k/(3*this.totalVisits + k));
	}

	public int arity() {
		return children == null ? 0 : children.values().size();
	}
}

class Action11{
	public OddMove move;
	int MCVisits, MCValue;
	int AMAFvisits, AMAFvalue;
	TreeNode11 nextState;
	public Action11(TreeNode11 nextState) {
		this.MCVisits = 0;
		this.MCValue = 0;
		this.AMAFvisits = 0;
		this.AMAFvalue = 0;
		this.nextState = nextState;
		this.move = nextState.parentMove;
	}
	
	
}