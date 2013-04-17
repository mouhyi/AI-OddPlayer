package draughts;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

import draughts.moves.Mtd;

/**
 * Definiuje okno gry.
 */
public class Frame extends javax.swing.JFrame {

	private ServerSocket serverSocket;
	private Player myPlayer = new Player();
	private Player myOpponent = new Player();
	private Socket socket;
	private InputStream inStream;
	private OutputStream outStream;
	private ObjectInputStream objInputStr;
	private ObjectOutputStream objOutputStr;
	private Thread tmain;
	private boolean gameIsOn = false;
	private boolean yourTurn;
	private boolean artificialGame = false;
	private Mtd mtd;
	private ITD itd;
	private double[] weights;

	/**
	 * Tworzy główne okno gry.
	 */
	public Frame() {
		initComponents();
		boardPanel1.setParentFrame(this);
		fillWeights();
		chooseTD();
	}

	public void fillWeights() {
		weights = new double[12];
		for (int i = 0; i < 12; i++)
			weights[i] = 0;
	}

	private void chooseTD() {
		setItd(new TDLambda(weights, 1, 1, 0, 0, 0, true));
	}

	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		backgroundPanel1 = new BackgroundPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		chatArea = new javax.swing.JTextArea();
		messageField = new javax.swing.JTextField();
		jButton2 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton5 = new javax.swing.JButton();
		boardPanel1 = new BoardPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Warcaby");
		setBounds(new java.awt.Rectangle(0, 0, 0, 0));
		setFocusTraversalPolicyProvider(true);
		setForeground(java.awt.Color.white);
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		chatArea.setBackground(new java.awt.Color(50, 32, 23));
		chatArea.setColumns(20);
		chatArea.setFont(new java.awt.Font("Georgia", 0, 11));
		chatArea.setForeground(new java.awt.Color(226, 226, 226));
		chatArea.setLineWrap(true);
		chatArea.setRows(3);
		chatArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		chatArea.setDisabledTextColor(new java.awt.Color(226, 226, 226));
		chatArea.setDoubleBuffered(true);
		chatArea.setEnabled(false);
		jScrollPane1.setViewportView(chatArea);

