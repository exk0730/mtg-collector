package mtg.collector.mtgc2.splash;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import mtg.collector.mtgc2.fileIO.MTGFileIO;
import mtg.collector.mtgc2.web.GathererConnector;
import mtg.collector.xml.org.Card;
import mtg.collector.xml.org.Cards;
import mtg.collector.xml.org.Deck;

/**
 *
 * @author Eric Kisner
 */
public class ProcessingSplashScreen extends JFrame {

    final private ArrayList<String> failedCards;
    private HashMap<String, List<String>> deckMappings;
    final JProgressBar progressBar = new JProgressBar();
    final JLabel label = new JLabel();

    public ProcessingSplashScreen( HashMap<String, List<String>> deckMappings ) {
        this.deckMappings = deckMappings;
        this.failedCards = new ArrayList<String>();
    }

    public ArrayList<String> getFailedCards() {
        return failedCards;
    }

    public ArrayList<Deck> doWork() {
        if( deckMappings.isEmpty() ) {
            JOptionPane.showMessageDialog( this, "No deck lists specified." );
            return null;
        }

        ArrayList<Deck> ret = new ArrayList<Deck>();
        Iterator<List<String>> itor = deckMappings.values().iterator();
        int max = 0;

        for( String deckName : deckMappings.keySet() ) {
            Deck curDeck = new Deck();
            curDeck.setDeckName( deckName );
            max = itor.next().size();
            showFrame( "Processing Deck: " + deckName, max );
            try {
                Cards newCards = new Cards();
                newCards.getCard().addAll( getCardList( deckMappings.get( deckName ) ) );
                curDeck.setCards( newCards );
                ret.add( curDeck );
            } catch( Exception e ) {
                JOptionPane.showMessageDialog( this,
                                               "An exception has occurred.\n " + e, "Error", JOptionPane.ERROR_MESSAGE );
                e.printStackTrace();
                this.dispose();
                return null;
            }
        }
        this.dispose();
        MTGFileIO.instance().setNewDecks( true );
        return ret;
    }

    /**
     * Returns a list of Cards after searching Gatherer
     * @param cardList
     * @return
     * @throws Exception
     */
    private List<Card> getCardList( List<String> cardList ) throws Exception {
        List<Card> ret = new ArrayList<Card>();
        for( int i = 0; i < cardList.size(); i++ ) {
            final int progress = i + 1;
            Card card = startSearch( cardList.get( i ), progress );
            Thread.sleep( 750 );
            if( card == null ) {
                continue;
            } else {
                ret.add( card );
            }
        }
        return ret;
    }

    private Card startSearch( String fullInput, final int progress ) throws Exception {
        String temp = reformatInputString( fullInput );
        final String cardName = temp.split( "," )[0];
        final int quantity = Integer.parseInt( temp.split( "," )[1] );

        javax.swing.SwingUtilities.invokeLater( new Runnable() {

            public void run() {
                updateProgress( cardName, progress );
            }
        } );

        List<Card> searchResults = new GathererConnector().returnResults( cardName );
        Card ret;
        if( searchResults.size() > 1 || searchResults.size() < 1 ) {
            failedCards.add( cardName );
            ret = null;
        } else {
            ret = searchResults.get( 0 );
            ret.setQuantity( quantity );
        }
        return ret;
    }

    private void updateProgress( String cardName, int progress ) {
        label.setText( "Processsing " + cardName );
        progressBar.setValue( progress );
    }

    private static String reformatInputString( String fullInput ) {
        int a = fullInput.length() - fullInput.lastIndexOf( "," );
        int lastIndex = fullInput.length() - a;
        String name = fullInput.substring( 0, lastIndex );
        if( name.contains( "," ) ) {
            return name.replace( ",", "" ) + "," + fullInput.charAt( fullInput.length() - 1 );
        }
        return fullInput;
    }

    private void showFrame( String title, int max ) {
        GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point center = graphicsEnv.getCenterPoint();
        int x = (int) center.getX();
        int y = (int) center.getY();

        JPanel panel = new JPanel();

        progressBar.setMinimum( 0 );
        progressBar.setMaximum( max );

        panel.setLayout( new GridLayout( 2, 0 ) );
        panel.setOpaque( true );
        panel.add( label );
        panel.add( progressBar );

        setTitle( title );
        setResizable( false );
        setAlwaysOnTop( true );
        setMinimumSize( new Dimension( 400, 100 ) );
        setBounds( x - 200, y - 50, 100, 100 );
        setVisible( true );
        add( panel );
        pack();
    }
}
