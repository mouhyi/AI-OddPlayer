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
import java.util.concurrent.Semaphore;

import odd.OddBoard.Piece;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

/*
 * Used the MTC implementation from http://mcts.ai/code/java.html
 */

public class MyPlayer extends Player {

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
	
	TreeNode22 tn;

	public MyPlayer() {
		super("MyPlayer");
		monitor = new Object();
		initLog();
		moveFreq = new int [TreeNode22.SIZE_DATA][TreeNode22.SIZE_DATA][2];
	}

	@Override
	public Move chooseMove(Board board) {
		
		// init vars
		this.myboard = (OddBoard) board;
		stop = false;
		TreeNode22.totalSims = 0;
		
		resetTimer();
//		System.out.println("------------Choose Move-------");

		try {
			synchronized (monitor) {
				monitor.wait();
			}
//			System.out.println("******************");
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

		tn = new TreeNode22(this.myboard, this, null);
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
		
		
//		log("Interrupt");

		// find best move
		int max = 0;
		for (TreeNode22 child : tn.children) {
			if (child.nVisits > max) {
				max = child.nVisits;
				updateBestMove(child.parentMove);
			}
		}
//		log("Done");
		synchronized (monitor) {
			monitor.notify();
		}
		
		//this.curT.interrupt();

//		log("Sims= " + TreeNode22.totalSims);
	}

	private void initLog() {
		File logFile = new File("logs/UCT22.txt");
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

}

class TreeNode22 implements Runnable {

	static Random r = new Random();
	static double epsilon = 1e-6;
	static int WIN = 1;
	static int LOSS = 0;
	static int totalSims = 0;
	static double EXPLORATION=0.4;

	TreeNode22[] children;
	int nVisits, totValue;
	OddBoard board;
	MyPlayer player;
	OddMove parentMove;
	
	static final int SIZE = 4;
	static final int SIZE_DATA = 2 * SIZE + 1;

	public TreeNode22(OddBoard board, MyPlayer player, OddMove parentMove) {
		super();
		this.nVisits = 0;
		this.totValue = 0;
		this.board = board;
		this.player = player;
		this.parentMove = parentMove;
	}

	public synchronized void run() {
		while (!player.stop) {
			selectAction();
		}
	}

	public void selectAction() {
		double value;
		// selection
		List<TreeNode22> visited = new LinkedList<TreeNode22>();
		TreeNode22 cur = this;
		visited.add(this);
		while (!cur.isLeaf()) {
			cur = cur.select();
			// System.out.println("Adding: " + cur);
			visited.add(cur);
		}

		if (isTerminal(cur.board)) {
			value = evaluateTerminal(cur.board);
		} else {

			// expansion
			cur.expand();
			TreeNode22 newNode = cur.select();
			visited.add(newNode);

			// simulation
			value = rollOut(newNode);
		}
		// propagation
		for (TreeNode22 node : visited) {
			// System.out.println(node);
			node.updateStats(value);
		}
	}

	public void expand() {
		OddBoard b;
		LinkedList<OddMove> moves = this.board.getValidMoves();
		children = new TreeNode22[moves.size()];
		for (int i = 0; i < moves.size(); i++) {
			b = (OddBoard) this.board.clone();
			b.move(moves.get(i));
			children[i] = new TreeNode22(b, this.player, moves.get(i));
		}
	}

	private TreeNode22 select() {
		return (this.board.getTurn() == this.player.getColor()) ? selectMax() : selectMin();
	}
	
	private TreeNode22 selectMax() {
		TreeNode22 selected = null;
		double bestValue = Double.MIN_VALUE;
		for (TreeNode22 c : children) {
			double uctValue = c.totValue / (c.nVisits + epsilon)
					+ EXPLORATION*Math.sqrt(Math.log(nVisits + 1) / (c.nVisits + epsilon))
					+ r.nextDouble() * epsilon;
			// small random number to break ties randomly in unexpanded nodes
			// System.out.println("UCT value = " + uctValue);
			if (uctValue > bestValue) {
				selected = c;
				bestValue = uctValue;
			}
		}
//		incFreq(selected.parentMove);
		return selected;
	}
	
	private TreeNode22 selectMin() {
		TreeNode22 selected = null;
		double bestValue = Double.MAX_VALUE;
		for (TreeNode22 c : children) {
			double uctValue = c.totValue / (c.nVisits + epsilon)
					- EXPLORATION*Math.sqrt(Math.log(nVisits + 1) / (c.nVisits + epsilon))
					- r.nextDouble() * epsilon;
			// small random number to break ties randomly in unexpanded nodes
			// System.out.println("UCT value = " + uctValue);
			if (uctValue < bestValue) {
				selected = c;
				bestValue = uctValue;
			}
		}
//		incFreq(selected.parentMove);
		return selected;
	}

/*	private void incFreq(OddMove m) {
		player.moveFreq[m.getDestRow()+SIZE][m.getDestCol()+SIZE][(m.getColor()==Piece.WP) ? 0 : 1] ++;
		
	}
	
	private int getMoveFreq(OddMove m){
		return player.moveFreq[m.getDestRow()+SIZE][m.getDestCol()+SIZE][(m.getColor()==Piece.WP) ? 0 : 1];
	}
	
	private OddMove firstOrderHeuristic(OddBoard b){
		OddMove best =null;
		int f=-1;
		for(OddMove m: b.getValidMoves()){
			if(getMoveFreq(m) > f){
				best = m;
			}
		}
		return best;
	}*/
	
	private OddMove randomMove(OddBoard b){
		return b.getValidMoves().get(r.nextInt(b.getValidMoves().size()));
	}

	public boolean isLeaf() {
		return children == null;
	}

	static boolean isTerminal(OddBoard node) {
		return (node.countEmptyPositions() < 1);
	}

	public double rollOut(TreeNode22 tn) {
		totalSims++;
		LinkedList<OddMove> moves = board.getValidMoves();
		OddBoard b = (OddBoard) tn.board.clone();

		while (!isTerminal(b)) {
			b.move(randomMove(b));
		}
		return evaluateTerminal(b);
	}

	public int evaluateTerminal(OddBoard b) {
		return (b.getWinner() == player.getColor()) ? WIN : LOSS;
	}

	public void updateStats(double value) {
		if(!player.stop){
			nVisits++;
			totValue += value;
		}
	}

	public int arity() {
		return children == null ? 0 : children.length;
	}
}