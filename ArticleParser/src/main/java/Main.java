import config.Config;
import config.Option;
import process.Extractor;
import process.format.Format;

/**
 * Met en oeuvre l'extraction et le parsing des articles.
 */
public class Main {
    public static void main(String[] args) {
        // Init.
        long startTime = System.currentTimeMillis();
        Config config = new Config();
        for (String arg : args) {
            Option option = Option.parse(arg);
            if (option == null) {
                config.addFile(arg);
            }
            else {
                option.applyOn(config, arg);
            }
        }

        // Ajout du format par défaut, si aucun format n'est défini
        if (config.getFormats().isEmpty()) {
            config.addFormat(Format.getDefault());
        }

        // Création de l'extracteur
        Extractor extractor = new Extractor(config);

        // Parsing
        System.out.println("Début du parsing des documents !");
        extractor.extract();
        System.out.println("--------\nFin du parsing des documents !");

        // Statistiques
        System.out.println(String.format(">> Nombre de fichiers parsés : %s", config.getFiles().size()));
        System.out.println(String.format(">> Temps total : %d ms", System.currentTimeMillis() - startTime));
    }
}
