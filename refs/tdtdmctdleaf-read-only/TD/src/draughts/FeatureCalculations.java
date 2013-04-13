package draughts;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FeatureCalculations {

	private CheckerModel[][] checkersArray;
	// private Player player;
	private Author author;

	private Set<List<MoveMessage>> myBeatableMoves;
	private Set<List<MoveMessage>> oppBeatableMoves;
	private Set<List<MoveMessage>> myNonBeatableMoves;
	private Set<List<MoveMessage>> oppNonBeatableMoves;

	public FeatureCalculations(CheckerModel[][] checkersArray, Player player) {
		this(checkersArray, player.getMAuthor());
	}

	public FeatureCalculations(CheckerModel[][] checkersArray, Author author) {
		// this.player = player;
		this.author = author;
		this.checkersArray = checkersArray;

		MovesFinder myMovesFinder = new MovesFinder(checkersArray, author);
		this.myBeatableMoves = myMovesFinder.getAllBeatings();
		this.myNonBeatableMoves = myMovesFinder.getAllNonBeatings();
		MovesFinder oppMovesFinder = new MovesFinder(checkersArray, author == Author.owner ? Author.opponent
				: Author.owner);
		this.oppBeatableMoves = oppMovesFinder.getAllBeatings();
		this.oppNonBeatableMoves = oppMovesFinder.getAllNonBeatings();
	}

	public int getMyPossibleMovesCount() {
		return myBeatableMoves.size() + myNonBeatableMoves.size();
	}

	public int getOppPossibleMovesCount() {
		return oppBeatableMoves.size() + oppNonBeatableMoves.size();
	}

	public int getMyPossibleBeatsCount() {
		return myBeatableMoves.size();
	}

	public int getOppPossibleBeatsCount() {
		return oppBeatableMoves.size();
	}

	public int getLongestBeatLength(Set<List<MoveMessage>> possibleMoves) {
		int max = 0;

		Iterator<List<MoveMessage>> it = possibleMoves.iterator();

		while (it.hasNext()) {
			List<MoveMessage> actual = it.next();
			max = (actual.size() > max) ? actual.size() : max;
		}
		return max;
	}

	public int getPossibleQueensCount(Set<List<MoveMessage>> moves) {
		int queensCount = 0;
		Iterator<List<MoveMessage>> it = moves.iterator();

		int queensRow = 0;
		if (author == Author.opponent)
			queensRow = 9;

		while (it.hasNext()) {
			List<MoveMessage> cur = it.next();
			CheckerModel lastChecker = cur.get(cur.size() - 1).getSecond();
			if (lastChecker.getI() == queensRow)
				queensCount++;
		}

		return queensCount;
	}

	public double[] getEvaluationFunctionFeatures() {

		double[] features = new double[12];
		int iAmWhite = author == Author.owner ? 1 : 0;

		for (int i = 0; i < 12; i++)
			features[i] = 0;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (checkersArray[i][j] == null) {
					continue;
				}
				if (checkersArray[i][j].getKind() == Sort.fullBlack) {
					features[1-iAmWhite]++;
				} else if (checkersArray[i][j].getKind() == Sort.fullWhite) {
					features[iAmWhite]++;
				} else if (checkersArray[i][j].getKind() == Sort.queenBlack) {
					features[3- iAmWhite]++;
				} else if (checkersArray[i][j].getKind() == Sort.queenWhite) {
					features[2+ iAmWhite]++;
				}
			}
		}

		features[4] = getOppPossibleBeatsCount();
		features[5] = getMyPossibleBeatsCount();
		features[6] = getLongestBeatLength(oppBeatableMoves);
		features[7] = getLongestBeatLength(myBeatableMoves);
		features[8] = getOppPossibleMovesCount();
		features[9] = getMyPossibleMovesCount();
		features[10] = getPossibleQueensCount(oppBeatableMoves) + getPossibleQueensCount(oppNonBeatableMoves);
		features[11] = getPossibleQueensCount(myBeatableMoves) + getPossibleQueensCount(myNonBeatableMoves);

		return features;
	}

}
