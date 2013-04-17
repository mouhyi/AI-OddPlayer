package draughts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Plansza gry.
 */
public class BoardPanel extends JPanel {

	private final int GAMES_COUNT=1000;
	private final boolean LEARNING_MODE=true;
	
	private Icons boardIcons = new Icons();
	private boolean chosen;
	private Checker chosenLast;
	private ArrayList<Checker> freeMoves = new ArrayList<Checker>();
	private ArrayList<Checker> mustBeFirstChosen = new ArrayList<Checker>();
	private Frame parentFrame;
	private Checker[][] checkersArray = new Checker[10][10];
	private ArrayList<Checker> toBeatList = new ArrayList<Checker>();
	private int whites, blacks;
	private ClickActionListener clickListener = new ClickActionListener();
	private RightClickListener rightClickListener = new RightClickListener();

	/**
	 * Tworzy planszę gry o 32 polach białych i 32 czarnych.
	 */
	public BoardPanel() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				checkersArray[i][j] = new Checker(i, j);
				if ((i + j) % 2 != 0) {
					checkersArray[i][j].setKind(Sort.blankBlack);
				} else {
					checkersArray[i][j].setKind(Sort.blankWhite);
				}
				setCheckerIcon(checkersArray[i][j], checkersArray[i][j].getKind());
			}
		}
		initializeLists();
	}

	/**
	 * Ustawia pionki białe i czarne na początkowych pozycjach.
	 */
	public void setKindsAndIcons() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i >= 0 && i < 4) {
					if ((i + j) % 2 != 0) {
						checkersArray[i][j].setKind(Sort.fullBlack);
					} else {
						checkersArray[i][j].setKind(Sort.blankWhite);
					}
				} else if (i >= 6 && i < 10) {
					if ((i + j) % 2 != 0) {
						checkersArray[i][j].setKind(Sort.fullWhite);
					} else {
						checkersArray[i][j].setKind(Sort.blankWhite);
					}
				} else {
					if ((i + j) % 2 != 0) {
						checkersArray[i][j].setKind(Sort.blankBlack);
					} else {
						checkersArray[i][j].setKind(Sort.blankWhite);
					}
				}
				setCheckerIcon(checkersArray[i][j], checkersArray[i][j].getKind());
			}
		}
		whites = blacks = 20;
		chosen = false;
		chosenLast = null;
	}

	private void initializeLists() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if ((i + j) % 2 != 0) {
					getCheckersArray()[i][j].addActionListener(clickListener);
				}
				getCheckersArray()[i][j].addMouseListener(rightClickListener);
			}
		}
	}

	/**
	 * Jeżeli wystąpiło bicie usuwa z planszy zbite pionki.
	 * 
	 * @param first
	 *            Pole, z którego ruszył się gracz.
	 * @param second
	 *            Pole, na które przesunął się gracz.
	 */
	public void deleteIfBeaten(CheckerModel first, CheckerModel second) {
		int iMax = Math.max(first.getI(), second.getI());
		int jMax = Math.max(first.getJ(), second.getJ());
		int jMin = Math.min(first.getJ(), second.getJ());
		Sort opponent1, opponent2;
		boolean deleted = false;

		if (second.getKind() == Sort.queenWhite) {
			opponent1 = Sort.fullBlack;
			opponent2 = Sort.queenBlack;
		} else {
			opponent1 = Sort.fullWhite;
			opponent2 = Sort.queenWhite;
		}

		if (second.getKind() == Sort.queenBlack || second.getKind() == Sort.queenWhite) {
			if ((first.getI() < second.getI() && first.getJ() < second.getJ())
					|| (first.getI() > second.getI() && first.getJ() > second.getJ())) {
				for (int p = 1; p < Math.abs(second.getI() - first.getI()); p++) {
					if (checkersArray[iMax - p][jMax - p].getKind() == opponent1
							|| (checkersArray[iMax - p][jMax - p].getKind() == opponent2)) {
						checkersArray[iMax - p][jMax - p].setKind(Sort.blankBlack);
						setCheckerIcon(checkersArray[iMax - p][jMax - p], Sort.blankBlack);
						deleted = true;
						break;
					}
				}
			} else {
				for (int p = 1; p <= Math.abs(first.getI() - second.getI()); p++) {
					if (checkersArray[iMax - p][jMin + p].getKind() == opponent1
							|| (checkersArray[iMax - p][jMin + p].getKind() == opponent2)) {
						checkersArray[iMax - p][jMin + p].setKind(Sort.blankBlack);
						setCheckerIcon(checkersArray[iMax - p][jMin + p], Sort.blankBlack);
						deleted = true;
						break;
					}
				}
			}
			if (deleted && second.getKind() == Sort.queenBlack) {
				whites--;
			} else if (deleted) {
				blacks--;
			}
		} else if (Math.abs(first.getI() - second.getI()) == 2 && Math.abs(first.getJ() - second.getJ()) == 2) {
			if (checkersArray[iMax - 1][jMax - 1].getKind() == Sort.fullWhite
					|| checkersArray[iMax - 1][jMax - 1].getKind() == Sort.queenWhite) {
				whites--;
			} else {
				blacks--;
			}
			checkersArray[iMax - 1][jMax - 1].setKind(Sort.blankBlack);
			setCheckerIcon(checkersArray[iMax - 1][jMax - 1], Sort.blankBlack);
			deleted = true;
		}
		if (deleted) {
			if (blacks == 0) {
				JOptionPane.showMessageDialog(null, "Koniec gry. Zwyciężył kolor biały.", "Koniec gry",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (whites == 0) {
				JOptionPane.showMessageDialog(null, "Koniec gry. Zwyciężył kolor czarny.", "Koniec gry",
						JOptionPane.INFORMATION_MESSAGE);
			}
			if (blacks == 0 || whites == 0) {
				finishGame();
			}
		}
	}

	private void finishGame() {
		parentFrame.getChatArea().append("\nAby rozpocząć grę naciśnij Nowa Gra.");
		parentFrame.getChatArea().setCaretPosition(parentFrame.getChatArea().getDocument().getLength());
		parentFrame.setGameIsOn(false);
	}

	/**
	 * Rysuje planszę.
	 * 
	 * @param g
	 *            Kontekst graficzny.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Checker b = getCheckersArray()[i][j];
				b.setBounds(32 * j, 32 * i, 32, 32);
				b.setFocusable(false);
				b.setBorder(null);
				this.add(b);
			}
		}
	}

	/**
	 * Ustawia ikonę pola, w zależności od jego typu.
	 * 
	 * @param b
	 *            Pole.
	 * @param kind
	 *            Typ pola.
	 */
	public void setCheckerIcon(Checker b, Sort kind) {
		if (kind == Sort.blankWhite) {
			b.setIcon(boardIcons.blankWhite);
		} else if (kind == Sort.blankBlack) {
			b.setIcon(boardIcons.blankBlack);
		} else if (kind == Sort.fullBlack) {
			b.setIcon(boardIcons.fullBlack);
		} else if (kind == Sort.fullWhite) {
			b.setIcon(boardIcons.fullWhite);
		} else if (kind == Sort.queenBlack) {
			b.setIcon(boardIcons.queenBlack);
		} else if (kind == Sort.queenWhite) {
			b.setIcon(boardIcons.queenWhite);
		}
	}

	private ArrayList<Checker> checkBeating(Checker jb) {
		ArrayList<Checker> beatingMoves = new ArrayList<Checker>();
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

		if (jb.getKind() == Sort.queenWhite || jb.getKind() == Sort.queenBlack) {
			for (int p = 2; p < 10; p++) {
				if (i + p < 10 && j + p < 10) {
					if (getCheckersArray()[i + p - 1][j + p - 1].getKind() == supporter1
							|| getCheckersArray()[i + p - 1][j + p - 1].getKind() == supporter2) {
						break;
					}
					if (getCheckersArray()[i + p - 1][j + p - 1].getKind() == opponent1
							|| getCheckersArray()[i + p - 1][j + p - 1].getKind() == opponent2) {
						if (getCheckersArray()[i + p][j + p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(getCheckersArray()[i + p][j + p]);
						int q = 1;
						while (i + p + q < 10 && j + p + q < 10
								&& getCheckersArray()[i + p + q][j + p + q].getKind() == Sort.blankBlack) {
							beatingMoves.add(getCheckersArray()[i + p + q][j + p + q]);
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i + p < 10 && j - p >= 0) {
					if (getCheckersArray()[i + p - 1][j - p + 1].getKind() == supporter1
							|| getCheckersArray()[i + p - 1][j - p + 1].getKind() == supporter2) {
						break;
					}
					if (getCheckersArray()[i + p - 1][j - p + 1].getKind() == opponent1
							|| getCheckersArray()[i + p - 1][j - p + 1].getKind() == opponent2) {
						if (getCheckersArray()[i + p][j - p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(getCheckersArray()[i + p][j - p]);
						int q = 1;
						while (i + p + q < 10 && j - p - q >= 0
								&& getCheckersArray()[i + p + q][j - p - q].getKind() == Sort.blankBlack) {
							beatingMoves.add(getCheckersArray()[i + p + q][j - p - q]);
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i - p >= 0 && j + p < 10) {
					if (getCheckersArray()[i - p + 1][j + p - 1].getKind() == supporter1
							|| getCheckersArray()[i - p + 1][j + p - 1].getKind() == supporter2) {
						break;
					}
					if (getCheckersArray()[i - p + 1][j + p - 1].getKind() == opponent1
							|| getCheckersArray()[i - p + 1][j + p - 1].getKind() == opponent2) {
						if (getCheckersArray()[i - p][j + p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(getCheckersArray()[i - p][j + p]);
						int q = 1;
						while (i - p - q >= 0 && j + p + q < 10
								&& getCheckersArray()[i - p - q][j + p + q].getKind() == Sort.blankBlack) {
							beatingMoves.add(getCheckersArray()[i - p - q][j + p + q]);
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i - p >= 0 && j - p >= 0) {
					if (getCheckersArray()[i - p + 1][j - p + 1].getKind() == supporter1
							|| getCheckersArray()[i - p + 1][j - p + 1].getKind() == supporter2) {
						break;
					}
					if (getCheckersArray()[i - p + 1][j - p + 1].getKind() == opponent1
							|| getCheckersArray()[i - p + 1][j - p + 1].getKind() == opponent2) {
						if (getCheckersArray()[i - p][j - p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(getCheckersArray()[i - p][j - p]);
						int q = 1;
						while (i - p - q >= 0 && j - p - q >= 0
								&& getCheckersArray()[i - p - q][j - p - q].getKind() == Sort.blankBlack) {
							beatingMoves.add(getCheckersArray()[i - p - q][j - p - q]);
							q++;
						}
						break;
					}
				}
			}
		} else {
			if (i - 2 >= 0 && j - 2 >= 0) {
				if ((getCheckersArray()[i - 1][j - 1].getKind() == opponent1 || getCheckersArray()[i - 1][j - 1]
						.getKind() == opponent2)
						&& getCheckersArray()[i - 2][j - 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(getCheckersArray()[i - 2][j - 2]);
				}

			}
			if (i - 2 >= 0 && j + 2 < 10) {
				if ((getCheckersArray()[i - 1][j + 1].getKind() == opponent1 || getCheckersArray()[i - 1][j + 1]
						.getKind() == opponent2)
						&& getCheckersArray()[i - 2][j + 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(getCheckersArray()[i - 2][j + 2]);
				}

			}
			if (i + 2 < 10 && j - 2 >= 0) {
				if ((getCheckersArray()[i + 1][j - 1].getKind() == opponent1 || getCheckersArray()[i + 1][j - 1]
						.getKind() == opponent2)
						&& getCheckersArray()[i + 2][j - 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(getCheckersArray()[i + 2][j - 2]);
				}

			}
			if (i + 2 < 10 && j + 2 < 10) {
				if ((getCheckersArray()[i + 1][j + 1].getKind() == opponent1 || getCheckersArray()[i + 1][j + 1]
						.getKind() == opponent2)
						&& getCheckersArray()[i + 2][j + 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(getCheckersArray()[i + 2][j + 2]);
				}

			}
		}
		return beatingMoves;
	}

	private void checkFree(Checker jb) {

		freeMoves.clear();
		int i = jb.getI();
		int j = jb.getJ();
		if (jb.getKind() == Sort.fullWhite) {
			if (i - 1 >= 0 && j - 1 >= 0) {
				if (getCheckersArray()[i - 1][j - 1].getKind() == Sort.blankBlack) {
					freeMoves.add(getCheckersArray()[i - 1][j - 1]);
				}
			}
			if (i - 1 >= 0 && j + 1 < 10) {
				if (getCheckersArray()[i - 1][j + 1].getKind() == Sort.blankBlack) {
					freeMoves.add(getCheckersArray()[i - 1][j + 1]);
				}
			}
		} else if (jb.getKind() == Sort.fullBlack) {
			if (i + 1 < 10 && j - 1 >= 0) {
				if (getCheckersArray()[i + 1][j - 1].getKind() == Sort.blankBlack) {
					freeMoves.add(getCheckersArray()[i + 1][j - 1]);
				}
			}
			if (i + 1 < 10 && j + 1 < 10) {
				if (getCheckersArray()[i + 1][j + 1].getKind() == Sort.blankBlack) {
					freeMoves.add(getCheckersArray()[i + 1][j + 1]);
				}
			}
		} else if (jb.getKind() == Sort.queenWhite || jb.getKind() == Sort.queenBlack) {
			for (int p = 1; p < 10; p++) {
				if (i + p < 10 && j + p < 10) {
					if (getCheckersArray()[i + p][j + p].getKind() == Sort.blankBlack) {
						freeMoves.add(getCheckersArray()[i + p][j + p]);
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i + p < 10 && j - p >= 0) {
					if (getCheckersArray()[i + p][j - p].getKind() == Sort.blankBlack) {
						freeMoves.add(getCheckersArray()[i + p][j - p]);
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i - p >= 0 && j + p < 10) {
					if (getCheckersArray()[i - p][j + p].getKind() == Sort.blankBlack) {
						freeMoves.add(getCheckersArray()[i - p][j + p]);
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i - p >= 0 && j - p >= 0) {
					if (getCheckersArray()[i - p][j - p].getKind() == Sort.blankBlack) {
						freeMoves.add(getCheckersArray()[i - p][j - p]);
					} else {
						break;
					}
				}
			}
		}

	}

	private void findMustBeFirstChosen() {
		mustBeFirstChosen.clear();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if ((i + j) % 2 != 0) {
					if (parentFrame.getMyPlayer().getMAuthor() == Author.owner) {
						if ((getCheckersArray()[i][j].getKind() == Sort.fullWhite || getCheckersArray()[i][j]
								.getKind() == Sort.queenWhite)
								&& checkBeating(getCheckersArray()[i][j]).size() != 0) {
							mustBeFirstChosen.add(getCheckersArray()[i][j]);
						}

					} else {
						if ((getCheckersArray()[i][j].getKind() == Sort.fullBlack || getCheckersArray()[i][j]
								.getKind() == Sort.queenBlack)
								&& checkBeating(getCheckersArray()[i][j]).size() != 0) {
							mustBeFirstChosen.add(getCheckersArray()[i][j]);
						}
					}
				}
			}
		}
	}

	/**
	 * Klasa nasłuchująca kliknięcia gracza w pola planszy.
	 */
	public class ClickActionListener implements ActionListener {

		/**
		 * Sprawdza czy użytkownik chce wykonać dozwolony ruch. Po wykonaniu
		 * ruchu decyduje czy użytkownik zakończył sekwencję ruchów oraz jeśli
		 * wystąpiły bicia usuwa zbite pionki z planszy. Wysyła informację o
		 * ruchu przeciwnikowi.
		 * 
		 * @param e
		 *            Zdarzenie.
		 * @see BoardPanel#deleteIfBeaten(Checker first, Checker second)
		 */
		public void actionPerformed(ActionEvent e) {
			if (parentFrame.isGameIsOn() && parentFrame.isYourTurn() && !parentFrame.isArtificialGame()) {
				Sort yourKind1, yourKind2;
				Checker jb = (Checker) e.getSource();
				if (chosen == false) {
					findMustBeFirstChosen();
				}

				if (parentFrame.getMyPlayer().getMAuthor() == Author.owner) {
					yourKind1 = Sort.fullWhite;
					yourKind2 = Sort.queenWhite;
				} else {
					yourKind1 = Sort.fullBlack;
					yourKind2 = Sort.queenBlack;
				}

				if ((jb.getKind() == yourKind1 || jb.getKind() == yourKind2) && chosen == false) {
					if (mustBeFirstChosen.size() > 0 && !mustBeFirstChosen.contains(jb)) {
						return;
					}
					toBeatList = checkBeating(jb);
					checkFree(jb);
					chosen = true;
					chosenLast = jb;
					jb.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				} else if (jb.getKind() == Sort.blankBlack && chosen == true
						&& (freeMoves.contains(jb) || toBeatList.contains(jb))) {
					if (toBeatList.size() > 0 && !toBeatList.contains(jb)) {
						return;
					}
					chosen = false;

					setCheckerIcon(jb, chosenLast.getKind());
					jb.setKind(chosenLast.getKind());
					chosenLast.setKind(Sort.blankBlack);
					setCheckerIcon(chosenLast, Sort.blankBlack);
					deleteIfBeaten(new CheckerModel(chosenLast), new CheckerModel(jb));
					chosenLast.setBorder(null);

					if (toBeatList.contains(jb) && (toBeatList = checkBeating(jb)).size() > 0) {
						parentFrame.sendMoveMessage(new MoveMessage(new CheckerModel(jb), new CheckerModel(
								chosenLast), parentFrame.getMyPlayer().getMAuthor(), false));
						parentFrame.setYourTurn(true);
						chosen = true;
						jb.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
						chosenLast = jb;
						toBeatList = checkBeating(jb);
					} else {
						changePossibleQueens(jb);
						parentFrame.sendMoveMessage(new MoveMessage(new CheckerModel(jb), new CheckerModel(
								chosenLast), parentFrame.getMyPlayer().getMAuthor(), true));
						parentFrame.setYourTurn(false);
						if (!parentFrame.isGameIsOn()) {
							return;
						}
						parentFrame.getChatArea().append("\nRuch przeciwnika...");
						parentFrame.getChatArea().setCaretPosition(parentFrame.getChatArea().getDocument().getLength());
					}

					return;
				}
			}
		}
	}

	private void changePossibleQueens(Checker jb) {
		if (jb.getKind() == Sort.fullWhite && jb.getI() == 0) {
			jb.setKind(Sort.queenWhite);
			setCheckerIcon(jb, Sort.queenWhite);
		} else if (jb.getKind() == Sort.fullBlack && jb.getI() == 9) {
			jb.setKind(Sort.queenBlack);
			setCheckerIcon(jb, Sort.queenBlack);
		}
	}

	public void makeMoves(List<MoveMessage> moves) {
		if (moves == null || moves.size() == 0) {
			if (parentFrame.getMyPlayer().getMAuthor() == Author.opponent) {
				JOptionPane.showMessageDialog(null, "Koniec gry. Zwyciężył kolor biały.", "Koniec gry",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (parentFrame.getMyPlayer().getMAuthor() == Author.owner) {
				JOptionPane.showMessageDialog(null, "Koniec gry. Zwyciężył kolor czarny.", "Koniec gry",
						JOptionPane.INFORMATION_MESSAGE);
			}
			finishGame();
			return;
		}
		for (int i = 0; i < moves.size(); i++) {
			CheckerModel first = moves.get(i).getFirst();
			CheckerModel second = moves.get(i).getSecond();

			setCheckerIcon(checkersArray[second.getI()][second.getJ()], first.getKind());
			checkersArray[second.getI()][second.getJ()].setKind(first.getKind());
			checkersArray[first.getI()][first.getJ()].setKind(Sort.blankBlack);
			setCheckerIcon(checkersArray[first.getI()][first.getJ()], Sort.blankBlack);
			deleteIfBeaten(new CheckerModel(checkersArray[first.getI()][first.getJ()]), new CheckerModel(
					checkersArray[second.getI()][second.getJ()]));
			if (i < moves.size() - 1) {
				parentFrame.sendMoveMessage(new MoveMessage(new CheckerModel(
						checkersArray[second.getI()][second.getJ()]), new CheckerModel(checkersArray[first
						.getI()][first.getJ()]), parentFrame.getMyPlayer().getMAuthor(), false));

				parentFrame.setYourTurn(true);
			} else {
				Checker possQueen = checkersArray[second.getI()][second.getJ()];
				changePossibleQueens(possQueen);
				parentFrame.sendMoveMessage(new MoveMessage(new CheckerModel(
						checkersArray[second.getI()][second.getJ()]), new CheckerModel(checkersArray[first
						.getI()][first.getJ()]), parentFrame.getMyPlayer().getMAuthor(), true));
				parentFrame.setYourTurn(false);
				parentFrame.getChatArea().append("\nRuch przeciwnika...");
				parentFrame.getChatArea().setCaretPosition(parentFrame.getChatArea().getDocument().getLength());
			}
		}
		
	}

	/**
	 * Klasa nasłuchująca kliknięć gracza na planszy.
	 */
	public class RightClickListener extends MouseAdapter {

		/**
		 * Jeżeli został naciśnięty prawy przycisk myszy, ostatnio zaznaczony
		 * pionek jest odznaczany.
		 * 
		 * @param e
		 *            Zdarzenie myszki.
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3 && parentFrame.isGameIsOn() && parentFrame.isYourTurn()
					&& chosenLast != null) {
				chosen = false;
				chosenLast.setBorder(null);
			}
		}
	}

	/**
	 * Zwraca okno, które zawiera planszę.
	 * 
	 * @return Okno, które zawiera planszę.
	 */
	public Frame getParentFrame() {
		return parentFrame;
	}

	/**
	 * Ustawia okno, które zawiera planszę.
	 * 
	 * @param parentFrame
	 *            Okno, które zawiera planszę.
	 */
	public void setParentFrame(Frame parentFrame) {
		this.parentFrame = parentFrame;
	}

	/**
	 * Zwraca tablicę pól.
	 * 
	 * @return Tablica pól.
	 */
	public Checker[][] getCheckersArray() {
		return checkersArray;
	}
}
