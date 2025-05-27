package at.ac.univie.hci.tsug.container;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import at.ac.univie.hci.tsug.elements.Comment;
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
        users.add(new User("test1", "test1@gmail.com", "123456789", 350));
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
                "Von Wien nach Salzburg",
                128,
                getUser(1),
                false,
                new ArrayList<>(List.of("Günstig")),
                new Pair<>("Wien", "Salzburg"),
                "Am komfortabelsten und schnellsten ist die Westbahn!",
                new ArrayList<>(List.of(new Comment(getUser(12), "Stimmt, so nice!")))
        ));

        posts.add(new Post(
                "Wie komme ich am besten zum Flughafen Wien?",
                42,
                getUser(12),
                true,
                new ArrayList<>(List.of("Flexibel", "Direkt")),
                new Pair<>("Stephansplatz", "Flughafen Wien"),
                "Fährt ein Bus oder Zug direkt dort hin, oder muss man ein Stück umsteigen?",
                new ArrayList<>(List.of(new Comment(getUser(5), "Schau mal über VOR Anachb App, da gibt es gute Verbindungen.")))
        ));

        posts.add(new Post(
                "Mit der Bim durch die Innenstadt",
                87,
                getUser(5),
                false,
                new ArrayList<>(),
                new Pair<>("Oper Wien", "Schwedenplatz"),
                "Gemütliche Fahrt mit Zwischenstopps an schönen Orten – perfekt bei jedem Wetter.",
                new ArrayList<>(List.of(new Comment(getUser(2), "Yes, I did this and it is so nice.")))
        ));

        posts.add(new Post(
                "Wie komme ich zum Hotel Sacher mit Öffis?",
                0,
                getUser(2),
                true,
                new ArrayList<>(List.of("Kurze Fahrt")),
                "Wien",
                "Welche Straßenbahn- oder U-Bahn-Linie ist am schnellsten vom Hauptbahnhof?",
                new ArrayList<>()
        ));

        posts.add(new Post(
                "Klettertag im Gesäuse – Anreise per Bahn",
                176,
                getUser(1),
                false,
                new ArrayList<>(List.of("Kurze Fahrt", "Flexibel")),
                new Pair<>("Johnsbachtal", "Haindlkar"),
                "Von Wien mit dem Zug nach Admont und weiter mit dem Regionalbus – tolle Verbindung ins Abenteuer!",
                new ArrayList<>(List.of(new Comment(getUser(9), "So gut!")))
        ));

        posts.add(new Post(
                "Abendverbindung von Mariazell nach Krems?",
                8,
                getUser(9),
                true,
                new ArrayList<>(List.of("Lange Fahrt")),
                new Pair<>("Mariazell", "Krems"),
                "Ich kann erst ab 18:00 – komme ich noch bis zur Unterkunft?",
                new ArrayList<>(List.of(
                        new Comment(getUser(1), "Definitiv!"),
                        new Comment(getUser(5), "Bestimmt, die Züge fahren bis Mitternacht.")
                ))
        ));

        posts.add(new Post(
                "Kleine Häuser, große Fragen: Öffi-Tipp?",
                1,
                getUser(3),
                true,
                new ArrayList<>(List.of("Günstig")),
                new Pair<>("Graz", "Lichtblickhof"),
                "Wie komme ich vom Hauptbahnhof Graz zum Tiny House Village – idealerweise ohne Taxi?",
                new ArrayList<>(List.of(new Comment(getUser(12), "Das würde mich auch interessieren...")))
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
    public static synchronized boolean updatePost(Post updatedPost) {
        if (updatedPost == null) return false;

        Post oldPost = getPost(updatedPost.getID());
        if (oldPost == null) return false;

        posts.remove(oldPost);
        posts.add(updatedPost);

        return true;
    }
    public static synchronized boolean updateUser(User updatedUser) {
        if (updatedUser == null) return false;

        User oldUser = getUser(updatedUser.getID());
        if (oldUser == null) return false;

        users.remove(oldUser);
        users.add(updatedUser);

        return true;
    }

}