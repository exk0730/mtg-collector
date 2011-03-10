package mtg.collector.mtgc2.controller;

import java.util.HashMap;
import javax.swing.JList;

/**
 *
 * @author Eric Kisner
 */
public class MTGDecksController extends AbstractMTGController {

    public static final String ELEMENT_DECKLIST_PROPERTY = "DecksList";
    public static final String ELEMENT_CARDLIST_PROPERTY = "CardsList";
    public static final String ELEMENT_TOTALCARDS_PROPERTY = "TotalCards";
    public static final String ELEMENT_CARD_CHANGED_PROPERTY = "CardSelected";
    public static final String ELEMENT_POSITION_TO_CARD_MAP = "DeckToCardMap";
    public static final String ELEMENT_OUTPUT_TEXT_PROPERTY = "OutputText";
    public static final String ELEMENT_DELETE_DECK_PROPERTY = "DeckToDelete";
    public static final String ELEMENT_DELETE_CARD_PROPERTY = "CardToDelete";
    public static final String ELEMENT_ADD_CARD_PROPERTY = "AddCardsToDeck";

    public void changeElementDeckList( JList deckList ) {
        setModelProperty( ELEMENT_DECKLIST_PROPERTY, deckList );
    }

    public void changeElementCardList( JList cardList ) {
        setModelProperty( ELEMENT_CARDLIST_PROPERTY, cardList );
    }

    public void changeElementTotalCards( int totalCards ) {
        setModelProperty( ELEMENT_TOTALCARDS_PROPERTY, totalCards );
    }

    public void changeElementCardSelected( int indexOfCard ) {
        setModelProperty( ELEMENT_CARD_CHANGED_PROPERTY, indexOfCard );
    }

    public void changeElementCardToDeckMap( HashMap<?, ?> mapping ) {
        setModelProperty( ELEMENT_POSITION_TO_CARD_MAP, mapping );
    }

    public void changeElementOutputText( String deckName ) {
        setModelProperty( ELEMENT_OUTPUT_TEXT_PROPERTY, deckName );
    }

    public void changeElementDeckToDelete( String deckName ) {
        setModelProperty( ELEMENT_DELETE_DECK_PROPERTY, deckName );
    }

    public void changeElementCardToDelete( String cardName ) {
        setModelProperty( ELEMENT_DELETE_CARD_PROPERTY, cardName );
    }

    public void changeElementAddCard( String deckName ) {
        setModelProperty( ELEMENT_ADD_CARD_PROPERTY, deckName );
    }
}
