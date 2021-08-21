package platform;

public class JSonForm {
    private String code;
    private String date;
    private int time;
    private int views;

    JSonForm(String code, String date, int time, int views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
    }

    public String getDate() {
        return date
                .replaceAll("-", "/")
                .replaceFirst("\\.[\\d]++", "");
    }

    public String getCode() {
        return code;
    }

    public int getTime() {
        return time;
    }

    public int getViews() {
        return views;
    }
}
