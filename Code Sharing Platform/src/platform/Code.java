package platform;

public interface Code {
    String getUuid();
    String getCode();
    String getDate();
    int getViews();
    void setViews(int views);
    int getTime();
}
