package mtg.collector.mtgc2.web;

import mtg.collector.mtgc2.utilities.WebParsingUtils;
import mtg.collector.mtgc2.enums.web.IdTypes;
import mtg.collector.mtgc2.enums.web.ClassTypes;
import mtg.collector.mtgc2.enums.web.HTMLTags;
import mtg.collector.mtgc2.enums.web.HTMLAttributes;
import mtg.collector.xml.org.Card;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * This class takes a card name string, and searches the Internet for it using
 * the database of MTG cards at wizards.com/gatherer.
 * It parses all HTML received based on the search, and is able to return a Card
 * object, which a user can decide to put into their collection (an XML file).
 * @author Eric Kisner
 */
public class GathererConnector {

    private final String BASE_URL = "http://gatherer.wizards.com";
    private final String SEARCH_STRING = BASE_URL + "/Pages/Search/Default.aspx?name=";

    /**
     * Returns the card results, mimicking an actual MTG Gatherer web search.
     * @param cardName The card's name that the user is searching for.
     * @return ArrayList A list of cards that have the given card name.
     */
    public ArrayList<Card> returnResults( final String cardName ) throws MalformedURLException, IOException {
        String curCard = "";
        String searchStr = "";
        ArrayList<Card> cards = new ArrayList<Card>();
        boolean cardsFound = false;
        boolean singleCardFound = false;

        String[] cardArray = cardName.split( " " );
        for( String str : cardArray ) {
            searchStr += "+[" + str + "]";
        }
        URL url = new URL( SEARCH_STRING + searchStr );
        BufferedReader reader = new BufferedReader( new InputStreamReader(
                initConnection( url ).getInputStream() ) );

        String line = null;
        /**
         * Keep reading lines until a card is found (defined by a certain
         * HTML sequence) then when the end of that HTML code is found,
         * parse a card object out of the HTML that was returned.
         *
         * Also, the user may search for the exact name of the card, so we
         * need to handle that case by only searching for a single card
         * which is defined by a different series of HTML code.
         */
        while( (line = reader.readLine()) != null ) {
            if( !cardsFound && !singleCardFound ) {
                if( startOfCard( line ) ) {
                    curCard = line;
                    cardsFound = true;
                } else if( startOfSingleCard( line ) ) {
                    curCard = line;
                    singleCardFound = true;
                }
            } else {
                curCard += line;
                if( singleCardFound ) {
                    if( endOfSingleCard( line ) ) {
                        Card card = parseSingleCard( curCard );
                        cards.add( card );
                        break;
                    }
                } else if( cardsFound ) {
                    if( endOfCard( line ) ) {
                        //Get the information of this card from the
                        //HTML-encoded line
                        Card card = parseCard( curCard );
                        cards.add( card );
                        curCard = "";
                        cardsFound = false;
                    }
                }
            }
        }
        //If the card is a single-word card name, the user only wants that card
        cards = checkSingleCardName( cardName, cards );
        try {
            return cards;
        } finally {
            reader.close();
        }
    }

    /**
     * Retrieve the connection to the given URL
     * @param url
     * @return
     * @throws Exception
     */
    private HttpURLConnection initConnection( URL url ) throws IOException {
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod( "GET" );
        huc.connect();
        return huc;
    }

    /**
     * Find the start of a card in the HTML code
     * Single-search
     * @param line
     * @return
     */
    private boolean startOfSingleCard( String line ) {
        return line.contains( "<!-- Card Details Table -->" );
    }

    /**
     * Find the end of a card in the HTML code
     * Single-search
     * @param line
     * @return
     */
    private boolean endOfSingleCard( String line ) {
        return line.contains( "<!-- End Card Details Table -->" );
    }

    /**
     * Find the start of a card in the HTML code
     * Multi-card search
     * @param line
     * @return
     */
    private boolean startOfCard( String line ) {
        return classFound( line, ClassTypes.cardItem );
    }

    /**
     * Find the end of the card in the HTML code.
     * The end line is 24 characters long and contains </table>
     * @param line
     * @return
     */
    private boolean endOfCard( String line ) {
        String toSearch = HTMLTags.end_table.getHtmlTag();
        final int endLineLength = 24;
        if( line.contains( toSearch ) && line.length() == endLineLength ) {
            return true;
        }
        return false;
    }

    /**
     * Finds the HTML code that contains card information and passes only that
     * portion to getCardObjFromHtml to create a Card object
     * @param cardToPrint
     * @return
     */
    private Card parseCard( String cardToPrint ) {
        String name = classContents( cardToPrint, ClassTypes.cardTitle, HTMLTags.end_span );
        String imageLink = classContents( cardToPrint, ClassTypes.leftCol, HTMLTags.start_td );
        String type = classContents( cardToPrint, ClassTypes.typeLine, HTMLTags.end_span );
        /** Uncomment for testing
        System.out.println("Name:\t" + name);
        System.out.println("Type:\t" + type);
        System.out.println("Image:\t" + imageLink);
         */
        return getCardObjFromHtml( name, type, imageLink, "" );
    }

    /**
     * A Gatherer search returns a different set of HTML code for single-card
     * matches than Multi-card matches, so we need to parse the code a different
     * way than parseCard.
     * @param cardToPrint
     * @return
     */
    private Card parseSingleCard( String cardToPrint ) {
        String name = idContents( cardToPrint, IdTypes.nameRow, HTMLTags.end_div );
        String type = idContents( cardToPrint, IdTypes.typeRow, HTMLTags.end_div );
        String imageLink = classContents( cardToPrint, ClassTypes.leftCol, HTMLTags.start_td );

        /** Uncomment for testing
        System.out.println("Name:\t" + name);
        System.out.println("Type:\t" + type);
        System.out.println("Image:\t" + imageLink);
         */
        return getCardObjFromHtml( name, type, imageLink );
    }

