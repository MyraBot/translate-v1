package bot.myra.translate.utils.loaders;

import bot.myra.translate.Translation;
import bot.myra.translate.utils.Platform;
import bot.myra.translate.utils.Utilities;

import java.util.*;

public class PropertiesLoader extends GenericLoader {

    String[] raw = new String[0];

    public PropertiesLoader(Platform platform, String iso) {
        super(platform, iso);
    }

    @Override
    public String toOriginalFormat() {
        return String.join("\n", raw);
    }

    @Override
    public void onUpdate(String key, String value) {
        for (int i = 0; i < raw.length; i++) {
            final String iKey = raw[i].split("=")[0];
            if (key.equals(iKey)) raw[i] = key + "=" + value;
        }
    }

    public void load() {
        raw = Utilities.readFile(platform.getPath(iso)).split("\n");

        if (isDefaultLang()) {
            super.map = loadInMap(raw);
            super.translations = super.map
                    .entrySet()
                    .stream()
                    .map(e -> new Translation(e.getKey(), e.getValue(), e.getValue()))
                    .toList();
        } else {
            final StringBuilder translationUpdated = new StringBuilder();
            final String[] originalStrings = Utilities.readFile(platform.getPath(Utilities.DEFAULT_LANGUAGE)).split("\n");
            final LinkedHashMap<String, String> originalMap = loadInMap(originalStrings);
            final LinkedHashMap<String, String> translationMap = loadInMap(raw);

            super.map = translationMap;
            super.translations = cleanFile(originalMap, translationMap, translationUpdated); // Clean the file

            Utilities.writeFile(platform.getPath(iso), translationUpdated.toString()); // Update file
        }
    }

    private LinkedHashMap<String, String> loadInMap(String[] raw) {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String string : raw) {
            if (string.length() == 0) continue;
            // String is comment
            if (string.startsWith("#")) {
                map.put(string, null); // Put comment without value in the map
                continue; // Skip rest of code
            }

            final String key = string.split("=", 0)[0]; // Get key of original language file
            final String value = string.length() == key.length() + "=".length() ? "" : string.split("=", 0)[1]; // Get original string

            map.put(key, value);
        }
        return map;
    }

    /**
     * Add missing keys and correct the order of the entries.
     *
     * @param originalMap        Original translations map.
     * @param translationMap     Translated translations map.
     * @param translationUpdated Updated .properties file.
     */
    private List<Translation> cleanFile(Map<String, String> originalMap, Map<String, String> translationMap, StringBuilder translationUpdated) {
        final List<Translation> raw = new ArrayList<>();

        originalMap.forEach((key, value) -> {
            // Pair is a comment
            if (key.startsWith("#")) translationUpdated.append(key).append("\n"); // Append key with empty value
                // Pair is normal translation
            else {
                final Optional<Map.Entry<String, String>> matchingEntry = translationMap.entrySet().stream().filter(entry -> key.equals(entry.getKey())).findFirst();
                // Key exists
                if (matchingEntry.isPresent()) {
                    final Map.Entry<String, String> translation = matchingEntry.get();

                    translationUpdated.append(translation.getKey()).append("=").append(translation.getValue()).append("\n"); // Append key with translated value
                    raw.add(new Translation(key, value, translation.getValue())); // Add translation
                }
                // Key doesn't exist yet
                else {
                    translationUpdated.append(key).append("=\n"); // Append key with empty value
                    raw.add(new Translation(key, value, "")); // Add translation with an empty translated string
                }
            }
        });
        return raw;
    }

}
