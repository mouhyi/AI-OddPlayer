package draughts.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import draughts.ITD;
import draughts.test.HeadlessTraining.OpponentCreateStrategy;
import draughts.test.PlayerUtils.PlayerKind;

public class TrainingTest {

	private int depth;
	private double alpha, gamma, lambda;
	private int gamesCount;
	private String logFilePath;
	// private String firstPlayerFile;
	// private String secondPlayerFile;
	private String firstPlayerOutFile;
	private String secondPlayerOutFile;

	// alpha, gamma, lambda
	public TrainingTest(String[] args) throws IOException {
		depth = Integer.parseInt(args[0]);
		alpha = Double.parseDouble(args[1]);
		gamma = Double.parseDouble(args[2]);
		lambda = Double.parseDouble(args[3]);
		gamesCount = Integer.parseInt(args[4]);
		logFilePath = args[5];

		ITD firstPlayer = null;

		if (args.length > 8) {
			PlayerKind firstKind = PlayerUtils.getKind(Integer
					.parseInt(args[6]));
			firstPlayer = PlayerUtils.loadPlayer(args[8], firstKind, alpha,
					gamma, lambda);
			// firstPlayerFile = args[8];
			firstPlayerOutFile = args[7];
		} else {
			PlayerKind firstKind = PlayerUtils.getKind(Integer
					.parseInt(args[6]));
			firstPlayer = PlayerUtils.createRandomPlayer(firstKind, alpha,
					gamma, lambda);
			firstPlayerOutFile = args[7];
		}

		OpponentCreateStrategy strategy = null;
		boolean saveSecondPlayer = false;
		ITD secondPlayer = null;
		if (args.length > 10) {
			// secondPlayerFile = args[11];
			secondPlayerOutFile = args[10];
			PlayerKind secondKind = PlayerUtils.getKind(Integer
					.parseInt(args[9]));
			secondPlayer = PlayerUtils.loadPlayer(args[11], secondKind, alpha,
					gamma, lambda);
			final ITD p = secondPlayer;
			strategy = new OpponentCreateStrategy() {
				public ITD getOpponent() {
					return p;
				}
			};
			saveSecondPlayer = true;
		} else {
			strategy = HeadlessTraining.RANDOM_OPPONENT;
		}

		HeadlessTraining training = new HeadlessTraining();

		File file = new File(logFilePath);
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		training.train(firstPlayer, gamesCount, out, strategy, depth);
		PlayerUtils.savePlayer(firstPlayer, firstPlayerOutFile);
		if (saveSecondPlayer) {
			PlayerUtils.savePlayer(secondPlayer, secondPlayerOutFile);
		}
		out.close();
	}

	public static void main(String[] args) throws Exception {
		new TrainingTest(args);
	}
}
