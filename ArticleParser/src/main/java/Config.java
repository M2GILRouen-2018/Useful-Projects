import java.io.File;
import java.util.*;

/**
 * Permet de définir tous les aspects liés à la configuration, au
 * sein d'un seul et même objet.
 */
public class Config {
    // CONSTANTE
    /**
     * Le nombre d'articles contenu dans chaque fichier par défaut
     */
    public static final int DEFAULT_SLICES = 1;

    /**
     * Le répertoire de destination par défaut
     */
    public static final String DEFAULT_DEST = ".";


    // ATTRIBUTS
    /**
     * L'ensemble des formats à utiliser
     */
    private final Set<Format> formats;

    /**
     * L'ensemble des fichiers à traiter
     */
    private final Set<String> files;

    /**
     * Le répertoire de destination des fichiers produits
     */
    private String destination;

    /**
     * Le nombre de découpes à réaliser
     */
    private int slices;


    // CONSTRUCTEUR
    public Config() {
        formats = new HashSet<>();
        files = new HashSet<>();
        destination = DEFAULT_DEST;
        slices = DEFAULT_SLICES;
    }


    // REQUETES
    /**
     * Renvoie les formats gérés par cette configuration.
     */
    public Set<Format> getFormats() {
        return Collections.unmodifiableSet(formats);
    }

    /**
     * Renvoie les fichiers à parser.
     */
    public Set<String> getFiles() {
        return Collections.unmodifiableSet(files);
    }

    /**
     * Renvoie le dossier de destination des fichiers produits
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Renvoie le dossier de destination des fichiers produits
     */
    public String getDestination(Format format) {
        if (format == null) {
            return getDestination();
        }
        return String.format("%s/%s", destination, format.getExtension());
    }

    /**
     * Renvoie le nombre d'articles par fichier à produire.
     */
    public int nbSlices() {
        return slices;
    }


    // COMMANDES
    /**
     * Ajoute un nouveau format à gérer au sein de la configuration.
     */
    public void addFormat(Format format) {
        formats.add(format);
    }

    /**
     * Ajoute un nouveau fichier à utiliser.
     */
    public void addFile(String filepath) {
        files.add(filepath);
    }

    /**
     * Modifie le dossier de destination des fichiers produits
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Modifie le nombre d'articles par fichier à produire.
     */
    public void setSlices(int slices) {
        this.slices = slices;
    }

    /**
     * Définit le sous-répertoire du répertoire de destination
     * utilisé par le format donné.
     */
    public void createDir(Format format) {
        // On ouvre un descripteur sur le répertoire à créer
        String dirName = getDestination(format);
        File dir = null;
        try {
            dir = new File(dirName);
        } catch (Exception exc) {
            throw new AssertionError("Echec de création du répertoire : Chemin inconnu");
        }

        // Si le répertoire existe déjà...
        if (dir.isDirectory()) {
            return;
        }
        // Sinon, on tente sa création
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new AssertionError("Echec de création du répertoire : Le processus a échoué...");
            }
            return;
        }
        throw new AssertionError(String.format("Echec de création du répertoire : %s %s",
                dirName, "Le chemin est déjà utilisé..."));
    }
}
