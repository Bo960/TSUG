package at.ac.univie.hci.tsug.elements;
import at.ac.univie.hci.tsug.R;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> comments;

    // Constructor
    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = (comments != null) ? comments : new ArrayList<>();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent, false);
        }
        TextView commentText = convertView.findViewById(R.id.commentText);
        Comment comment = comments.get(position);

        // format author in bold text
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String author = comment.getAuthor();
        String commentTxt = comment.getCommentText();
        int start = sb.length();
        sb.append(author);
        sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(": ").append(commentTxt);
        commentText.setText(sb);

        return convertView;
    }

    // update list
    public void updateComments(List<Comment> newComments) {
        comments.clear();
        if (newComments != null) {
            comments.addAll(newComments);
        }
        notifyDataSetChanged();
    }
}
