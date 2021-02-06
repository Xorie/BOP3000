package application.bop3000.inspiration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import application.bop3000.R;

public class Inspiration_newpost extends AppCompatActivity{
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static final int PICK_IMAGE = 1;
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration_newpost);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
    }
    //NB! etterspørselen etter tillatelse for kamera fungerer ikke
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

    private void UploadPic() {
        UploadPic();
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
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                } else {
                    Toast.makeText(getApplicationContext(), "Klikket på kamera", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivity(intent);
                }
            }
        });
        builder.show();

    }
}