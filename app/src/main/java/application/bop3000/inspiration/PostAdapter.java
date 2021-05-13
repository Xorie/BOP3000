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
import application.bop3000.database.User;
import application.bop3000.login.Login;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    static Context context;
    private List<Post> PostList;
    public User user;
    public PostAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_recview, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder PostViewholder, int i) {
        // Room DB and DAO
        MyDatabase myDatabase = MyDatabase.getDatabase(context);
        final KnittersboxDao knittersboxDao = myDatabase.getKnittersboxDao();
        String hentimage = PostList.get(i).getPost_imagepath();
        Uri mUri = Uri.parse(hentimage);
        int userID = Integer.parseInt(PostList.get(i).getUserID());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                user = knittersboxDao.hentBrukerID(userID);
            }
        });

        t.start();

        //if checkbox in new post is checked
        //the user name is set to anonymous user
        try {
            t.join();
            PostViewholder.post_image.setImageURI(mUri);
            int checkbox = PostList.get(i).getPost_checkbox();
            if (checkbox == 0) {
                PostViewholder.post_user.setText(user.getDisplayname());
            }
            else {
                PostViewholder.post_user.setText("Anonym Bruker");
            }
            PostViewHolder.post_text.setText(PostList.get(i).getPost_text());
            PostViewHolder.post_title.setText(PostList.get(i).getPost_tittle());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
