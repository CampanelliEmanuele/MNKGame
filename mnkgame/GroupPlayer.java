package mnkgame;

import java.util.Random;

public class GroupPlayer implements MNKPlayer{
	
	private MNKBoard B;
	
	private MNKGameState myWin;

	private MNKGameState yourWin;
	private int TIMEOUT;
	private Random rand;

	public GroupPlayer(){}

	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_sec){
	
		rand = new Random(System.currentTimeMillis());
		B = new MNKBoard(M,N,K);

		//myWin: vittoria di questo giocatore
		myWin = first ? MNKGameState.WINP1 : MNKGameState.WINP2;
		
		//Vittoria dell'avversario
		yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;	
		TIMEOUT = timeout_in_sec;
	}

	/*
	 * Deve ritornare la cella in cui mettere il segno
	 */
	public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC){
		System.out.println(FC[0].hashCode());

		//se rimane una singola mossa da fare
		if(FC.length == 1)
			return FC[0];

		return FC[0];	
	}

	public String playerName(){
		return "";
	}

	public static void main (String[] args) {
		System.out.println("lol");
	}
}
