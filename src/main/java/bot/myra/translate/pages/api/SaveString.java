package bot.myra.translate.pages.api;

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

public class SaveString {

    public static Object saveWord(Request req, Response res) {
        final JSONObject json = new JSONObject(req.body()); // Create JSONObjet out of body

        // Code is invalid
        if (!json.getString("code").equals(Utilities.CODE)) {
            System.out.println("Rip");
            return "{\"status\": \"Invalid Code!\"}";
        }

        // Update translations count
        final Document user = Database.users.find(Filters.eq("username", req.cookie("username"))).first();
        user.replace("translations", user.getInteger("translations") + 1);
        Database.users.findOneAndReplace(Filters.eq("username", req.cookie("username")), user);

        // Update language file
        final String language = json.getString("language"); // Get language to edit
        final String key = json.getString("key"); // Get key of word
        final String translation = json.getString("value"); // Get translation

        final Platform platform = Platform.get(json.getString("platform"));
        final GenericLoader loader = platform.getLoader(language);
        loader.update(key, translation);

        String nextKey = "null";
        boolean foundCurrent = false;
        for (int i = 0; i < loader.getTranslations().size(); i++) {
            final Translation iTranslation = loader.getTranslations().get(i);
            if (iTranslation.isComment()) continue;

            if (iTranslation.getKey().equals(key)) {
                foundCurrent = true;
                if (loader.getTranslations().size() - 1 == i) break;
            } else if (foundCurrent && iTranslation.getTranslatedText().isBlank()) {
                nextKey = iTranslation.getKey();
                break;
            }
        }


        return """
                {
                    "status": "Oki doki",
                    "next": "{next}"
                }
                """
                .replace("{next}", nextKey);
    }

}
