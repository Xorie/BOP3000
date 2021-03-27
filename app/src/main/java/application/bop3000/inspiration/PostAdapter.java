package application.bop3000.inspiration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Post;
import application.bop3000.login.Login;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    static Context context;
    private List<Post> PostList;

    public PostAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_recview, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder PostViewholder, int i) {
        String hentimage = PostList.get(i).getPost_imagepath();
        Uri mUri = Uri.parse(hentimage);
        PostViewholder.post_image.setImageURI(mUri);
        PostViewholder.post_user.setText(Login.getUser().getDisplayname());
        PostViewHolder.post_text.setText(PostList.get(i).getPost_text());
        PostViewHolder.post_title.setText(PostList.get(i).getPost_tittle());
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        static ImageView post_image;
        static TextView post_text, post_title, post_user;

        PostViewHolder( @NonNull final View itemView){
            super(itemView);
            post_user = itemView.findViewById(R.id.srPostUser);
            post_text = itemView.findViewById(R.id.srPostTxt);
            post_title = itemView.findViewById(R.id.srPostTitle);
            post_image = itemView.findViewById(R.id.srPostImage);
                }
    }

    @Override
    public int getItemCount() {
        if (PostList == null) {
            return 0;
        }
        return PostList.size();
    }

    public void setTasks(List<Post> postList) {
        PostList = postList;
        notifyDataSetChanged();
    }
}
