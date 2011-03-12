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
        return types;
    }

    public static boolean contains( String type ) {
        Iterator<CardType> iter = types.iterator();
        while( iter.hasNext() ) {
            if( iter.next().getType().toLowerCase().contains( type.toLowerCase() ) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean isArtifact( String type ) {
        return (type.contains( Artifact.getType() ));
    }

    public static boolean isLand( String type ) {
        return (type.contains( Land.getType() ));
    }

    public static boolean isInstant( String type ) {
        return (type.contains( Instant.getType() ));
    }

    public static boolean isSorcery( String type ) {
        return (type.contains( Sorcery.getType() ));
    }

    public static boolean isCreature( String type ) {
        return (type.contains( Creature.getType() ));
    }

    public static boolean isEnchantment( String type ) {
        return (type.contains( Enchantment.getType() ));
    }
}
