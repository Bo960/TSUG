package at.ac.univie.hci.tsug.activities;


import at.ac.univie.hci.tsug.elements.User;

public class HistoryPost {
    private String title;
    private String content;
    private User user;


    public HistoryPost(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user=user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    public User getUser() {
        return user;
    }
}
