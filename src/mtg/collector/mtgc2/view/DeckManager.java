package mtg.collector.mtgc2.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import mtg.collector.mtgc2.utilities.DeckUtils;
import mtg.collector.mtgc2.controller.MTGDecksController;
import mtg.collector.mtgc2.fileIO.MTGFileIO;
import mtg.collector.mtgc2.model.MTGDecksModel;
import mtg.collector.xml.org.Card;
import mtg.collector.xml.org.Deck;

/**
 * 
 * @author Eric Kisner
 */
public class DeckManager extends AbstractMTGView {

    private static final int VIEW_WIDTH = 750;
    private static final int VIEW_HEIGHT = 500;
    private MTGDecksController controller;
    private JFrame cardFrame = null;
    private HashMap<Integer, Card> mapping = null;
    private Deck selectedDeck = null;

    /** Creates new form DeckManager */
    public DeckManager() {
        super( new Dimension( VIEW_WIDTH, VIEW_HEIGHT ) );
        initComponents();
        initController();
    }

    private void initController() {
        MTGDecksModel m = new MTGDecksModel();
        controller = new MTGDecksController();
        controller.addView( this );
        controller.addModel( m );
        controller.changeElementDeckList( DeckUtils.returnDeckJList() );
    }

    @Override
    public void modelPropertyChange( PropertyChangeEvent evt ) {
        if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_DECKLIST_PROPERTY ) ) {
            setDeckList( (JList) evt.getNewValue() );

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_CARDLIST_PROPERTY ) ) {
            setCardList( (JList) evt.getNewValue() );

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_CARD_CHANGED_PROPERTY ) ) {
            displayCardInfo( (Integer) evt.getNewValue() );

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_POSITION_TO_CARD_MAP ) ) {
            mapping = (HashMap<Integer, Card>) evt.getNewValue();

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_TOTALCARDS_PROPERTY ) ) {
            jlDeckCardCount.setText( "Total cards: " + (Integer) evt.getNewValue() );

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_OUTPUT_TEXT_PROPERTY ) ) {
            DeckUtils.outputTextFile( evt.getNewValue(), this );

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_DELETE_DECK_PROPERTY ) ) {
            if( JOptionPane.showConfirmDialog( this, "Do you really want to delete " + selectedDeck.getDeckName() + "?" )
                == JOptionPane.OK_OPTION ) {
                delete( (String) evt.getNewValue() );
            }

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_DELETE_CARD_PROPERTY ) ) {
            if( JOptionPane.showConfirmDialog( this, "Do you really want to delete " + (String) evt.getNewValue() + "?" )
                == JOptionPane.OK_OPTION ) {
                delete( getSelectedDeck(), (String) evt.getNewValue() );
            }

        } else if( evt.getPropertyName().equals( MTGDecksController.ELEMENT_ADD_CARD_PROPERTY ) ) {
            initAddManager( (String) evt.getNewValue() );
        }
    }

    private void initAddManager( String deckName ) {
        final AddManager add = new AddManager( this );
        for( Deck deck : MTGFileIO.instance().getCurDecks() ) {
            if( deckName.equals( deck.getDeckName() ) ) {
                add.setDeck( deck );
                this.setVisible( false );
                break;
            }
        }
    }

    private void delete( String deckName ) {
        MTGFileIO.instance().deleteDeck( selectedDeck );
        JOptionPane.showMessageDialog( this, "Successfully deleted " + deckName );
        controller.changeElementDeckList( DeckUtils.returnDeckJList() );
    }

    private void delete( String deckName, String cardName ) {
        for( Deck deck : MTGFileIO.instance().getCurDecks() ) {
            if( deckName.equals( deck.getDeckName() ) ) {
                List<Card> cards = deck.getCards().getCard();
                for( Card card : cards ) {
                    if( card.getCardName().equals( cardName ) ) {
                        MTGFileIO.instance().deleteCard( deck, card );
                        controller.changeElementCardList( getCardList() );
                        break;
                    }
                }
            }
        }
    }

    private void setDeckList( final JList list ) {
        list.setBackground( new java.awt.Color( 204, 204, 204 ) );
        list.setFont( new java.awt.Font( "Monospaced", 0, 14 ) );
        list.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        list.addListSelectionListener( new ListSelectionListener() {

            public void valueChanged( ListSelectionEvent e ) {
                if( !e.getValueIsAdjusting() ) {
                    selectedDeck = DeckUtils.getDeckObj( (String) list.getSelectedValue() );
                }
            }
        } );
        jspDeck.setViewportView( list );
    }

    private void setCardList( final JList list ) {
        list.setBackground( new java.awt.Color( 204, 204, 204 ) );
        list.setFont( new java.awt.Font( "Monospaced", 0, 14 ) );
        list.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        list.addListSelectionListener( new ListSelectionListener() {

            public void valueChanged( ListSelectionEvent e ) {
                if( e.getValueIsAdjusting() ) {
                    int index = list.getSelectedIndex();
                    controller.changeElementCardSelected( index );
                }
            }
        } );
        list.addMouseListener( new MouseListener() {

            public void mouseClicked( MouseEvent e ) {
            }

            public void mousePressed( MouseEvent e ) {
            }

            public void mouseEntered( MouseEvent e ) {
            }

            public void mouseExited( MouseEvent e ) {
            }

            public void mouseReleased( MouseEvent e ) {
                if( e.isPopupTrigger() ) {
                    System.out.println( "hello again" );
                }
            }
        } );
        list.addKeyListener( new KeyListener() {

            public void keyTyped( KeyEvent e ) {
            }

            public void keyReleased( KeyEvent e ) {
            }

            public void keyPressed( KeyEvent e ) {
                if( e.getKeyCode() == KeyEvent.VK_DELETE ) {
                    controller.changeElementCardToDelete( getSelectedCard() );
                }
            }
        } );
        jsCardDisplay.setViewportView( list );
    }

    private JList getCardList() {
        String dName = getSelectedDeck();
        if( dName != null ) {
            return DeckUtils.returnCardJList( dName, controller );
        } else {
            return null;
        }
    }

    private String getSelectedDeck() {
        String toRet = null;
        for( Component c : jspDeck.getComponents() ) {
            if( c instanceof JViewport ) {
                for( Component comp : ((JViewport) c).getComponents() ) {
                    if( comp instanceof JList ) {
                        toRet = (String) ((JList) comp).getSelectedValue();
                    }
                }
            }
        }
        return toRet;
    }

    private String getSelectedCard() {
        String toRet = null;
        for( Component c : jsCardDisplay.getComponents() ) {
            if( c instanceof JViewport ) {
                for( Component comp : ((JViewport) c).getComponents() ) {
                    if( comp instanceof JList ) {
                        toRet = (String) ((JList) comp).getSelectedValue();
                    }
                }
            }
        }
        return toRet;
    }

    private void displayCardInfo( Integer index ) {
        Card card = mapping.get( index );
        URL url;
        try {
            url = new URL( card.getImageLink() );
        } catch( MalformedURLException e ) {
            url = null;
        }
        JLabel label;
        if( url == null ) {
            label = new JLabel( "Image unavailable." );
        } else {
            label = new JLabel( new ImageIcon( url ) );
        }

        cardFrame = new JFrame();
        JPanel panel = new JPanel();
        JLabel quantity = new JLabel( "Quantity: " + card.getQuantity() );

        panel.setOpaque( true );
        panel.add( label );
        panel.add( quantity );

        cardFrame.setName( card.getCardName() );
        cardFrame.setResizable( false );
        cardFrame.setAlwaysOnTop( true );
        cardFrame.setMinimumSize( new Dimension( 200, 200 ) );
        cardFrame.setLocationRelativeTo( this );
        cardFrame.setVisible( true );
        cardFrame.add( panel );
        cardFrame.pack();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jpDeckPanel = new javax.swing.JPanel();
    jspDeck = new javax.swing.JScrollPane();
    jpCardPanel = new javax.swing.JPanel();
    jsCardDisplay = new javax.swing.JScrollPane();
    jlDeckCardCount = new javax.swing.JLabel();
    showDetailsButton = new javax.swing.JButton();
    outputTextFileButton = new javax.swing.JButton();
    deleteDeckButton = new javax.swing.JButton();
    addCardButton = new javax.swing.JButton();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jmiHome = new javax.swing.JMenuItem();
    jmiExit = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Deck Manager");
    setMinimumSize( new java.awt.Dimension( VIEW_WIDTH, VIEW_HEIGHT ));

    jpDeckPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Decks", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N
    jpDeckPanel.setName("jpDeckPanel"); // NOI18N

    jspDeck.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, java.awt.Color.lightGray));
    jspDeck.setName("jspDeck"); // NOI18N

    javax.swing.GroupLayout jpDeckPanelLayout = new javax.swing.GroupLayout(jpDeckPanel);
    jpDeckPanel.setLayout(jpDeckPanelLayout);
    jpDeckPanelLayout.setHorizontalGroup(
      jpDeckPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jpDeckPanelLayout.createSequentialGroup()
        .addComponent(jspDeck, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
        .addContainerGap())
    );
    jpDeckPanelLayout.setVerticalGroup(
      jpDeckPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jpDeckPanelLayout.createSequentialGroup()
        .addComponent(jspDeck, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        .addContainerGap())
    );

    jpCardPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cards", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N
    jpCardPanel.setName("jpCardPanel"); // NOI18N

    jsCardDisplay.setBackground(new java.awt.Color(255, 255, 255));
    jsCardDisplay.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, java.awt.Color.lightGray));
    jsCardDisplay.setName("jsCardDisplay"); // NOI18N

    jlDeckCardCount.setText("Total Cards:");
    jlDeckCardCount.setName("jlDeckCardCount"); // NOI18N

    javax.swing.GroupLayout jpCardPanelLayout = new javax.swing.GroupLayout(jpCardPanel);
    jpCardPanel.setLayout(jpCardPanelLayout);
    jpCardPanelLayout.setHorizontalGroup(
      jpCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jpCardPanelLayout.createSequentialGroup()
        .addGroup(jpCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jpCardPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jsCardDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE))
          .addGroup(jpCardPanelLayout.createSequentialGroup()
            .addGap(95, 95, 95)
            .addComponent(jlDeckCardCount, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jpCardPanelLayout.setVerticalGroup(
      jpCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jpCardPanelLayout.createSequentialGroup()
        .addComponent(jsCardDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
        .addComponent(jlDeckCardCount, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    showDetailsButton.setText("Show Details>>>");
    showDetailsButton.setName("showDetailsButton"); // NOI18N
    showDetailsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        showDetailsButtonActionPerformed(evt);
      }
    });

    outputTextFileButton.setText("Deck to Text File");
    outputTextFileButton.setName("outputTextFileButton"); // NOI18N
    outputTextFileButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        outputTextFileButtonActionPerformed(evt);
      }
    });

    deleteDeckButton.setText("Delete Deck");
    deleteDeckButton.setName("deleteDeckButton"); // NOI18N
    deleteDeckButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteDeckButtonActionPerformed(evt);
      }
    });

    addCardButton.setText("Add Card");
    addCardButton.setName("addCardButton"); // NOI18N
    addCardButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addCardButtonActionPerformed(evt);
      }
    });

    jMenuBar1.setName("jMenuBar1"); // NOI18N

    jMenu1.setText("File");
    jMenu1.setName("jMenu1"); // NOI18N

    jmiHome.setText("Home");
    jmiHome.setName("miHome"); // NOI18N
    jmiHome.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jmiHomeActionPerformed(evt);
      }
    });
    jMenu1.add(jmiHome);

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
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jpDeckPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(showDetailsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(outputTextFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(deleteDeckButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(addCardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jpCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
            .addGap(144, 144, 144)
            .addComponent(showDetailsButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(outputTextFileButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(deleteDeckButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(addCardButton))
          .addComponent(jpCardPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jpDeckPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

	private void jmiHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiHomeActionPerformed
            close( false );
            new CollectorGUI().setVisible( true );
}//GEN-LAST:event_jmiHomeActionPerformed

	private void jmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExitActionPerformed
            close( true );
}//GEN-LAST:event_jmiExitActionPerformed

	private void showDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDetailsButtonActionPerformed
            controller.changeElementCardList( getCardList() );
}//GEN-LAST:event_showDetailsButtonActionPerformed

	private void outputTextFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputTextFileButtonActionPerformed
            controller.changeElementOutputText( getSelectedDeck() );
	}//GEN-LAST:event_outputTextFileButtonActionPerformed

	private void deleteDeckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteDeckButtonActionPerformed
            controller.changeElementDeckToDelete( getSelectedDeck() );
	}//GEN-LAST:event_deleteDeckButtonActionPerformed

	private void addCardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCardButtonActionPerformed
            controller.changeElementAddCard( getSelectedDeck() );
	}//GEN-LAST:event_addCardButtonActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JButton addCardButton;
  public javax.swing.JButton deleteDeckButton;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenuBar jMenuBar1;
  public javax.swing.JLabel jlDeckCardCount;
  private javax.swing.JMenuItem jmiExit;
  private javax.swing.JMenuItem jmiHome;
  private javax.swing.JPanel jpCardPanel;
  private javax.swing.JPanel jpDeckPanel;
  public javax.swing.JScrollPane jsCardDisplay;
  private javax.swing.JScrollPane jspDeck;
  public javax.swing.JButton outputTextFileButton;
  private javax.swing.JButton showDetailsButton;
  // End of variables declaration//GEN-END:variables
}
