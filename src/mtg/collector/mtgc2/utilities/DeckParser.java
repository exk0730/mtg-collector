package mtg.collector.mtgc2.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mtg.collector.xml.org.Deck;
import mtg.collector.xml.org.Card;

/**
 * Static methods to help parse decks from a file.
 * @author Eric Kisner
 */
public class DeckParser {

    public static int getCardCount( Deck deck ) {
        int count = 0;
        for( Card card : deck.getCards().getCard() ) {
            count += card.getQuantity();
        }
        return count;
    }

    /**
     * Returns an array list of deck objects from a list of strings
     * @param strs
     * @return
     */
    public static HashMap<String, List<String>> parseDeckLists( ArrayList<String> allLists ) {
        int curDeckStart = 0;
        int curDeckEnd = 0;
        final String parenth = "(";
        final String end = "(end)";
        boolean curDeckFound = false;
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();

        for( int i = 0; i < allLists.size(); i++ ) {
            String str = allLists.get( i );
            if( !curDeckFound ) {
                if( str.contains( parenth ) ) {
                    curDeckStart = i;
                    curDeckFound = true;
                    continue;
                }
            } else {
                //If the next deck has been found, denoted by (end), process the
                //lines between start and end, and initialize a Deck object
                if( str.equals( end ) ) {
                    curDeckEnd = i;
                    curDeckFound = false;
                    ret.putAll( getAllContents( allLists, curDeckStart, curDeckEnd ) );
                }
            }
        }

        return ret;
    }

    /**
     * Returns a deck list as a Mapping of Deck name to Card names
     * @param curStrings
     * @param curDeckStart
     * @param curDeckEnd We don't want the last string in the deck because it's
     * not a valid card. So <code>curDeckEnd</code> is the very last line in the
     * file, specified by "(end)"
     * @return
     */
    private static HashMap<String, List<String>> getAllContents( ArrayList<String> deckList,
                                                                 int curDeckStart, int curDeckEnd ) {
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        List<String> cardList = new ArrayList<String>();

        String deckName = deckList.get( curDeckStart ).replace( "(", "" ).replace( ")", "" );
        for( int i = curDeckStart + 1; i < curDeckEnd; i++ ) {
            cardList.add( deckList.get( i ) );
        }
        ret.put( deckName, cardList );
        return ret;
    }
}
