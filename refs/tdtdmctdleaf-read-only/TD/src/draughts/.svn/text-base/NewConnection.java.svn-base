package draughts;



import java.net.ServerSocket;
import javax.swing.JOptionPane;

/**
 * Okno tworzenia nowego połączenia.
 */
public class NewConnection extends javax.swing.JFrame {

    private Frame parentFrame;
    
    /**
     * Tworzy okno nowego połączenia.
     * @param parentFrame Okno, z którego użytkownik otworzył okno połączenia.
     */
    public NewConnection(Frame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        portField = new javax.swing.JTextField();
        portLabel = new javax.swing.JLabel();
        newConnectionButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        playerNameField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nowe połączenie");
        setResizable(false);

        portField.setText("8189");

        portLabel.setText("Port:");

        newConnectionButton.setText("Stwórz połączenie");
        newConnectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newConnectionButtonActionPerformed(evt);
            }
        });

        nameLabel.setText("Twoje imię:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(portLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(playerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(newConnectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portLabel)
                    .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(playerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(newConnectionButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void newConnectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newConnectionButtonActionPerformed
        try {
            parentFrame.setServerSocket(new ServerSocket(Integer.parseInt(portField.getText())));
            if(playerNameField.getText().indexOf("bot")>-1)
            	parentFrame.setArtificialGame(true);
            parentFrame.setMyPlayer(new Player(playerNameField.getText(), Author.owner));
            parentFrame.getChatArea().append("<" + playerNameField.getText() + "> " + "Połączony...");
            parentFrame.getChatArea().append("\nOczekiwanie na drugiego gracza...");
            parentFrame.refreshSocket();
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Nie udało się utworzyć połączenia", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_newConnectionButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton newConnectionButton;
    private javax.swing.JTextField playerNameField;
    private javax.swing.JTextField portField;
    private javax.swing.JLabel portLabel;
    // End of variables declaration//GEN-END:variables
}
