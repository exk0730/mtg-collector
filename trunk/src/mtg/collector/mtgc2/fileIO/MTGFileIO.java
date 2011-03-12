package mtg.collector.mtgc2.fileIO;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JLabel;
import mtg.collector.mtgc2.jaxb.MtgJAXBReader;
import mtg.collector.mtgc2.jaxb.MtgJAXBWriter;
import mtg.collector.xml.org.Deck;
import mtg.collector.xml.org.Card;

/**
 * Class responsible for adding cards to the MTG File
 * @author Eric Kisner
 */
public class MTGFileIO {

    private final File DECKS_FILE = new File( "src/mtg/collector/xml/decks.xml" );
    public List<Deck> curDecksInFile;
    public HashMap<String, JLabel> images;
    public MtgJAXBReader reader = null;
    public MtgJAXBWriter writer = null;
    public static MTGFileIO instance = null;
    public boolean newDecks;

    public static MTGFileIO instance() {
        if( instance == null ) {
            instance = new MTGFileIO();
        }
        return instance;
    }

    private MTGFileIO() {
        importAll();
        newDecks = false;
    }

    public void setNewDecks( boolean newDecks ) {
        this.newDecks = newDecks;
    }

    private void importAll() {
        openDeckIO();
        curDecksInFile = reader.readDecks();
    }

    public void openDeckIO() {
        resetIO();
        reader = new MtgJAXBReader( DECKS_FILE );
        writer = new MtgJAXBWriter( DECKS_FILE );
    }

    private void resetIO() {
        reader = null;
        writer = null;
    }

    //--------------------------------------------------
    //-------- MTGDeckFile Handling Functions ----------
    //--------------------------------------------------
    /**
     * Writes a deck to deck.xml.
     * If the deck being imported is already in the database, the writer
     * will check if new cards need to be added to it.
     * Otherwise, write the entire deck to the file.
     * @param deck The deck to write to the XML deck file.
     */
    public boolean writeDeck( Deck deck ) {
        openDeckIO();
        ArrayList<Card> difference = reader.getCardDifference( deck );
        if( difference == null ) {
            writer.writeDeck( deck );
            return true;
        } else if( difference.isEmpty() ) {
            return false;
        } else {
            writer.append( difference, deck );
            return true;
        }
    }

    public void deleteDeck( Deck deckToDelete ) {
        writer.deleteDeck( deckToDelete );
        newDecks = true;
    }

    public void deleteCard( Deck deckToDeleteFrom, Card cardToDelete ) {
        writer.deleteCard( deckToDeleteFrom, cardToDelete );
        newDecks = true;
    }

    public List<Deck> getCurDecks() {
        if( newDecks ) {
            return importNewDecks();
        }
        return curDecksInFile;
    }

    private List<Deck> importNewDecks() {
        openDeckIO();
        curDecksInFile = reader.readDecks();
        newDecks = false;
        return curDecksInFile;
    }
}
