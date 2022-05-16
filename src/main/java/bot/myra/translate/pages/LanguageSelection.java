package bot.myra.translate.pages;

import bot.myra.translate.Language;
import bot.myra.translate.utils.Utilities;
import org.bson.Document;
import spark.Request;
import spark.Response;

import java.util.List;

public class LanguageSelection {

    public static Object onPageVisit(Request req, Response res) {
        final Document user = Utilities.authorize(req, res);
        if (user == null) return null;

        String html = Utilities.readResource("languageSelection/language-selection.html"); // Get html file

        final StringBuilder languagesHtml = new StringBuilder();
        final List<String> languages = user.getList("languages", String.class);
        languages.forEach(language -> {
            String languageHtml = Utilities.readResource("languageSelection/language.html"); // Get template for each language
            final Language lang = Language.getLanguageByIsoCode(language); // Get language details
            languageHtml = languageHtml
                    .replace("{$language}", lang.getIsoCode())
                    .replace("{$language.name}", lang.getName())
                    .replace("{$language.image}", lang.getFlag());

            languagesHtml.append(languageHtml); // Add language to all other languages
        });

        return html
                .replace("{$username}", req.cookie("username"))
                .replace("{$languages}", languagesHtml.toString()); // Languages selection
    }

}
