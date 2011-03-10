package mtg.collector.mtgc2.enums.web;

/**
 * Id types found for the HTML attribute "id"
 * Usually within a "div" tag
 * @author Eric Kisner
 */
public enum IdTypes {

    nameRow( "_nameRow\"" ),
    manaRow( "_manaRow\"" ),
    typeRow( "_typeRow\"" ),
    rulesRow( "_textRow\"" ),
    ptRow( "_ptRow\"" ),
    rarityRow( "_rarityRow\"" );
    private String idType;

    private IdTypes( String idType ) {
        this.idType = idType;
    }

    public String getIdType() {
        return idType;
    }
}
