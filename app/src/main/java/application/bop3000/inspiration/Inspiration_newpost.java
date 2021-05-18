package application.bop3000.inspiration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.CheckBox;
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
    //to open either gallery or camera
    private static final int GALLERY_REQUEST = 100;
    private static final int CAMERA_REQUEST = 200;

    private ImageView selectedImageview;
    private EditText titleEdithText, textEdithText;
    Button btn_save;
    CheckBox inspiration_checkBox;
    Bitmap bitmap;
    private MyDatabase DB;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration_newpost);
        this.selectedImageview = findViewById(R.id.new_memory_selected_image);
        this.titleEdithText = findViewById(R.id.new_memory_title);
        this.textEdithText = findViewById(R.id.new_memory_txt);
        this.inspiration_checkBox = findViewById(R.id.inspiration_checkBox);
        btn_save = findViewById(R.id.inspiration_save);
        btn_save.setOnClickListener(this);
    }

    //save image to an internal file, then save the whole post in room database
    public void saveToInternalStorage (Bitmap bitmap) {
        DB = MyDatabase.getDatabase(getApplicationContext());
        final KnittersboxDao postDao = DB.getKnittersboxDao();
        final Post post = new Post();

        //saves the image on the mobiles internal file system
        bitmap = ((BitmapDrawable) selectedImageview.getDrawable()).getBitmap();
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        //the file that is made internally on the mobile
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, titleEdithText.getText().toString() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            //saves the post in room databasen
            final int user = Login.getUser().getUserID();
            String imagepath = file.toString();
            final String title = titleEdithText.getText().toString();
            final String post_text = textEdithText.getText().toString();

            //checks if the title fields is empty
            if (title.isEmpty()) {
                Toast.makeText(this, "Du må fylle inn titelfeltet!", Toast.LENGTH_LONG).show();
            } else {
                post.setUserID(String.valueOf(user));
                post.setPost_tittle(title);
                post.setPost_text(post_text);
                post.setPost_imagepath(imagepath);
                //checks if the checkbox is checked
                //if checked the user is anonymous when the post is posted
                if (inspiration_checkBox.isChecked()) {
                    post.setPost_checkbox(1);
                }
                else {
                    post.setPost_checkbox(0);
                }
                //saves the post in the room database
                new Thread(() -> postDao.insertNewPost(post)).start();
                Toast.makeText(getApplicationContext(), "Innlegget er lagret!", Toast.LENGTH_LONG).show();
                finish();
            }
        }catch(Exception e){
                e.printStackTrace();
            }
        //closes the FileOutputStream and goes back to inspirasjonsside
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

    //opens gallery and request user for permission to use it
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openGallery(View view) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Velge bilde"), GALLERY_REQUEST);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST);

            //gives the user a second chance to allow use
            if(ActivityCompat.shouldShowRequestPermissionRationale(Inspiration_newpost.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "Endre tillatelser i appinnstillinger!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //opens camera and request user permission to use it
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openCamera(View view) {
        //if camera has permission then it opens camera
        //else request it from the user
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

            //gives the user a second chance to allow use
            if(ActivityCompat.shouldShowRequestPermissionRationale(Inspiration_newpost.this,
                    Manifest.permission.CAMERA)) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "Endre tillatelser i appinnstillinger!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //displays the selected image in an imageview
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //selected image from gallery
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                selectedImageview.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Kunne ikke laste inne bildet", Toast.LENGTH_LONG).show();
            }
        }
        //image from camera
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

    //calls on function to store image internally on the mobile and to the room database
    @Override
    public void onClick(View v) {
        saveToInternalStorage(bitmap);
    }

    //back button to inspirasjonsside
    public void back(View view){
        Intent i = new Intent(this, Inspiration.class);
        startActivity(i);
    }
}
