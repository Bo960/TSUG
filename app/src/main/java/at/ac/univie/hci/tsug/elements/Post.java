package at.ac.univie.hci.tsug.elements;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Post implements Parcelable {
    //Instancevariable:
    private final int ID;
    private static int next_ID = 1;
    private String title;
    private int likes;
    private boolean isFrage;
    //TRUE: Wenn es eine Frage ist!
    //FALSE: Wenn es ein Tipp ist!
    private ArrayList<String> tags;
    private User user;
    private boolean isRegion = true;
    private String region;
    private boolean isRoute = false;
    private Pair<String, String> route;
    private String des;
    private LocalDate date;
    private LocalTime time;

    //Constructor:
    public Post(String title, int likes, User user, boolean isFrage, ArrayList<String> tags, String region, String des) {
        //Error Handeling:
        if(title.isEmpty())
            throw new IllegalArgumentException("Titel cannot be empty!\n");
        if(likes < 0)
            throw new IllegalArgumentException("Likes cannot be neagtive!\n");
        if(region.isEmpty())
            throw new IllegalArgumentException("Region cannot be empty!\n");

        //Assign Values:
        this.ID = next_ID++;

        this.title = title;
        this.likes = likes;
        this.isFrage = isFrage;
        this.tags = tags;
        this.user = user;

        this.isRegion = true;
        this.region = region;

        this.isRoute = false;
        this.route = null;

        this.des = des;

        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }
    public Post(String title, int likes, User user, boolean isFrage, ArrayList<String> tags, Pair<String, String> route, String des) {
        //Error Handeling:
        if(title.isEmpty())
            throw new IllegalArgumentException("Titel cannot be empty!\n");
        if(likes < 0)
            throw new IllegalArgumentException("Likes cannot be neagtive!\n");
        if(route.first.isEmpty())
            throw new IllegalArgumentException("Start of the Route cannot be empty!\n");
        if(route.second.isEmpty())
            throw new IllegalArgumentException("End of the Route cannot be empty!\n");

        //Assign Values:
        this.ID = next_ID++;

        this.title = title;
        this.likes = likes;
        this.isFrage = isFrage;
        this.tags = tags;
        this.user = user;

        this.isRegion = false;
        this.region = "";

        this.isRoute = true;
        this.route = route;

        this.des = des;

        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }


    protected Post(Parcel in) {
        ID = in.readInt();
        title = in.readString();
        likes = in.readInt();
        isFrage = in.readByte() != 0;
        tags = in.createStringArrayList();
        isRegion = in.readByte() != 0;
        region = in.readString();
        isRoute = in.readByte() != 0;
        des = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    //Getter Methode:
    public int getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public int getLikes() {
        return likes;
    }
    public String getLikesStyle() {
        if(likes > 1000) {
            return (likes/1000 + "k");
        }
        else
            return Integer.toString(likes);
    }
    public User getUser() {
        return user;
    }
    public String getFrage() {
        if(isFrage)
            return "Frage";
        else
            return "Tipp";
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public String getRegion() {
        return region;
    }
    public Pair<String, String> getRoute() {
        return route;
    }
    public String getDes() {
        return des;
    }
    public LocalDate getDate() {
        return date;
    }
    public LocalTime getTime() {
        return time;
    }
    public String getType() {
        if (isFrage)
            return "FRAGE";
        else
            return "TIPP";
    }
    public boolean isRegion() {
        return isRegion;
    }

    //Setter Methode:
    public void setTitle(String title) {
        this.title = title;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
    public void setRegion(String region) {
        if(isRoute) {
            this.isRegion = true;

            this.isRoute = false;
            this.route = null;
        }
        this.region = region;
    }
    public void setRoute(Pair<String, String> route) {
        if(isRegion) {
            this.isRoute = true;

            this.isRegion = false;
            this.region = null;
        }
        this.route = route;
    }
    public void setDes(String des) {
        this.des = des;
    }

    //Memberfunctions:
    public synchronized int addLike(User liker) {
        if (liker != null && !liker.hasLiked(ID)) {
            likes++;
            liker.addLikedPost(ID);  // Add post ID to user's likedPosts
            this.user.newLike();     // Increment user's total likes
        }
        return likes;
    }

    public synchronized int removeLike(User liker) {
        if (liker != null && liker.hasLiked(ID)) {
            likes--;
            liker.removeLikedPost(ID);
            this.user.lostLike();
        }
        return likes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(title);
        parcel.writeInt(likes);
        parcel.writeByte((byte) (isFrage ? 1 : 0));
        parcel.writeStringList(tags);
        parcel.writeByte((byte) (isRegion ? 1 : 0));
        parcel.writeString(region);
        parcel.writeByte((byte) (isRoute ? 1 : 0));
        parcel.writeString(des);
    }
}
