package mtg.collector.mtgc2.utilities;

/**
 *
 * @author Eric Kisner
 */
public class StringUtils {

    /**
     * Counts the number of str in sub
     */
    public static int countMatches( String str, String sub ) {
        int count = 0;
        int idx = 0;
        while( (idx = str.toLowerCase().indexOf( sub, idx )) != -1 ) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     * Helper method to return only the needed information out of the given string.
     * The start and end characters will not be included in the returned string
     * @param cardContents String to parse the information out of
     * @param charToStartAt Start parsing at this string sequence
     * @param charToEndAt End parsing at this string sequence
     * @return
     */
    public static String parseInfo( String cardContents, String charToStartAt, String charToEndAt ) {
        int startTextLoc = cardContents.indexOf( charToStartAt )
                           + charToStartAt.length();
        int endTextLoc = cardContents.indexOf( charToEndAt, startTextLoc + 1 );
        return cardContents.substring( startTextLoc, endTextLoc );
    }

    /**
     * Same as above method except it keeps the start and end string sequences.
     * @param cardContents
     * @param charToStartAt
     * @param charToEndAt
     * @return
     */
    public static String parseInfoInclusive( String cardContents, String charToStartAt, String charToEndAt ) {
        int startTextLoc = cardContents.indexOf( charToStartAt );
        int endTextLoc = cardContents.indexOf( charToEndAt, startTextLoc + 1 )
                         + charToEndAt.length();
        return cardContents.substring( startTextLoc, endTextLoc );
    }

    /**
     * Test
     * @param args
     */
    public static void main( String[] args ) {
        String str = "Blah has broken blah of the blah with the blah.";
        String toMatch = "blah";
        System.out.println( StringUtils.countMatches( str, toMatch ) );
    }
}
