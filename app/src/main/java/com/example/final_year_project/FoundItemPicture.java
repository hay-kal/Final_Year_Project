package com.example.final_year_project;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.EnforceTabletReachabilityBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TakePictureBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.actuation.EnforceTabletReachability;
import com.aldebaran.qi.sdk.object.camera.TakePicture;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.image.EncodedImage;
import com.aldebaran.qi.sdk.object.image.EncodedImageHandle;
import com.aldebaran.qi.sdk.object.image.TimestampedImageHandle;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class FoundItemPicture extends RobotActivity implements RobotLifecycleCallbacks {
    private byte[] pictureArray;
    private Animate animate;

    private ListStorage listStorage;

    ImageView ivBack, ivHome;
    private static final String TAG = "CameraExample";
    private CameraDevice cameraDevice;
    private CameraManager cameraManager;

    // The button used to start take picture action.
    private Button button;
    // An image view used to show the picture.
    private ImageView pictureView;
    // The QiContext provided by the QiSDK.
    private QiContext qiContext;
    // TimestampedImage future.
    private Future<TimestampedImageHandle> timestampedImageHandleFuture;

    Future<TakePicture> takePictureFuture;

    Button TakePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_item_picture);

        // Initialize pictureView
        pictureView = findViewById(R.id.picture_view);
        TakePic = findViewById(R.id.btnTakePic);
        // Register the RobotLifecycleCallbacks to this Activity.
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);

//        // Check if the WRITE_EXTERNAL_STORAGE permission is granted
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, so request it
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    1); // You can use any request code here
//        } else {
//            // Permission is already granted, proceed with your operations
//            // For example, you can initialize the QiSDK here
//            QiSDK.register(this, this);
//        }



        QiSDK.register(this, this);

