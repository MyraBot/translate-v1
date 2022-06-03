package bot.myra.translate;

public class Translation {

    private final String key;
    private String originalText;
    private String translatedText;

    public Translation(String key, String originalText, String translatedText) {
        this.key = key;
        this.originalText = originalText;
        this.translatedText = translatedText;
    }

    public String getKey() {
        return key;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public Translation setTranslatedText(String text) {
        translatedText = text;
        return this;
    }

}
