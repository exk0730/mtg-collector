package mtg.collection.fileio;

import mtg.collector.mtgc2.fileIO.MTGFileIO;
import mtg.collector.xml.org.Card;
import mtg.collector.xml.org.Cards;
import mtg.collector.xml.org.Deck;

/**
 *
 * @author Eric Kisner
 */
public class TestMTGFileIO {

	public static void main( String[] args ) {
		MTGFileIO fileIO = MTGFileIO.instance();

		Deck deck = new Deck();
		deck.setDeckName( "Test Deck" );
		Cards cards = new Cards();
		deck.setCards( cards );
		for( int i = 0; i < 4; i++ ) {
			Card c = new Card();
			c.setCardName( "test" + i );
			deck.getCards().getCard().add( c );
		}

		System.out.println( "Writing deck - " + fileIO.getCurDecks().size() );
		fileIO.writeDeck( deck );
		fileIO.newDecks = true;
		System.out.println( "Wrote deck - " + fileIO.getCurDecks().size() );
		fileIO.deleteDeck( deck );
		fileIO.newDecks = true;
		System.out.println( "Deleted deck - " + fileIO.getCurDecks().size() );


		deck = new Deck();
		deck.setDeckName( "Test Deck" );
		cards = new Cards();
		deck.setCards( cards );
		Card toDel = null;
		for( int i = 0; i < 4; i++ ) {
			Card c = new Card();
			c.setCardName( "test" + i );
			toDel = c;
			deck.getCards().getCard().add( c );
		}

		System.out.println( "Writing deck - " + fileIO.getCurDecks().size() );
		fileIO.writeDeck( deck );
		fileIO.newDecks = true;
		System.out.println( "Wrote deck - " + fileIO.getCurDecks().size() );
		fileIO.deleteCard( deck, toDel );
		fileIO.newDecks = true;
		System.out.println( "Deleted card - " + fileIO.getCurDecks().get( fileIO.getCurDecks().size()-1 ).getCards().getCard().size() );
	}
}
