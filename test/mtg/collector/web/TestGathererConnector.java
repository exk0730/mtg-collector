package mtg.collector.web;

import mtg.collector.mtgc2.web.GathererConnector;
import mtg.collector.mtgc2.utilities.CardUtils;

/**
 *
 * @author Eric Kisner
 */
public class TestGathererConnector {

    public static void main( String[] args ) {
        final String cardName = "Howling Mine";

        System.out.println( "Returning cards for cardname " + cardName + "...\n" );
        try {
            mtg.collector.xml.org.Card ret = new GathererConnector().returnResults( cardName ).get( 0 );
            System.out.println( CardUtils.toString( ret ) );
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
