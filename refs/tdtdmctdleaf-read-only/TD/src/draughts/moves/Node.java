package draughts.moves;

import java.util.Arrays;

import draughts.CheckerModel;

public class Node {

	private final CheckerModel[][] board;

	public Node(CheckerModel[][] board) {
		this.board = BoardUtils.makeCopy(board);
	}

	public CheckerModel[][] getBoard() {
		return board;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (CheckerModel[] arr : board) {
			result = prime * result + Arrays.hashCode(arr);
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		for (int i = 0; i < 10; i++) {
			if (!Arrays.equals(board[i], other.board[i])) {
				return false;
			}
		}
		return true;
	}
}
