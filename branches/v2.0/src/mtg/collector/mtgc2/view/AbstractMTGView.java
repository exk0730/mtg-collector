package mtg.collector.mtgc2.view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import javax.swing.JFrame;

/**
 *
 * @author Eric Kisner
 */
public abstract class AbstractMTGView extends JFrame {

    public AbstractMTGView( Dimension dim ) {
        setLocation( dim );
    }

    public abstract void modelPropertyChange( final PropertyChangeEvent evt );

    public void close( boolean closeApp ) {
        if( closeApp ) {
            System.exit( 0 );
        }
        dispose();
    }

    private void setLocation( Dimension dim ) {
        double width = dim.getWidth();
        double height = dim.getHeight();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        double x = env.getCenterPoint().getX();
        double y = env.getCenterPoint().getY();

        setBounds( (int) (x - (width / 2)),
                   (int) (y - (height / 2)),
                   (int) width,
                   (int) height );
    }
}
