package at.ac.univie.hci.tsug.container;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

public class Container extends Application {

    private static Container instance;
    private static final Set<User> users = new HashSet<>();
    private static final Set<Post> posts = new HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initalizeList();
    }

    public static Container getInstance() {
        return instance;
    }

    public void initalizeList() {
        users.add(new User("KarlMayer", "kmayer@gmail.com", "Password123"));
        users.add(new User("SarahJohnson", "sjohnson@yahoo.com", "SecurePass456"));
        users.add(new User("test1", "test1@gmail.com", "123456789"));
        users.add(new User("GertrudeDieGeile", "susi.live@gmail.com", "callMeMaybe;)"));
        users.add(new User("wow_fan_96", "leroyjenkins96@gmail.com", "wowisaws"));

        users.add(new User("MikeBrown", "mbrown@outlook.com", "Brownie789"));
        users.add(new User("EmilyDavis", "edavis@gmail.com", "EmPass2023"));
        users.add(new User("xxXX_Travel_man_69_420_XXxx", "kyle2001htl@gmail.com", "§$dac%HCDa&!"));
        users.add(new User("sebiShorty", "vanderbellen@gmail.com", "koglerDrinktRadler101"));
        users.add(new User("BrandonNelson", "bnelson@gmail.com", "BrandonN!"));

        users.add(new User("MeganCarter", "mcarter@protonmail.com", "Megan2023"));
        users.add(new User("JustinMitchell", "jmitchell@icloud.com", "JustinM!"));
        users.add(new User("KaylaPerez", "kperez@gmail.com", "PerezKayla"));
        users.add(new User("ReichlMukiBude", "reichlbreitl@gmail.com", "CreatineAndMozartkugel"));
        users.add(new User("Werner Reisswolf", "merzedesgs500@yahoo.com", "zugfahren?nein"));

        users.add(new User("JackHughmann", "@HughJackmannyahoo.com", "wolverine69"));
        users.add(new User("ObamaDrama", "ObambaBinLaden@yahoo.com", "ObamasLastName"));
        users.add(new User("bestRyan", "ryanGosling@yahoo.com", "NotReynolds"));
        users.add(new User("OliviaTurner", "oturner@matrix.com", "OliviaT!"));
        users.add(new User("ToddHowdy", "chessclubbeth@icloud.com", "whoIsLaughingNow"));

        posts.add(new Post(
                "Sunrise at Dachstein",
                128,
                getUser(1),
                true,
                new ArrayList<String>(){},
                new Pair<>("Schladming", "Dachstein Glacier"),
                "Started before dawn to catch this magical moment at 2700m altitude!"
        ));
        posts.add(new Post(
                "Tonight On Top Gear",
                8,
                getUser("JackHughmann"),
                false,
                new ArrayList<String>(){},
                "England",
                "Jeremy is fat as fuck."
        ));
        posts.add(new Post(
                "Almabtrieb in Tyrol",
                342,
                getUser(12),
                false,
                new ArrayList<String>(){},
                new Pair<>("Innsbruck", "Stubai Valley"),
                "The annual cattle drive from mountain pastures is a spectacle of bells and flowers"
        ));
        posts.add(new Post(
                "Hidden Courtyards of Vienna",
                87,
                getUser(5),
                true,
                new ArrayList<String>(){},
                new Pair<>("Stephansplatz", "Judenplatz"),
                "Discovering secret passages and Renaissance courtyards in the 1st district"
        ));
        posts.add(new Post(
                "Island Train Ride",
                6942,
                getUser(20),
                false,
                new ArrayList<String>(){},
                "island",
                "Here is maybe one train treck and one train that one is in a mueseum LOL!"
        ));
        posts.add(new Post(
                "Sachertorte Taste Test",
                215000,
                getUser(2),
                true,
                new ArrayList<String>(){},
                new Pair<>("Hotel Sacher", "Demel"),
                "The ultimate Vienna chocolate cake showdown - which is better?"
        ));
        posts.add(new Post(
                "Via Ferrata in Gesäuse",
                176,
                getUser(1),
                false,
                new ArrayList<String>(){},
                new Pair<>("Johnsbachtal", "Haindlkar"),
                "Iron cables and breathtaking views in Austria's most dramatic national park"
        ));
        posts.add(new Post(
                "Blowing up a childrens hospital",
                1234,
                getUser(17),
                true,
                new ArrayList<String>(){},
                "middleEast",
                "Merica America Bald eagle and guns RAHHH!"
        ));
        posts.add(new Post(
                "Night Skiing in Sölden",
                298,
                getUser(9),
                false,
                new ArrayList<String>(){},
                new Pair<>("Sölden Base", "Giggijoch"),
                "Floodlit slopes until 10pm with the best après-ski in the Alps!"
        ));
        posts.add(new Post(
                "The Tiny House Village",
                153,
                getUser(3),
                true,
                new ArrayList<String>(){},
                new Pair<>("Graz", "Lichtblickhof"),
                "A community living in homes smaller than 20m² - surprisingly cozy!"
        ));
    }

    //POST FUNCTIONS:
    public static synchronized boolean addPost(Post post) {
        if (post != null && !posts.contains(post))
            return posts.add(post);
        return false;
    }
    public static synchronized Post getPost(int ID) {
        for (Post post : posts) {
            if (post.getID() == ID)
                return post;
        }
        return null;
    }
    public static synchronized Post getPost(String title) {
        for (Post post : posts) {
            if (Objects.equals(post.getTitle(), title))
                return post;
        }
        return null;
    }
    public static synchronized boolean removePost(int ID) {
        Post post = getPost(ID);
        return post != null && posts.remove(post);
    }
    public static synchronized boolean removePost(Post post) {
        return posts.remove(post);
    }
    public static synchronized Set<Post> getAllPosts() {
        return new HashSet<>(posts);
    }
    public static synchronized ArrayList<Post> getListOfPosts() {
        return new ArrayList<>(posts);
    }
    public static synchronized int countPosts() {
        return posts.size();
    }

    //USER FUNCTIONS:
    public static synchronized boolean addUser(User user) {
        if (user != null && !users.contains(user))
            return users.add(user);
        return false;
    }
    public static synchronized User getUser(int ID) {
        for (User user : users) {
            if (user.getID() == ID)
                return user;
        }
        return null;
    }
    public static synchronized User getUser(String name) {
        for (User user : users) {
            if (Objects.equals(user.getName(), name))
                return user;
        }
        return null;
    }
    public static synchronized boolean removeUser(int ID) {
        User user = getUser(ID);
        return user != null && users.remove(user);
    }
    public static synchronized boolean removeUser(User user) {
        return users.remove(user);
    }
    public static synchronized Set<User> getAllUsers() {
        return new HashSet<>(users);
    }
    public static synchronized int countUsers() {
        return users.size();
    }
}
