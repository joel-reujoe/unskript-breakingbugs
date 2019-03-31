package com.example.munde.unskript.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.NetworkError;
import com.example.munde.unskript.R;
import com.example.munde.unskript.activity.helper.Common;
import com.example.munde.unskript.activity.helper.ConnectionDetector;
import com.example.munde.unskript.activity.helper.FilePath;
import com.example.munde.unskript.activity.helper.ScalingUtilities;
import com.example.munde.unskript.activity.response.TestResponse;
import com.example.munde.unskript.activity.webservice.API;
import com.example.munde.unskript.activity.webservice.RestClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StartActivity extends AppCompatActivity {

    ConnectionDetector cd;
    ImageView imageView;
    Button submit;
    Common common;
    FirebaseStorage storage;
    private static final String IMAGE_DIRECTORY_NAME = "Unskript";
    String asset_photo_name="",Photo="", imageOf="",userChosenTask="",selectedImagePath="",ImageTaken = "",contentType="",imageTaken="",selected="";
    private static final int CAMERA_PIC_REQUEST	= 101;
    private static final int MEDIA_TYPE_IMAGE 	= 102;
    private static final int SELECT_PICTURE 	= 103;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        try{
            cd=new ConnectionDetector(StartActivity.this);
            storage=FirebaseStorage.getInstance();
            imageView=findViewById(R.id.imgToUpload);
            submit=findViewById(R.id.btnSubmit);
            common=new Common();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageOf="testImage";
                    selectImage();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadToFirebase(asset_photo_name,"testimage");
//                    downloadUrl();
                }
            });

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
    private void selectImage(){
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (items[item].equals("Take Photo")) {
                    userChosenTask = "Take Photo";
                    //if (result)
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChosenTask = "Choose from Library";
                    //if (result)
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }
    void cameraIntent(){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }

    void galleryIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    public Uri getOutputMediaFileUri(int type) {
        //return Uri.fromFile(getOutputMediaFile(type));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            File tempFile = getOutputMediaFile(type);
            Uri uri = FileProvider.getUriForFile(StartActivity.this,"com.scinotic.istats.android.fileprovider", tempFile);
            if(imageOf.equalsIgnoreCase("testImage")) {
                selectedImagePath = tempFile.getAbsolutePath();
                System.out.println(selectedImagePath);
//                selectedProfileImagePath = getFilePathFromURI(UserRegistration1.this,uri);
            }

            return uri;

        }else {

            return Uri.fromFile(getOutputMediaFile(type));

        }
    }

    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString(),IMAGE_DIRECTORY_NAME);
        //File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString());

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs())
                return null;

        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = null;

        if (type == MEDIA_TYPE_IMAGE) {

            if (imageOf.equalsIgnoreCase("testImage")) {

                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_p" + timeStamp + ".jpg");

            }

        }else {
            return null;
        }

        return mediaFile;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_PIC_REQUEST) {
                imageTaken = "yes";
                selected = "Camera";
                Uri selectedImageUri = fileUri;

                if(imageOf.equalsIgnoreCase("testImage")){

                    if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        selectedImagePath = getPathOfCameraPicture(selectedImageUri);
                    }
                    contentType = getContentType(selectedImageUri);
                    getCameraPhotoOrientation(getApplicationContext(), selectedImageUri, selectedImagePath);
                    asset_photo_name = selectedImagePath.substring(selectedImagePath.lastIndexOf('/') + 1);
                    selectedImagePath = decodeFile1(selectedImagePath,400,400,asset_photo_name);
                    //profilePicUri = fileUri;
                    File file = new File(selectedImagePath);
                    fileUri = getImageContentUri(StartActivity.this,file);
                    Bitmap bitmapCamera = createScaledBitmap(selectedImagePath, imageView.getWidth(), imageView.getHeight());
                    imageView.setImageBitmap(bitmapCamera);
//                    uploadToFirebase(asset_photo_name,"testimage");
                    ImageTaken = "Yes";

                }
            } else if (requestCode == SELECT_PICTURE) {
                imageTaken = "yes";
                selected = "Picture";

                if (imageOf.equalsIgnoreCase("testImage")) {

                    fileUri = data.getData();

                    if (fileUri != null) {
                        System.out.println(fileUri);
                        selectedImagePath = FilePath.getPath(this, fileUri);
                        contentType = "image/jpeg";
                        System.out.println(selectedImagePath);
                        asset_photo_name = selectedImagePath.substring(selectedImagePath.lastIndexOf('/') + 1);
                        System.out.println(asset_photo_name);
                        selectedImagePath = decodeFile1(selectedImagePath, 400, 400, asset_photo_name);
                        Bitmap bitmapCamera = createScaledBitmap(selectedImagePath, imageView.getWidth(), imageView.getHeight());
                        imageView.setImageBitmap(bitmapCamera);
//                        uploadToFirebase(asset_photo_name, "testimage");
                        ImageTaken = "Yes";

                    }
                }
            }
        }
    }
    public String getPathOfCameraPicture(Uri uri) {

        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        //Source is Dropbox or other similar local file path
        if (cursor == null)
            result = uri.getPath();
        else {

            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();

        }

        return result;

    }
    public void uploadToFirebase(String asset_photo_name,String type) {
        StorageReference storageReference;
        Photo = "gs://unskript-d89f8.appspot.com/";
        if (type.equalsIgnoreCase("testimage")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmapUpload = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            bitmapUpload.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            Photo = "gs://unskript-d89f8.appspot.com/testImage" + asset_photo_name;
            byte[] image = baos.toByteArray();
            storageReference = storage.getReference("testImage/" + asset_photo_name);
            UploadTask uploadTask = storageReference.putBytes(image);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    common.showMessage(getApplicationContext(), "Could not upload image");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    common.showMessage(getApplicationContext(), "Successfully upload");
                    downloadUrl();
                }
            });
        }
    }

    public String downloadUrl(){
        StorageReference storageReference;
        String url;
        Photo = "gs://unskript-d89f8.appspot.com/";
        common.showMessage(getApplicationContext(),asset_photo_name);
        storageReference=storage.getReference();
        storageReference.child("testImage/"+asset_photo_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                common.showMessage(getApplicationContext(),uri.getAuthority()+uri.getPath());
                callNodeApi(uri.getAuthority()+uri.getPath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                common.showMessage(getApplicationContext(),"Could Not Get Url");
            }
        });
        return "";
    }

    public void callNodeApi(String url)
    {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog dialog = ProgressDialog.show(StartActivity.this, "", "Please Wait...");

            try {
                dialog.show();
                OkHttpClient okClient = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20,TimeUnit.SECONDS)
                        .build();
                RestClient.client = new Retrofit.Builder().baseUrl(RestClient.baseUrl)
                        .client(okClient).addConverterFactory(GsonConverterFactory.create()).build();
                System.out.print(okClient);
                API api = RestClient.client.create(API.class);
                Map<String, String> params = new HashMap<String, String>();
                params.put("url", url);
                params.put("name",asset_photo_name);
                System.out.println("params:"+params);
                System.out.print("url:"+url);
                System.out.print(api);


                Call<TestResponse> call = api.testResponse(params);

                call.enqueue(new Callback<TestResponse>() {
                    @Override
                    public void onResponse(Call<TestResponse> call, Response<TestResponse> response) {
                        dialog.dismiss();
                        System.out.println("NodeResponse--"+response.body());
                        if(response.body().getStatus().toString().trim().equalsIgnoreCase("true")){
                            common.showMessage(getApplicationContext(),response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<TestResponse> call, Throwable t) {
                        dialog.dismiss();
                        System.out.print(t.getMessage());
                        common.showMessage(getApplicationContext(),"Could not get response");
                    }
                });

            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    private String decodeFile1(String path,int DESIREDWIDTH, int DESIREDHEIGHT,String imgname)
    {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            } else {
                unscaledBitmap.recycle();
                return path;
            }
            // Store to tmp file
            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/testFile");
            if (!mFolder.exists())
            {
                mFolder.mkdir();
            }

            //String s = "tmp.png";
            File f = new File(mFolder.getAbsolutePath(), imgname);
            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                fos.flush();
                fos.close();
            }
            catch (FileNotFoundException e)
            {

                e.printStackTrace();
            }
            catch (Exception e)
            {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        System.out.println("Compress Image Path"+strMyImagePath);
        return strMyImagePath;

    }

    public Bitmap createScaledBitmap(String pathName, int width, int height) {
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opt);
        opt.inSampleSize = calculateBmpSampleSize(opt, width, height);
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, opt);
    }

    public int calculateBmpSampleSize(BitmapFactory.Options opt, int width, int height) {
        final int outHeight = opt.outHeight;
        final int outWidth = opt.outWidth;
        int sampleSize = 1;
        if (outHeight > height || outWidth > width) {
            final int heightRatio = Math.round((float) outHeight / (float) height);
            final int widthRatio = Math.round((float) outWidth / (float) width);
            sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return sampleSize;
    }
    private String getContentType(Uri uri) {

        ContentResolver cR = getContentResolver();
        return cR.getType(uri);

    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {

        int rotate = 0;

        try {

            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Matrix mtx = new Matrix();

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    mtx.preRotate(rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    mtx.preRotate(rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    mtx.preRotate(rotate);
                    break;
            }

            Log.d("SCAdminComponents", "getCameraPhotoOrientation - Exif orientation : " + orientation);
            Log.d("SCAdminComponents", "getCameraPhotoOrientation - Rotate value : " + rotate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rotate;

    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

}
