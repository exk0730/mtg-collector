package mtg.collector.mtgc2.enums.web;

/**
 * Enum describing an HTML Attribute
 * @author Eric Kisner
 */
public enum HTMLAttributes {

    alt( "alt" ),
    href( "href" ),
    htmlclass( "class" ),
    id( "id" ),
    src( "src" );
    private String attribute;

    private HTMLAttributes( String attribute ) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }
}
