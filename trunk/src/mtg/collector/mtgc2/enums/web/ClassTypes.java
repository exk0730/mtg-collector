package mtg.collector.mtgc2.enums.web;

/**
 * Class types found for the HTML attribute "class"
 * @author Eric Kisner
 */
public enum ClassTypes {

    leftCol( "\"leftCol\"" ),
    cardTitle( "\"cardTitle\"" ),
    typeLine( "\"typeLine\"" ),
    textBox( "\"cardtextbox\"" ),
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
