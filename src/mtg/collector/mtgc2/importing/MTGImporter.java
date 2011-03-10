package mtg.collector.mtgc2.importing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import mtg.collector.mtgc2.fileIO.MTGFileIO;
import mtg.collector.mtgc2.view.AbstractMTGView;
import mtg.collector.mtgc2.splash.ProcessingSplashScreen;
import mtg.collector.xml.org.Deck;

/**
 *
 * @author Eric Kisner
 */
public class MTGImporter {

    private AbstractMTGView view;
    private MTGFileIO fileIO;

    public MTGImporter( AbstractMTGView view ) {
        this.view = view;
        fileIO = MTGFileIO.instance();
    }

    public void importFile() {
        try {
            MTGTextParser parser = new MTGTextParser( new MTGFileChooser().importFile() );
            HashMap<String, List<String>> deckLists = parser.parseDecks();
            Thread t = new Thread( new WriteImportsThread( deckLists ) );
            t.start();
        } catch( IllegalStateException e ) {
            JOptionPane.showMessageDialog( view, e.getMessage() );
            return;
        } catch( MalformedURLException e ) {
            JOptionPane.showMessageDialog( view, e );
            return;
        } catch( IOException e ) {
            JOptionPane.showMessageDialog( view, e );
            return;
        } catch( MTGFileFormatException e ) {
            JOptionPane.showMessageDialog( view, e );
            return;
        }
    }

    private void displayErrorCards( final ArrayList<String> failedCards ) {
        if( failedCards.isEmpty() ) {
            return;
        }

        try {
            javax.swing.SwingUtilities.invokeAndWait( new Runnable() {

                public void run() {
                    String display = "These cards were not found by Gatherer:\n";
                    for( String str : failedCards ) {
                        display += "\t-" + str + "\n";
                    }
                    JOptionPane.showMessageDialog( view, display, "Failed Cards",
                                                   JOptionPane.WARNING_MESSAGE );
                }
            } );
        } catch( InterruptedException e ) {
            JOptionPane.showMessageDialog( view, "Interrupted Exception " + e );
            return;
        } catch( Exception e ) {
            JOptionPane.showMessageDialog( view, "Exception " + e );
            return;
        }
    }

    private class WriteImportsThread extends Thread {

        private HashMap<String, List<String>> deckLists;
        private int deckSize;

        public WriteImportsThread( HashMap<String, List<String>> deckLists ) {
            this.deckLists = deckLists;
            deckSize = deckLists.size();
        }

        @Override
        public void run() {
            //Importing decks process
            if( deckSize == 0 ) {
                JOptionPane.showMessageDialog( view, "No decks in file to import." );
            } else {
                ProcessingSplashScreen splash = new ProcessingSplashScreen( deckLists );
                ArrayList<Deck> decks = splash.doWork();
                if( decks == null ) {
                    return;
                }
                displayErrorCards( splash.getFailedCards() );
                for( Deck deck : decks ) {
                    if( fileIO.writeDeck( deck ) ) {
                        JOptionPane.showMessageDialog( view, "Successfully imported " + deck.getDeckName() );
                    }
                }
            }
        }
    }
}
