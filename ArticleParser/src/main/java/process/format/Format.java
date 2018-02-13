package process.format;

import java.util.Set;

/**
 * Permet de définir des format de sortie différents,
 * à partir du flux d'éléments XML entrant.
 */
public interface Format {
    // COMMANDES
    /**
     * Réalise l'action de formattage afin d'exprimer le texte fourni,
     * selon le format établi.
     */
    String format(String txt);

    /**
     * Renvoie l'extension de fichier associé à ce même format
     */
    String getExtension();


    // METHODES STATIQUES
    /**
     * Renvoie l'ensemble des formats disponibles
     */
    static Set<Format> values() {
        return FormatRegistry.REGISTERED_FORMATS;
    }

    /**
     * Renvoie le format par défaut
     */
    static Format getDefault() {
        return SimpleFormat.DEFAULT;
    }

    /**
     * Renvoie le format associé à l'extension associée.
     */
    static Format fromString(String extension) {
        for (Format format : Format.values()) {
            if (format.getExtension().equals(extension)) {
                return format;
            }
        }
        return null;
    }
}