//        TakePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                takePicture();
//                timestampedImageHandleFuture.andThenConsume(timestampedImageHandle ->
//
//                { //does not run
//
//                    // Consume take picture action when it's ready
//                    Log.i(TAG, "Picture taken");
//
//                    // get picture
//                    EncodedImageHandle encodedImageHandle = timestampedImageHandle.getImage();
//
//                    EncodedImage encodedImage = encodedImageHandle.getValue();
//                    Log.i(TAG, "PICTURE RECEIVED!");
//
//                    // get the byte buffer and cast it to byte array
//                    ByteBuffer buffer = encodedImage.getData();
//                    buffer.rewind();
//                    final int pictureBufferSize = buffer.remaining();
//                    final byte[] pictureArray = new byte[pictureBufferSize];
//                    buffer.get(pictureArray);
//
//                    Log.i(TAG, "PICTURE RECEIVED! (" + pictureBufferSize + " Bytes)");
//                    // display picture
//                    Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureBufferSize);
//                    saveImageToFile(FoundItemPicture.this, pictureBitmap, "FoundItem");
//                    runOnUiThread(() -> pictureView.setImageBitmap(pictureBitmap));
//
//
//                });
//
//
//            }
//        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your operations
                // For example, you can initialize the QiSDK here
                QiSDK.register(this, this);
            } else {
                // Permission denied, handle this situation (e.g., show a message)
                Log.e(TAG, "PERMISSION DENIED");
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    private void startCameraPreview() {
        // Code to start the camera preview on a SurfaceView or TextureView
        // You can use the Camera API or Camera2 API for this part
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Future<TakePicture> takePictureFuture = TakePictureBuilder.with(qiContext).buildAsync();

        // Create a new say action.
        Say say = SayBuilder.with(qiContext).withText("Starting picture taking! Hold the item clearly in front of me! And say cheese when ready").build();



        Say sayBack = SayBuilder.with(qiContext).withText("Going back").build();

        Say sayCheese = SayBuilder.with(qiContext).withText("Taking picture.").build();
        Say sayTaken = SayBuilder.with(qiContext).withText("Picture has been taken!").build();

        say.run();

        // Create the PhraseSet 1.
        PhraseSet phraseSetBack = PhraseSetBuilder.with(qiContext).withTexts("back", "previous", "previous page", "go back").build();

        // Create the PhraseSet 2.
        PhraseSet phraseSetCheese = PhraseSetBuilder.with(qiContext).withTexts("cheese", "take picture").build();

        // Create a new listen action.
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetBack, phraseSetCheese).build();

        this.qiContext = qiContext;

        while (true) {
            ListenResult listenResult = listen.run();



            Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
            // Identify the matched phrase set.
            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();
            if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetBack)) {
                Log.i(TAG, "Heard phrase set: back command");
                sayBack.run();

                Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                        .withResources(R.raw.bowing_b001) // Set the animation resource.
                        .build(); // Build the animation.

                animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                        .withAnimation(animation) // Set the animation.
                        .build(); // Build the animate action.


                // Run the animate action asynchronously.
                Future<Void> animateFuture = animate.async().run();

                Log.i(TAG, "Heard phrase set: Previous Page");


                startBackActivity(pictureArray);

            } else if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetCheese)) {
                Log.i(TAG, "Heard phrase set: picture taking command");

                // Build the action.
                EnforceTabletReachability enforceTabletReachability = EnforceTabletReachabilityBuilder.with(qiContext).build();

                // If needed, subscribe to the positionReached() signal
                // in order to know when the tablet has reached its final position.
                enforceTabletReachability.addOnPositionReachedListener(() -> Log.i(TAG, "On position reached"));

                // Run the action asynchronously
                Future<Void> enforceTabletReachabilityFuture = enforceTabletReachability.async().run();

                sayCheese.run();
                takePicture();
                timestampedImageHandleFuture.andThenConsume(timestampedImageHandle -> { //does not run

                    // Consume take picture action when it's ready
                    Log.i(TAG, "Picture taken");

                    // get picture
                    EncodedImageHandle encodedImageHandle = timestampedImageHandle.getImage();

                    EncodedImage encodedImage = encodedImageHandle.getValue();
                    Log.i(TAG, "PICTURE RECEIVED!");

                    // get the byte buffer and cast it to byte array
                    ByteBuffer buffer = encodedImage.getData();
                    buffer.rewind();
                    final int pictureBufferSize = buffer.remaining();
                    final byte[] pictureArray = new byte[pictureBufferSize];
                    buffer.get(pictureArray);

                    Log.i(TAG, "PICTURE RECEIVED! (" + pictureBufferSize + " Bytes)");
                    // display picture
                    Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureBufferSize);
                    saveImageToFile(this, pictureBitmap, "FoundItem");
                    runOnUiThread(() -> pictureView.setImageBitmap(pictureBitmap));


                });

                sayTaken.run();
            }
        }
    }

    public void takePicture() {
        Future<TakePicture> takePictureFuture = TakePictureBuilder.with(qiContext).buildAsync();

        // Check that the Activity owns the focus.
        if (qiContext == null) {
            return;
        }

        timestampedImageHandleFuture = takePictureFuture.andThenCompose(takePicture -> {
            Log.i(TAG, "take picture launched!");
            return takePicture.async().run();
        });

        timestampedImageHandleFuture.andThenConsume(timestampedImageHandle -> {
            // Consume take picture action when it's ready
            Log.i(TAG, "Picture taken");

            // Get the picture
            EncodedImageHandle encodedImageHandle = timestampedImageHandle.getImage();

            EncodedImage encodedImage = encodedImageHandle.getValue();
            Log.i(TAG, "PICTURE RECEIVED!");

            // Get the byte buffer and cast it to byte array
            ByteBuffer buffer = encodedImage.getData();
            buffer.rewind();
            final int pictureBufferSize = buffer.remaining();

            pictureArray = new byte[pictureBufferSize];


            buffer.get(pictureArray);

            Log.i(TAG, "PICTURE RECEIVED! (" + pictureBufferSize + " Bytes)");

//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted, save the picture to storage
//                boolean saveSuccessful = saveImageToFile(this, pictureArray, "found_item_picture");
//                if (saveSuccessful) {
//                    Log.i(TAG, "Image saved to storage successfully!");
//                } else {
//                    Log.e(TAG, "Failed to save image to storage!");
//                }
//            } else {
//                // Permission is not granted, handle this situation (e.g., show a message)
//                Log.e(TAG, "WRITE_EXTERNAL_STORAGE permission is not granted!");
//            }

            runOnUiThread(() -> displayPicture(pictureArray));
        });
    }



    private void displayPicture(byte[] pictureData) {
        // Display the picture in the ImageView
        Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
        pictureView.setImageBitmap(pictureBitmap);
    }


    private void startBackActivity(byte[] pic) {
        Intent intent = new Intent(FoundItemPicture.this, FoundItemFormActivity.class);
        intent.putExtra("PICTURE_ARRAY", pic);
        Log.i(TAG, "Image Intenting " + Arrays.toString(pic));
        startActivity(intent);
    }

    @Override
    public void onRobotFocusLost() {
        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }



    public static boolean saveImageToFile(Context context, Bitmap imageBitmap, String name) {
        try {
            String fileName = name + "_" + System.currentTimeMillis() + ".jpg";

            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Camera"); // Use "Pictures" directory for internal storage

            // For devices running Android Q (API level 29) and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);
            }

            // Save the image to MediaStore
            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues); // Use INTERNAL_CONTENT_URI for internal storage

            // Open an output stream and compress the bitmap to the imageUri
            OutputStream outputStream = contentResolver.openOutputStream(imageUri);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();

            // For devices running Android Q (API level 29) and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0);
                contentResolver.update(imageUri, contentValues, null, null);
            }
            Log.i(TAG, "Heard phrase set: image saved!");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
