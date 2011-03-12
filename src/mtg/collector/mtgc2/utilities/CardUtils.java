package mtg.collector.mtgc2.utilities;

import mtg.collector.xml.org.Card;

/**
 * Utility methods for dealing with JAXB Card objects.
 * @author Eric Kisner
 */
public class CardUtils {

    public static boolean equals( Card a, Card b ) {
        return cardToString( a ).equals( cardToString( b ) );
    }

    /**
     * Returns a Card object as a string. Since the JAXB Card code is regenerated on rebuild, there is no point to
     * create a method in the JAXB class. Instead, we just need to use a static method here.
     * @param card The Card object we want to view as a string.
     * @return The string that represents this Card object.
     */
    public static String cardToString( Card card ) {
        String name = card.getCardName();
        String type = card.getCardType();
        String image = card.getImageLink();
        String quantity = String.valueOf( card.getQuantity() );

        String ret = "";
        ret += (name != null) ? formatString( "Card Name:", name ) : "";
        ret += (type != null) ? formatString( "Card Type:", type ) : "";
        ret += (image != null) ? formatString( "Card Image Link:", image ) : "";
        ret += formatString( "Quantity:", quantity );
        ret += "\n\n";
        return ret;
    }

    /**
     * Adds spaces to a string in order to make it line up with other strings.
     * @see CardUtils#cardToString(mtg.collector.xml.org.Card)
     */
    private static String formatString( String pre, String post ) {
        final int minStrLength = 20;
        String ret = pre;
        int length = pre.length();
        if( length < minStrLength ) {
            for( int i = 0; i < (minStrLength - length); i++ ) {
                ret += " ";
            }
        }
        return ret + post + "\n";
    }
}
