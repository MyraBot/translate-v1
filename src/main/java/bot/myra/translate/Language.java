package bot.myra.translate;

import java.util.Arrays;

public enum Language {

    CATALAN("ca", "Catalan", "https://cdn.discordapp.com/attachments/865515316963442729/865515388259139594/Catalan.png"),
    CZECH("cs", "Czech", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-czechia_1f1e8-1f1ff.png"),
    GERMAN("de", "German", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-germany_1f1e9-1f1ea.png"),
    ENGLISH("en-GB", "English", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-united-kingdom_1f1ec-1f1e7.png"),
    GREEK("el", "Greek", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-greece_1f1ec-1f1f7.png"),
    SPANISH("es", "Spanish", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-spain_1f1ea-1f1f8.png"),
    FRENCH("fr", "French", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-france_1f1eb-1f1f7.png"),
    HINDI("hi", "Hindi", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-india_1f1ee-1f1f3.png"),
    ITALIAN("it", "Italian", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-italy_1f1ee-1f1f9.png"),
    KOREAN("ko", "Korean", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-south-korea_1f1f0-1f1f7.png"),
    POLISH("pl", "Polish", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-poland_1f1f5-1f1f1.png"),
    ROMANIAN("ro", "Romanian", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-romania_1f1f7-1f1f4.png"),
    RUSSIAN("ru", "Russian", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-russia_1f1f7-1f1fa.png"),
    SANSKRIT("sa", "Sanskrit", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-india_1f1ee-1f1f3.png"),
    SWEDISH("sv", "Swedish", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-sweden_1f1f8-1f1ea.png"),
    VIETNAMESE("vi", "Vietnamese", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/282/flag-vietnam_1f1fb-1f1f3.png"),
    TURKISH("tr", "Turkish", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/322/flag-turkey_1f1f9-1f1f7.png");

    private final String isoCode;
    private final String name;
    private final String flag;

    Language(String isoCode, String name, String flag) {
        this.isoCode = isoCode;
        this.name = name;
        this.flag = flag;
    }

    public static Language getLanguageByIsoCode(String isoCode) {
        try {
            return Arrays.stream(Language.values()).filter(language -> language.getIsoCode().equals(isoCode)).findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

}
