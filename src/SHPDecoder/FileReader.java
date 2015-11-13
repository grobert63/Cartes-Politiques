package SHPDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileReader {

    private String path = "";

    private File file = null;

    public FileReader(String path) throws FileNotFoundException {
        setPath(path);
    }

    public void setPath(String path) throws FileNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            this.path = path;
            this.file = file;
        }
        else {
            throw new FileNotFoundException("Le fichier n'existe pas");
        }
    }

    FileInputStream getFileInputStream() throws FileNotFoundException {
        try {
            return new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            throw new FileNotFoundException("Le fichier n'existe pas");
        }
    }
}
