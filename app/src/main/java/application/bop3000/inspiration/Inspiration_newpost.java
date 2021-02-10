package application.bop3000.inspiration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Post;

public class Inspiration_newpost extends AppCompatActivity{
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int PICK_IMAGE = 1;
    EditText newpost_txt;
    Button btn_save;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration_newpost);

        //sjekker om kamera har tillatelse eller ikke
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        newpost_txt = findViewById(R.id.newpost_text);
        btn_save = findViewById(R.id.inspiration_save);
    }
    //tror den fungerer
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                UploadPic();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    //https://stackoverflow.com/questions/38552144/how-get-permission-for-camera-in-android-specifically-marshmallow

    private String UploadPic() {
        UploadPic();
        return null;
    }

    //https://stackoverflow.com/questions/13977245/android-open-camera-from-button
    //https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
    //kan åpne både kamera og fil systemet
    public void UploadPic(View view) {
        CharSequence menu[] = new CharSequence[]{"Galleri", "Åpne kamera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Last opp bilde");
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        Toast.makeText(getApplicationContext(), "Klikket på galleri", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Velg et bilde"), PICK_IMAGE);

                    } else {
                        Toast.makeText(getApplicationContext(), "Klikket på kamera", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivity(intent);

                    }
                }
            });builder.show();
    }
    public void onSaveImage( View v) {
        String saveToInternalStorage;
        Bitmap bitmapImage = null;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) { e.printStackTrace();}
        finally { try {fos.close();}
        catch (IOException e) {e.printStackTrace();}}
    }//return mypath.getAbsolutePath();



    //fungerer å lagre teksten
    public void onSave(View v) {
        Post post = new Post();
        post.setPost_text(newpost_txt.getText().toString());

        //lagrer teksten
        MyDatabase userDatabase = MyDatabase.getDatabase(getApplicationContext());
        KnittersboxDao postDao = userDatabase.getKnittersboxDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                postDao.registerNewPost(post);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "tekst er lagret", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    private void loadImage(String path){
        try {
            File file = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
            ImageView img=(ImageView)findViewById(R.id.srPostImage);
            img.setImageBitmap(b); // it will display the image in imageview
            String file_path = UploadPic(); // store this file_path in db
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //tilbake knapp til insp side
    public void onclickback(View view){
        Intent intent = new Intent(Inspiration_newpost.this, Inspiration.class);
        startActivity(intent);
    }
}