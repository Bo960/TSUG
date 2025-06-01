package at.ac.univie.hci.tsug.elements;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

// data structure for comments
public class Comment implements Serializable {
    private String authorTxt;
    private String commentTxt;
    private User user;
    private LocalDate date;
    private LocalTime time;


    public Comment(User user, String commentText) {
        //Error Handeling:
        if (commentText.isEmpty())
            throw new IllegalArgumentException("Text cannot be empty!\n");

        //Assign Values:
        this.authorTxt = user.getName();
        this.commentTxt = commentText;

        this.user = user;

        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public String getAuthor() {
        return authorTxt;
    }

    public User getUser() {
        return user;
    }

    public String getCommentText() {
        return commentTxt;
    }

    public void setCommentText(String newText) {
        this.commentTxt = newText;
    }
}
