package bot.myra.translate;

public class Translation {

    private final String key;
    private String originalText;
    private String translatedText;
    private final boolean isComment;

    public Translation(String key, String originalText, String translatedText, boolean isComment) {
        this.key = key;
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.isComment = isComment;
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

    public boolean isComment() {
        return isComment;
    }
}
