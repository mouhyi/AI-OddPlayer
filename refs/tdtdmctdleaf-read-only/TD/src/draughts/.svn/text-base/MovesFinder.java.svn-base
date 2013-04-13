package draughts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import draughts.moves.BoardUtils;

public class MovesFinder {

	private static final int BOARD_SIZE = 10;
	private final CheckerModel[][] m_board;
	private final Author author;

	/**
	 * Creates a new instance of MovesFinder which will return possible moves for specified player in specified board
	 * position.
	 * 
	 * @param board
	 *            checkers positions. This class makes a copy of constructors argument so <code>board</code> will not be
	 *            modified by it and can be kept by the caller
	 * @param author
	 *            author equal to <code>Author.owner</code> means that current player is playing white, otherwise
	 *            current player is playing black
	 */
	public MovesFinder(CheckerModel[][] board, Author author) {
		if (board == null || author == null) {
			throw new IllegalArgumentException();
		}
		this.m_board = BoardUtils.makeCopy(board);
		this.author = author;
	}

	/**
	 * Returns set of possible moves in given board situation. Each element of the set describes one sequence of moves.
	 * A sequence may consist of one or more single moves. A sequence consists of more than one move if and only if each
	 * of its moves is a beating. This method implements the following checkers rules:
	 * <ul>
	 * <li>beatings are obligatory
	 * <li>if there is more than one sequence of beatings, sequence with most beatings must be chosen
	 * <li>if several sequences of beating are tied for most number of beatings, any of them may be chosen
	 * </ul>
	 * If there are no legal moves for a player in given position, this method returns an empty set.
	 * 
	 * @return
	 */
	public Set<List<MoveMessage>> getLegalMoves() {
		return getMoves(true, true, new LongestResultsStrategy());
	}

	public Set<List<MoveMessage>> getAllNonBeatings() {
		return getMoves(true, false, new AllResultsStrategy());
	}

	public Set<List<MoveMessage>> getAllBeatings() {
		return getMoves(false, true, new AllResultsStrategy());
	}

