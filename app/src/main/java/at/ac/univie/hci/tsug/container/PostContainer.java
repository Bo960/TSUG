package at.ac.univie.hci.tsug.container;

import static at.ac.univie.hci.tsug.container.UserContainer.users;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

public class PostContainer extends Application {
    private static PostContainer instance;
    private static final Set<Post> posts = new HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initializeList();
    }

    public static PostContainer getInstance() {
        return instance;
    }

    private void initializeList() {
        posts.add(new Post(
                "Sunrise at Dachstein",
                128,
                UserContainer.get(1),
                new ArrayList<String>(){},
                new Pair<>("Schladming", "Dachstein Glacier"),
                "Started before dawn to catch this magical moment at 2700m altitude!"
        ));
        posts.add(new Post(
                "Almabtrieb in Tyrol",
                342,
                UserContainer.get(12),
                new ArrayList<String>(){},
                new Pair<>("Innsbruck", "Stubai Valley"),
                "The annual cattle drive from mountain pastures is a spectacle of bells and flowers"
        ));
        posts.add(new Post(
                "Hidden Courtyards of Vienna",
                87,
                UserContainer.get(5),
                new ArrayList<String>(){},
                new Pair<>("Stephansplatz", "Judenplatz"),
                "Discovering secret passages and Renaissance courtyards in the 1st district"
        ));
        posts.add(new Post(
                "Sachertorte Taste Test",
                215,
                UserContainer.get(2),
                new ArrayList<String>(){},
                new Pair<>("Hotel Sacher", "Demel"),
                "The ultimate Vienna chocolate cake showdown - which is better?"
        ));
        posts.add(new Post(
                "Via Ferrata in Gesäuse",
                176,
                UserContainer.get(1),
                new ArrayList<String>(){},
                new Pair<>("Johnsbachtal", "Haindlkar"),
                "Iron cables and breathtaking views in Austria's most dramatic national park"
        ));
        posts.add(new Post(
                "Night Skiing in Sölden",
                298,
                UserContainer.get(9),
                new ArrayList<String>(){},
                new Pair<>("Sölden Base", "Giggijoch"),
                "Floodlit slopes until 10pm with the best après-ski in the Alps!"
        ));
        posts.add(new Post(
                "The Tiny House Village",
                153,
                UserContainer.get(3),
                new ArrayList<String>(){},
                new Pair<>("Graz", "Lichtblickhof"),
                "A community living in homes smaller than 20m² - surprisingly cozy!"
        ));
    }

    public static synchronized boolean add(Post post) {
        if (post != null && !posts.contains(post))
            return posts.add(post);
        return false;
    }
    public static synchronized Post get(int ID) {
        for (Post post : posts) {
            if (post.getID() == ID)
                return post;
        }
        return null;
    }
    public static synchronized Post get(String title) {
        for (Post post : posts) {
            if (Objects.equals(post.getTitel(), title))
                return post;
        }
        return null;
    }
    public static synchronized boolean remove(int ID) {
        Post post = get(ID);
        return post != null && posts.remove(post);
    }
    public static synchronized boolean remove(Post post) {
        return posts.remove(post);
    }
    public static synchronized User author(int ID) {
        return Objects.requireNonNull(get(ID)).getUser();
    }
    public static synchronized Set<Post> getAll() {
        return new HashSet<>(posts);
    }
    public static synchronized int count() {
        return posts.size();
    }
}