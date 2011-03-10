package mtg.collector.mtgc2.enums.mtgcard;

import java.util.Iterator;
import java.util.Arrays;

/**
 * All combinations of Magic The Gathering card colors.
 * @author Eric Kisner
 */
public enum CardColor {

    Red( "Red" ), RedOrWhite( "Red or White" ), RedOrBlack( "Red or Black" ),
    RedOrGreen( "Red or Green" ), RedOrBlue( "Red or Blue" ),
    White( "White" ), WhiteOrRed( "White or Red" ), WhiteOrBlack( "White or Black" ),
    WhiteOrGreen( "White or Green" ), WhiteOrBlue( "White or Blue" ),
    Black( "Black" ), BlackOrRed( "Black or Red" ), BlackOrWhite( "Black or White" ),
    BlackOrGreen( "Black or Green" ), BlackOrBlue( "Black or Blue" ),
    Green( "Green" ), GreenOrRed( "Green or Red" ), GreenOrWhite( "Green or White" ),
    GreenOrBlack( "Green or Black" ), GreenOrBlue( "Green or Blue" ),
    Blue( "Blue" ), BlueOrRed( "Blue or Red" ), BlueOrWhite( "Blue or White" ),
    BlueOrBlack( "Blue or Black" ), BlueOrGreen( "Blue or Green" );
    //TwoOrBlack( "Two or Black" ); //edge case (Beseech the Queen)
    private String color;

    private CardColor( String color ) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static CardColor getColor( String color ) {
        Iterator<CardColor> iter = Arrays.asList( CardColor.values() ).iterator();
        while( iter.hasNext() ) {
            CardColor curColor = iter.next();
            if( curColor.getColor().equals( color ) ) {
                return curColor;
            }
        }
        return null;
    }

    public static String shorthandColor( CardColor color ) {
        String ret;
        char[] colorSH;
        String temp = color.getColor();
        if( temp.contains( "or" ) ) {
            colorSH = shorthandColor( temp.split( " or " ) );
            ret = colorSH[0] + "/" + colorSH[1];
        } else {
            colorSH = shorthandColor( color.getColor() );
            ret = String.valueOf( colorSH[0] );
        }

        return ret;
    }

    private static char[] shorthandColor( String... colors ) {
        char[] colorChars = new char[colors.length];
        for( int i = 0; i < colors.length; i++ ) {
            char shorthandColor = colors[i].charAt( 0 );

            switch( shorthandColor ) {
                case 'B':
                    if( colors[i].equals( "Blue" ) ) {
                        shorthandColor = 'U';
                    } else {
                        shorthandColor = 'B';
                    }
                    break;
                default:
                    break;
            }
            colorChars[i] = shorthandColor;
        }
        return colorChars;
    }
}
