package mtg.collector.mtgc2.importing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mtg.collector.mtgc2.utilities.DeckParser;
import mtg.collector.xml.org.Card;
import mtg.collector.mtgc2.web.GathererConnector;

/**
 * Parses a text file, for user-made MTG files
 * @author Eric Kisner
 */
public class MTGTextParser {

    private final String CARDS = "-CARDS-";
    private final String DECKS = "-DECKS-";
    private StringBuffer cardsBuffer = new StringBuffer();
    private StringBuffer decksBuffer = new StringBuffer();
    private File file;
    private GathererConnector connector;

    /**
     * Default constructor
     * @param file The manually-made file
     * @throws IOException
     */
    public MTGTextParser( File file ) throws IOException, MTGFileFormatException {
        this.file = file;
        initAll();
    }

    public MTGTextParser( String var ) {
        //constructor for test purposes
        cardsBuffer = new StringBuffer( var );
    }

    public File getFile() {
        return file;
    }

    /**
     * Checks that the file isn't null, it is formatted correctly, and that
     * it contains some information
     * @throws IOException
     */
    private void initAll() throws IOException, MTGFileFormatException {
        connector = new GathererConnector();
        if( file == null ) {
            throw new MTGFileFormatException( "No file selected." );
        }
        StringBuffer fileContents = readString( file );
        int indexOfCards = fileContents.indexOf( CARDS );
        int indexOfDecks = fileContents.indexOf( DECKS );
        if( indexOfCards == -1 && indexOfDecks == -1 ) {
            throw new MTGFileFormatException( file.getName() );
        }
        if( indexOfCards > indexOfDecks ) {
            throw new MTGFileFormatException( file.getName() );
        }
        initBuffers( fileContents );
        if( cardsBuffer.length() == 0 && decksBuffer.length() == 0 ) {
            throw new MTGFileFormatException( file.getName() );
        }
    }

    /**
     * Parses cards in a file made by the user.
     * The file includes exact names for Magic cards, and should only return
     * one result per search on Gatherer
     * @return Empty list if format is incorrect or there is nothing to import.
     * Or return the list of cards in the file.
     * @throws IOException
     * @throws Exception
     */
    public ArrayList<Card> parseCards() throws IOException, MalformedURLException {
        if( cardsBuffer.length() == 0 ) {
            return new ArrayList<Card>();
        }

        ArrayList<Card> cardsInFile = new ArrayList<Card>();
        for( String str : cardsBuffer.toString().split( "\n" ) ) {
            if( str.length() < 2 ) {
                continue;
            }
            //cardComponents[0] is the card name, [1] is the quantity
            String[] cardComponents = str.split( "," );
            //Weird case where we have to trim the end and beginning white-space
            //or HttpURLConnection will not process correctly
            //TODO: Make a GUI or something
            ArrayList<Card> temp = connector.returnResults( cardComponents[0].trim() );
            //The cards in the file should only return one card result because
            //the user should be putting exact card names in the file
            if( temp.size() != 1 ) {
                return new ArrayList<Card>();
            }

            try {
                int quantity = Integer.parseInt( cardComponents[1].trim() );
                temp.get( 0 ).setQuantity( quantity );
            } catch( NumberFormatException e ) {
                return new ArrayList<Card>();
            }
            cardsInFile.add( temp.get( 0 ) );
        }
        return cardsInFile;
    }

    /**
     * Parses decks in a user-configured file.
     * The file includes card names and quantities in a specific format that
     * is used to parse a deck.
     * @return
     */
    public HashMap<String, List<String>> parseDecks() {
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        if( decksBuffer.length() == 0 ) {
            return ret;
        }

        ArrayList<String> temp = new ArrayList<String>();
        for( String str : decksBuffer.toString().split( "\n" ) ) {
            temp.add( str.trim() );
        }
        ret.putAll( DeckParser.parseDeckLists( temp ) );
        return ret;
    }

    /**
     * Simple method to read everything into a String from a file
     * @param file
     * @return
     * @throws IOException
     */
    private StringBuffer readString( File file ) throws IOException {
        int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader( file );
        try {
            while( (len = reader.read( chr )) > 0 ) {
                buffer.append( chr, 0, len );
            }
        } finally {
            reader.close();
        }
        return buffer;
    }

    /**
     * Initializes the separate buffers to their respective contents.
     * @param fileContents
     */
    private void initBuffers( StringBuffer fileContents ) {
        int beginCardsLoc = fileContents.indexOf( CARDS ) + 1;
        int prefixLngth = CARDS.length() + 1;
        int beginDecksLoc = fileContents.indexOf( DECKS ) + 1;
        int suffixLngth = DECKS.length() + 1;

        //If CARDS doesn't exist, only populate the decks buffer
        //IF DECKS doesn't exist, only populate the cards buffer
        if( beginCardsLoc < 0 ) {
            decksBuffer.append( fileContents.substring( beginDecksLoc
                                                        + suffixLngth ).toString() );
        } else if( beginDecksLoc < 0 ) {
            cardsBuffer.append( fileContents.substring( beginCardsLoc
                                                        + prefixLngth ).toString() );
        } else {
            //Card Contents begin after CARDS
            cardsBuffer.append( fileContents.substring( beginCardsLoc
                                                        + prefixLngth, beginDecksLoc ).toString() );
            //Deck Contents begin after DECKS
            decksBuffer.append( fileContents.substring( beginDecksLoc
                                                        + suffixLngth ).toString() );
        }
    }
}
