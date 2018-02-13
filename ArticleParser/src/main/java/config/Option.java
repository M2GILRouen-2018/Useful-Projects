package config;

import process.format.Format;

/**
 * Permet de représenter les arguments optionnels du parser, qui servent
 * à la modification de la configuration utilisée.
 *
 * La syntaxe générale d'une option est :
 *    -flag_option=arg1,arg2,...
 */
public enum Option {
    // VALEURS
    /**
     * Permet de définir les différents format à utiliser.
     */
    FORMAT("-f") {
        public void applyEffectOn(Config config, String[] parameters) {
            for (String extension : parameters) {
                // Dans le cas "-all", on ajoute tous les format possible
                if (extension.equals("all")) {
                    for (Format format : Format.values()) {
                        config.addFormat(format);
                    }
                    return;
                }

                // Sinon, on teste l'extension pour voir la correspondance avec un format existant
                Format format = Format.fromString(extension);
                if (format != null) {
                    config.addFormat(format);
                }
            }
        }
    },

    /**
     * Permet de définir le dossier de destination, où
     * seront placés les fichiers produits pendant l'extraction
     */
    DESTINATION("-d") {
        public void applyEffectOn(Config config, String[] parameters) {
            if (parameters.length != 1) {
                throw new AssertionError(String.format("%s : Seule une valeur peut être définie à la fois"
                        ,getFlag()));
            }

            config.setDestination(parameters[0]);
        }
    },

    /**
     * Permet de modifier le nombre d'article présents au maximum,
     * dans chaque fichier produit.
     */
    SLICES("-s") {
        public void applyEffectOn(Config config, String[] parameters) {
            if (parameters.length != 1) {
                throw new AssertionError(String.format("%s : Seule une valeur peut être définie à la fois"
                ,getFlag()));
            }

            config.setSlices(Integer.parseInt(parameters[0]));
        }
    };

    // METHODES DE CLASSE
    /**
     * Récupère l'option correspondant à chaine fournie
     * (Identifie la ressemblance en début de chaîne en tant que flag d'option)
     */
    public static Option parse(String s) {
        for (Option o : Option.values()) {
            if (s.startsWith(o.getFlag())) {
                return o;
            }
        }
        return null;
    }


    // ATTRIBUT
    /**
     * Le flag associé à cette option
     */
    private final String flag;


    // CONSTRUCTEUR
    Option(String flag) {
        this.flag = flag;
    }


    // REQUETE

    /**
     * Renvoie le flag associé à cette option
     */
    public String getFlag() {
        return flag;
    }


    // METHODE
    /**
     * Applique la modification sur la configuration fournie,
     * à partir de la commande entrée.
     */
    public void applyOn(Config config, String command) {
        String[] terms = null;
        try {
            terms = command.split("=");
        } catch (IndexOutOfBoundsException exc) {
            throw new AssertionError(String.format("L'argument %s est invalide (Usage : %s)",
                    command, "-flag_option=arg1,arg2,..."));
        }

        String[] parameters = terms[1].split(",");
        applyEffectOn(config, parameters);
    }

    /**
     * Définit la façon dont agissent les différentes options du parser
     */
    protected abstract void applyEffectOn(Config config, String[] parameters);
}
