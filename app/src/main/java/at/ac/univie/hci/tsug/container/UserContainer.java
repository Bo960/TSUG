package at.ac.univie.hci.tsug.container;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.User;

public class UserContainer extends Application {
    private static UserContainer instance;
    static final Set<User> users = new HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        inintalizeList();
    }

    public static UserContainer getInstance() {
        return instance;
    }

    public static Set<User> getUsers() {
        return users;
    }

    private void inintalizeList() {
        users.add(new User("KarlMayer", "kmayer@gmail.com", "Password123"));
        users.add(new User("SarahJohnson", "sjohnson@yahoo.com", "SecurePass456"));
        users.add(new User("GertrudeDieGeile", "susi.live@gmail.com", "callMeMaybe;)"));

        users.add(new User("", "@gmail.com", ""));
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
        users.add(new User("AndrewRoberts", "aroberts@yahoo.com", "AndyR123"));
        users.add(new User("OliviaTurner", "oturner@matrix.com", "OliviaT!"));
        users.add(new User("ToddHowdy", "chessclubbeth@icloud.com", "whoIsLaughingNow"));
    }

    public static synchronized boolean add(User user) {
        if (user != null && !users.contains(user))
            return users.add(user);
        return false;
    }
    public static synchronized User get(int ID) {
        for (User user : users) {
            if (user.getID() == ID)
                return user;
        }
        return null;
    }
    public static synchronized User get(String name) {
        for (User user : users) {
            if (Objects.equals(user.getName(), name))
                return user;
        }
        return null;
    }
    public static synchronized boolean remove(int ID) {
        User user = get(ID);
        return user != null && users.remove(user);
    }
    public static synchronized boolean remove(User user) {
        return users.remove(user);
    }
    public static synchronized Set<User> getAll() {
        return new HashSet<>(users);
    }
    public static synchronized int count() {
        return users.size();
    }
}