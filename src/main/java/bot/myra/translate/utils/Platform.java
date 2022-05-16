package bot.myra.translate.utils;

import bot.myra.translate.utils.loaders.GenericLoader;
import bot.myra.translate.utils.loaders.JsonLoader;
import bot.myra.translate.utils.loaders.PropertiesLoader;
import bot.myra.translate.utils.loaders.SlashCommandsLoader;

import java.util.Arrays;

public enum Platform {

    BOT("./bot/{iso}.properties"),
    WEBSITE("./website.json"),
    COMMANDS("./commands.json");

    private final String path;

    Platform(String path) {
        this.path = path;
    }

    public static Platform get(String platform) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(platform))
                .findFirst()
                .get();
    }

    public String getPath(String iso) {
        return path.replace("{iso}", iso);
    }

    public GenericLoader getLoader(String iso) {
        GenericLoader translations = null;
        switch (this) {
            case BOT -> {
                final PropertiesLoader loader = new PropertiesLoader(this, iso);
                loader.load();
                translations = loader;
            }
            case WEBSITE -> {
                final JsonLoader loader = new JsonLoader(this, iso);
                System.out.println(getPath(iso));
                loader.loadObject(getPath(iso));
                translations = loader;
            }
            case COMMANDS -> {
                final SlashCommandsLoader loader = new SlashCommandsLoader(this, iso);
                loader.load(getPath(iso));
                translations = loader;
            }
        }
        return translations;
    }

}
