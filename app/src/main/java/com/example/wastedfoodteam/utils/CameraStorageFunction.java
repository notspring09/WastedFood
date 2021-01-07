package com.example.wastedfoodteam.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wastedfoodteam.global.Variable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CameraStorageFunction {
    //permission constants
    public static final int CAMERA_REQUEST_CODE = 200;
    public static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    public static final int IMAGE_PICK_GALLERY_CODE = 400;
    public static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission array
    private final String[] cameraPermission;
    private final String[] storagePermission;

    //image pick uri
    private Uri image_uri;
    // instance for firebase storage and StorageReference
    final FirebaseStorage storage;
    final StorageReference storageReference;
    private String storage_location;
    ImageView imageView;
    final Activity myActivity;
    final Context myContext;

    public interface HandleUploadImage {
        void onSuccess(String url);
        void onError();
    }

    public Uri getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(Uri image_uri) {
        this.image_uri = image_uri;
    }

    public CameraStorageFunction(Activity activity, Context context, ImageView imageView) {
        myActivity = activity;
        myContext = context;
        this.imageView = imageView;
        //init permission arrays
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    //start of for camera handle
    protected void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        myActivity.startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    public void showImagePickDialog() {
        //display in dialog
        String[] options = {"Máy ảnh", "Kho điện thoại"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setTitle("Chọn ảnh").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if (which == 0) {
                    //camera clicked
                    if (checkCameraPermission()) {
                        //permission granted
                        pickFromCamera();
                    } else {
                        //permission not granted, request
                        requestCameraPermission();
                    }
                } else {
                    //gallery clicked
                    if (checkStoragePermission()) {
                        //permission granted
                        pickFromGallery();
                    } else {
                        //permission not granted, request
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = myActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Log.i("CameraStorageFunction", "image_uri :" + image_uri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        myActivity.startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED); //return true/false
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(myActivity, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.checkSelfPermission(myContext, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean resultExternalStorage = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return resultCamera && resultExternalStorage;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(myActivity, cameraPermission, CAMERA_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                Log.i("CameraStorageFunction", "image pick gallery");
                //image pick from gallery

                //save picked image uri
                image_uri = data.getData();

                //image picked from camera
                assert image_uri != null;
                CommonFunction.setImageViewSrc(myContext, image_uri.toString(), imageView);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image pick from camera
                CommonFunction.setImageViewSrc(myContext, image_uri.toString(), imageView);
            }
        }
    }

    // UploadImage method
    public void uploadImage(final HandleUploadImage handleUploadImage) {
        // Defining the child of storageReference
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

        // adding listeners on upload
        // or failure of image
        if (image_uri == null) {
            image_uri = Variable.uri;
        }
        ref.putFile(image_uri).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(
                            UploadTask.TaskSnapshot taskSnapshot) {

                        // Image uploaded successfully
                        //Toast.makeText(myActivity, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                storage_location = uri.toString();
                                handleUploadImage.onSuccess(storage_location);
                                //Toast.makeText(myActivity, uri.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error, Image not uploaded
                        Toast.makeText(myActivity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