		messageField.setBackground(new java.awt.Color(50, 32, 23));
		messageField.setFont(new java.awt.Font("Georgia", 0, 11));
		messageField.setForeground(new java.awt.Color(226, 226, 226));
		messageField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		messageField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				messageFieldKeyPressed(evt);
			}
		});

		jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/draughts/newConnection.jpg"))); // NOI18N
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/draughts/send.jpg"))); // NOI18N
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/draughts/connect.jpg"))); // NOI18N
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/draughts/newGame.jpg"))); // NOI18N
		jButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton5ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout boardPanel1Layout = new javax.swing.GroupLayout(boardPanel1);
		boardPanel1.setLayout(boardPanel1Layout);
		boardPanel1Layout.setHorizontalGroup(boardPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 320, Short.MAX_VALUE));
		boardPanel1Layout.setVerticalGroup(boardPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 320, Short.MAX_VALUE));

		javax.swing.GroupLayout backgroundPanel1Layout = new javax.swing.GroupLayout(backgroundPanel1);
		backgroundPanel1.setLayout(backgroundPanel1Layout);
		backgroundPanel1Layout.setHorizontalGroup(backgroundPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				backgroundPanel1Layout.createSequentialGroup().addContainerGap().addGroup(
						backgroundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
								jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING,
								javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addGroup(
								javax.swing.GroupLayout.Alignment.LEADING,
								backgroundPanel1Layout.createSequentialGroup().addComponent(messageField,
										javax.swing.GroupLayout.PREFERRED_SIZE, 321,
										javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(
										jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
										javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(137, 137, 137).addGroup(
						backgroundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
								jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 122,
								javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton3,
								javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(
						80, 80, 80)).addGroup(
				backgroundPanel1Layout.createSequentialGroup().addGap(68, 68, 68).addComponent(boardPanel1,
						javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE).addComponent(
						jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(
						110, 110, 110)));
		backgroundPanel1Layout.setVerticalGroup(backgroundPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				backgroundPanel1Layout.createSequentialGroup().addGroup(
						backgroundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
								backgroundPanel1Layout.createSequentialGroup().addGap(81, 81, 81).addComponent(
										boardPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)).addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								backgroundPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jButton5,
										javax.swing.GroupLayout.PREFERRED_SIZE, 33,
										javax.swing.GroupLayout.PREFERRED_SIZE).addGap(36, 36, 36))).addGroup(
						backgroundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								backgroundPanel1Layout.createSequentialGroup().addComponent(jButton2,
										javax.swing.GroupLayout.PREFERRED_SIZE, 22,
										javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3,
										javax.swing.GroupLayout.PREFERRED_SIZE, 22,
										javax.swing.GroupLayout.PREFERRED_SIZE).addGap(20, 20, 20)).addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								backgroundPanel1Layout.createSequentialGroup().addComponent(jScrollPane1,
										javax.swing.GroupLayout.PREFERRED_SIZE, 59,
										javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
										backgroundPanel1Layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING).addComponent(messageField,
												javax.swing.GroupLayout.PREFERRED_SIZE, 19,
												javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton4,
												javax.swing.GroupLayout.PREFERRED_SIZE, 20,
												javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()))));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				backgroundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				backgroundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Tworzy wątek czekający na połączenie. Gdy połączenie zostaje nawiązane rozpoczyna przesyłanie danych.
	 * 
	 * @see Frame#begin()
	 */
	public void refreshSocket() {
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					socket = serverSocket.accept();
					begin();
				} catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
		});
		t.start();
	}

	/**
	 * Przesyła imiona przeciwników, a następnie rozpoczyna odbiór danych.
	 */
	public void begin() {
		try {
			tmain = new Thread(new Runnable() {

				public void run() {
					try {
						inStream = socket.getInputStream();
						outStream = socket.getOutputStream();
						objOutputStr = new ObjectOutputStream(new BufferedOutputStream(outStream));
						objOutputStr.flush();
						objInputStr = new ObjectInputStream(new BufferedInputStream(inStream));

						objOutputStr.writeObject(new TextMessage(myPlayer.getName(), myPlayer.getMAuthor()));
						objOutputStr.flush();

						chatArea.append("\nPołączenie nawiązane pomyślnie...");
						chatArea.setCaretPosition(chatArea.getDocument().getLength());

						Object o;
						while ((o = objInputStr.readObject()) != null) {
							if (socket.getSoTimeout() > 0) {
								socket.setSoTimeout(0);
							}
							if (o instanceof SocketClosing) {
								ifSocketClosing();
							} else if (o instanceof TextMessage) {
								ifTextMessage((TextMessage) o);
							} else if (o instanceof StartGame) {
								ifStartGame((StartGame) o);
							} else if (o instanceof MoveMessage) {
								ifMoveMessage((MoveMessage) o);
							}
						}
					} catch (SocketTimeoutException e) {
						chatArea.append("\nGracz znalazł już przeciwnika. Spróbuj połączyć się później...\n");
						chatArea.setCaretPosition(chatArea.getDocument().getLength());
						tryToCloseSocket(socket);
						return;
					} catch (SocketException e) {
						if (myPlayer.getMAuthor() == Author.owner) {
							refreshSocket();
						}
					} catch (IOException e) {
						System.out.println(e.toString());
					} catch (ClassNotFoundException e) {
						System.out.println(e.toString());
					}

				}
			});
			tmain.start();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void ifMoveMessage(MoveMessage o) {
		if ((myPlayer.getMAuthor() == Author.owner && o.getMAuthor() == Author.owner)
				|| (myPlayer.getMAuthor() == Author.opponent && o.getMAuthor() == Author.opponent)) {
			sendObject(o);
		} else {
			CheckerModel first = o.getFirst();
			CheckerModel second = o.getSecond();

			boardPanel1.getCheckersArray()[first.getI()][first.getJ()].setKind(first.getKind());
			boardPanel1.getCheckersArray()[second.getI()][second.getJ()].setKind(second.getKind());
			boardPanel1.setCheckerIcon(boardPanel1.getCheckersArray()[first.getI()][first.getJ()], first.getKind());
			boardPanel1.setCheckerIcon(boardPanel1.getCheckersArray()[second.getI()][second.getJ()], second.getKind());

			boardPanel1.deleteIfBeaten(second, first);
			if (!gameIsOn) {
				return;
			}
			if (o.isEndsTurn()) {
				yourTurn = true;
				chatArea.append("\nTwój ruch...");
				chatArea.setCaretPosition(chatArea.getDocument().getLength());
				if (artificialGame) {
					boardPanel1.makeMoves(mtd.getBestMoveDefault(boardPanel1.getCheckersArray(), myPlayer, getItd()));
				}
			}
		}
	}

	private void ifStartGame(StartGame o) {
		if ((myPlayer.getMAuthor() == Author.owner && o.getMAuthor() == Author.owner)
				|| (myPlayer.getMAuthor() == Author.opponent && o.getMAuthor() == Author.opponent)) {
			sendObject(o);
		} else {
			gameIsOn = true;
			boardPanel1.setKindsAndIcons();
			chatArea.append("\nRozpoczynamy nową grę...");
			if (myPlayer.getMAuthor() == Author.owner) {
				chatArea.append("\nTwój kolor : biały...");
				chatArea.append("\nTwój ruch...");
				yourTurn = true;
			} else {
				chatArea.append("\nTwój kolor : czarny...");
				chatArea.append("\nRuch przeciwnika...");
				yourTurn = false;
			}
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
		}
	}

	private void ifTextMessage(TextMessage o) {
		if ((myPlayer.getMAuthor() == Author.owner && o.getMAuthor() == Author.owner)
				|| (myPlayer.getMAuthor() == Author.opponent && o.getMAuthor() == Author.opponent)) {
			sendObject(o);
		}
		String line = o.getMessage();
		if (myOpponent.getName().equals("")) {
			myOpponent.setName(line);
		}// ustawianie imienia przeciwnika
		else {
			chatArea.append("\n<" + myOpponent.getName() + "> " + line);
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
		}
	}

	private void ifSocketClosing() {
		tryToCloseSocket(socket);
		myOpponent.setName("");
		gameIsOn = false;
		chatArea.append("\nPołączenie z użytkownikiem zerwane...");

		if (myPlayer.getMAuthor() == Author.owner) {
			chatArea.append("\nOczekiwanie na drugiego gracza...");
			refreshSocket();
		} else {
			chatArea.append("\nStwórz nowe połączenie...");
		}
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
		return;
	}

	private void tryToCloseSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private void send() {
		if (!myOpponent.getName().equals("") && !messageField.getText().equals("")) {
			try {
				objOutputStr.writeObject(new TextMessage(messageField.getText(), myPlayer.getMAuthor()));
				objOutputStr.flush();
				chatArea.append("\n<" + myPlayer.getName() + "> " + messageField.getText());
				chatArea.setCaretPosition(chatArea.getDocument().getLength());
				messageField.setText("");
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	/**
	 * Wysyła obiekt do przeciwnika.
	 * 
	 * @param o
	 *            Obiekt.
	 */
	public void sendObject(Object o) {
		if (socket != null && socket.isBound()) {
			try {
				objOutputStr.writeObject(o);
				objOutputStr.flush();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}

	/**
	 * Wysyła wiadomość ruchu do przeciwnika.
	 * 
	 * @param message
	 *            Wiadomość ruchu.
	 */
	public void sendMoveMessage(MoveMessage message) {
		sendObject(message);
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
		try {
			if (socket != null && !socket.isClosed()) {
				sendObject(new SocketClosing());
			}
			if (objInputStr != null)
				objInputStr.close();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}// GEN-LAST:event_formWindowClosing

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
		if (gameIsOn) {
			JOptionPane.showMessageDialog(null, "W trakcie rozgrywki nie możesz rozpocząć nowej gry", "Uwaga",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (socket != null && socket.isBound() && !myOpponent.getName().equals("")) {
			startGame();
		}
	}// GEN-LAST:event_jButton5ActionPerformed

	/**
	 * Rozpoczyna grę.
	 * 
	 * @see BoardPanel#setKindsAndIcons()
	 */
	public void startGame() {
		boardPanel1.setKindsAndIcons();
		gameIsOn = true;
		sendObject(new StartGame(myPlayer.getMAuthor()));
		chatArea.append("\nRozpoczynamy nową grę...");
		if (myPlayer.getMAuthor() == Author.owner) {
			chatArea.append("\nTwój kolor : biały...");
			chatArea.append("\nTwój ruch...");
			yourTurn = true;
			if (artificialGame) {
				boardPanel1.makeMoves(mtd.getBestMoveDefault(boardPanel1.getCheckersArray(), myPlayer, getItd()));
			}
		} else {
			chatArea.append("\nTwój kolor : czarny...");
			chatArea.append("\nRuch przeciwnika...");
			yourTurn = false;
		}
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
	}

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		if (myPlayer.getMAuthor() != Author.owner) {
			new Connection(this).setVisible(true);
		}
	}// GEN-LAST:event_jButton3ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
		send();
	}// GEN-LAST:event_jButton4ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		new NewConnection(this).setVisible(true);
	}// GEN-LAST:event_jButton2ActionPerformed

	private void messageFieldKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_messageFieldKeyPressed
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			send();
		}
	}// GEN-LAST:event_messageFieldKeyPressed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private BackgroundPanel backgroundPanel1;
	private BoardPanel boardPanel1;
	private javax.swing.JTextArea chatArea;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField messageField;

	// End of variables declaration//GEN-END:variables

	/**
	 * Ustawia gniazdo serwera.
	 * 
	 * @param serverSocket
	 *            Gniazdo serwera.
	 */
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * Zwraca textarea rozmowy graczy.
	 * 
	 * @return Textarea rozmowy graczy.
	 */
	public javax.swing.JTextArea getChatArea() {
		return chatArea;
	}

	/**
	 * Zwraca gniazdo gracza.
	 * 
	 * @return Gniazdo gracza.
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Ustawia gniazdo gracza.
	 * 
	 * @param socket
	 *            Gniazdo gracza.
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Zwraca gracza.
	 * 
	 * @return Gracz.
	 */
	public Player getMyPlayer() {
		return myPlayer;
	}

	/**
	 * Ustawia gracza.
	 * 
	 * @param myPlayer
	 *            Gracz.
	 */
	public void setMyPlayer(Player myPlayer) {
		this.myPlayer = myPlayer;
		this.mtd = new Mtd(myPlayer.getMAuthor());
	}

	/**
	 * Ustawia informację o trwaniu gry.
	 * 
	 * @return Informacja o trwaniu gry.
	 */
	public boolean isGameIsOn() {
		return gameIsOn;
	}

	/**
	 * Ustawia informację o trwaniu gry.
	 * 
	 * @param gameIsOn
	 *            Informacja o trwaniu gry.
	 */
	public void setGameIsOn(boolean gameIsOn) {
		this.gameIsOn = gameIsOn;
	}

	/**
	 * Zwraca informację o ruchu gracza.
	 * 
	 * @return Informacja o ruchu gracza.
	 */
	public boolean isYourTurn() {
		return yourTurn;
	}

	/**
	 * Ustawia informację o ruchu gracza.
	 * 
	 * @param yourTurn
	 *            Informacja o ruchu gracza.
	 */
	public void setYourTurn(boolean yourTurn) {
		this.yourTurn = yourTurn;
	}

	public boolean isArtificialGame() {
		return artificialGame;
	}

	public void setArtificialGame(boolean artificialGame) {
		this.artificialGame = artificialGame;
	}

	public void setItd(ITD itd) {
		this.itd = itd;
	}

	public ITD getItd() {
		return itd;
	}
}

/**
 * Obiekty tej klasy informują o zerwaniu połączenia przez któregoś z graczy.
 */
class SocketClosing implements Serializable {

	public SocketClosing() {
	}
}

/**
 * Obiekty tej klasy informują o rozpoczęciu nowej gry.
 */
class StartGame implements Serializable {

	private Author mAuthor;

	public StartGame() {

	}

	public StartGame(Author mAuthor) {
		this.mAuthor = mAuthor;
	}

	public Author getMAuthor() {
		return mAuthor;
	}

	public void setMAuthor(Author mAuthor) {
		this.mAuthor = mAuthor;
	}
}
