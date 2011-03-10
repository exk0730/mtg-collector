package mtg.collector.mtgc2.jaxb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import mtg.collector.xml.org.*;

/**
 *
 * @author Eric Kisner
 */
public class MtgJAXBReader {

    private final String DECKS = "decks";
    private final String CLASS_TO_BE_BOUND = "mtg.collector.xml.org";
    private File file;
    Decks decks;

    public MtgJAXBReader( File file ) {
        this.file = file;
        init();
    }

    private void init() {
        try {
            JAXBContext jc = JAXBContext.newInstance( CLASS_TO_BE_BOUND );
            Unmarshaller um = jc.createUnmarshaller();
            if( file.getName().contains( DECKS ) ) {
                decks = (Decks) um.unmarshal( file );
            }
        } catch( JAXBException e ) {
            System.err.println( e.getMessage() );
            e.printStackTrace();
            System.exit( 1 );
        }
    }

    /**
     * Returns the list of deck elements in the XML file
     * @return
     */
    public List<Deck> readDecks() {
        return decks.getDeck();
    }

    public ArrayList<Card> getCardDifference( Deck deckToWrite ) {
        ArrayList<Card> difference = new ArrayList<Card>();

        Deck deckInFile = getDeckOnName( deckToWrite );
        if( deckInFile == null ) {
            return null;
        }
        List<Card> deckCards_import = getCardsInDeck( deckToWrite );

        for( Card curCard_import : deckCards_import ) {
            if( cardMatch( curCard_import, deckInFile ) ) {
                continue;
            } else {
                difference.add( curCard_import );
            }
        }
        return difference;
    }

    private boolean cardMatch( Card card, Deck deck ) {
        for( Card curCard : getCardsInDeck( deck ) ) {
            if( card.getCardName().equals( curCard.getCardName() ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a deck in the XML file with the same name as a
     * deck being imported.
     * @param deck Deck that is going to be imported
     * @return
     */
    private Deck getDeckOnName( Deck deckToImport ) {
        for( Deck deckFromFile : readDecks() ) {
            if( deckToImport.getDeckName().equals( deckFromFile.getDeckName() ) ) {
                return deckFromFile;
            }
        }
        return null;
    }

    private List<Card> getCardsInDeck( Deck deck ) {
        return deck.getCards().getCard();
    }
}