    /**
     * Takes portions of HTML code which contain the needed information to create
     * a card object. Used for Multi-Card searches.
     * @param name Portion of HTML code which contains the card name
     * @param type Portion of HTML code which contains the card type
     * @param image Portion of HTML code which contains the card image link
     * @param garbage Garbage parameter to distinguish between different methods of the same name.
     * @return Card object
     */
    private Card getCardObjFromHtml( String name, String type, String image, String garbage ) {
        String cardName = WebParsingUtils.parseCardName( name );
        String cardType = "";
        String imageLink = BASE_URL + WebParsingUtils.parseImageLink( image );
        String power_toughness = WebParsingUtils.parsePowerToughness( type );
        if( power_toughness != null ) {
            cardType = WebParsingUtils.parseType( type.replace( power_toughness, "" ) );
        } else {
            cardType = WebParsingUtils.parseType( type );
        }

        Card ret = new Card();
        ret.setCardName( cardName );
        ret.setImageLink( imageLink );
        ret.setCardType( cardType );

        return ret;
    }

    /**
     * Takes portions of HTML code which contain the needed information to create
     * a card object. Used for Single-Card searches.
     * @see GathererConnector#getCardObjFromHtml(java.lang.String, java.lang.String, java.lang.String, java.lang.String) 
     */
    private Card getCardObjFromHtml( String name, String type, String image ) {
        String cardName = WebParsingUtils.parseSingleCardName( name );
        String cardType = WebParsingUtils.parseSingleType( type );
        String imageLink = BASE_URL + WebParsingUtils.parseSingleImageLink( image );

        Card ret = new Card();
        ret.setCardName( cardName );
        ret.setImageLink( imageLink );
        ret.setCardType( cardType );
        return ret;
    }

    /**
     * Checks if a certain class is found.
     * Example: <span class="cardTitle">
     * @param line
     * @param classType
     * @return
     */
    private boolean classFound( String line, ClassTypes classType ) {
        String toSearch = HTMLAttributes.htmlclass.getAttribute() + "="
                          + classType.getClassType();
        return( line.contains( toSearch ) );
    }

    /**
     * Gets only the contents within the current tag of a specified class.
     * Example: <span class="cardTitle"> CONTENTS </span>
     * @param line
     * @param classType Specifies what class the current tag is associated with.
     * @param endTag
     * @return
     */
    private String classContents( String line, ClassTypes classType, HTMLTags endTag ) {
        String classStart = HTMLAttributes.htmlclass.getAttribute() + "="
                            + classType.getClassType();
        int classStartLoc = line.indexOf( classStart ) + classStart.length() + 1;
        int endTagLoc = line.indexOf( endTag.getHtmlTag(), classStartLoc );
        return line.substring( classStartLoc, endTagLoc );
    }

    /**
     * Gets only the contents within the current div, which is specified by the
     * div's id attribute. This method should be used when a single card is
     * returned from a search.
     *
     * @param line An example string input:
     * <div id="ctl00_ctl00_ctl00_MainContent_SubContent_SubContent_nameRow" class="row">
     * <div class="label">
     * Card Name:</div>
     * <div class="value">
     * Akroma, Angel of Wrath</div>
     * </div>
     *
     * @param idType The id type we are looking for. In the above sample input, we would want to find the second-to-last
     * line which we can then further parse to get the name of the card.
     * @param endTag Where we should stop searching. In this case, it would be "</div>".
     * @return Example string output:
     * "                  Akroma, Angel of Wrath</div>"
     */
    private String idContents( String line, IdTypes idType, HTMLTags endTag ) {
        final String PADDING = "                    ";
        String idStart = idType.getIdType();
        int idStartLoc = line.indexOf( idStart ) + idStart.length() + 1;
        String valueDivTag = ClassTypes.value.getClassType();
        int valueDivTagLoc = line.indexOf( valueDivTag, idStartLoc )
                             + valueDivTag.length() + 1;
        final String endTagStr = PADDING + endTag.getHtmlTag();
        int endTagLoc = line.indexOf( endTagStr, valueDivTagLoc );
        String idText = line.substring( valueDivTagLoc, endTagLoc );
        return idText;
    }

    /**
     * If the user searches for a card whose full name may be contained in other card's names, return only the
     * single-named card, or all cards with the given search name if a single-card does not exist.
     * @param cardName The card name that the user searched for.
     * @param cards The cards that were returned by Gatherer search with the given name.
     * 
     * @return An array list of Card objects. It will either have a single element, which means that there is
     * a card that exactly matches the searched name, or it will have multiple Card elements, which means
     * that there are multiple cards that have the same name contained within the search name.
     * For example: If I search Akroma - this should return a handful of cards that ALL contain the word "Akroma" in
     * them. However, if I search for Forest, there may be a lot of cards with the name "Forest" in them, but
     * there is really only one card that should match this search: the basic-land Forest.
     */
    private ArrayList<Card> checkSingleCardName( String cardName, ArrayList<Card> cards ) {
        ArrayList<Card> ret = new ArrayList<Card>();

        for( Card card : cards ) {
            if( card.getCardName().toLowerCase().equals( cardName.toLowerCase() ) ) {
                ret.add( card );
                return ret;
            }
        }
        return cards;
    }
}
