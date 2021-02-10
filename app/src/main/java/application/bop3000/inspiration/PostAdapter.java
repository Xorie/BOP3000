package application.bop3000.inspiration;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context context;
    private List<Post> mPostList;

    public PostAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_recview, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder PostViewholder, int i) {
        PostViewHolder.post_text.setText(mPostList.get(i).getPost_text());
    }

    public List<Post> getData() {
        return mPostList;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        Context context;
        static TextView post_text, post_id, post_user;
        //ImageView editImage;
        MyDatabase mDb;

        PostViewHolder(@NonNull final View itemView) {
            super(itemView);
            mDb = MyDatabase.getDatabase(context);
            post_text = itemView.findViewById(R.id.srPostTxt);
        }
    }
    @Override
    public int getItemCount() {
        if (mPostList == null) {
            return 0;
        }
        return mPostList.size();

    }

    public void setTasks(List<Post> postList) {
        mPostList = postList;
        notifyDataSetChanged();
    }
}
