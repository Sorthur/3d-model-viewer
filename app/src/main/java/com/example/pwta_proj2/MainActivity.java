package com.example.pwta_proj2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

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

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable breadRenderable;
    private SceneView backgroundSceneView;
    private SceneView transparentSceneView;

    private ArFragment arFragment;
    private Renderable model;
    private ViewRenderable viewRenderable;
    private int clickNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main );
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        // Load model.glb from assets folder or http url
        arFragment = (ArFragment) supportFragmentManager.findFragmentById(R.id.arFragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            clickNo++;

            // the 3d model comes to the scene only the first time we tap the screen
            if (clickNo == 1) {

                Anchor anchor = hitResult.createAnchor();
                ModelRenderable.builder()
                        .setSource(this, R.raw.bread)

//                        .setIsFilamentGltf(true)
                        .build()
                        .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                        .exceptionally(throwable -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Something is not right" + throwable.getMessage()).show();
                            return null;
                        });
            }
        });
    }

    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {

        // Creating a AnchorNode with a specific anchor
        AnchorNode anchorNode = new AnchorNode(anchor);

        // attaching the anchorNode with the ArFragment
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        TransformableNode transform = new TransformableNode(arFragment.getTransformationSystem());

        // change size
        float x1 = 0.1f, x2 = 0.1f;
        transform.setWorldScale(new Vector3(x1, x1, x1));
        transform.setLocalScale(new Vector3(x2, x2 ,x2));
        // attaching the anchorNode with the TransformableNode
        transform.setParent(anchorNode);

        // attaching the 3d model with the TransformableNode that is
        // already attached with the node
        transform.setRenderable(modelRenderable);
        transform.select();
    }

//    public void loadModels() {
//        WeakReference<MainActivity> weakActivity = new WeakReference<>(this);
//        ModelRenderable.builder()
////                .setSource((Context)this, Uri.parse("https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb"))
//                .setSource((Context)this, R.raw.bread)
////                .Ń(true)
////                .setAsyncLoadEnabled(true)
//                .build()
//                .thenAccept(model -> {
//                    MainActivity activity = weakActivity.get();
//                    if (activity != null) {
//                        activity.model = model;
//                    }
//                })
//                .exceptionally(throwable -> {
//                    Toast.makeText(
//                            this, "Unable to load model", Toast.LENGTH_LONG).show();
//                    return null;
//                });
//        ViewRenderable.builder()
////                .setView(this, R.layout.view_model_title)
//                .build()
//                .thenAccept(viewRenderable -> {
//                    MainActivity activity = weakActivity.get();
//                    if (activity != null) {
//                        activity.viewRenderable = viewRenderable;
//                    }
//                })
//                .exceptionally(throwable -> {
//                    Toast.makeText(this, "Unable to load model", Toast.LENGTH_LONG).show();
//                    return null;
//                });
//    }
//
//    @Override
//    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
//        if (model == null || viewRenderable == null) {
//            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Create the Anchor.
//        Anchor anchor = hitResult.createAnchor();
//        AnchorNode anchorNode = new AnchorNode(anchor);
//        anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//        // Create the transformable model and add it to the anchor.
//        TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
//        model.setParent(anchorNode);
//        model.setRenderable(this.model);
////                .animate(true).start();
//        model.select();
//
//        Node titleNode = new Node();
//        titleNode.setParent(model);
//        titleNode.setEnabled(false);
//        titleNode.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));
//        titleNode.setRenderable(viewRenderable);
//        titleNode.setEnabled(true);
//    }
}