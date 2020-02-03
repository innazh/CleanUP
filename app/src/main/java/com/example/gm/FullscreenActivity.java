package com.example.gm;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileInputStream;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


    private ImageView result;
    private Button postBtn;
    private EditText descET, titleET;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        Bitmap bmp = null;
        //find all ids
        titleET = findViewById(R.id.titleET);
        cardView = findViewById(R.id.cardView);
        postBtn = cardView.findViewById(R.id.postBtn);
        result = findViewById(R.id.postImgView);
        descET = cardView.findViewById(R.id.descET);

        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setImageBitmap(bmp);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post = new Intent(FullscreenActivity.this, MapsActivity.class);
                post.setFlags(FLAG_ACTIVITY_CLEAR_TOP);

                String descData = descET.getText().toString();
                String  titleData = titleET.getText().toString();
                LatLng curr = new LatLng(43.771771, -79.501046);

                post.putExtra("desc", descData );
                post.putExtra("title", titleData);
                post.putExtra("loc", curr);

                startActivity(post);
            }
        });
    }
}
