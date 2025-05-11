package at.ac.univie.hci.tsug.elements;

import android.provider.ContactsContract;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

enum Rank {Bronze, Silber, Gold, Diamant} //TODO MORE RANKS
public class User {
    private final int ID;
    private static int next_ID = 1;
    private String name;
    private String email;
    private Rank rank = Rank.Bronze;
    private int likes = 0;
    private int questions = 0;
    private int answers = 0;

    //List of Post types:
    private ArrayList<Integer> likedPosts;
    private ArrayList<Integer> seenPosts;
    private ArrayList<Integer> createdPosts;

    //Constructor:
    public User(String name, String email) {
        //Error Handeling:
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty!\n");
        if(email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty!\n");

        //Assign Values:
        this.ID = next_ID++;
        this.name = name;
        this.email = email;
    }

    //Getter-Methode:
    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRank() {
        switch (this.rank) {
            case Bronze:
                return "Bronze";
            case Silber:
                return "Silber";
            case Gold:
                return "Gold";
            case Diamant:
                return "Diamant";
            default:
                return "";
        }
    }

    public int getQuestions() {
        return questions;
    }

    public int getLikes() {
        return likes;
    }

    public int getAnswers() {
        return answers;
    }

    public ArrayList<Integer> getLikedPosts() {
        return likedPosts;
    }

    public ArrayList<Integer> getSeenPosts() {
        //TODO deltet +30day year old seen posts!!
        return seenPosts;
    }

    public ArrayList<Integer> getCreatedPosts() {
        return createdPosts;
    }

    //Setter-Methode:
    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Memberfunctions:
    public int newQuestion() {
        return ++questions;
    }
    public int newAnswer() {
        return ++answers;
    }
    public int newLike() {
        ++likes;
        if(likes == 100)
            rank = Rank.Bronze;
        else if(likes == 250)
            rank = Rank.Silber;
        else if(likes == 500)
            rank = Rank.Gold;
        else if(likes == 1000)
            rank = Rank.Diamant;

        return likes;
    }
    public void addLikedPost(Post newPost) {
        likedPosts.add(newPost.getID());
    }
    public void addSeenPost(Post newPost) {
        seenPosts.add(newPost.getID());
    }
    public void addCreatedPost(Post newPost) {
        createdPosts.add(newPost.getID());
    }
}