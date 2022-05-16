package bot.myra.translate.utils.loaders;

import bot.myra.translate.Translation;
import bot.myra.translate.utils.Platform;
import bot.myra.translate.utils.Utilities;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Json files must contain all languages in one file.
 */
public class JsonLoader extends GenericLoader {

    private JSONObject languages = null;
    private JSONObject language = null;

    public JsonLoader(Platform platform, String iso) {
        super(platform, iso);
    }

    @Override
    public String toOriginalFormat() {
        return languages.toString(4);
    }

    @Override
    public void onUpdate(String encodedKey, String value) {
        final String[] objectsPath = encodedKey.split(":");
        final String key = objectsPath[objectsPath.length -1];
        JSONObject currentObject = language;
        for (int i = 0; i < objectsPath.length - 1; i++) {
            currentObject = currentObject.getJSONObject(objectsPath[i]);
        }
        currentObject.put(key, value);
        languages.put(iso, language);
    }

    public void loadObject(String path) {
        final String content = Utilities.readFile(path);
        languages = new JSONObject(content);
        final JSONObject defaultLang = languages.getJSONObject(Utilities.DEFAULT_LANGUAGE);
        final JSONObject translatedLang = languages.has(iso) ? languages.getJSONObject(iso) : new JSONObject();
        final JSONObject translatedLangSorted = sort(defaultLang, translatedLang);
        Utilities.writeFile(path, toOriginalFormat());
        language = translatedLangSorted;

        super.map = mapJson("", language);
        final Map<String, String> originalMapped = mapJson("", defaultLang);

        final List<Translation> translations = new ArrayList<>();
        originalMapped.forEach((key, value) -> {
            final Translation translation = new Translation(key, value, super.map.getOrDefault(key, ""));
            translations.add(translation);
        });
        super.translations = translations;
    }

    private JSONObject sort(JSONObject defaultLang, JSONObject translatedLang) {
        final JSONObject translatedLangSorted = new JSONObject();
        for (String key : defaultLang.keySet()) {
            // Value is a normal string
            if (isPrimitive(defaultLang, key)) {
                if (translatedLang.has(key)) translatedLangSorted.put(key, translatedLang.getString(key));
                else translatedLangSorted.put(key, "");
            }
            // Value is a json object
            else if (isObject(defaultLang, key)) {
                final JSONObject value = sort(defaultLang.getJSONObject(key), translatedLang.has(key) ? translatedLang.getJSONObject(key) : new JSONObject());
                translatedLangSorted.put(key, value);
            } else new IllegalStateException("Unsupported json type").printStackTrace();
        }
        return translatedLangSorted;
    }

    private LinkedHashMap<String, String> mapJson(String path, JSONObject json) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String key : json.keySet()) {
            try {
                final String value = json.getString(key);
                map.put(path + key, value);
            } catch (JSONException e1) {
                try {
                    map.putAll(mapJson(path + key + ":", json.getJSONObject(key)));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return map;
    }

    private boolean isPrimitive(JSONObject object, String key) {
        try {
            object.getString(key);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private boolean isObject(JSONObject object, String key) {
        try {
            object.getJSONObject(key);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

}
