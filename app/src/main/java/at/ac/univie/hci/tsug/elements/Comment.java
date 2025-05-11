package at.ac.univie.hci.tsug.elements;

import java.time.LocalDate;
import java.time.LocalTime;

// data structure for comments
public class Comment {
    private String authorTxt;
    private String commentTxt;
    private User user;
    private LocalDate date;
    private LocalTime time;


    public Comment(User user, String commentText) {
        //Error Handeling:
        if(commentText.isEmpty())
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

    public String getCommentText() {
        return commentTxt;
    }
}
