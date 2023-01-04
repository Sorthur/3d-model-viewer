package com.example.pwta_proj2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.File;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.concurrent.CompletableFuture;


public class MainActivity extends AppCompatActivity {

    public String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            //filePath = uri.getPath();
            filePath = Utils.getNameFromURI(context, uri);
            // TODO: Include full path with filename

            TextView textViewStatus = findViewById(R.id.textViewStatus);
            textViewStatus.setText(filePath);


            //Toast.makeText(context,uri.getPath(), Toast.LENGTH_SHORT).show();
        }
    }



    public void openFileD(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
    }

}