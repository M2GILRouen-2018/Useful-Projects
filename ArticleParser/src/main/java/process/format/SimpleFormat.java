package process.format;

import org.json.JSONObject;
import org.json.XML;

/**
 * Permet de définir des format de sortie, à partir du flux
 * XML entrant
 */
enum SimpleFormat implements Format {
    // VALEURS
    DEFAULT("xml") {
        public String format(String txt) {
            return txt;
        }
    },
    JSON("json") {
        public String format(String txt) {
            String formatted = null;
            try {
                JSONObject xmlJSONObj = XML.toJSONObject(txt);
                formatted = xmlJSONObj.toString(2);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            return formatted;
        }
    };

    // ATTRIBUTS
    private final String extension;


    // CONSTRUCTEUR
    private SimpleFormat(String extension) {
        this.extension = extension;
    }


    // COMMANDES
    public String getExtension() {
        return extension;
    }
}
