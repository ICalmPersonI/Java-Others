package platform;


import javax.servlet.http.HttpServletResponse;

public class Utils {
    public static void responseToHTML(HttpServletResponse response) {
        response.addHeader("Content-Type", "text/html");
    }

    public static void responseToJson(HttpServletResponse response) {
        response.addHeader("Content-Type", "application/json");
    }


}


