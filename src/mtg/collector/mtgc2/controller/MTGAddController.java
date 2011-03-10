package mtg.collector.mtgc2.controller;

import java.util.HashMap;
import mtg.collector.mtgc2.utilities.AddCardState;

/**
 *
 * @author Eric Kisner
 */
public class MTGAddController extends AbstractMTGController {

    public static final String ELEMENT_STATE_PROPERTY = "State";
    public static final String ELEMENT_ADD_CARD_PROPERTY = "AddCards";

    public void changeElementState( AddCardState state ) {
        setModelProperty( ELEMENT_STATE_PROPERTY, state );
    }

    public void changeElementAddCard( HashMap<?, ?> cardChoices ) {
        setModelProperty( ELEMENT_ADD_CARD_PROPERTY, cardChoices );
    }
}
