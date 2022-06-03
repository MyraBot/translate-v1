package bot.myra.translate.pages;

import bot.myra.translate.Database;
import bot.myra.translate.Translation;
import bot.myra.translate.utils.Platform;
import bot.myra.translate.utils.Utilities;
import bot.myra.translate.utils.loaders.GenericLoader;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.Arrays;

public class StringEditor {

    public static Object onPageVisit(Request req, Response res) {
        final Document user = Utilities.authorize(req, res);
        if (user == null) return null;

        final String iso = req.params("language");
        final String query = req.queryParams("key");

        final Platform platform = Platform.get(req.params("platform"));
        final GenericLoader loader = platform.getLoader(iso);
        final Translation entry = loader.getTranslations().stream().filter(it -> it.getKey().equals(query)).findFirst().get();

        return Utilities.readResource("stringEditor/stringEditor.html")
                .replace("{$string.key}", query) // The key of the string
                .replace("{$string.original}", entry.getOriginalText())
                .replace("{$string.translation}", entry.getTranslatedText());
    }

}
