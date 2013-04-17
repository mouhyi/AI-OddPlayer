package draughts.moves;

import java.util.List;

import draughts.Checker;
import draughts.CheckerModel;
import draughts.MoveMessage;
import draughts.Sort;

public class BoardUtils {

	public static final int BOARD_SIZE = 10;

	public static CheckerModel[][] makeCopy(CheckerModel[][] board) {

		CheckerModel[][] result = new CheckerModel[BOARD_SIZE][];
		for (int i = 0; i < BOARD_SIZE; i++) {
			CheckerModel[] temp = new CheckerModel[BOARD_SIZE];
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] == null) {
					temp[j] = null;
				} else {
					temp[j] = new CheckerModel(board[i][j]);
				}
			}
			result[i] = temp;
		}
		return result;
	}

	public static CheckerModel[][] fromChecker(Checker[][] board) {
		CheckerModel[][] result = new CheckerModel[BOARD_SIZE][];
		for (int i = 0; i < BOARD_SIZE; i++) {
			CheckerModel[] temp = new CheckerModel[BOARD_SIZE];
			for (int j = 0; j < BOARD_SIZE; j++) {

				Sort kind = board[i][j].getKind();
				if (Sort.blankBlack.equals(kind) || Sort.blankWhite.equals(kind)) {
					temp[j] = null;
				} else {
					temp[j] = new CheckerModel(board[i][j].getI(), board[i][j].getJ(), board[i][j].getKind());
				}
			}
			result[i] = temp;
		}
		return result;
	}

	public static CheckerModel[][] performMoves(CheckerModel[][] board, List<MoveMessage> moves) {
		CheckerModel[][] result = makeCopy(board);
		for (MoveMessage mm : moves) {
			int dx = Math.abs(mm.getFirst().getI() - mm.getSecond().getI());
			int dy = Math.abs(mm.getFirst().getJ() - mm.getSecond().getJ());
			int directionX = (mm.getSecond().getI() - mm.getFirst().getI()) / dx;
			int directionY = (mm.getSecond().getJ() - mm.getFirst().getJ()) / dy;
			if (dx == dy && dx > 1) {
				for (int i = 1; i < dx; i++) {
					int iIdx = mm.getFirst().getI() + directionX * i;
					int jIdx = mm.getFirst().getJ() + directionY * i;
					// result[iIdx][jIdx].setKind(Sort.blankBlack);
					result[iIdx][jIdx] = null;
				}
				// result[mm.getFirst().getI()][mm.getFirst().getJ()].setKind(Sort.blankBlack);
				result[mm.getFirst().getI()][mm.getFirst().getJ()] = null;
				result[mm.getSecond().getI()][mm.getSecond().getJ()] = new CheckerModel(mm.getSecond().getI(),
						mm.getSecond().getJ(), mm.getFirst().getKind());
			} else if (dx == 1 && dy == 1) {
				result[mm.getFirst().getI()][mm.getFirst().getJ()] = null;
				result[mm.getSecond().getI()][mm.getSecond().getJ()] = new CheckerModel(mm.getSecond().getI(),
						mm.getSecond().getJ(), mm.getFirst().getKind());
			} else {
				throw new RuntimeException("first [i = " + mm.getFirst().getI() + ", j = " + mm.getFirst().getJ()
						+ "] second [i = " + mm.getSecond().getI() + ", j = " + mm.getSecond().getJ() + "]");
			}
			if (mm.isEndsTurn()) {
				if (mm.getFirst().getKind().equals(Sort.fullWhite) && mm.getSecond().getI() == 0) {
					result[mm.getSecond().getI()][mm.getSecond().getJ()].setKind(Sort.queenWhite);
				}
				if (mm.getFirst().getKind().equals(Sort.fullBlack) && mm.getSecond().getI() == 9) {
					result[mm.getSecond().getI()][mm.getSecond().getJ()].setKind(Sort.queenBlack);
				}
			}
		}
		return result;
	}

	public static boolean isBeating(List<MoveMessage> list, CheckerModel[][] board) {
		MoveMessage mm = list.get(0);
		int dx = Math.abs(mm.getFirst().getI() - mm.getSecond().getI());
		int dy = Math.abs(mm.getFirst().getJ() - mm.getSecond().getJ());
		if (Math.abs(dx) == 1) {
			return false;
		}
		int directionX = (mm.getSecond().getI() - mm.getFirst().getI()) / dx;
		int directionY = (mm.getSecond().getJ() - mm.getFirst().getJ()) / dy;
		for (int i = 1; i < dx; i++) {
			int iIdx = mm.getFirst().getI() + directionX * i;
			int jIdx = mm.getFirst().getJ() + directionY * i;
			if (board[iIdx][jIdx] != null) {
				return true;
			}
		}
		return false;
	}
}
