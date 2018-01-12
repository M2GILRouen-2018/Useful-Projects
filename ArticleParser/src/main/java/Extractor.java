import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Extractor {
    // CONSTANTE
    /**
     * Le préfixe utilisé pour désigner les fichiers produits
     */
    public static final String DEST_FILE_PREFIX = "article";


    // ATTRIBUTS
    /**
     * La table des écrivains, classifiés selon le format représenté
     */
    private final Map<Format, BufferedWriter> writers;

    /**
     * La configuration utilisée par cet extracteur
     */
    private final Config config;

    private int articleCount;
    private long last_pmid;


    // CONSTRUCTEUR
    public Extractor(Config config) {
        this.config = config;
        writers = new HashMap<>();
        reset();
    }


    // COMMANDES
    /**
     * Ajoute le format supplémentaire à utilisé, auprès de l'extracteur.
     * L'écrivain associé à ce format sera alors défini lors d'un prochain
     * appel à getWriter().
     *
     * @param format Le nouveau type de format à utiliser
     * @throws IOException
     */
    public BufferedWriter getWriter(Format format) throws IOException {
        if (!writers.containsKey(format)) {
            throw new AssertionError(String.format("Format %s non supporté...", format.toString()));
        }

        BufferedWriter writer = writers.get(format);
        if (writer == null || articleCount >= config.nbSlices()) {
            // Création du nouveau fichier
            String newFileName = String.format(
                    "%s/%s_%d.%s",
                    config.getDestination(format),
                    DEST_FILE_PREFIX,
                    last_pmid,
                    format.getExtension()
            );

            // Ouverture d'un flux d'écriture vers ce fichier
            writer = new BufferedWriter(new FileWriter(new File(newFileName)));
            writers.put(format, writer);
            System.out.println(String.format(">> >> Fichier %s généré", newFileName));
            articleCount = 0;
        }
        return writer;
    }


    /**
     * Effectue l'opération d'extraction d'articles des fichiers prévus à lire.
     *
     * Pour chaque fichier défini dans la configuration initiale : cela crée
     * plusieurs fichiers dont le nombre d'articles contenus est défini
     * au sein de cette même configuration.
     */
    public void extract() {
        for (String filepath : config.getFiles()) {
            System.out.println(String.format("--------\n>> Parsing de %s ...", filepath));

            // Définition du flux d'entrée
            BufferedReader input = null;
            try {
                File in = new File(filepath);
                input = new BufferedReader(new FileReader(in));
            } catch (Exception exc) {
                exc.printStackTrace();
                return;
            }

            // Parsing ligne à ligne
            StringBuilder articleBuilder = null;
            try {
                while (input.ready()) {
                    // Lecture de la ligne
                    String line = input.readLine();
                    if (line.trim().startsWith("<PubmedArticle")) {
                        articleBuilder = new StringBuilder();
                    }

                    // Enregistrement de la lgine
                    if (articleBuilder != null) {
                        articleBuilder.append(line);
                        articleBuilder.append('\n');
                    }

                    // Lecture du dernier ID
                    if (line.trim().startsWith("<PMID")) {
                        last_pmid = Long.parseLong(line.split("[<>]")[2]);
                    }

                    // Fin de lecture de l'article
                    if (line.trim().startsWith("</PubmedArticle>")) {
                        // Ecriture d'un fichier propre à chaque format
                        for (Format format : writers.keySet()) {
                            // Ouverture du fichier de destination
                            BufferedWriter bw = getWriter(format);

                            // Parsing du contenu + écriture dans le fichier de destination
                            String text = articleBuilder.toString();
                            bw.write(format.format(text));
                            bw.flush();

                            // Fin de la lecture de l'élément
                            ++articleCount;
                        }
                        articleBuilder = null;
                    }
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            System.out.println(String.format(">> Document %s parsé !", filepath));
        }

        reset();
    }

    /**
     * Réinitialise l'état de l'extracteur pour une réutilisation éventuelle
     */
    private void reset() {
        articleCount = 0;
        last_pmid = 0;

        writers.clear();
        for (Format format : config.getFormats()) {
            writers.put(format, null);
            config.createDir(format);
        }
    }
}
