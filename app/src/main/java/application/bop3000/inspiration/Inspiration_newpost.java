package application.bop3000.inspiration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Post;
import application.bop3000.login.Login;

public class Inspiration_newpost extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_REQUEST = 100;
    private static final int CAMERA_REQUEST = 200;
    private ImageView selectedImageview;
    private EditText titleEdithText, textEdithText;
    Button btn_save;
    Bitmap bitmap;
    private MyDatabase DB;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration_newpost);
        this.selectedImageview = (ImageView) findViewById(R.id.new_memory_selected_image);
        this.titleEdithText = (EditText) findViewById(R.id.new_memory_title);
        this.textEdithText = (EditText) findViewById(R.id.new_memory_txt);

        //sjekker om kamera har tillatelse eller ikke
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }
        //lagre knapp
        btn_save = findViewById(R.id.inspiration_save);
        btn_save.setOnClickListener((View.OnClickListener) this);
    }

    //https://www.tutorialspoint.com/how-to-write-an-image-file-in-internal-storage-in-android
    //meste av koden kommer her, alt annet er fra stackoverflow
    //lagre bildet på en intern mappe, så lagre innlegget i databasen
    public void saveToInternalStorage (Bitmap bitmap) {
        DB = MyDatabase.getDatabase(getApplicationContext());
        final KnittersboxDao postDao = DB.getKnittersboxDao();
        final Post post = new Post();

        //lagre bilde internt i mobilen
        bitmap = ((BitmapDrawable) selectedImageview.getDrawable()).getBitmap();
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE); //lager en mappe
        File file = new File(directory, titleEdithText.getText().toString() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            //lagrer innlegget i room databasen
            Intent i = getIntent();
            final int user = Login.getUser().getUserID();
            String imagepath = file.toString();
            final String title = titleEdithText.getText().toString();
            final String post_text = textEdithText.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, "Du må fylle inn titelfeltet!", Toast.LENGTH_LONG).show();
            } else {
                post.setUserID(String.valueOf(user));
                post.setPost_tittle(title); //title på bildet
                post.setPost_text(post_text); //teksten til bildet
                post.setPost_imagepath(imagepath); //selve bildetveien
                new Thread(() -> postDao.insertNewPost(post)).start();
                Toast.makeText(getApplicationContext(), "Innlegget er lagret!", Toast.LENGTH_LONG).show();
                finish();
            }
        }catch(Exception e){
                e.printStackTrace();
            }
            finally{
                try {
                    fos.close();
                    Intent i = new Intent(this, Inspiration.class);
                    startActivity(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    //åpner galleri
    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Velge bilde"), GALLERY_REQUEST);
    }

    //åpner kamera
    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }


    //https://www.youtube.com/watch?v=ykbU41xhDrY
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                selectedImageview.setImageBitmap(BitmapFactory.decodeStream(imageStream));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Kunne ikke laste inne bildet", Toast.LENGTH_LONG).show();
            }

        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                Bundle ex = data.getExtras();
                Bitmap image = (Bitmap)ex.get("data");
                selectedImageview.setImageBitmap(image);
            } catch (Exception e) {
                Toast.makeText(this, "Kunne ikke laste inne bildet", Toast.LENGTH_LONG).show();
            }
        }
    }


    //kaller på funksjon for å lagre bilde internt på mobilen og til room databasen
    @Override
    public void onClick(View v) {
        saveToInternalStorage(bitmap);
    }

    //tilbake til inpirasjonssiden
    public void back(View view){
        Intent i = new Intent(this, Inspiration.class);
        startActivity(i);
    }
}
