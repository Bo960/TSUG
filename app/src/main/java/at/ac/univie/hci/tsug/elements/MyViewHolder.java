package at.ac.univie.hci.tsug.elements;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import at.ac.univie.hci.tsug.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView type;
    TextView title;
    LinearLayout visRoute;
    LinearLayout visRegion;
    TextView start;
    TextView end;
    TextView region;
    TextView likes;


    public MyViewHolder(@NonNull View itemView, RecyclerviewInterface recyclerViewInterface) {
        super(itemView);

        this.type = itemView.findViewById(R.id.rv_type);
        this.title = itemView.findViewById(R.id.rv_title);

        this.visRoute = itemView.findViewById(R.id.visRoute);
        this.visRegion = itemView.findViewById(R.id.visRegion);

        this.start = itemView.findViewById(R.id.rv_start);
        this.end = itemView.findViewById(R.id.rv_end);
        this.region = itemView.findViewById(R.id.rv_region);
        this.likes = itemView.findViewById(R.id.rv_likes);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onPostClick(pos);
                    }

                }
            }
        });
    }
}
