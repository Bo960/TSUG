package at.ac.univie.hci.tsug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class HistoryPostAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryPostActivity> posts;

    //Constructor
    public HistoryPostAdapter(Context context, List<HistoryPostActivity> posts) {
        this.context = context;
        this.posts = posts;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryPostActivity post = posts.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.history_post, parent, false);
        }

        // Hier wird die TextView im Layout gesetzt
        TextView postTitle = convertView.findViewById(R.id.post_title);
        postTitle.setText(post.getTitle());


        return convertView;
    }
}
