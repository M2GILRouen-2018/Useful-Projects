package process.format;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Permet de lister l'ensemble des formats qui seront à mis
 * disposition pour l'application.
 */
abstract class FormatRegistry {
    // ENREGISTREMENT DES FORMATS DISPONIBLES
    /**
     * L'ensemble des classes définissant des formats exploitables
     * pour l'application.
     *
     * 2 cas possibles :
     *  - Énumération étendant Format : chargement des valeurs énumérées.
     *  - Classe étendant Format : chargement d'une instance.
     */
    private static final Class<?>[] FORMAT_CLASSES_TO_LOAD = {
            SimpleFormat.class,
            FDDFormat.class
    };


    // CHARGEMENT A L'EXECUTION
    /**
     * Renvoie l'ensemble des formats enregistrés auprès du registre
     * des formats.
     */
    private static Set<Format> registeredFormats() {
        Set<Format> set = new HashSet<>();

        // Chargement des instances de format à utiliser
        for (Class<?> clazz : FORMAT_CLASSES_TO_LOAD) {
            if (Format.class.isAssignableFrom(clazz)) {
                if (clazz.isEnum()) {
                    for (Format format : (Format[]) clazz.getEnumConstants()) {
                        set.add(format);
                    }
                } else {
                    try {
                        set.add((Format) clazz.newInstance());
                    } catch (Exception e) {
                        // On ignore l'ajout, dans le cas d'une exception.
                    }
                }
            }
        }

        // On renvoie une collection non modifiable
        return Collections.unmodifiableSet(set);
    }

    // La constante transmise à la classe Format pour une utilisation simplifiée.
    static final Set<Format> REGISTERED_FORMATS = registeredFormats();
}
