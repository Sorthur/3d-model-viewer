package com.example.pwta_proj2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dinuscxj.gesture.MultiTouchGestureDetector;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.net.URI;
import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    public String filePath = "";
    private MultiTouchGestureView imgView;
    AlertDialog.Builder builder;

    private ModelRenderable breadRenderable;
    private SceneView backgroundSceneView;
    private SceneView transparentSceneView;

    private ArFragment arFragment;
    private Renderable model;
    private ViewRenderable viewRenderable;
    private FragmentManager supportFragmentManager;
    private int clickNo = 0;
    private int chosen3dModel;
    int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportFragmentManager = getSupportFragmentManager();

        MultiTouchGestureView imgView;

    }

    public void turnOfAr(View v) {
        setContentView(R.layout.activity_main);
        arFragment = null;
    }

    public void runViewer3d(View v) {
        String[] listItems = new String[]{"dom", "samochód", "wyspa"};
        final int[] checkedItem = {-1};

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("3D Model");

        builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
            checkedItem[0] = which;
            TextView textViewStatus = findViewById(R.id.textViewStatus);
            textViewStatus.setText(listItems[which]);

            imgView = findViewById(R.id.imageView);

            switch (listItems[which]) {
                case "dom":
                    chosen3dModel = R.raw.dom;
                    break;
                case "samochód":
                    chosen3dModel = R.raw.car;
                    break;
                case "wyspa":
                    chosen3dModel = R.raw.terrain;
                    break;

                default:
                    dialog.dismiss();
            }

            dialog.dismiss();
            set3dModel(chosen3dModel);
        }).setNegativeButton("Cancel", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void set3dModel(int chosen3dModelId) {
        try {
            setContentView(R.layout.viewer_3d);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Load model.glb from assets folder or http url
        arFragment = (ArFragment) supportFragmentManager.findFragmentById(R.id.arFragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            clickNo++;
            if (clickNo == 1) {

                Anchor anchor = hitResult.createAnchor();
                ModelRenderable.builder()
                        .setSource(this, chosen3dModelId)
                        .build()
                        .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                        .exceptionally(throwable -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Error: " + throwable.getMessage()).show();
                            return null;
                        });
            }
        });
    }

    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);

        anchorNode.setParent(arFragment.getArSceneView().getScene());
        TransformableNode transform = new TransformableNode(arFragment.getTransformationSystem());

        // change size
        float x1 = 0.1f, x2 = 0.1f;
        transform.setWorldScale(new Vector3(x1, x1, x1));
        transform.setLocalScale(new Vector3(x2, x2 ,x2));
        transform.setParent(anchorNode);

        transform.setRenderable(modelRenderable);
        transform.select();
    }

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
        String[] listItems = new String[]{"BMP File", "PNG File"};
        final int[] checkedItem = {-1};

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("2D Model");

        builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
            checkedItem[0] = which;
            TextView textViewStatus = findViewById(R.id.textViewStatus);
            textViewStatus.setText(listItems[which]);

            imgView = findViewById(R.id.imageView);

            switch (listItems[which]) {
//                case "JPEG File":
//                    imgView.setImage(context.getResources().getDrawable(R.drawable.androidjpeg));
//                    break;
                case "BMP File":
                    imgView.setImage(context.getResources().getDrawable(R.drawable.car));
                    break;
                case "PNG File":
                    imgView.setImage(context.getResources().getDrawable(R.drawable.dom));
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