package draughts;


import java.net.Socket;
import javax.swing.JOptionPane;


/**
 * Okno podłączenia pod istniejące gniazdo sieciowe.
 */
public class Connection extends javax.swing.JFrame {

    private Frame parentFrame;

    /**
     * Tworzy obiekt klasy i inicjalizuje pole <code>parentFrame</code> danym parametrem.
     * @param parentFrame Okno, z którego użytkownik otworzył okno połączenia.
     */
    public Connection(Frame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hostField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        portField.setText("8189");
        jLabel3 = new javax.swing.JLabel();
        playerNameField = new javax.swing.JTextField();
        connectionButton = new javax.swing.JButton();

        setTitle("Połącz");
        setResizable(false);

        hostField.setText("localhost");

        jLabel1.setText("Host:");

        jLabel2.setText("Port:");

        jLabel3.setText("Twoje imię:");

        connectionButton.setText("Połącz");
        connectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(connectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(14, 14, 14))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(playerNameField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(portField)
                            .addComponent(hostField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(hostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(connectionButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionButtonActionPerformed
        try {
            if (playerNameField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Podaj imię", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            parentFrame.setSocket(new Socket(hostField.getText(), Integer.parseInt(portField.getText())));
            parentFrame.getSocket().setSoTimeout(5000);
            if(playerNameField.getText().indexOf("bot")>-1)
            	parentFrame.setArtificialGame(true);
            parentFrame.setMyPlayer(new Player(playerNameField.getText(), Author.opponent));
            parentFrame.getChatArea().append("<" + playerNameField.getText() + "> " + "Połączony...");
            parentFrame.begin();
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Nie udało się otworzyć połączenia", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_connectionButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectionButton;
    private javax.swing.JTextField hostField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField playerNameField;
    private javax.swing.JTextField portField;
    // End of variables declaration//GEN-END:variables
}
