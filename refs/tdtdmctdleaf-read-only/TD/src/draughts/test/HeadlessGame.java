package draughts.test;

import java.util.List;

import draughts.Author;
import draughts.Checker;
import draughts.CheckerModel;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.Player;
import draughts.Sort;
import draughts.moves.BoardUtils;
import draughts.moves.Mtd;

public class HeadlessGame {

	private static final int MAX_NONBEATINGS = 50;

	private Checker[][] createStartingBoard() {
		Checker[][] checkersArray = new Checker[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				checkersArray[i][j] = new Checker(i, j);
				if ((i + j) % 2 != 0) {
					if (i < 4) {
						checkersArray[i][j].setKind(Sort.fullBlack);
					} else if (i >= 6) {
						checkersArray[i][j].setKind(Sort.fullWhite);
					} else {
						checkersArray[i][j].setKind(Sort.blankBlack);
					}
				} else {
					checkersArray[i][j].setKind(Sort.blankWhite);
				}
				// setCheckerIcon(checkersArray[i][j],
				// checkersArray[i][j].getKind());
			}
		}
		return checkersArray;
	}

	public Boolean playGame(ITD whitePlayer, ITD blackPlayer, boolean learningWhite, int i, int depth) {
		Checker[][] bboard = createStartingBoard();
		CheckerModel[][] board = BoardUtils.fromChecker(bboard);

		Mtd whiteMtd = new Mtd(Author.owner);
		Mtd blackMtd = new Mtd(Author.opponent);
		Player white = new Player("white bot", Author.owner);
		Player black = new Player("black bot", Author.opponent);

		boolean finished = false;
		boolean whiteWins = false;
		boolean draw = false;
		int noBeatingsCount = 0;
		while (!finished) {
			List<MoveMessage> list = whiteMtd.getBestMove(board, 0, depth, white, whitePlayer);
			if (list == null) {
				finished = true;
				whiteWins = false;
			} else {
				if (BoardUtils.isBeating(list, board)) {
					noBeatingsCount = 0;
				} else {
					noBeatingsCount++;
					finished = noBeatingsCount >= MAX_NONBEATINGS;
					draw = noBeatingsCount >= MAX_NONBEATINGS;
				}
				board = BoardUtils.performMoves(board, list);
			}
			if (!finished) {
				list = blackMtd.getBestMove(board, 0, depth, black, blackPlayer);
				if (list == null) {
					finished = true;
					whiteWins = true;
				} else {
					if (BoardUtils.isBeating(list, board)) {
						noBeatingsCount = 0;
					} else {
						noBeatingsCount++;
						finished = noBeatingsCount >= MAX_NONBEATINGS;
						draw = noBeatingsCount >= MAX_NONBEATINGS;
					}
					board = BoardUtils.performMoves(board, list);
				}
			}
		}
		if (whiteWins) {
			blackPlayer.updateWeights(blackMtd.getGameData());
		} else if (!draw) {
			whitePlayer.updateWeights(whiteMtd.getGameData());
		}

		// if ((whiteWins || draw) && !learningWhite) {
		// blackPlayer.updateWeights(blackMtd.getGameData());
		// } else if ((!whiteWins || draw) && learningWhite) {
		// whitePlayer.updateWeights(whiteMtd.getGameData());
		// }

		return draw ? null : whiteWins;

	}

	public Boolean playGame(ITD whitePlayer, ITD blackPlayer, boolean learningWhite, int i) {
		return playGame(whitePlayer, blackPlayer, learningWhite, i, 2);
	}
}
