package at.ac.univie.hci.tsug.elements;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashSet;
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
    private HashSet<Integer> likedPosts = new HashSet<>();
    private HashSet<Integer> seenPosts = new HashSet<>();
    private HashSet<Integer> createdPosts = new HashSet<>();

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
    public User(String name, String email, String password, int likes) {
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
        this.likes = likes;
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
    public synchronized int getID() {
        return ID;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized String getEmail() {
        return email;
    }

    public synchronized String getPassword() {
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

    public synchronized int getQuestions() {
        return questions;
    }

    public synchronized int getLikes() {
        return likes;
    }

    public synchronized int getAnswers() {
        return answers;
    }

    public synchronized HashSet<Integer> getLikedPosts() {
        return likedPosts;
    }

    public synchronized HashSet<Integer> getSeenPosts() {
        //TODO deltet +30day year old seen posts!!
        return seenPosts;
    }

    public synchronized HashSet<Integer> getCreatedPosts() {
        return createdPosts;
    }

    //Setter-Methode:
    public synchronized void setEmail(String email) {
        this.email = email;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }
    public synchronized void setPassword(String password) {this.password = password;}

    //Memberfunctions:
    public synchronized int newQuestion() {
        return ++questions;
    }
    public synchronized int newAnswer() {
        return ++answers;
    }
    public synchronized int newLike() {
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
    public synchronized int lostLike() {
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
    public synchronized void addLikedPost(int ID) {
        likedPosts.add(ID);
    }
    public synchronized void removeLikedPost(int ID) {
        likedPosts.remove(ID);
    }
    public synchronized void addSeenPost(int ID) {
        seenPosts.add(ID);
        //saveUserData();
    }
    public synchronized void addCreatedPost(int ID) {
        createdPosts.add(ID);
        //saveUserData();
    }

    public synchronized boolean hasLiked(int ID) {
        return likedPosts.contains(ID);
    }
    public synchronized boolean hasSeen(int ID) {
        return seenPosts.contains(ID);
    }
    public synchronized boolean hasCreated(int ID) {
        return createdPosts.contains(ID);
    }

    @Override
    public synchronized int describeContents() {
        return 0;
    }

    protected User(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        likes = in.readInt();
        questions = in.readInt();
        answers = in.readInt();

        // Read HashSets from Parcel
        likedPosts = (HashSet<Integer>) in.readSerializable();
        seenPosts = (HashSet<Integer>) in.readSerializable();
        createdPosts = (HashSet<Integer>) in.readSerializable();
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

        // Write HashSets to Parcel
        parcel.writeSerializable(likedPosts);
        parcel.writeSerializable(seenPosts);
        parcel.writeSerializable(createdPosts);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return this.ID == user.ID; // oder: this.username.equals(user.username)
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID); // oder: Objects.hash(username)
    }

}