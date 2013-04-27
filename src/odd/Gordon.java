package odd;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import odd.OddBoard;
import odd.OddMove;


public class Gordon extends Player {

	Random R = new Random();
	int me;
    public Gordon() {
        super("Gordon");
    }

    public Gordon(Random r) {
        super("Gordon");
        R=r;
    }
    	
	
    public Move chooseMove(Board board) {
    	me = (board.getTurn() % 2); // if player 1, me = 1. if player 2, me = 0
       odd.OddBoard pb = (odd.OddBoard) board; 
       LinkedList<odd.OddMove> validMoves = pb.getValidMoves();
       
             
       
       MCTnode head = new MCTnode(null, null); //make a null node to head our tree
       MCTlayer firstLayer = new MCTlayer(validMoves, head); //Create a layer with all possible moves from our first move 
       
       
       long start = System.currentTimeMillis();
       long end = start + 3000;
       int i = 0;
       while (System.currentTimeMillis() < end)
       {
           
 
    	   
	       odd.OddBoard sim = (odd.OddBoard) board.clone(); //create a copy of the board to run simulation on hopefully!
	       MCTlayer current = firstLayer; //Now every time through the for loop we start back up taking the best from the first layer
	       
	       
	       //Go through and select all the best nodes that we have seen so far
	       while(current != null){ //Current layer is the top layer 
	    	   
	    	   sim.move(current.getBest().getMove()); //make the best move from the top layer
	    	   
	    	   
	    	   //Reasons why it is always going through the same number of times
	    	   		//Get best is always the same, because then you're adding a layer every time, makes sense because thats why you always choose the first node
	    	   
	    	   
	    	   if (current.getBest().getNextLayer() != null){
	    		   current = current.getBest().getNextLayer(); //adjust the current layer to that below the move we just made, repeat until we are in a null layer
	    	   }
	    	   else{
	    		   if(sim.getValidMoves().size() != 0){
	    			   MCTlayer temp = new MCTlayer(sim.getValidMoves(), current.getBest()); //Creates the new layer
	    			   current.getBest().setNext(temp);
	    			   current = temp;
	    			   break;
	    		   }
	    		   break;
	    	   }
	       }
	       //Run the simulation based on this board and return the winner player 1 = 1, player 2 = -1
	       int result = RandomSim(sim); //need to change to get the player
	       MCTnode currentNode = current.getParent(); //set current node to that of the parent of the newly created layer
	       while(currentNode != head){
	    	   currentNode.update(result); //update the current nodes value
	    	   currentNode = currentNode.getLayer().getParent(); //make the current node the parent of the current nodes layer
	       }
	       i++;
       }
       return firstLayer.getBest().getMove();
    }

    
    //player1 wins return 1 and player 2 wins return -1
    //player is 1 or 2 for player 1 or player 2
    public int RandomSim(odd.OddBoard board){
    	Random R = new Random();
        OddBoard sim = (OddBoard) board;
        LinkedList<OddMove> validMoves = sim.getValidMoves();
        while(!validMoves.isEmpty()){
        	//System.out.println("rollout");
            sim.move(validMoves.get(R.nextInt(validMoves.size()))); 
            validMoves = sim.getValidMoves(); //look into what is faster, making a whole new list or deleting an element?
        }
        if(me == 1){
        	if(sim.getWinner() == 1) return 1;
        	else return -1;
        }
        if(me == 0){
        	if(sim.getWinner() == 2) return 1;
        	else return -1;
        }
        return 100000;
    }
}

//current idea is to create an arraylist to keep track of the fors sibling at each level so you can go through them
//could store each level in a priority queue 
class MCTlayer {
	private MCTnode parent;
	private PriorityQueue<MCTnode> layer;
	public MCTlayer(LinkedList<odd.OddMove> moves, MCTnode parent){
		this.parent = parent;
		layer = new PriorityQueue<MCTnode>(moves.size());
		odd.OddMove current;
		for(int i = 0; i < moves.size(); i++){
			current = moves.get(i);
			layer.add(new MCTnode(current, this));
			
		}
	}
	public MCTnode getBest(){
		return layer.peek();
	}
	public MCTnode getParent(){
		return parent;
	}
	public void printLayer(){
		MCTnode cur = layer.poll();
		int i = 0;
		while(!layer.isEmpty()){
			System.out.println("move " + i + " " + cur.getUCTscore());
			cur = layer.poll();
			i += 1;
		}
	}
	public void popOffTheTopTest(){
		layer.poll();
	}
	public void add(MCTnode node){
		layer.add(node);
	}
}


//Have to be able to update and keep the score
//Can do it by keeping track of number of turns or maybe a continous update
//for now will store number of turns and will update and store values
//Need to set the comparable value to be the UCTscore
class MCTnode implements Comparable <MCTnode>{
	private double numSim;
	private int rawScore;
	private double UCTscore;
	private MCTlayer nextLayer;
	private odd.OddMove move;
	private MCTlayer currentLayer;
	public MCTnode(odd.OddMove move, MCTlayer layer){
		numSim = 0;
		rawScore = 0;
		this.move = move;
		currentLayer = layer;
	}
public void update(int i){
	this.numSim ++;
	this.rawScore = this.rawScore + i;
	this.UCTscore = this.rawScore;
	currentLayer.popOffTheTopTest();
	currentLayer.add(this);
}
public double getUCTscore(){
	return UCTscore;
}
public odd.OddMove getMove(){
	return this.move;
}
public void setNext(MCTlayer next){
	this.nextLayer = next;
}
public MCTlayer getNextLayer(){
	return nextLayer;
}
public MCTlayer getLayer(){
	return currentLayer;
}
public int compareTo(MCTnode node){
	if (this.getUCTscore() < node.getUCTscore()) return +1;
	if (this.getUCTscore() > node.getUCTscore()) return -1;
	else return 0;
}
public int getNumSim(){
	return (int) this.numSim;
}
}
