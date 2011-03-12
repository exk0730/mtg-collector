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
import mtg.collector.xml.org.Cards;
import mtg.collector.xml.org.Deck;

import static mtg.collector.mtgc2.enums.mtgcard.CardType.*;

/**
 *
 * @author Eric Kisner
 */
public class DeckUtils {

    /**
     * Returns a Deck object as a string. Since the JAXB Deck code is regenerated on rebuild, there is no point to
     * create a method in the JAXB class. Instead, we just need to use a static method here.
     * @param card The Deck object we want to view as a string.
     * @return The string that represents this Deck object.
     */
    public static String deckToString( Deck deck ) {
        String ret = "";
        for( Card c : deck.getCards().getCard() ) {
            ret += CardUtils.cardToString( c );
        }
        return ret;
    }

    /**
     * Returns true if one deck is equal to another. This checks whether a deck's contents (or, its cards) are
     * all equal to each other, as well as the deck name.
     * @param a A deck we want to compare to another.
     * @param b A deck we want to compare to another.
     * @return True if deck a has the same contents as deck b, else false.
     */
    public static boolean equals( Deck a, Deck b ) {
        return deckToString( a ).equals( deckToString( b ) );
    }

    /**
     * Builds a deck object from a deck's name.
     * @param deckName The name of the deck we are looking for.
     * @return The deck object, or null if there are no such decks with this name.
     */
    public static Deck getDeckObj( String deckName ) {
        for( Deck deck : MTGFileIO.instance().getCurDecks() ) {
            if( deck.getDeckName().equals( deckName ) ) {
                return deck;
            }
        }
        return null;
    }

    /**
     * Returns all decks in the current XML database file as a JList.
     * @return A JList containing all deck names as its selections.
     */
    public static JList returnDeckJList() {
        MTGFileIO model = MTGFileIO.instance();
        ArrayList<String> ret = new ArrayList<String>();
        for( Deck deck : model.getCurDecks() ) {
            ret.add( deck.getDeckName() );
        }
        return makeJList( ret.toArray() );
    }

    /**
     * Returns a deck's card list as a JList.
     * @param selectedDeck The name of the deck we are going after.
     * @param controller
     * @return
     */
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

    public static Deck sortDeck( Deck deck ) {
        List<Card> instants = new ArrayList<Card>();
        List<Card> sorceries = new ArrayList<Card>();
        List<Card> enchantments = new ArrayList<Card>();
        List<Card> artifacts = new ArrayList<Card>();
        List<Card> creatures = new ArrayList<Card>();
        List<Card> lands = new ArrayList<Card>();
        List<Card> unknown = new ArrayList<Card>();

        Deck toRet = new Deck();
        toRet.setDeckName( deck.getDeckName() );
        Cards cards = new Cards();

        for( Card card : deck.getCards().getCard() ) {
            String type = card.getCardType();

            if( isInstant( type ) ) {
                instants.add( card );
            } else if( isSorcery( type ) ) {
                sorceries.add( card );
            } else if( isEnchantment( type ) ) {
                enchantments.add( card );
            } else if( isArtifact( type ) ) {
                artifacts.add( card );
            } else if( isCreature( type ) ) {
                creatures.add( card );
            } else if( isLand( type ) ) {
                lands.add( card );
            } else {
                unknown.add( card );
            }
        }
        cards.getCard().addAll( instants );
        cards.getCard().addAll( sorceries );
        cards.getCard().addAll( enchantments );
        cards.getCard().addAll( artifacts );
        cards.getCard().addAll( creatures );
        cards.getCard().addAll( lands );
        cards.getCard().addAll( unknown );
        toRet.setCards( cards );
        return toRet;
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
