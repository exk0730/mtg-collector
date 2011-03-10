package mtg.collector.mtgc2.importing;

/**
 *
 * @author Eric Kisner
 */
public class MTGFileFormatException extends Exception {

    public MTGFileFormatException( String fileName ) {
        super( "Format of the file " + fileName + " is incorrect.\n"
               + "Please correct the format by following the example under "
               + "the \"Help\" menu." );
    }
}
