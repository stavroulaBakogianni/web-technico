package gr.europeandynamics.web.technico.importers;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FilesImporter {

    void importFile(String filePath) throws IOException, OutOfMemoryError, FileNotFoundException;
}
