package com.example.pwta_proj2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import java.io.File;

import com.dinuscxj.gesture.MultiTouchGestureDetector;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.net.URI;
import java.util.concurrent.CompletableFuture;



public class MainActivity extends AppCompatActivity {

    public String filePath = "";
    private MultiTouchGestureView imgView;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MultiTouchGestureView imgView;

    }

    int requestCode = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Context context = getApplicationContext();
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK ) {
            if (data == null) {
                return;
            }

            Uri uri = data.getData();

            filePath = Utils.getNameFromURI(context, uri);
            // TODO: Include full path with filename


        }
    }



    public void openFileD(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
    }


    public void select2DModel(View view) {
        Context context = getApplicationContext();
        String[] listItems = new String[]{"JPEG File", "BMP File", "PNG File"};
        final int[] checkedItem = {-1};

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("2D Model");

        builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
            checkedItem[0] = which;
            TextView textViewStatus = findViewById(R.id.textViewStatus);
            textViewStatus.setText(listItems[which]);

            imgView = findViewById(R.id.imageView);

            switch (listItems[which]) {
                case "JPEG File":
                    imgView.setImage(context.getResources().getDrawable(R.drawable.androidjpeg));
                    break;
                case "BMP File":
                    imgView.setImage(context.getResources().getDrawable(R.drawable.androidbmp));
                    break;
                case "PNG File":
                    imgView.setImage(context.getResources().getDrawable(R.drawable.androidpng));
                    break;
                default:
                    dialog.dismiss();
            }

            dialog.dismiss();
        }).setNegativeButton("Cancel", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }





}


