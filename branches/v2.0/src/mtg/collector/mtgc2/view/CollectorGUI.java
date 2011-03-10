package mtg.collector.mtgc2.view;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import mtg.collector.mtgc2.importing.MTGImporter;

/**
 * The entry point to the MTG Collector User Interface. This application will become obsolete if
 * Wizards(TM) changes their Gatherer search HTML code in ANY way. Unfortunately, there was no other
 * solution to this problem. Since I'm really only storing strings, I could not find any application or
 * database that held the information that I needed, nor was I willing to go through tens of thousands
 * of cards to create my own database.
 *
 *
 * === Version 1 of MTGC ====
 * This version contained a separate "card" collector feature, where the user could search for a single
 * card, and add that to his/her ENTIRE collection. Since I was the only one planning to use this application,
 * I decided that I would never need, or want, to add all of my cards to this program. So, I scratched that
 * portion of the application.
 *
 * === Version 2.0 of MTGC ===
 * This version is a simple deck editor/viewer. The user can add new decks (by importing a decklist), delete
 * decks, print decks out to a file, or add cards to an existing deck. In the future, if this could ever
 * become something other than for my own use, I will be adding extra features including:
 * -Ability to delete a card
 * -Ability to arbitrarily create a new deck
 * -Ability to delete multiple cards at once
 *
 * === Version 2.1 of MTGC ===
 * I have decided to strip almost of the Web functionality out of the program. It is difficult to understand
 * what the code is really doing, so I see no point in keeping most of the HTML parsing. Instead, the database
 * will ONLY consist of: Card name, card image link, and quantity. I feel that this is a reasonable solution,
 * since it will strip out most of the complicated code (trying to find the card power/toughness, mana cost, etc).
 * I have decided to deprecate or comment all functions/methods that deal with anything other than the
 * above-mentioned attributes of a Card object. This also means changing the XML schema. I will keep a copy
 * of Version 2.0's schema in case I ever want to include those features again.
 *
 *
 * @author Eric Kisner
 */
public class CollectorGUI extends AbstractMTGView {

    private static final int VIEW_WIDTH = 250;
    private static final int VIEW_HEIGHT = 250;

    /** Creates new form CollectorGUI */
    public CollectorGUI() {
        super( new Dimension( VIEW_WIDTH, VIEW_HEIGHT ) );
        initComponents();
    }

    @Override
    public void modelPropertyChange( PropertyChangeEvent evt ) {
        //The user can only click some buttons, so there is really no point
        //in throwing any property changes here.
        //However, since this class extends AbstractMTGView for positiong and closing
        //instructions, we still need to override this method.
    }

    //-------------------------------------------------
    //----------------Generated Code-------------------
    //-------------------------------------------------
    @SuppressWarnings( "unchecked" )
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    deckManagerButton = new java.awt.Button();
    importManagerButton = new java.awt.Button();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jmiExit = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("MTG Collector Interface");
    setBackground(new java.awt.Color(0, 0, 51));
    setFont(new java.awt.Font("Courier New", 0, 12));
    setMinimumSize( new java.awt.Dimension( VIEW_WIDTH, VIEW_HEIGHT ));
    setName(""); // NOI18N
    setResizable(false);

    deckManagerButton.setFont(new java.awt.Font("DialogInput", 0, 12));
    deckManagerButton.setLabel("Deck Manager");
    deckManagerButton.setName("deckManagerButton"); // NOI18N
    deckManagerButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deckManagerButtonActionPerformed(evt);
      }
    });

    importManagerButton.setActionCommand("");
    importManagerButton.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
    importManagerButton.setLabel("Import File");
    importManagerButton.setName("importManagerButton"); // NOI18N
    importManagerButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importManagerButtonActionPerformed(evt);
      }
    });

    jMenuBar1.setName("jMenuBar1"); // NOI18N

    jMenu1.setText("File");
    jMenu1.setName("jMenu1"); // NOI18N

    jmiExit.setText("Exit");
    jmiExit.setName("miExit"); // NOI18N
    jmiExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jmiExitActionPerformed(evt);
      }
    });
    jMenu1.add(jmiExit);

    jMenuBar1.add(jMenu1);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(90, 90, 90)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(deckManagerButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
          .addComponent(importManagerButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
        .addGap(96, 96, 96))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(85, 85, 85)
        .addComponent(importManagerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(51, 51, 51)
        .addComponent(deckManagerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(93, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void deckManagerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deckManagerButtonActionPerformed
        close( false );
        new DeckManager().setVisible( true );
    }//GEN-LAST:event_deckManagerButtonActionPerformed

    private void jmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExitActionPerformed
        close( true );
    }//GEN-LAST:event_jmiExitActionPerformed

	private void importManagerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importManagerButtonActionPerformed
            MTGImporter importer = new MTGImporter( this );
            importer.importFile();
	}//GEN-LAST:event_importManagerButtonActionPerformed

    public static void main( String args[] ) {
        java.awt.EventQueue.invokeLater( new Runnable() {

            public void run() {
                new CollectorGUI().setVisible( true );
            }
        } );
    }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private java.awt.Button deckManagerButton;
  private java.awt.Button importManagerButton;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem jmiExit;
  // End of variables declaration//GEN-END:variables
}
