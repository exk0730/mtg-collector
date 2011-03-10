package mtg.collector.mtgc2.model;

import java.util.HashMap;
import javax.swing.JList;
import mtg.collector.mtgc2.controller.MTGDecksController;

/**
 *
 * @author Eric Kisner
 */
public class MTGDecksModel extends AbstractMTGModel {

    private JList decksList;
    private JList cardsList;
    private Integer totalCards;
    private Integer cardIndex;
    private String deckName;
    private String cardName;
    private String deckToAddTo;
    private HashMap<?, ?> mapping;

    public JList getCardsList() {
        return cardsList;
    }

    public void setCardsList( JList cardsList ) {
        JList oldList = this.cardsList;
        this.cardsList = cardsList;

        firePropertyChange( MTGDecksController.ELEMENT_CARDLIST_PROPERTY, oldList, cardsList );
    }

    public JList getDecksList() {
        return decksList;
    }

    public void setDecksList( JList decksList ) {
        JList oldList = this.decksList;
        this.decksList = decksList;

        firePropertyChange( MTGDecksController.ELEMENT_DECKLIST_PROPERTY, oldList, decksList );
    }

    public Integer getTotalCards() {
        return totalCards;
    }

    public void setTotalCards( Integer totalCards ) {
        Integer oldValue = this.totalCards;
        this.totalCards = totalCards;

        firePropertyChange( MTGDecksController.ELEMENT_TOTALCARDS_PROPERTY, oldValue, totalCards );
    }

    public void setCardSelected( Integer cardIndex ) {
        Integer oldValue = this.cardIndex;
        this.cardIndex = cardIndex;

        firePropertyChange( MTGDecksController.ELEMENT_CARD_CHANGED_PROPERTY, oldValue, cardIndex );
    }

    public void setDeckToCardMap( HashMap<?, ?> mapping ) {
        HashMap<?, ?> oldValue = this.mapping;
        this.mapping = mapping;

        firePropertyChange( MTGDecksController.ELEMENT_POSITION_TO_CARD_MAP, oldValue, mapping );
    }

    public void setOutputText( String deckName ) {
        String oldValue = this.deckName;
        this.deckName = deckName;

        firePropertyChange( MTGDecksController.ELEMENT_OUTPUT_TEXT_PROPERTY, oldValue, deckName );
    }

    public void setDeckToDelete( String deckName ) {
        String oldValue = this.deckName;
        this.deckName = deckName;

        firePropertyChange( MTGDecksController.ELEMENT_DELETE_DECK_PROPERTY, oldValue, deckName );
    }

    public void setCardToDelete( String cardName ) {
        String oldValue = this.cardName;
        this.cardName = cardName;

        firePropertyChange( MTGDecksController.ELEMENT_DELETE_CARD_PROPERTY, oldValue, cardName );
    }

    public void setAddCardsToDeck( String deckToAddTo ) {
        String oldValue = this.deckToAddTo;
        this.deckToAddTo = deckToAddTo;

        firePropertyChange( MTGDecksController.ELEMENT_ADD_CARD_PROPERTY, oldValue, deckToAddTo );
    }
}
