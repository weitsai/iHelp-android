package edu.stu.ihelp.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

public class CaptureActivity extends Activity {
    public static final int REQ_VIDEO_CAPTURE = 0;
    public static final int REQ_PHOTO_CAPTURE = 1;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int requestCode = this.getIntent().getIntExtra ("reqCode", -1);
                
        if (requestCode == REQ_VIDEO_CAPTURE) {
            Intent takeVideoIntent = new Intent (MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult (takeVideoIntent, REQ_VIDEO_CAPTURE);
       } else if (requestCode == REQ_PHOTO_CAPTURE) {
            Intent takePhotoIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult (takePhotoIntent, REQ_PHOTO_CAPTURE);
        } else {
            Toast.makeText(this, "Not valid requestCode", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Not valid resultCode", Toast.LENGTH_SHORT).show();
        } else {
            if (requestCode == REQ_VIDEO_CAPTURE || requestCode == REQ_PHOTO_CAPTURE) {
                Intent intent = new Intent(this, General.class);
                this.startActivity(intent);
            } else {
                Toast.makeText(this, "Not valid requestCode", Toast.LENGTH_SHORT).show();
            }
        }
        
        this.finish();
    }
}
