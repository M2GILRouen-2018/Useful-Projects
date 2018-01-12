/**
 * Met en oeuvre l'extraction et le parsing des articles.
 */
public class Main {
    public static void main(String[] args) {
        // Init.
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
            config.addFormat(Format.DEFAULT);
        }

        // Création de l'extracteur
        Extractor extractor = new Extractor(config);

        // Parsing
        System.out.println("Début du parsing des documents !");
        extractor.extract();
        System.out.println("Fin du parsing des documents !");
    }
}
