package mtg.collector.mtgc2.model;

import java.io.File;
import mtg.collector.mtgc2.controller.MTGImportController;

/**
 *
 * @author Eric Kisner
 */
public class MTGImportModel extends AbstractMTGModel {

    private File deckFile;

    public File getDeckFile() {
        return deckFile;
    }

    public void setImportDeckFile( File file ) {
        File oldVal = deckFile;
        deckFile = file;

        firePropertyChange( MTGImportController.ELEMENT_IMPORT_DECK_FILE,
                            oldVal, deckFile );
    }
}
