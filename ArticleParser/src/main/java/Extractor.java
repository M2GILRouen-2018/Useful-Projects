import java.io.*;

public class Extractor {
    // CONSTANTE
    public static final int ARTICLES_PER_FILE = 1000;
    public static final String DEST_FILE_PREFIX = "result";


    // ATTRIBUTS
    private final Format format;
    private BufferedWriter writer;
    private int articleCount;
    private int fileCount;
    private long last_pmid;


    // CONSTRUCTEUR
    public Extractor(Format format) {
        this.format = format;
        this.writer = null;
    }
    public Extractor() {
        this(Format.DEFAULT);
    }


    // COMMANDES
    public BufferedWriter getWriter() throws IOException {
        if (writer == null || articleCount >= ARTICLES_PER_FILE) {
            ++fileCount;
            String newFileName = String.format(
                    "%s_%d+.%s", DEST_FILE_PREFIX, last_pmid, format.getExtension()
            );
            writer = new BufferedWriter(new FileWriter(new File(newFileName)));
            System.out.println(String.format(">> >> Fichier %s généré", newFileName));
            articleCount = 0;
        }
        return writer;
    }


    public void extract(String filePath) {
        // Définition du flux d'entrée
        BufferedReader input = null;
        try {
            File in = new File(filePath);
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

                // Fin de lecture de l'élément
                if (line.trim().startsWith("</PubmedArticle>")) {
                    // Ecriture dans le fichier de destination
                    BufferedWriter bw = getWriter();
                    String text = articleBuilder.toString();
                    bw.write(format.format(text));
                    bw.flush();

                    // Fin de la lecture de l'élément
                    articleBuilder = null;
                    ++articleCount;
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    // POINT D'ENTREE
    public static void main(String[] args) {
        // Init.
        Format format = Format.DEFAULT;
        int start = 0;
        if (args.length > 1 && args[0].equals("--json")) {
            start = 1;
            format = Format.JSON;
        }

        // Parsing
        System.out.println("Début du parsing des documents !");
        for (int k = start; k < args.length; ++k) {
            System.out.println("--------");
            System.out.println(String.format(">> Parsing de %s ...", args[k]));
            new Extractor(format).extract(args[k]);
            System.out.println(String.format(">> Document %s parsé !", args[k]));
        }
        System.out.println("--------");
        System.out.println("Fin du parsing des documents !");
    }
}
