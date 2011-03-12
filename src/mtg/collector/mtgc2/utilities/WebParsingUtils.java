package mtg.collector.mtgc2.utilities;

import mtg.collector.mtgc2.enums.web.HTMLTags;
import mtg.collector.mtgc2.enums.web.HTMLAttributes;

/**
 * Static methods to parse certain card information out of the HTML-encdoded
 * string. This class should only be used when multiple cards are returned
 * from the search.
 * @author Eric Kisner
 */
public class WebParsingUtils {

    private static final String IMG_TAG = HTMLTags.start_img.getHtmlTag();

    /**
     * Parse a card's name out of the given string
     * @param cardContents
     * @return
     */
    public static String parseCardName( String cardContents ) {
        final String charToStartAt = ">";
        final String charToEndAt = "</a>";
        return StringUtils.parseInfo( cardContents, charToStartAt, charToEndAt );
    }

    /**
     * Parse a card's type out of the given string
     * @param cardContents
     * @return
     */
    public static String parseType( String cardContents ) {
        return cardContents.trim();
    }

    /**
     * Parse a card's power/toughness out of the given string
     * @param cardContents Power/Toughness is contained within the "Type"
     * string, so this parameter will usually be type
     * @return
     */
    public static String parsePowerToughness( String cardContents ) {
        final String charToStartAt = "(";
        final String charToEndAt = ")";
        if( cardContents.contains( charToStartAt ) ) {
            return StringUtils.parseInfoInclusive( cardContents, charToStartAt, charToEndAt );
        } else {
            return null;
        }

    }


    /**
     * Parse a card's image html link out of the given string
     * @param cardContents
     * @return
     */
    public static String parseImageLink( String cardContents ) {
        final String charToStartAt = IMG_TAG + " " + HTMLAttributes.src + "=\"";
        final String charToEndAt = "\"";
        String link = StringUtils.parseInfo( cardContents, charToStartAt, charToEndAt );
        link = link.replace( "&amp;", "&" );
        link = link.replaceFirst( "../..", "" );
        return link;
    }


    //---------------------------------------
    //Single Card Parsing methods
    //---------------------------------------
    /**
     * Card name for a single-card search
     * @param cardContents
     * @return
     */
    public static String parseSingleCardName( String cardContents ) {
        return cardContents.replace( HTMLTags.end_div.getHtmlTag(), "" ).trim();
    }

    /**
     * Card type for single-card search
     * @param cardContents
     * @return
     */
    public static String parseSingleType( String cardContents ) {
        return cardContents.replace( HTMLTags.end_div.getHtmlTag(), "" ).trim();
    }

    /**
     * Use WebParsingUtils.parseImageLink for parsing image of single-card search
     * @param cardContents
     * @return
     */
    public static String parseSingleImageLink( String cardContents ) {
        String ret = cardContents.substring( cardContents.indexOf( IMG_TAG ) );
        return WebParsingUtils.parseImageLink( ret );
    }
}
