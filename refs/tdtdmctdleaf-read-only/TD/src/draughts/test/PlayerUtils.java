package draughts.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import draughts.ITD;
import draughts.NullITD;
import draughts.TDLambda;
import draughts.TDLeaf;
import draughts.TDMC;

public class PlayerUtils {

	private static final double a = 99;
	private static final double b = 0.027;

	private static final double DEFAULT_ALPHA = 0.0001;
	private static final double DEFAULT_GAMMA = 1;
	private static final double DEFAULT_LAMBDA = 0.9;

	public enum PlayerKind {
		TD, TD_LEAF, TDMC, TDMCSimple, NO_LEARNING;
	}

	public static PlayerKind getKind(int which) {
		switch (which) {
		case 0:
			return PlayerKind.TD;
		case 1:
			return PlayerKind.TD_LEAF;
		case 2:
			return PlayerKind.TDMC;
		case 3:
			return PlayerKind.TDMCSimple;
		case 4:
			return PlayerKind.NO_LEARNING;
		default:
			throw new IllegalArgumentException();
		}
	}

	public static ITD loadPlayer(String filePath, PlayerKind kind)
			throws IOException {
		return loadPlayer(filePath, kind, DEFAULT_ALPHA, DEFAULT_GAMMA,
				DEFAULT_LAMBDA);
	}

	public static ITD loadPlayer(String filePath, PlayerKind kind,
			double alpha, double gamma, double lambda) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String line = in.readLine();
			String[] tokens = line.split(" ");
			if (tokens.length != 12) {
				throw new IOException(
						"wrong file format: expected 8 weights but got "
								+ tokens.length);
			}
			double[] weights = new double[12];
			for (int i = 0; i < 12; i++) {
				try {
					weights[i] = Double.parseDouble(tokens[i]);
				} catch (NumberFormatException e) {
					throw new IOException("wrong file format: " + tokens[i]
							+ " is not a valid double value");
				}
			}
			switch (kind) {
			case TD:
				return new TDLambda(weights, a, b, alpha, gamma, lambda, false);
			case TD_LEAF:
				return new TDLeaf(weights, a, b, alpha, gamma, lambda);
			case TDMC:
				return new TDMC(weights, gamma, lambda, alpha, a, b);
			case TDMCSimple:
				return new TDLambda(weights, a, b, alpha, gamma, lambda, true);
			case NO_LEARNING:
				return new NullITD(weights, a, b);
			default:
				throw new IllegalArgumentException(kind
						+ " is not a valid learining algorithm");
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static ITD createRandomPlayer(PlayerKind kind, double alpha,
			double gamma, double lambda) {
		Random rand = new Random();
		double[] weights = new double[12];
		for (int i = 0; i < 12; i++) {
			weights[i] = rand.nextDouble() * 10 - 5;
		}
		switch (kind) {
		case TD:
			return new TDLambda(weights, a, b, alpha, gamma, lambda, false);
		case TD_LEAF:
			return new TDLeaf(weights, a, b, alpha, gamma, lambda);
		case TDMC:
			return new TDMC(weights, gamma, lambda, alpha, a, b);
		case TDMCSimple:
			return new TDLambda(weights, a, b, alpha, gamma, lambda, true);
		case NO_LEARNING:
			return new NullITD(weights, a, b);
		default:
			throw new IllegalArgumentException(kind
					+ " is not a valid learining algorithm");
		}
	}

	public static ITD createRandomPlayer(PlayerKind kind) {
		return createRandomPlayer(kind, DEFAULT_ALPHA, DEFAULT_GAMMA,
				DEFAULT_LAMBDA);
	}

	public static void savePlayer(ITD player, String filePath)
			throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filePath));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < player.getWeigths().length; i++) {
				sb.append(player.getWeigths()[i]);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			out.write(sb.toString());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
