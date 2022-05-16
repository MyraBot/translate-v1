package bot.myra.translate.pages;

import bot.myra.translate.Language;
import bot.myra.translate.utils.Utilities;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;

public class PlatformSelection {

    public static Object onPageVisit(@NotNull Request req, Response res) {
        final Document user = Utilities.authorize(req, res);
        if (user == null) return null;

        final Language language = Language.getLanguageByIsoCode(req.params("language"));

        final String html = Utilities.readResource("platformSelection/page.html")
                .replace("{$language.icon}", language.getFlag())
                .replace("{$language.name}", language.getName())
                .replace("{$language.iso}", language.getIsoCode());
        return html;
    }

}
