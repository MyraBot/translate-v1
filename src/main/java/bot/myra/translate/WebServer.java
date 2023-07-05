package bot.myra.translate;

import bot.myra.translate.pages.*;
import bot.myra.translate.pages.api.SaveString;
import bot.myra.translate.utils.Utilities;

import static spark.Spark.*;

public class WebServer {

    /**
     * The {@link spark.Spark} library uses Jetty.
     *
     * @param args Some useless arguments
     */
    public static void main(String[] args) {
        Utilities.init();
        port(1515); // Set port to 1515
        staticFileLocation("/"); // Set the location for files

        // Pages
        get("/log-in", Login::onPageVisit); // Login Page
        get("/languages", LanguageSelection::onPageVisit); // Language selection
        get("/translate/:language", PlatformSelection::onPageVisit); // Platform selection
        get("/translate/:platform/:language", StringOverview::onPageVisit); // Translation overview
        get("/translate/:platform/:language/editor", StringEditor::onPageVisit); // Translate a word

        path("/api", () -> {
            post("/edit", "application/json", SaveString::saveWord); // Save translated word
        });

        // Errors
        notFound((req, res) -> Utilities.readResource("errors/404.html"));

        // Redirects
        redirect.get("/", "/log-in");
    }

}
