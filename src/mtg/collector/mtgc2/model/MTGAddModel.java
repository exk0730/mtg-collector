package mtg.collector.mtgc2.model;

import java.util.HashMap;
import mtg.collector.mtgc2.utilities.AddCardState;
import mtg.collector.mtgc2.controller.MTGAddController;

/**
 *
 * @author Eric Kisner
 */
public class MTGAddModel extends AbstractMTGModel {

    private AddCardState state;
    private HashMap<?, ?> cardChoices;

    public void setState( AddCardState newValue ) {
        AddCardState oldValue = this.state;
        this.state = newValue;

        firePropertyChange( MTGAddController.ELEMENT_STATE_PROPERTY, oldValue, newValue );
    }

    public void setAddCards( HashMap<?, ?> newValue ) {
        HashMap<?, ?> oldValue = this.cardChoices;
        this.cardChoices = newValue;

        firePropertyChange( MTGAddController.ELEMENT_ADD_CARD_PROPERTY, oldValue, newValue );
    }
}
