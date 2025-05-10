package at.ac.univie.hci.tsug.comments;

// data structure for comments
public class Comment {
    private String authorTxt;
    private String commentTxt;

    public Comment(String author, String commentText) {
        this.authorTxt = author;
        this.commentTxt = commentText;
    }

    public String getAuthor() {
        return authorTxt;
    }

    public String getCommentText() {
        return commentTxt;
    }
}
