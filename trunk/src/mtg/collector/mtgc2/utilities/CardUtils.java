package mtg.collector.mtgc2.utilities;

import mtg.collector.xml.org.Card;

/**
 * Utility methods for dealing with JAXB Card objects.
 * @author Eric Kisner
 */
public class CardUtils {

    /**
     * Returns a Card object as a string. Since the JAXB Card object is regenerated, there is no point to
     * create a toString in the class. Instead, we just need to use a static method.
     * @param card
     * @return
     */
    public static String toString( Card card ) {
        String name = card.getCardName();
        String type = card.getCardType();
        String rarity = card.getRarity();
        String image = card.getImageLink();
        String power_toughness = card.getPowerToughness();
        String castingCost = card.getManaCost();
        String rulesText = card.getRulesText();
        String quantity = String.valueOf( card.getQuantity() );

        String ret = "";
        ret += (name != null) ? formatString( "Card Name:", name ) : "";
        ret += (type != null) ? formatString( "Card Type:", type ) : "";
        ret += (rarity != null) ? formatString( "Card Rarity:", rarity ) : "";
        ret += (image != null) ? formatString( "Card Image Link:", image ) : "";
        ret += (power_toughness != null) ? formatString( "Power/Toughness:", power_toughness ) : "";
        ret += (castingCost != null) ? formatString( "Casting Cost:", castingCost ) : "";
        ret += (rulesText != null) ? formatString( "Rules Text:", rulesText ) : "";
        ret += formatString( "Quantity:", quantity );
        ret += "\n\n";
        return ret;
    }

    /**
     * Adds spaces to a string in order to make it line up with other strings.
     * @see CardUtils#toString(mtg.collector.xml.org.Card) 
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
