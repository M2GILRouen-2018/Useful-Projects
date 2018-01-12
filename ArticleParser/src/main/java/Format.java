import org.json.JSONObject;
import org.json.XML;

public enum Format {
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
    private Format(String extension) {
        this.extension = extension;
    }

    // COMMANDES
    public abstract String format(String txt);
    public String getExtension() {
        return extension;
    }
}