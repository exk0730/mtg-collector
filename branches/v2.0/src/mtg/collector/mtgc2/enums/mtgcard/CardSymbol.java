package mtg.collector.mtgc2.enums.mtgcard;

/**
 *
 * @author Eric Kisner
 */
public enum CardSymbol {

    Tap( "Tap" ),
    Untap( "Untap" ),
    Case1( "Two or Black" ),
    X( "Variable Colorless" );
    private String symbol;

    private CardSymbol( String symbol ) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
