package mtg.collector.mtgc2.enums.web;

/**
 * Class types found for the HTML attribute "class"
 * @author Eric Kisner
 */
public enum ClassTypes {

    leftCol( "\"leftCol\"" ),
    cardInfo( "\"cardInfo\"" ),
    cardTitle( "\"cardTitle\"" ),
    manaCost( "\"manaCost\"" ),
    typeLine( "\"typeLine\"" ),
    textBox( "\"cardtextbox\"" ),
    rulesText( "\"rulesText\"" ),
    setVersions( "\"rightCol\"" ),
    cardItem( "\"cardItem " ),
    value( "\"value\"" );
    private String classType;

    private ClassTypes( String classType ) {
        this.classType = classType;
    }

    public String getClassType() {
        return classType;
    }
}
