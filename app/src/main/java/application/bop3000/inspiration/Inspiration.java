package application.bop3000.inspiration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Post;

public class Inspiration extends AppCompatActivity {
    MyDatabase DB;
    PostAdapter pAdapter;
    RecyclerView recView;
    TextView posttxt, posttitle;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration);

        recView = findViewById(R.id.insp_recview);
        recView.setLayoutManager(new LinearLayoutManager(this));
        DB = MyDatabase.getDatabase(getApplicationContext());
        posttxt = findViewById(R.id.srPostTxt);
        posttitle = findViewById(R.id.srPostTitle);
        imageview = findViewById(R.id.srPostImage);


        pAdapter = new PostAdapter(this);
        recView.setAdapter(pAdapter);
        //pAdapter.setCustomItemClickListener(onItemClickListener);

        //fra insperasjonssiden til nytt innlegg siden
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Inspiration.this, Inspiration_newpost.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {
        AtomicReference<String> myData = new AtomicReference<>("");
        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<Post> post = DB.getKnittersboxDao().loadAllPost();
            runOnUiThread(() -> pAdapter.setTasks(post));
            });
    }


}