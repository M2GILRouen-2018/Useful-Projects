package process.format;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FDDFormat implements Format {
    // CONSTANTES
    /**
     * Pattern de reconnaissance du l'élément xml
     */
    private static final Pattern XML_PATTERN = Pattern.compile("<([^\\s]*)( [^\\s]*\\=\\\"[^\\s]*\\\")*>(.*)<\\/([^\\s]*)>");

    /**
     * Table des éléments à lire + les headers utilisés pour l'écriture
     */
    private static final Map<String, String> ELEMENTS = new HashMap<>();
    static {
        ELEMENTS.put("ArticleTitle", "T.");
        ELEMENTS.put("AbstractText", "A.");
        ELEMENTS.put("DescriptorName", "I.");
    }

    // REQUÊTES
    @Override
    public String getExtension() {
        return "fdd";
    }

    // COMMANDES
    @Override
    public String format(String txt) {
        // Recueil des données
        Map<String, Set<String>> data = new HashMap<>(); {
            for (String s : ELEMENTS.keySet()) {
                data.put(s, new TreeSet<>());
            }
        }

        // Obtention des informations
        Matcher m = XML_PATTERN.matcher(txt);
        while (m.find()) {
            String key = m.group(1);
            String value = m.group(3);

            if (ELEMENTS.containsKey(key)) {
                data.get(key).add(value);
            }
        }

        // Construction du texte
        StringBuilder builder = new StringBuilder();
        for (String elt : data.keySet()) {
            builder.append(String.format(
               "%s %s\n", ELEMENTS.get(elt), String.join(" | ", data.get(elt))
            ));
        }

        // Renvoi du texte
        return builder.toString();
    }
}
