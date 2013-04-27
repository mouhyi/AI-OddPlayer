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
 * UCT RAVE
 * Used the MTC implementation from http://mcts.ai/code/java.html
 */

public class Mouhyi extends Player {

	boolean stop = false;

	private OddMove bestMove;
	int[][][] moveFreq;

	protected static final int DEFAULT_TIMEOUT = 3300;
	private TimerTask timeoutTask;
	private Timer timer = new Timer();
	private int timeout = DEFAULT_TIMEOUT;
	Thread curT;
	PrintStream bw;
	Random R;
	private Object monitor;
	OddBoard myboard;

	TreeNodeRAVE tn;

	public Mouhyi() {
		super("Mouhyi");
		monitor = new Object();
		initLog();
		moveFreq = new int[TreeNodeRAVE.SIZE_DATA][TreeNodeRAVE.SIZE_DATA][2];
	}

	@Override
	public Move chooseMove(Board board) {

		// init vars
		this.myboard = (OddBoard) board;
		stop = false;
		TreeNodeRAVE.totalSims = 0;

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

		tn = new TreeNodeRAVE(this.myboard, this, null, null);
		this.curT = (new Thread(tn));
		this.curT.start();
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
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
		for (Action child : tn.children.values()) {
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

		log("Sims= " + TreeNodeRAVE.totalSims);
	}

	private void initLog() {
		File logFile = new File("logs/Mouhyi.txt");
		try {
			bw = new PrintStream(new FileOutputStream(logFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void log(String str) {
//		if (bw != null) bw.println(str);
	}

}

class TreeNodeRAVE implements Runnable {

	static Random r = new Random();
	static double epsilon = 1e-6;
	static int WIN = 1;
	static int LOSS = 0;
	static int totalSims = 0;
	static double EXPLORATION = 0.4;

	HashMap<Integer, Action> children;

	OddBoard board;
	Mouhyi player;
	OddMove parentMove;
	TreeNodeRAVE parent;
	int totalVisits;
	
	// parameters
	private static double b=5;
	private static final double k=50000;
	// fix Timer, Sysout time
	// increase k with time

	static final int SIZE = 4;
	static final int SIZE_DATA = 2 * SIZE + 1;

	public TreeNodeRAVE(OddBoard board, Mouhyi player, OddMove parentMove,
			TreeNodeRAVE parent) {
		super();
		this.totalVisits = 0;
		this.board = board;
		this.player = player;
		this.parentMove = parentMove;
		this.parent = parent;
	}

	public synchronized void run() {
		while (!player.stop) {
			selectAction();
		}
	}

	public void selectAction() {
		double value;
		// selection
		List<TreeNodeRAVE> visited = new LinkedList<TreeNodeRAVE>();
		List<OddMove> moves = new LinkedList<OddMove>();
		TreeNodeRAVE cur = this;
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
			TreeNodeRAVE newNode = cur.select();
			visited.add(newNode);
			moves.add(newNode.parentMove);

			// simulation
			value = newNode.rollOut(moves);
		}
		// propagation
		TreeNodeRAVE node;
		for (int i = 0; i < visited.size()-1; i++) {
			node = visited.get(i);
			node.updateStats(value, moves.get(i));
			node.updateAMAF(value, moves, i);
		}
	}

	public void expand() {
		OddBoard b;
		LinkedList<OddMove> moves = this.board.getValidMoves();
		children = new HashMap<Integer, Action>();
		for (OddMove m: moves) {
			b = (OddBoard) this.board.clone();
			b.move(m);
			children.put(hash(m), new Action(new TreeNodeRAVE(b, this.player, m, this)));
		}
	}

	private TreeNodeRAVE select() {
		return (this.board.getTurn() == this.player.getColor()) ? selectMax()
				: selectMin();
	}

	private TreeNodeRAVE selectMax() {
		TreeNodeRAVE selected = null;
		double bestValue = Double.MIN_VALUE;
		for (Action a : children.values()) {
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

	private TreeNodeRAVE selectMin() {
		TreeNodeRAVE selected = null;
		double bestValue = Double.MAX_VALUE;
		for (Action a : children.values()) {
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

	public double rollOut( List<OddMove> actions) {
		totalSims++;
		OddMove m;
		OddBoard b = (OddBoard) this.board.clone();

		while (!isTerminal(b)) {
			m = randomMove(b);
			b.move(m);
			actions.add(m);
		}
		return evaluateTerminal(b);
	}

	public int evaluateTerminal(OddBoard b) {
		return (b.getWinner() == player.getColor()) ? WIN : LOSS;
	}

	public void updateStats(double value, OddMove m) {
		if (!player.stop) {
			totalVisits++;
			Action a = children.get(hash(m));
			a.MCValue += value;
			a.MCVisits++;
		}
	}

	private void updateAMAF(double value, List<OddMove> moves, int level) {
		Action a;
		OddMove m;
		if (!player.stop) {
			// update very subsequent action of the simulation with the same colour to play
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
	
	private double getRAVEvalue(Action a){
		double b = getBeta(a);
		double MCscore = (a.MCValue) / (a.MCVisits + epsilon);
		double AMAFscore = (a.AMAFvalue) / (a.AMAFvisits + epsilon);
		
		return ( (1-b)*MCscore + b*AMAFscore);
	}
	
	private double getBeta2(Action a){
		return (a.AMAFvalue/ (a.MCValue + a.AMAFvalue + 4*a.MCValue*a.AMAFvalue*b*b));
	}
	
	private double getBeta(Action a){
		return Math.sqrt(k/(3*this.totalVisits + k));
	}

	public int arity() {
		return children == null ? 0 : children.values().size();
	}
}

class Action{
	public OddMove move;
	int MCVisits, MCValue;
	int AMAFvisits, AMAFvalue;
	TreeNodeRAVE nextState;
	public Action(TreeNodeRAVE nextState) {
		this.MCVisits = 0;
		this.MCValue = 0;
		this.AMAFvisits = 0;
		this.AMAFvalue = 0;
		this.nextState = nextState;
		this.move = nextState.parentMove;
	}
	
	
}