package bot.myra.translate.utils.loaders;

import bot.myra.translate.Translation;
import bot.myra.translate.utils.Platform;
import bot.myra.translate.utils.Utilities;

import java.util.*;

abstract public class GenericLoader {

    Platform platform;
    String iso;
    Map<String, String> original = new LinkedHashMap<>();
    Map<String, String> map = new LinkedHashMap<>();
    List<Translation> translations = new ArrayList<>();

    public GenericLoader(Platform platform, String iso) {
        this.platform = platform;
        this.iso = iso;
    }

    public boolean isDefaultLang() {
        return iso.equals(Utilities.DEFAULT_LANGUAGE);
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void update(String key, String value) {
        final boolean translationExists = translations.stream().anyMatch(it -> it.key().equals(key));
        if (translationExists) translations.removeIf(it -> it.key().equals(key));
        final Translation translation = iso.equals(Utilities.DEFAULT_LANGUAGE)
                ? new Translation(key, value, value)
                : new Translation(key, original.get(key), value);

        onUpdate(key, value);
        map.put(key, value);
        translations.add(translation);

        Utilities.writeFile(platform.getPath(iso), toOriginalFormat());
    }

    public abstract void onUpdate(String key, String value);

    public abstract String toOriginalFormat();

}
