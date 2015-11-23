package SHPDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Classe permettant de récuperer un flux de lecture pour un fichier à partir de son chemin
 */
public class FileReader {

    private String _path = "";

    private File _file = null;

    /**
     * Crée un nouveau lecteur de fichier
     * @param path Chemin du fichier
     * @throws FileNotFoundException Le fichier n'a pas été trouvé a l'emplacement donné
     */
    public FileReader(String path) throws FileNotFoundException {
        setPath(path);
    }

    /**
     * Met à jour le chemin du fichier
     * @param path Nouveau chemin du fichier
     * @throws FileNotFoundException Le fichier n'a pas été trouvé a l'emplacement donné
     */
    public void setPath(String path) throws FileNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            this._path = path;
            this._file = file;
        }
        else {
            throw new FileNotFoundException("Le fichier n'existe pas");
        }
    }

    /**
     * Retourne le chemin du fichier
     * @return Chemin du fichier
     */
    public String getPath() {
        return _path;
    }

    /**
     * Retourne le flux de lecture du fichier
     * @return Flux de lecture
     * @throws FileNotFoundException Le fichier n'existe pas
     */
    public FileInputStream getFileInputStream() throws FileNotFoundException {
        try {
            return new FileInputStream(_file);
        }
        catch (FileNotFoundException e)
        {
            throw new FileNotFoundException("Le fichier n'existe pas");
        }
    }
}
