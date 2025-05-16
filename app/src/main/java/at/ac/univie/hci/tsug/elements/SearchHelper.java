package at.ac.univie.hci.tsug.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.Post;

public class SearchHelper {
    //Einfache Suche nur nach Suchbegriff (ohne Filter).
    public static ArrayList<Post> simpleSearchReturn(String searchTerm) {
        String term = (searchTerm == null ? "" : searchTerm.trim())
                .toLowerCase(Locale.getDefault());
        List<Post> all = new ArrayList<>(Container.getAllPosts());
        if (term.isEmpty()) {
            return new ArrayList<>(all);
        }
        ArrayList<Post> out = new ArrayList<>();
        for (Post p : all) {
            if (p.getTitle().toLowerCase().contains(term) ||
                    p.getDes().toLowerCase().contains(term)) {
                out.add(p);
            }
        }
        return out;
    }
}