	public Set<List<MoveMessage>> getMoves(boolean lookForNonBeatings, boolean lookForBeatings,
			UpdateResultSetStrategy strategy) {

		Sort myRegular = Author.owner.equals(author) ? Sort.fullWhite : Sort.fullBlack;
		Sort myQueen = Author.owner.equals(author) ? Sort.queenWhite : Sort.queenBlack;
		EnumSet<Sort> mySorts = EnumSet.of(myQueen, myRegular);

		boolean foundBeating = false;
		int maxLength = 0;
		Set<List<MoveMessage>> result = new HashSet<List<MoveMessage>>();

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				CheckerModel currChecker = m_board[i][j];
				if (currChecker != null && mySorts.contains(currChecker.getKind())) {
					CheckerModel[][] board = BoardUtils.makeCopy(m_board);
					List<CheckerModel> beatings = lookForBeatings ? checkBeating(currChecker, board) : null;
					if ((beatings == null || beatings.isEmpty()) && !foundBeating && lookForNonBeatings) {
						List<CheckerModel> nonBeatings = checkFree(currChecker, board);
						for (CheckerModel nonBeating : nonBeatings) {
							MoveMessage mm = new MoveMessage(new CheckerModel(currChecker),
									new CheckerModel(nonBeating), author, true);
							List<MoveMessage> newResult = new ArrayList<MoveMessage>(1);
							newResult.add(mm);
							maxLength = strategy.updateResult(result, maxLength, newResult);
						}
					} else if (beatings != null && !beatings.isEmpty()) {
						if (!foundBeating) {
							foundBeating = true;
							result.clear();
						}
						for (CheckerModel beating : beatings) {
							MoveMessage mm = new MoveMessage(new CheckerModel(currChecker), new CheckerModel(beating),
									author, false);
							CheckerModel[][] copy = BoardUtils.performMoves(board, Arrays.asList(mm));
							// MovesFinder helper = new MovesFinder(copy, author);
							// Set<List<MoveMessage>> helpResult = helper.getMoves(false, true, strategy);
							CheckerModel checkerAfterMove = new CheckerModel(beating.getI(), beating.getJ(),
									currChecker.getKind());
							Set<List<MoveMessage>> helpResult = getBeatingsForChecker(strategy, copy, checkerAfterMove);
							if (helpResult == null || helpResult.isEmpty()) {
								mm.setEndsTurn(true);
								List<MoveMessage> newResult = new ArrayList<MoveMessage>(1);
								newResult.add(mm);
								maxLength = strategy.updateResult(result, maxLength, newResult);
							} else {
								for (List<MoveMessage> list : helpResult) {
									List<MoveMessage> newResult = new ArrayList<MoveMessage>(list.size() + 1);
									MoveMessage newMessage = new MoveMessage(new CheckerModel(mm.getFirst()),
											new CheckerModel(mm.getSecond()), mm.getMAuthor(), mm.isEndsTurn());
									newResult.add(newMessage);
									newResult.addAll(list);
									maxLength = strategy.updateResult(result, maxLength, newResult);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private Set<List<MoveMessage>> getBeatingsForChecker(UpdateResultSetStrategy strategy, CheckerModel[][] board,
			CheckerModel checker) {
		int maxLength = 0;
		Set<List<MoveMessage>> result = new HashSet<List<MoveMessage>>();
		List<CheckerModel> beatings = checkBeating(checker, board);
		if (beatings != null && !beatings.isEmpty()) {
			for (CheckerModel beating : beatings) {
				MoveMessage mm = new MoveMessage(new CheckerModel(checker), new CheckerModel(beating), author, false);
				CheckerModel[][] copy = BoardUtils.performMoves(board, Arrays.asList(mm));
				CheckerModel checkerAfterMove = new CheckerModel(beating.getI(), beating.getJ(), checker.getKind());
				Set<List<MoveMessage>> helpResult = getBeatingsForChecker(strategy, copy, checkerAfterMove);
				if (helpResult == null || helpResult.isEmpty()) {
					mm.setEndsTurn(true);
					List<MoveMessage> newResult = new ArrayList<MoveMessage>(1);
					newResult.add(mm);
					maxLength = strategy.updateResult(result, maxLength, newResult);
				} else {
					for (List<MoveMessage> list : helpResult) {
						List<MoveMessage> newResult = new ArrayList<MoveMessage>(list.size() + 1);
						MoveMessage newMessage = new MoveMessage(new CheckerModel(mm.getFirst()), new CheckerModel(
								mm.getSecond()), mm.getMAuthor(), mm.isEndsTurn());
						newResult.add(newMessage);
						newResult.addAll(list);
						maxLength = strategy.updateResult(result, maxLength, newResult);
					}
				}
			}
		}
		return result;
	}

	private interface UpdateResultSetStrategy {
		int updateResult(Set<List<MoveMessage>> results, int maxLength, List<MoveMessage> newResult);
	}

	private class AllResultsStrategy implements UpdateResultSetStrategy {
		public int updateResult(Set<List<MoveMessage>> results, int maxLength, List<MoveMessage> newResult) {
			results.add(newResult);
			return Math.max(maxLength, newResult.size());
		}
	}

	private class LongestResultsStrategy implements UpdateResultSetStrategy {
		public int updateResult(Set<List<MoveMessage>> results, int maxLength, List<MoveMessage> newResult) {
			if (maxLength > newResult.size()) {
				return maxLength;
			}
			if (maxLength == newResult.size()) {
				results.add(newResult);
				return maxLength;
			}
			// we found a longer result (longer beating sequence)
			results.clear();
			results.add(newResult);
			// return results.size();
			return newResult.size();
		}
	}

	// private Checker[][] makeCopy(Checker[][] board) {
	// Checker[][] result = new Checker[BOARD_SIZE][];
	// for (int i = 0; i < BOARD_SIZE; i++) {
	// Checker[] temp = new Checker[BOARD_SIZE];
	// for (int j = 0; j < BOARD_SIZE; j++) {
	// temp[j] = new Checker(board[i][j]);
	// }
	// result[i] = temp;
	// }
	// return result;
	// }

	private ArrayList<CheckerModel> checkBeating(CheckerModel jb, CheckerModel[][] board) {
		ArrayList<CheckerModel> beatingMoves = new ArrayList<CheckerModel>();
		int i = jb.getI();
		int j = jb.getJ();
		Sort opponent1, opponent2, supporter1, supporter2;

		if (jb.getKind() == Sort.fullWhite || jb.getKind() == Sort.queenWhite) {
			opponent1 = Sort.fullBlack;
			opponent2 = Sort.queenBlack;
			supporter1 = Sort.fullWhite;
			supporter2 = Sort.queenWhite;
		} else {
			opponent1 = Sort.fullWhite;
			opponent2 = Sort.queenWhite;
			supporter1 = Sort.fullBlack;
			supporter2 = Sort.queenBlack;
		}

		EnumSet<Sort> opponents = EnumSet.of(opponent1, opponent2);
		EnumSet<Sort> supporters = EnumSet.of(supporter1, supporter2);

		if (jb.getKind() == Sort.queenWhite || jb.getKind() == Sort.queenBlack) {
			for (int p = 2; p < 10; p++) {
				if (i + p < 10 && j + p < 10) {
					if (board[i + p - 1][j + p - 1] != null
							&& supporters.contains(board[i + p - 1][j + p - 1].getKind())) {
						break;
					}
					if (board[i + p - 1][j + p - 1] != null
							&& supporters.contains(board[i + p - 1][j + p - 1].getKind())) {
						if (board[i + p][j + p] != null) {
							break;
						}
						beatingMoves.add(new CheckerModel(i + p, j + p, Sort.blankBlack));
						int q = 1;
						while (i + p + q < 10 && j + p + q < 10 && board[i + p + q][j + p + q] == null) {
							beatingMoves.add(new CheckerModel(i + p + q, j + p + q, Sort.blankBlack));
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i + p < 10 && j - p >= 0) {
					if (board[i + p - 1][j - p + 1] != null
							&& supporters.contains(board[i + p - 1][j - p + 1].getKind())) {
						break;
					}
					if (board[i + p - 1][j - p + 1] != null
							&& opponents.contains(board[i + p - 1][j - p + 1].getKind())) {
						if (board[i + p][j - p] != null) {
							break;
						}
						beatingMoves.add(new CheckerModel(i + p, j - p, Sort.blankBlack));
						int q = 1;
						while (i + p + q < 10 && j - p - q >= 0 && board[i + p + q][j - p - q] == null) {
							beatingMoves.add(new CheckerModel(i + p + q, j - p - q, Sort.blankBlack));
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i - p >= 0 && j + p < 10) {
					if (board[i - p + 1][j + p - 1] != null
							&& supporters.contains(board[i - p + 1][j + p - 1].getKind())) {
						break;
					}
					if (board[i - p + 1][j + p - 1] != null
							&& opponents.contains(board[i - p + 1][j + p - 1].getKind())) {
						if (board[i - p][j + p] != null) {
							break;
						}
						beatingMoves.add(new CheckerModel(i - p, j + p, Sort.blankBlack));
						int q = 1;
						while (i - p - q >= 0 && j + p + q < 10 && board[i - p - q][j + p + q] == null) {
							beatingMoves.add(new CheckerModel(i - p - q, j + p + q, Sort.blankBlack));
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i - p >= 0 && j - p >= 0) {
					if (board[i - p + 1][j - p + 1] != null
							&& supporters.contains(board[i - p + 1][j - p + 1].getKind())) {
						break;
					}
					if (board[i - p + 1][j - p + 1] != null
							&& opponents.contains(board[i - p + 1][j - p + 1].getKind())) {
						if (board[i - p][j - p] != null) {
							break;
						}
						beatingMoves.add(new CheckerModel(i - p, j - p, Sort.blankBlack));
						int q = 1;
						while (i - p - q >= 0 && j - p - q >= 0 && board[i - p - q][j - p - q] == null) {
							beatingMoves.add(new CheckerModel(i - p - q, j - p - q, Sort.blankBlack));
							q++;
						}
						break;
					}
				}
			}
		} else {
			if (i - 2 >= 0 && j - 2 >= 0) {
				if (board[i - 1][j - 1] != null && opponents.contains(board[i - 1][j - 1].getKind())
						&& board[i - 2][j - 2] == null) {
					beatingMoves.add(new CheckerModel(i - 2, j - 2, Sort.blankBlack));
				}

			}
			if (i - 2 >= 0 && j + 2 < 10) {
				if (board[i - 1][j + 1] != null && opponents.contains(board[i - 1][j + 1].getKind())
						&& board[i - 2][j + 2] == null) {
					beatingMoves.add(new CheckerModel(i - 2, j + 2, Sort.blankBlack));
				}

			}
			if (i + 2 < 10 && j - 2 >= 0) {
				if (board[i + 1][j - 1] != null && opponents.contains(board[i + 1][j - 1].getKind())
						&& board[i + 2][j - 2] == null) {
					beatingMoves.add(new CheckerModel(i + 2, j - 2, Sort.blankBlack));
				}

			}
			if (i + 2 < 10 && j + 2 < 10) {
				if (board[i + 1][j + 1] != null && opponents.contains(board[i + 1][j + 1].getKind())
						&& board[i + 2][j + 2] == null) {
					beatingMoves.add(new CheckerModel(i + 2, j + 2, Sort.blankBlack));
				}

			}
		}
		return beatingMoves;
	}

	/**
	 * Returns a list of board fields on which specified checker can move without beating opponents checker.
	 * 
	 * @param jb
	 * @return
	 */
	private List<CheckerModel> checkFree(CheckerModel jb, CheckerModel[][] board) {

		List<CheckerModel> freeMoves = new ArrayList<CheckerModel>();
		// freeMoves.clear();
		int i = jb.getI();
		int j = jb.getJ();
		if (jb.getKind() == Sort.fullWhite) {
			if (i - 1 >= 0 && j - 1 >= 0) {
				if (board[i - 1][j - 1] == null) {
					freeMoves.add(new CheckerModel(i - 1, j - 1, Sort.blankBlack));
				}
			}
			if (i - 1 >= 0 && j + 1 < 10) {
				if (board[i - 1][j + 1] == null) {
					freeMoves.add(new CheckerModel(i - 1, j + 1, Sort.blankBlack));
				}
			}
		} else if (jb.getKind() == Sort.fullBlack) {
			if (i + 1 < 10 && j - 1 >= 0) {
				if (board[i + 1][j - 1] == null) {
					freeMoves.add(new CheckerModel(i + 1, j - 1, Sort.blankBlack));
				}
			}
			if (i + 1 < 10 && j + 1 < 10) {
				if (board[i + 1][j + 1] == null) {
					freeMoves.add(new CheckerModel(i + 1, j + 1, Sort.blankBlack));
				}
			}
		} else if (jb.getKind() == Sort.queenWhite || jb.getKind() == Sort.queenBlack) {
			for (int p = 1; p < 10; p++) {
				if (i + p < 10 && j + p < 10) {
					if (board[i + p][j + p] == null) {
						freeMoves.add(new CheckerModel(i + p, j + p, Sort.blankBlack));
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i + p < 10 && j - p >= 0) {
					if (board[i + p][j - p] == null) {
						freeMoves.add(new CheckerModel(i + p, j - p, Sort.blankBlack));
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i - p >= 0 && j + p < 10) {
					if (board[i - p][j + p] == null) {
						freeMoves.add(new CheckerModel(i - p, j + p, Sort.blankBlack));
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i - p >= 0 && j - p >= 0) {
					if (board[i - p][j - p] == null) {
						freeMoves.add(new CheckerModel(i - p, j - p, Sort.blankBlack));
					} else {
						break;
					}
				}
			}
		}
		return freeMoves;
	}
}
