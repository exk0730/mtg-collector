package mtg.collector.mtgc2.utilities;

import java.util.ArrayList;
import java.util.List;
import mtg.collector.xml.org.Card;
import mtg.collector.xml.org.Cards;
import mtg.collector.xml.org.Deck;

/**
 *
 * @author Eric Kisner
 */
public class Utils {

    public static String toString( Card card ) {
        String ret = "";
        ret += "Card Name: " + card.getCardName() + "\n";
        ret += "Card Type: " + card.getCardType() + "\n";
        ret += "Card Mana: " + card.getManaCost() + "\n";
        ret += "Card P/T: " + card.getPowerToughness() + "\n";
        ret += "Card Rarity: " + card.getRarity() + "\n";
        ret += "Card Rules: " + card.getRulesText() + "\n";
        ret += "Card Image Link: " + card.getImageLink() + "\n";
        return ret;
    }

    public static String toString( Deck deck ) {
        String ret = "";
        for( Card c : deck.getCards().getCard() ) {
            ret += "\n" + c.getCardName();
        }
        return ret;
    }

    public static boolean equals( Deck a, Deck b ) {
        String retA = Utils.toString( a );
        String retB = Utils.toString( b );
        if( retA.equals( retB ) ) {
            return true;
        }
        return false;
    }

    public static boolean equals( Card a, Card b ) {
        String retA = Utils.toString( a );
        String retB = Utils.toString( b );
        if( retA.equals( retB ) ) {
            return true;
        }
        return false;
    }

    public static Deck sortDeck( Deck deck ) {
        final String i = "instant";
        final String s = "sorcery";
        final String e = "enchant";
        final String a = "artifact";
        final String c = "creature";
        final String l = "land";
        List<Card> instants = new ArrayList<Card>();
        List<Card> sorceries = new ArrayList<Card>();
        List<Card> enchantments = new ArrayList<Card>();
        List<Card> artifacts = new ArrayList<Card>();
        List<Card> creatures = new ArrayList<Card>();
        List<Card> lands = new ArrayList<Card>();
        List<Card> unknown = new ArrayList<Card>();

        Deck toRet = new Deck();
        toRet.setDeckName( deck.getDeckName() );
        Cards cards = new Cards();

        for( Card card : deck.getCards().getCard() ) {
            String type = card.getCardType().toLowerCase();

            if( type.contains( i ) ) {
                instants.add( card );
            } else if( type.contains( s ) ) {
                sorceries.add( card );
            } else if( type.contains( e ) ) {
                enchantments.add( card );
            } else if( type.contains( a ) ) {
                artifacts.add( card );
            } else if( type.contains( c ) ) {
                creatures.add( card );
            } else if( type.contains( l ) ) {
                lands.add( card );
            } else {
                unknown.add( card );
            }
        }
        cards.getCard().addAll( instants );
        cards.getCard().addAll( sorceries );
        cards.getCard().addAll( enchantments );
        cards.getCard().addAll( artifacts );
        cards.getCard().addAll( creatures );
        cards.getCard().addAll( lands );
        cards.getCard().addAll( unknown );
        toRet.setCards( cards );
        return toRet;
    }
}
