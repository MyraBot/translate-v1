package bot.myra.translate.utils.loaders;

import bot.myra.translate.Translation;
import bot.myra.translate.utils.Platform;
import bot.myra.translate.utils.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SlashCommandsLoader extends GenericLoader {

    private JSONArray commands;

    public SlashCommandsLoader(Platform platform, String iso) {
        super(platform, iso);
    }

    @Override
    public String toOriginalFormat() {
        return commands.toString(4);
    }

    @Override
    public void onUpdate(String key, String value) {
        for (int i = 0; i < commands.length(); i++) {
            final JSONObject json = commands.getJSONObject(i);
            final String commandName = key.split("\\.")[0];
            final String path = updatePath(key);
            if (json.getString("name").equals(commandName)) {
                commands.put(i, update(json, path, value));
            }
        }
    }

    private JSONObject update(JSONObject json, String path, String translation) {
        final String key = path.split("\\.")[0];
        // End of path ➜ update value
        if (key.equals(path)) {
            json.getJSONObject(key + "_localizations").put(iso, translation);
        }
        // We still need to go down the path
        else {
            // Path is json array
            if (key.matches(".+\\[\\d+]")) {
                final String cleanedKey = key.replaceAll("\\[\\d+]", "");
                final int index = Integer.parseInt(key.replaceAll("[^\\d+]", ""));
                final JSONArray array = json.getJSONArray(cleanedKey);
                array.put(index, update(array.getJSONObject(index), updatePath(path), translation));
            }
            // Path is json object
            else {
                json.put(key, update(json.getJSONObject(key), updatePath(path), translation));
            }
        }
        return json;
    }

    private String updatePath(String path) {
        return path.split("\\.", 2)[1];
    }

    public void load(String path) {
        final String content = Utilities.readFile(path);
        commands = new JSONArray(content);

        for (int i = 0; i < commands.length(); i++) {
            final JSONObject json = commands.getJSONObject(i);
            scanObject(json, json.getString("name") + ".");
        }
    }

    private void scanObject(JSONObject object, String identifier) {
        for (String key : object.keySet()) {
            if (key.endsWith("_localizations") && isObject(object, key)) {
                loadLocalization(object, key, identifier + key.substring(0, key.length() - "_localizations".length()));
            } else if (isArray(object, key)) {
                scanArray(object.getJSONArray(key), identifier + key);
            }
        }
    }

    private void scanArray(JSONArray array, String identifier) {
        for (int i = 0; i < array.length(); i++) {
            final JSONObject object = array.getJSONObject(i);
            scanObject(object, identifier + "[" + i + "].");
        }
    }

    private void loadLocalization(JSONObject json, String key, String identifier) {
        final String defaultKey = key.substring(0, key.length() - "_localizations".length());
        final String original = json.getString(defaultKey);
        final String string = json.getJSONObject(key).has(iso) ? json.getJSONObject(key).getString(iso) : "";

        final Translation translation = new Translation(identifier, original, string);
        super.translations.add(translation);
    }

    private boolean isObject(JSONObject object, String key) {
        try {
            object.getJSONObject(key);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private boolean isArray(JSONObject object, String key) {
        try {
            object.getJSONArray(key);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

}
