package mtg.collector.mtgc2.utilities;

import mtg.collector.mtgc2.enums.web.HTMLTags;
import mtg.collector.mtgc2.enums.web.ClassTypes;
import mtg.collector.mtgc2.enums.web.HTMLAttributes;
import mtg.collector.mtgc2.enums.mtgcard.CardColor;
import mtg.collector.mtgc2.enums.mtgcard.CardSymbol;
import mtg.collector.mtgc2.enums.mtgcard.CardType;
import mtg.collector.mtgc2.utilities.StringUtils;

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
     * Parse a card's mana cost out of the given string
     * @param cardContents
     * @return
     */
    public static String parseCastingCost( String cardContents ) {
        if( cardContents.contains( CardType.Land.getType() ) ) {
            return "0";
        }

        String manaCost = "";
        String[] imgs = cardContents.trim().split( IMG_TAG );
        //Need to start at 1 because imgs[0] contains blank space
        for( int i = 1; i < imgs.length; i++ ) {
            manaCost += parseManaSymbol( imgs[i] );
        }
        return manaCost;
    }

    /**
     * Parse a card's rarity out of the given string
     * @param cardContents
     * @return
     */
    public static String parseRarity( String cardContents ) {
        final String charToStartAt = IMG_TAG;
        final String charToEndAt = " />";
        String rarity = StringUtils.parseInfo( cardContents, charToStartAt, charToEndAt );
        return StringUtils.parseInfo( rarity, "rarity=", "\"" );
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
     * Parse the card's rules text out of the given string
     * @param cardContents
     * @return
     */
    public static String parseRulesText( String cardContents ) {
        final String pStart = HTMLTags.start_p.getHtmlTag();
        final String pEnd = HTMLTags.end_p.getHtmlTag();
        final String textBox = HTMLTags.start_div.getHtmlTag() + " "
                               + HTMLAttributes.htmlclass.getAttribute() + "="
                               + ClassTypes.textBox.getClassType() + ">";
        final String divEnd = HTMLTags.end_div.getHtmlTag();
        String rulesText = "";
        String toSplit = "";

        if( cardContents.contains( pStart ) ) {
            rulesText = cardContents.replace( pEnd, "" ).trim();
            toSplit = pStart;
        } else {
            rulesText = cardContents.replace( divEnd, "" ).trim();
            toSplit = textBox;
        }

        String[] paragraphs = rulesText.split( toSplit );
        rulesText = "";

        for( int i = 0; i < paragraphs.length; i++ ) {
            //If the rules text on the card contains any mana costs, we need
            //to parse that out.
            if( paragraphs[i].contains( IMG_TAG ) ) {
                int imgCount = StringUtils.countMatches( paragraphs[i], IMG_TAG );
                while( imgCount != 0 ) {
                    final String charToStartAt = IMG_TAG;
                    final String charToEndAt = " />";
                    String curImg = StringUtils.parseInfoInclusive(
                            paragraphs[i], charToStartAt, charToEndAt );
                    String manaCost = parseManaSymbol( curImg );
                    paragraphs[i] = paragraphs[i].replace( curImg, manaCost );
                    //If a mana cost has the same colors, the above line will
                    //replace all instances so we don't need to replace anymore
                    if( !paragraphs[i].contains( IMG_TAG ) ) {
                        break;
                    }
                    imgCount--;
                }
            }
            rulesText += paragraphs[i] + " ";
        }
        //We don't need the reminder text, so let's take that out.
        final String remTxt_start = "<i>";
        final String remTxt_end = "</i>";

        if( rulesText.contains( remTxt_start ) ) {
            String reminderText = rulesText.substring( rulesText.indexOf( remTxt_start ),
                                                       rulesText.indexOf( remTxt_end ) + remTxt_end.length() );
            rulesText = rulesText.replace( reminderText, "" );
        }
        return rulesText;
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

    /**
     * Helper method to parse a mana cost from the given string
     * @param str An example input-string would be:
     * <img src="/Handlers/Image.ashx?size=small&amp;name=5&amp;type=symbol" alt="5" align="absbottom" />
     * @return
     */
    private static String parseManaSymbol( String str ) {
        Integer colorlessMana;
        final String charToStartAt = HTMLAttributes.alt + "=\"";
        final String charToEndAt = "\"";
        String manaSymbol = StringUtils.parseInfo( str, charToStartAt, charToEndAt );
        //We need to check for the tap, untap, or X symbol
        if( containsSymbol( manaSymbol ) ) {
            if( manaSymbol.equals( CardSymbol.X.getSymbol() ) ) {
                manaSymbol = "X";
            }
            return "(" + manaSymbol.toUpperCase() + ")";
        }
        try {
            colorlessMana = Integer.parseInt( manaSymbol );
        } catch( NumberFormatException nfe ) {
            //Expected if this portion of the mana cost is a Mana symbol
            colorlessMana = null;
        }
        if( colorlessMana != null ) {
            return "(" + colorlessMana + ")";
        } else {
            return "(" + CardColor.shorthandColor( CardColor.getColor( manaSymbol ) ) + ")";
        }
    }

    /**
     * Checks if this mana string contains Tap Untap or Variable Cost (X)
     * Example: alt="Variable Colorless" returns true
     * @param str The mana string to check
     * @return
     */
    private static boolean containsSymbol( String str ) {
        final String case1 = CardSymbol.Case1.getSymbol();
        final String tap = CardSymbol.Tap.getSymbol();
        final String untap = CardSymbol.Untap.getSymbol();
        final String varCost = CardSymbol.X.getSymbol();
        if( str.contains( tap ) || str.contains( untap ) || str.contains( varCost ) || str.contains( case1 ) ) {
            return true;
        }
        return false;
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

    public static String parseSinglePT( String cardContents ) {
        String ret = cardContents.replace( HTMLTags.end_div.getHtmlTag(), "" ).trim();
        ret = ret.replace( " ", "" );
        return "(" + ret + ")";
    }

    /**
     * Card rarity for single-card search
     * @param cardContents
     * @return
     */
    public static String parseSingleRarity( String cardContents ) {
        final String charToStartAt = "\'";
        final String charToEndAt = "\'";
        String rarity = StringUtils.parseInfo( cardContents, charToStartAt, charToEndAt );
        return rarity.substring( 0, 1 ).toUpperCase();
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

    /**
     * Use WebParsingUtils.parseCastingCost for parsing cc of single-card search
     * @param cardContents
     * @return
     */
    public static String parseSingleCastingCost( String cardContents ) {
        return WebParsingUtils.parseCastingCost( cardContents );
    }

    /**
     * Use WebParsingUtils.parseRulesText for parsing rules text of single-card search
     * @param cardContents
     * @return
     */
    public static String parseSingleRulesText( String cardContents ) {
        return WebParsingUtils.parseRulesText( cardContents );
    }
}
