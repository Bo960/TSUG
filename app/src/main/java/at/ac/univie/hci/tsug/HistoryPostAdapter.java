package at.ac.univie.hci.tsug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class HistoryPostAdapter extends BaseAdapter {
    private ArrayList<HistoryPost> posts;
    private Context context;
    //Constructor
    public HistoryPostAdapter(Context context, ArrayList<HistoryPost> posts) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) { return posts.get(position).getUser().getID(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.history_post, parent, false);
        }
        HistoryPost post = posts.get(position);

        // Hier wird die TextView im Layout gesetzt
        TextView postTitle = convertView.findViewById(R.id.post_title);
        postTitle.setText(post.getTitle());
        TextView author = convertView.findViewById(R.id.post_author);
        author.setText(post.getUser().getName());
        TextView context = convertView.findViewById(R.id.post_context);
        context.setText(post.getContent());

        return convertView;
    }
}
