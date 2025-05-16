package at.ac.univie.hci.tsug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import at.ac.univie.hci.tsug.R;
import at.ac.univie.hci.tsug.container.Container;
import at.ac.univie.hci.tsug.elements.Post;
import at.ac.univie.hci.tsug.elements.PostAdapter;
import at.ac.univie.hci.tsug.elements.RecyclerviewInterface;
import at.ac.univie.hci.tsug.elements.User;

public class LikedPostsActivity extends AppCompatActivity implements RecyclerviewInterface {
    private String activityName = "Favoriten";
    private User currentUser;
    public ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_posts);

        //Recieveing User from Home:
        currentUser = getIntent().getParcelableExtra("user");

        posts = Container.getListOfPosts();
        ArrayList<Post> likedPosts = new ArrayList<>();

        for(Post post : posts) {
            if (currentUser.getLikedPosts().contains(post.getID())) {
                likedPosts.add(post);
            }
        }

        setPosts(likedPosts);
        showPosts();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //Homescreen
                        intent = new Intent(LikedPostsActivity.this, HomeActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;

                    case R.id.nav_neuer_beitrag:
                        //Beitrag erstellen Seite
                        intent = new Intent(LikedPostsActivity.this, CreateActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;

                    case R.id.nav_account:
                        //Account settings Seite
                        intent = new Intent(LikedPostsActivity.this, AccountActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                        //Von Position-Rechts nach Position-Links
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        return true;
                }
                return false;
            }
        });

        //TOP NAVIGATION:
        ImageButton backNav = findViewById(R.id.nav_back);
        backNav.setOnClickListener(v -> finish());

        ImageButton setNav = findViewById(R.id.nav_einstellungen);
        setNav.setOnClickListener(v -> {
            Intent intent = new Intent(LikedPostsActivity.this, SettingsActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_up_out);
        });

        /*
        likedPostsListView= findViewById(R.id.liked_questions_list);
        likedPostsList= new ArrayList<>();

        ArrayList<Integer> liked= currentUser.getLikedPosts();
        if(liked!=null) {
            for (int postId : liked) {
                Post post = Container.getPost(postId);
                if (post != null) {
                    likedPostsList.add(new HistoryPost(post.getTitle(), post.getDes(), post.getUser()));
                }
            }
        }

        postAdapter= new HistoryPostAdapter(LikedPostsActivity.this, likedPostsList);
        likedPostsListView.setAdapter(postAdapter);
        */
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(LikedPostsActivity.this, PostActivity.class);

        intent.putExtra("beitrag_id", posts.get(position).getID());

        intent.putExtra("user", currentUser);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
    //Generating the List for the Recyclerview:
    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void showPosts()
    {
        RecyclerView recyclerView = findViewById(R.id.liked_questions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(LikedPostsActivity.this));

        recyclerView.setAdapter(new PostAdapter(LikedPostsActivity.this, getApplicationContext(), posts));

    }
}