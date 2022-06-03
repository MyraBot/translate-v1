package bot.myra.translate.pages;

import bot.myra.translate.Language;
import bot.myra.translate.utils.Platform;
import bot.myra.translate.utils.Utilities;
import bot.myra.translate.utils.loaders.GenericLoader;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;

public class StringOverview {

    public static Object onPageVisit(@NotNull Request req, Response res) {
        Utilities.disableCaching(res);

        // Language is the default language
        if (req.params("language").equals(Utilities.DEFAULT_LANGUAGE)) {
            return getDefaultLanguage(req, res);
        }
        // Language is any other language
        else {
            return getLanguage(req, res);
        }
    }

    public static Object getLanguage(Request req, Response res) {
        final Document user = Utilities.authorize(req, res);
        if (user == null) return null;

        final String iso = req.params("language");
        final Platform platform = Platform.get(req.params("platform"));
        final GenericLoader translations = platform.getLoader(iso);

        final StringBuilder stringsHtml = new StringBuilder();
        translations.getTranslations().forEach(translation -> {
            String stringHtml;
            if (translation.getTranslatedText().isEmpty()) stringHtml = Utilities.readResource("stringsOverview/language-string-untranslated.html");
            else stringHtml = Utilities.readResource("stringsOverview/language-string.html");
            stringHtml = stringHtml
                    .replace("{$string.key}", translation.getKey())
                    .replace("{$string.original}", translation.getOriginalText())
                    .replace("{$string.translation}", translation.getTranslatedText());
            stringsHtml.append(stringHtml);
        });

        return Utilities.readResource("stringsOverview/language-strings.html").replace("{$strings}", stringsHtml);
    }

    public static Object getDefaultLanguage(Request req, Response res) {
        final Document user = Utilities.authorize(req, res);
        if (user == null) return null;

        final String iso = req.params("language");

        final Platform platform = Platform.get(req.params("platform"));
        final GenericLoader loader = platform.getLoader(iso);

        final StringBuilder entriesHtml = new StringBuilder();
        final String entryTemplate = Utilities.readResource("stringsOverview/default-language-string.html");
        loader.getMap().forEach((key, value) -> {
            final String entry = entryTemplate
                    .replace("{$string.key}", key)
                    .replace("{$string.value}", value);
            entriesHtml.append(entry);
        });

        return Utilities.readResource("stringsOverview/default-language-strings.html")
                .replace("{$language.name}", Language.getLanguageByIsoCode(req.params("language")).getName()) // Name of language
                .replace("{$strings}", entriesHtml); // All strings
    }

}
