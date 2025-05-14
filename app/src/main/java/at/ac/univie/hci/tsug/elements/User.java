package at.ac.univie.hci.tsug.elements;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

enum Rank {Bronze, Silber, Gold, Diamant} //TODO MORE RANKS

public class User implements Parcelable {
    private final int ID;
    private static int next_ID = 1;
    private String name;
    private String email;
    private String password;
    private Rank rank = Rank.Bronze;
    private int likes = 0;
    private int questions = 0;
    private int answers = 0;

    //List of Post types:
    private ArrayList<Integer> likedPosts;
    private ArrayList<Integer> seenPosts;
    private ArrayList<Integer> createdPosts;

    //Constructor:
    public User(String name, String email, String password) {
        //Error Handeling:
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty!\n");
        if(email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty!\n");
        if(password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty!\n");

        //Assign Values:
        this.ID = next_ID++;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    protected User(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        likes = in.readInt();
        questions = in.readInt();
        answers = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getPassword() {
        return password;
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
    public int lostLike() {
        --likes;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeInt(likes);
        parcel.writeInt(questions);
        parcel.writeInt(answers);
    }
}