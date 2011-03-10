package mtg.collector.mtgc2.addutils;

import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import mtg.collector.mtgc2.utilities.Utils;
import mtg.collector.mtgc2.fileIO.MTGFileIO;
import mtg.collector.mtgc2.view.AbstractMTGView;
import mtg.collector.xml.org.Card;
import mtg.collector.xml.org.Deck;

/**
 *
 * @author Eric Kisner
 */
public class AddCardWriter {

    AbstractMTGView view;
    HashMap<JCheckBox, Card> cardChoices;
    Deck deck;

    public AddCardWriter( AbstractMTGView view, HashMap<JCheckBox, Card> cardChoices, Deck deck ) {
        this.view = view;
        this.cardChoices = cardChoices;
        this.deck = deck;
    }

    public void writeCards() {
        for( JCheckBox jcb : cardChoices.keySet() ) {
            if( jcb.isSelected() ) {
                Card curCard = cardChoices.get( jcb );
                int quantity = -1;

                while( quantity == -1 ) {
                    quantity = getQuantity( curCard.getCardName() );
                }

                if( quantity == 0 ) {
                    jcb.setSelected( false );
                    continue;
                }

                curCard.setQuantity( quantity );
                deck.getCards().getCard().add( curCard );
                jcb.setSelected( false );
            }
        }
        deck = Utils.sortDeck( deck );
        JOptionPane.showMessageDialog( view, (MTGFileIO.instance().writeDeck( deck ) ? deck.getDeckName() + " was written to file."
                                              : deck.getDeckName() + " already exists in file.") );
    }

    /**
     * Displays an input pane to get the quantity of the current card the user
     * has.
     * @param cardName The current card's name, displayed on the input window
     * @return
     */
    private int getQuantity( String cardName ) {
        try {
            return Integer.parseInt( JOptionPane.showInputDialog( view, "How many " + cardName + " to add?" ) );
        } catch( NumberFormatException e ) {
            JOptionPane.showMessageDialog( view, "Quantity needs to be a number" );
        } catch( NullPointerException e ) {
            return 0;
        }
        return -1;
    }
}
