package draughts.test;

import java.io.BufferedWriter;
import java.io.IOException;

import draughts.ITD;
import draughts.test.PlayerUtils.PlayerKind;

public class HeadlessTraining {

	public interface OpponentCreateStrategy {
		ITD getOpponent();
	}

	public static final OpponentCreateStrategy RANDOM_OPPONENT = new OpponentCreateStrategy() {
		public ITD getOpponent() {
			return PlayerUtils.createRandomPlayer(PlayerKind.NO_LEARNING);
		}
	};

	public void train(ITD trainedPlayer, int numGames, BufferedWriter out,
			OpponentCreateStrategy strategy, int depth) throws IOException {
		HeadlessGame headlessGame = new HeadlessGame();

		int trainedPlayerWinsCounter = 0;
		int drawCounter = 0;

		for (int i = 0; i < numGames; i++) {
			ITD opponent = strategy.getOpponent();
			ITD whitePlayer = null, blackPlayer = null;
			boolean trainingWhite = false;
			if (i % 2 == 0) {
				trainingWhite = true;
				whitePlayer = trainedPlayer;
				blackPlayer = opponent;
			} else {
				trainingWhite = false;
				whitePlayer = opponent;
				blackPlayer = trainedPlayer;
			}
			Boolean whiteWins = headlessGame.playGame(whitePlayer, blackPlayer,
					trainingWhite, i, depth);
			if (whiteWins == null) {
				drawCounter++;
				out.write(i + ";" + trainedPlayerWinsCounter + ";"
						+ drawCounter + "\n");
			} else if ((trainingWhite && whiteWins)
					|| (!trainingWhite && !whiteWins)) {
				trainedPlayerWinsCounter++;
				out.write(i + ";" + trainedPlayerWinsCounter + ";"
						+ drawCounter + "\n");
			} else {
				out.write(i + ";" + trainedPlayerWinsCounter + ";"
						+ drawCounter + "\n");
				/*
				 * out.write(" Wektor po nauce: "); ITD playingPlayer =
				 * trainingWhite ? whitePlayer : blackPlayer; for (int j = 0; j
				 * < playingPlayer.getWeigths().length; j++) {
				 * out.write(String.valueOf(playingPlayer.getWeigths()[j]));
				 * out.write(" "); }
				 */
			}
			out.flush();
		}

		out.write("\nGracz uczacy sie wygral " + trainedPlayerWinsCounter
				+ " i zremisowal " + drawCounter + " z " + numGames + " gier.");

	}

	public void train(ITD trainedPlayer, int numGames, BufferedWriter out,
			int depth) throws IOException {
		train(trainedPlayer, numGames, out, RANDOM_OPPONENT, depth);
	}
}
