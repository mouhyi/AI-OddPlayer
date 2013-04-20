package odd;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;
import java.util.LinkedList;
import java.util.Random;

public class Guatham extends Player	{
	Random R = new Random();
	//Node head;
	int myID;	
	int[] stats = new int[130];
	public Guatham()
	{
		super("Guatham");
	}
	
	/*@Override	
	public void gameStarted(String msg)
	{
		
		//initialize stuff before the game 
	}*/

	public OddMove runSimulation(OddBoard board)
	{
		LinkedList<OddMove> validMovesA = board.getValidMoves();
		//how best to increase number of simulations
		int nSim =20*(240/validMovesA.size());
		
		for(int i=0;i<nSim;i++)
		{
			int k=0;	
			for(OddMove o : validMovesA)
			{
				OddBoard board1 = new OddBoard(board);
				//board 1 changes
				board1.move(o);
				//Node n = new Node();
				//n.setParent(null);
				//n.setBoard(board1);
				while(board1.getValidMoves().size()>0) //while you are not at the end of the game
				{
					LinkedList<OddMove> validMoves = board1.getValidMoves();
//					System.out.println("SIZE IS:"+validMoves.size());
					OddMove selected  = validMoves.get(R.nextInt(validMoves.size()));
					board1.move(selected);
					//Node n1 = new Node();
					//n1.setBoard(board1);
					//n1.setParent(n);
					//n = n1;			
				}
				board1.determineWinner();
				int winner = board1.getWinner();
				if(winner == myID) //if you won by making this move
				{
					stats[k] +=1;
				}
				
				
				//this section will be used if you want to store data along all of the random paths, which may be useful
				/*while(n.getParent() != null)
				{
					if(winner == playerID) //i.e. if you won the simulation
					{
						n.setData(++n.getData());
						n.setCount(++n.getCount());
					}
					else
					{
						n.setCount(++n.getCount());
					}
					n = n.getParent();		
				}*/
				k++;
			}
		}
		//go through the stats, and pick the best
		int max= 0;
		int maxpos = 0;
		for(int j=0;j<validMovesA.size();j++)
		{
			if(stats[j]>=max)
			{
				max = stats[j];
				maxpos = j;
			}
																		
		}
		return validMovesA.get(maxpos);
	}
	@Override
	public Move chooseMove(Board board)
	{
		OddBoard pb = (OddBoard)board;
		myID = this.getColor();
	        return runSimulation(pb);
	}	
}
