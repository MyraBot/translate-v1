package bot.myra.translate.utils;

import bot.myra.translate.Database;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class Utilities {
    public static Properties config = new Properties();

    public static final String DEFAULT_LANGUAGE = "en-GB";
    public static final String CODE = "7YHTQNzmyC3fj8IcwJwj";
    public static final String LANGUAGE_PATH_LOCAL = "C:\\Users\\Marian\\Desktop\\Translations\\{$platform}\\{$language.isoCode}.properties";
    public static final String LANGUAGE_PATH_SERVER = "/root/translations/{$platform}/{$language.isoCode}.properties";
    public static String LANGUAGE_PATH = "./{$platform}/{$language.isoCode}.properties";

    public static void init() {
        try {
            config.load(Utilities.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if the user is logged in. If the login data is invalid, the user will get redirected to the login page.
     * <p>
     * If the visited page is a translation page, it gets checked if the user is responsible for the language he's editing.
     * If not a 401 http error gets thrown.
     *
     * @param req A {@link Request}.
     * @param res A {@link Response}.
     * @return If the login data is correct, the database user gets returned. Otherwise, this method will return null.
     */
    public static Document authorize(Request req, Response res) {
        // Authentication
        final Document user = Database.users.find(Filters.and(
                Filters.eq("username", req.cookie("username")), // Right username
                Filters.eq("password", req.cookie("password"))) // Right password
        ).first();
        // User wasn't found
        if (user == null) {
            res.removeCookie("username");
            res.removeCookie("password");
            res.redirect("/log-in?error=invalidLoginData");
            return null;
        }

        // User is on page where you can translate
        if (req.uri().startsWith("/translate")) {
            final List<String> languages = user.getList("languages", String.class);
            final String language = req.params("language"); // Get language the user is currently editing
            // User isn't allowed to edit this language
            if (!languages.contains(language)) {
                Spark.halt(401); // Access denied
                return null;
            }
        }

        return user;
    }

    /**
     * Disable caching on the page.
     *
     * @param res A {@link Response}.
     */
    public static void disableCaching(Response res) {
        // Stop page caching
        res.header("Cache-Control", "no-cache, no-store, must-revalidate");
        res.header("Pragma", "no-cache");
        res.header("Expires", "0");
    }

    /**
     * @param filePath A path to the file starting from the resource folder.
     * @return Returns the content of the file as a UTF 8 String.
     */
    public static String readResource(String filePath) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            InputStream inputStream = Utilities.class.getClassLoader().getResourceAsStream(filePath);

            byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    /**
     * Reads a file.
     *
     * @return Returns the content of the file.
     */
    public static String readFile(String path) {
        final File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StringBuilder string = new StringBuilder();
        try (FileReader reader = new FileReader(path
                , StandardCharsets.UTF_8)) {
            int content;
            while ((content = reader.read()) != -1) {
                string.append((char) content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string.toString();
    }

    public static void writeFile(String path, String text) {
        if (!new File(path).exists()) {
            final File file = new File(path);
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(path), StandardCharsets.UTF_8)) {
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
