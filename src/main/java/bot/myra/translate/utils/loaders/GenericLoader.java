package bot.myra.translate.utils.loaders;

import bot.myra.translate.Translation;
import bot.myra.translate.utils.Platform;
import bot.myra.translate.utils.Utilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        final Translation translation = iso.equals(Utilities.DEFAULT_LANGUAGE)
                ? new Translation(key, value, value, false) // isComment = false because you can't edit comments
                : new Translation(key, original.get(key), value, false);

        onUpdate(key, value);
        map.put(key, value);
        translations.stream().filter(it -> it.getKey().equals(key)).findFirst().get().setTranslatedText(value);
        Utilities.writeFile(platform.getPath(iso), toOriginalFormat());
    }

    public abstract void onUpdate(String key, String value);

    public abstract String toOriginalFormat();

}
