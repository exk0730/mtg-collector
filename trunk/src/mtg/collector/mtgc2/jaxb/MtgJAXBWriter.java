package mtg.collector.mtgc2.jaxb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import mtg.collector.mtgc2.utilities.Utils;
import mtg.collector.xml.org.*;

/**
 *
 * @author Eric Kisner
 */
public class MtgJAXBWriter {

    private final String DECKS = "decks";
    private final String CLASS_TO_BE_BOUND = "mtg.collector.xml.org";
    private File file;
    private JAXBContext jc;
    Decks decks;

    public MtgJAXBWriter( File file ) {
        this.file = file;
        init();
    }

    private void init() {
        try {
            jc = JAXBContext.newInstance( CLASS_TO_BE_BOUND );
            Unmarshaller um = jc.createUnmarshaller();
            if( file.getName().contains( DECKS ) ) {
                decks = (Decks) um.unmarshal( file );
            }
        } catch( JAXBException e ) {
            System.err.println( e.getMessage() );
            e.printStackTrace();
        }
    }

    public void writeDeck( Deck deck ) {
        deck = Utils.sortDeck( deck );
        decks.getDeck().add( deck );
        File out = file;
        try {
            constructMarshaller().marshal( decks, out );
        } catch( Exception e ) {
            System.err.println( e.getMessage() );
            e.printStackTrace();
            System.exit( 1 );
        }
    }

    /**
     *
     * @param deck
     */
    public void deleteDeck( Deck deck ) {
        List<Deck> curDecks = decks.getDeck();
        int index = -1;
        for( int i = 0; i < curDecks.size(); i++ ) {
            if( Utils.equals( deck, curDecks.get( i ) ) ) {
                index = i;
            }
        }
        if( index == -1 ) {
            return;
        }
        decks.getDeck().remove( index );
        File out = file;
        try {
            constructMarshaller().marshal( decks, out );
        } catch( Exception e ) {
            System.err.println( e.getMessage() );
            e.printStackTrace();
            System.exit( 1 );
        }
    }

    public void deleteCard( Deck deck, Card card ) {
        List<Card> cardList = deck.getCards().getCard();
        int index = -1;
        for( int i = 0; i < cardList.size(); i++ ) {
            if( Utils.equals( card, cardList.get( i ) ) ) {
                index = i;
            }
        }
        if( index == -1 ) {
            return;
        }
        deleteDeck( deck );
        deck.getCards().getCard().remove( index );
        writeDeck( deck );
    }

    public void append( ArrayList<Card> cardsToAppend, Deck d ) {
        for( Deck deck : decks.getDeck() ) {
            if( d.getDeckName().equals( deck.getDeckName() ) ) {
                for( Card card : cardsToAppend ) {
                    deck.getCards().getCard().add( card );
                }
                d = deck;
                deleteDeck( deck );
                writeDeck( d );
                break;
            }
        }
    }

    private Marshaller constructMarshaller() throws Exception {
        Marshaller m = jc.createMarshaller();
        m.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        return m;
    }
}
