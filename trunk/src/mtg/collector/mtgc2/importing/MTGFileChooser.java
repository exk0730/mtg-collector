package mtg.collector.mtgc2.importing;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author Eric Kisner
 */
public class MTGFileChooser {

    public File importFile() throws IllegalStateException {
        JFileChooser chooser = getFileChooser();
        int r = chooser.showOpenDialog( new JFrame() );
        if( r == JFileChooser.APPROVE_OPTION ) {
            return chooser.getSelectedFile();
        } else {
            throw new IllegalStateException( "No file chosen." );
        }
    }

    private static JFileChooser getFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory( new File( "." ) );
        chooser.setFileFilter( new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept( File f ) {
                return f.getName().toLowerCase().endsWith( ".mtgf" ) || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "MTG File";
            }
        } );
        return chooser;
    }
}
