package mtg.collector.mtgc2.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JList;
import javax.swing.JOptionPane;
import mtg.collector.mtgc2.controller.MTGDecksController;
import mtg.collector.mtgc2.fileIO.MTGFileIO;
import mtg.collector.mtgc2.view.AbstractMTGView;
import mtg.collector.xml.org.Card;
import mtg.collector.xml.org.Deck;

/**
 *
 * @author Eric Kisner
 */
public class DeckUtilities {

    public static Deck getDeckObj( String deckName ) {
        for( Deck deck : MTGFileIO.instance().getCurDecks() ) {
            if( deck.getDeckName().equals( deckName ) ) {
                return deck;
            }
        }
        return null;
    }

    public static JList returnDeckJList() {
        MTGFileIO model = MTGFileIO.instance();
        ArrayList<String> ret = new ArrayList<String>();
        for( Deck deck : model.getCurDecks() ) {
            ret.add( deck.getDeckName() );
        }
        return makeJList( ret.toArray() );
    }

    public static JList returnCardJList( String selectedDeck, MTGDecksController controller ) {
        MTGFileIO model = MTGFileIO.instance();
        Deck deck = null;
        for( Deck d : model.getCurDecks() ) {
            if( d.getDeckName().equals( selectedDeck ) ) {
                deck = d;
                break;
            }
        }
        controller.changeElementTotalCards( getTotalCards( deck ) );
        List<String> cardNames = new ArrayList<String>();
        HashMap<Integer, Card> mapping = new HashMap<Integer, Card>();
        int counter = 0;
        for( Card card : deck.getCards().getCard() ) {
            cardNames.add( card.getCardName() );
            mapping.put( counter, card );
            counter++;
        }
        controller.changeElementCardToDeckMap( mapping );
        return makeJList( cardNames.toArray() );
    }

    public static void outputTextFile( Object selection, AbstractMTGView view ) {
        int i = JOptionPane.showConfirmDialog( view, "Do you want to use this for Statistic Viewing?" );
        if( i != 0 && i != 1 ) {
            return;
        }
        boolean stats = (i == 0) ? true : false;
        Deck deckToSave = new Deck();
        for( Deck deck : MTGFileIO.instance().getCurDecks() ) {
            if( ((String) selection).equals( deck.getDeckName() ) ) {
                deckToSave = deck;
            }
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter(
                    new File( "MTGFiles/" + deckToSave.getDeckName() + "_stats-" + stats + ".txt" ) ) );
            String cardList = "";
            for( Card card : deckToSave.getCards().getCard() ) {
                if( stats ) {
                    cardList += card.getQuantity() + " " + card.getCardName();
                } else {
                    cardList += card.getCardName() + "," + card.getQuantity();
                }
                cardList += "\n";
            }
            writer.write( cardList );
            writer.flush();
            JOptionPane.showMessageDialog( view, deckToSave.getDeckName() + " saved to disk." );
        } catch( IOException e ) {
            JOptionPane.showMessageDialog( view, e );
            return;
        } finally {
            try {
                writer.close();
            } catch( Exception e ) {
            }
        }
    }

    private static JList makeJList( Object[] data ) {
        final JList jlCardList = new JList( data );
        jlCardList.setBackground( new java.awt.Color( 153, 153, 153 ) );
        jlCardList.setFont( new java.awt.Font( "Monospaced", 0, 12 ) );
        jlCardList.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        return jlCardList;
    }

    private static int getTotalCards( Deck deck ) {
        int ret = 0;
        for( Card c : deck.getCards().getCard() ) {
            ret += c.getQuantity();
        }
        return ret;
    }
}
