package at.ac.univie.hci.tsug;

import android.util.Pair;

import java.util.ArrayList;

public class Post {
    //Instancevariable:
    private final int ID;
    private static int next_ID = 1;
    private String titel;
    private int likes;
    private ArrayList<String> tags;
    private boolean isRegion = true;
    private String region;
    private boolean isRoute = false;
    private Pair<String, String> route;
    private String des;

    //Constructor:
    public Post(String titel, int likes, ArrayList<String> tags, String region, String des) {
        //Error Handeling:
        if(titel.isEmpty())
            throw new IllegalArgumentException("Titel cannot be empty!\n");
        if(likes < 0)
            throw new IllegalArgumentException("Likes cannot be neagtive!\n");
        if(region.isEmpty())
            throw new IllegalArgumentException("Region cannot be empty!\n");
        if(des.isEmpty())
            throw new IllegalArgumentException("Description cannot be empty!\n");

        //Assign Values:
        this.ID = next_ID++;

        this.titel = titel;
        this.likes = likes;
        this.tags = tags;

        this.isRegion = true;
        this.region = region;

        this.isRoute = false;
        this.route = null;

        this.des = des;
    }
    public Post(String titel, int likes, ArrayList<String> tags, Pair<String, String> route, String des) {
        //Error Handeling:
        if(titel.isEmpty())
            throw new IllegalArgumentException("Titel cannot be empty!\n");
        if(likes < 0)
            throw new IllegalArgumentException("Likes cannot be neagtive!\n");
        if(route.first.isEmpty())
            throw new IllegalArgumentException("Start of the Route cannot be empty!\n");
        if(route.second.isEmpty())
            throw new IllegalArgumentException("End of the Route cannot be empty!\n");
        if(des.isEmpty())
            throw new IllegalArgumentException("Description cannot be empty!\n");

        //Assign Values:
        this.ID = next_ID++;

        this.titel = titel;
        this.likes = likes;
        this.tags = tags;

        this.isRegion = false;
        this.region = null;

        this.isRoute = true;
        this.route = route;

        this.des = des;
    }

    //Getter Methode:
    public int getID() {
        return ID;
    }
    public String getTitel() {
        return titel;
    }
    public int getLikes() {
        return likes;
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

    //Setter Methode:
    public void setTitel(String titel) {
        this.titel = titel;
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
        return ++likes;
    }
    public int unlike() {
        if(likes > 0)
            return --likes;
        else
            return 0;
    }
}
