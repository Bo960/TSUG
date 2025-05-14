package at.ac.univie.hci.tsug.elements;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.ac.univie.hci.tsug.R;

public class PostAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final RecyclerviewInterface recyclerviewInterface;
    Context context;
    List<Post> posts;

    public PostAdapter(RecyclerviewInterface recyclerviewInterface, Context context, List<Post> posts) {
        this.recyclerviewInterface = recyclerviewInterface;
        this.context = context;
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.post_view, parent, false), recyclerviewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.type.setText(posts.get(position).getType());
        holder.title.setText(posts.get(position).getTitle());

        if(posts.get(position).isRegion()) {
            holder.visRoute.setVisibility(GONE);
            holder.visRegion.setVisibility(VISIBLE);

            holder.region.setText(posts.get(position).getRegion());

            holder.start.setText("START");
            holder.end.setText("END");
        }
        else {
            holder.visRoute.setVisibility(VISIBLE);
            holder.visRegion.setVisibility(GONE);

            holder.region.setText("REGION");

            holder.start.setText(posts.get(position).getRoute().first);
            holder.end.setText(posts.get(position).getRoute().second);
        }

        holder.likes.setText(posts.get(position).getLikesStyle());
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }
}
