package at.ac.univie.hci.tsug;

public class HistoryPostActivity {
    private String title;
    private String content;

    public HistoryPostActivity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
