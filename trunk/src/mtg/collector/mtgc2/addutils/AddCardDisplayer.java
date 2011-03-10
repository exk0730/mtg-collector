package mtg.collector.mtgc2.addutils;

import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import mtg.collector.mtgc2.utilities.AddCardState;
import mtg.collector.mtgc2.controller.AbstractMTGController;
import mtg.collector.mtgc2.controller.MTGAddController;
import mtg.collector.mtgc2.view.AbstractMTGView;
import mtg.collector.mtgc2.view.AddManager;
import mtg.collector.mtgc2.web.GathererConnector;
import mtg.collector.xml.org.Card;

/**
 *
 * @author Eric Kisner
 */
public class AddCardDisplayer implements Runnable {

    private AddManager view;
    private MTGAddController controller;
    private JPanel resultsPanel;
    private ArrayList<Card> cardResults;
    private HashMap<JCheckBox, Card> cardChoices = new HashMap<JCheckBox, Card>();

    public AddCardDisplayer( AbstractMTGView view, AbstractMTGController controller, String cardName ) {
        this.view = (AddManager) view;
        this.controller = (MTGAddController) controller;
        init( cardName );
    }

    private void init( String cardName ) {
        resultsPanel = new JPanel();
        resultsPanel.setLayout( new GridLayout( 0, 2, 5, 5 ) );
        resultsPanel.setOpaque( false );
        try {
            cardResults = new GathererConnector().returnResults( cardName );
        } catch( Exception e ) {
            JOptionPane.showMessageDialog( view, e );
            e.printStackTrace();
            return;
        }
    }

    public HashMap<JCheckBox, Card> getCardChoices() {
        return cardChoices;
    }

    public void run() {
        if( cardResults.isEmpty() ) {
            view.amntFoundLabel.setText( "0 cards found" );
        } else {
            if( cardResults.size() > 24 ) {
                JOptionPane.showMessageDialog( view, "Please refine your search"
                                                     + " to receive better results.\nThe following displays "
                                                     + "the first 25 cards found." );
            }
            ImageIcon ii = null;
            for( int i = 0; i < cardResults.size(); i++ ) {
                Card card = cardResults.get( i );
                JCheckBox toAdd = new JCheckBox( card.getCardName() );
                cardChoices.put( toAdd, card );
                resultsPanel.add( toAdd );
                try {
                    ii = new ImageIcon( new URL( card.getImageLink() ) );
                } catch( MalformedURLException e ) {
                    JOptionPane.showInputDialog( view, "Error retrieving card image.",
                                                 "URL Error", JOptionPane.ERROR_MESSAGE );
                }
                if( ii == null ) {
                    resultsPanel.add( new JLabel( "Image unavailable" ) );
                } else {
                    resultsPanel.add( new JLabel( ii ) );
                }
                //Update the JLabel that displays how many cards have been
                //found so far.
                final int progress = i + 1;
                javax.swing.SwingUtilities.invokeLater( new Runnable() {

                    public void run() {
                        view.amntFoundLabel.setText( progress + " card(s) found." );
                    }
                } );

                try {
                    Thread.sleep( 100 );
                } catch( InterruptedException ignore ) {
                }
            }
        }

        javax.swing.SwingUtilities.invokeLater( new Runnable() {

            public void run() {
                view.jsCardResults.setViewportView( resultsPanel );
                controller.changeElementState( AddCardState.Finished );
            }
        } );
    }
}
