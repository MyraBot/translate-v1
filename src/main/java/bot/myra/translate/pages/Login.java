package bot.myra.translate.pages;

import bot.myra.translate.Database;
import bot.myra.translate.utils.Utilities;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import spark.Request;
import spark.Response;

public class Login {

    public static Object onPageVisit(Request req, Response res) {
        // User has already saved login data
        if (req.cookie("username") != null && req.cookie("password") != null) {
            final Document user = Database.users.find(Filters.and(
                    Filters.eq("username", req.cookie("username")), // Right username
                    Filters.eq("password", req.cookie("password"))) // Right password
            ).first();
            // Users cookie login data is invalid
            if (user != null) {
                res.redirect("/languages"); // Redirect to languages tab
                return "test";
            }

            res.removeCookie("username");
            res.removeCookie("password");
        }

        return Utilities.readResource("login/login.html");
    }

}
