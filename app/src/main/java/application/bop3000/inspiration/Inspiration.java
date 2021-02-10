package application.bop3000.inspiration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Transition;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Post;

public class Inspiration extends AppCompatActivity {
    MyDatabase DB;
    PostAdapter pAdapter;
    RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration);

        recView = findViewById(R.id.insp_recview);
        recView.setLayoutManager(new LinearLayoutManager(this));
        DB = MyDatabase.getDatabase(getApplicationContext());

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
        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<Post> post = DB.getKnittersboxDao().loadAllPost();
            runOnUiThread(() -> pAdapter.setTasks(post));
        });
    }

}