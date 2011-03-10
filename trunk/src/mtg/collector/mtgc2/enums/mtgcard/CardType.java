package mtg.collector.mtgc2.enums.mtgcard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Magic The Gathering Card Types
 * @author Eric Kisner
 */
public enum CardType {

    Artifact( "Artifact" ),
    Land( "Land" ),
    Instant( "Instant" ),
    Sorcery( "Sorcery" ),
    Creature( "Creature" ),
    Enchantment( "Enchantment" ),
    Planeswalker( "Planeswalker" ),
    LegendaryArtifact( "Legendary Artifact" ),
    LegendaryLand( "Legendary Land" ),
    LegendaryCreature( "Legendary Creature" ),
    LegendaryEnchantment( "Legendary Enchantment" );
    private String type;
    private final static ArrayList<CardType> types = new ArrayList<CardType>();

    static {
        types.addAll( Arrays.asList( CardType.values() ) );
    }

    private CardType( String type ) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ArrayList<CardType> allTypes() {
        return new ArrayList<CardType>( types );
    }

    public static boolean contains( String type ) {
        Iterator<CardType> iter = types.iterator();
        while( iter.hasNext() ) {
            if( iter.next().getType().contains( type ) ) {
                return true;
            }
        }
        return false;
    }
}
