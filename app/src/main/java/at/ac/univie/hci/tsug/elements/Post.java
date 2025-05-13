package at.ac.univie.hci.tsug.elements;

import android.util.Pair;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Post {
    //Instancevariable:
    private final int ID;
    private static int next_ID = 1;
    private String title;
    private int likes;
    private boolean  type;
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
    public Post(String title, int likes, User user, boolean type, ArrayList<String> tags, String region, String des) {
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
        this.type = type;
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
    public Post(String title, int likes, User user, boolean type, ArrayList<String> tags, Pair<String, String> route, String des) {
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
        this.type = type;
        this.tags = tags;
        this.user = user;

        this.isRegion = false;
        this.region = null;

        this.isRoute = true;
        this.route = route;

        this.des = des;

        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }


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
    public User getUser() {
        return user;
    }
    public String getType() {
        if(type)
            return "Frage";
        else
            return "Tipp";
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public String getRegion() {
        if(isRoute)
            throw new IllegalArgumentException("There is no Region!\n");
        return region;
    }
    public Pair<String, String> getRoute() {
        if(isRegion)
            throw new IllegalArgumentException("There is no Route!\n");
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

    //Setter Methode:
    public void setTitel(String title) {
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
    public int like() {
        this.user.newLike();
        return ++likes;
    }
    public int unlike() {
        if(likes > 0) {
            this.user.lostLike();
            return --likes;
        }
        else
            return 0;
    }
}
