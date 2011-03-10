package mtg.collector.mtgc2.controller;

import java.io.File;

/**
 *
 * @author Eric Kisner
 */
public class MTGImportController extends AbstractMTGController {

    public static final String ELEMENT_IMPORT_DECK_FILE = "ImportDeckFile";

    public void changeElementDeckFile( File file ) {
        setModelProperty( ELEMENT_IMPORT_DECK_FILE, file );
    }
}
