package SHPDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *classe permettant de recuperer un flux de lecture pour un fichier a partir de son chemin
 */
public class FileReader {

    private String path = "";

    private File file = null;

    /**
     *cree un nouveau lecteur de fichier
     * @param path chemin du fichier
     * @throws FileNotFoundException le fichier n'a pas ete trouve a l'emplacement donne
     */
    public FileReader(String path) throws FileNotFoundException {
        setPath(path);
    }

    /**
     * met a jour le chemin du fichier
     * @param path nouveau chemin du fichier
     * @throws FileNotFoundException le fichier n'a pas ete trouve a l'emplacement donne
     */
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

    /**
     * retourne le chemin du fichier
     * @return chemin du fichier
     */
    public String getPath() {
        return path;
    }

    /**
     * retourne le flux de lecture du fichier
     * @return flux de lecture
     * @throws FileNotFoundException le fichier n'existe pas
     */
    public FileInputStream getFileInputStream() throws FileNotFoundException {
        try {
            return new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            throw new FileNotFoundException("Le fichier n'existe pas");
        }
    }
}
