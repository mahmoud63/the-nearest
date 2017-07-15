package com.example.mohammed.withoutname;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddPlaceActivity extends AppCompatActivity {
    private TrackGPS gps;
    double longitude;
    double latitude;
    private Button mNext, mPrevious;
    private ViewFlipper mFlipper;
    private ViewFlipper flipper;
    private float initialX;
    int imageNum = 4;
    List myList = new ArrayList();
    int PageNum = 1;
    int imageCounter=0;
    long UimageCounter=0;
    private EditText mName, mDescription, mTags, mAddress, mETweb, mETphone, mETphone2;
    private Spinner mCategory;
    private ImageView mPhoto, mPhoto1, mPhoto2, mPhoto3, mLogo;
    private Uri ImageUri, ImageUri1, ImageUri2, ImageUri3, LogoUri;
    final List<String> areas = new ArrayList<String>();
    ArrayAdapter<String> areasAdapter;

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH_images = "image";
    public static final int SELECT_FILE = 1234;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 12345;
    private int REQUEST_GPS = 123456;

    FirebaseDatabase firebasedatabase;
    DatabaseReference mDatabase;
    DatabaseReference mDatabaseCategory;
    private StorageReference mStorageRef;


    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;


    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        Init();

        Button BTnext=(Button)findViewById(R.id.BTnext1);
        BTnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PageNum == 2) {
                    flipper.showNext();
                    PageNum++;


                } else if (PageNum == 3) {

                    //Method Submit

                    PageNum++;
                    flipper.showNext();

                } else if (PageNum == 1) {
                    PageNum++;

                    flipper.showNext();
//                        mPrevious.setEnabled(true);
                }
                else if (PageNum == 4) {

                    //Method Submit



                }

            }
        });

        Button BTperevios=(Button)findViewById(R.id.BTprevious);
        BTperevios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PageNum == 1) {


                } else if (PageNum == 2) {

                    //                      mPrevious.setEnabled(false);
                    PageNum--;
                    flipper.showPrevious();
                } else if (PageNum == 3) {

                    PageNum--;
                    flipper.showPrevious();
                }
                else if (PageNum == 4) {

                    PageNum--;
                    flipper.showPrevious();
                }

            }
        });

    }

    private void Init() {
        areasAdapter = new ArrayAdapter<String>(AddPlaceActivity.this, android.R.layout.simple_spinner_item, areas);


        flipper = (ViewFlipper) findViewById(R.id.Flipper);
        flipper.setInAnimation(this, android.R.anim.fade_in);
        flipper.setOutAnimation(this, android.R.anim.fade_out);

        mNext = (Button) findViewById(R.id.BTnext);


//        mPrevious.setEnabled(false);


        mName = (EditText) findViewById(R.id.ET_Name);
        mDescription = (EditText) findViewById(R.id.ET_Description);
        mTags = (EditText) findViewById(R.id.ET_Tags);
        mAddress = (EditText) findViewById(R.id.ET_Address);
        mETphone = (EditText) findViewById(R.id.ET_Phone1);
        mETweb = (EditText) findViewById(R.id.ET_Website);
        mETphone2 = (EditText) findViewById(R.id.ET_Phone2);


        mPhoto = (ImageView) findViewById(R.id.IVimage0);
        mPhoto1 = (ImageView) findViewById(R.id.IVimage);
        mPhoto2 = (ImageView) findViewById(R.id.IVimage2);
        mPhoto3 = (ImageView) findViewById(R.id.IVimage3);
        mLogo = (ImageView) findViewById(R.id.IVlogo);

        mCategory = (Spinner) findViewById(R.id.SpCategory);


        firebasedatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = firebasedatabase.getInstance().getReferenceFromUrl("https://round-around-6db6f.firebaseio.com/");
        //mDatabaseCategory = firebasedatabase.getReferenceFromUrl("https://without-name.firebaseio.com/");
        fillSpinnerCategory();


    }



    void fillSpinnerCategory() {

        areas.add("RESTAURANT");
        areas.add("CAFE");
        areas.add("BANK");
        areas.add("MARKET");
        areas.add("MOSQUE");
        areas.add("ATM");
        areas.add("HOSPITAL");
        areas.add("FUEL STATION");
        areas.add("PHARMACY");
        areas.add("OTHER");
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(areasAdapter);
    }

    int b = 0;

    public void selectImage(View v) {
        imageNum = 1;
        selectImage();
        b = 1;

    }

    int n = 0;

    public void selectImage2(View v) {


        if (b == 0) {
            Toast.makeText(this, "fill first image first", Toast.LENGTH_SHORT).show();
        } else {
            imageNum = 2;
            selectImage();
            n = 1;
        }
    }

    public void selectImage3(View v) {

        if (n == 0) {
            Toast.makeText(this, "fill above images first ", Toast.LENGTH_SHORT).show();
        } else {

            imageNum = 3;
            selectImage();
        }
    }

    public void selectLogo(View v) {

        imageNum = 4;
        selectImage();
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddPlaceActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"
                , "Delete", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddPlaceActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddPlaceActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result){
                        cameraIntent();

                    }

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();


                } else if (items[item].equals("Delete")) {

                    switch (imageNum) {
                        case 1:
                            mPhoto1.setImageBitmap(null);
                            ImageUri1 = null;
                            b = 0;

                            break;
                        case 2:

                            mPhoto2.setImageBitmap(null);
                            ImageUri2 = null;
                            n = 0;

                            break;


                        case 3:

                            mPhoto3.setImageBitmap(null);
                            ImageUri3 = null;

                            // Do something here related to button 2
                            break;

                        case 4:
                            mLogo.setImageBitmap(null);
                            LogoUri = null;

                            break;
                    }


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {

            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.CAMERA)) {


                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Thread.sleep(2000);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);


            } else if (requestCode == REQUEST_CAMERA) {

                onCaptureImageResult(data);


                // ImageUri = data.getData();
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void onCaptureImageResult(Intent data) {
        try {


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
//        data.setData(ImageUri);
                ImageUri = data.getData();
                ImageUri = getImageUri(this, thumbnail);
                switch (imageNum) {
                    case 1:
                        ImageUri1 = ImageUri;
                        mPhoto1.setImageBitmap(thumbnail);
                        break;
                    case 2:
                        ImageUri2 = ImageUri;
                        mPhoto2.setImageBitmap(thumbnail);
                        break;
                    case 3:
                        ImageUri3 = ImageUri;
                        mPhoto3.setImageBitmap(thumbnail);
                        break;
                    case 4:
                        LogoUri = ImageUri;
                        mLogo.setImageBitmap(thumbnail);
                        break;


                }

                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception r) {
                Log.e("Error",r.getMessage());
            }

        } catch (Exception c) {
            Log.e("Error",c.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {

            //  ImageUri = getImageUri(this, bm);
            switch (imageNum) {
                case 1:
                    ImageUri1 = getImageUri(this, bm);
                    mPhoto1.setImageBitmap(bm);
                    break;
                case 2:
                    ImageUri2 = getImageUri(this, bm);
                    mPhoto2.setImageBitmap(bm);
                    break;
                case 3:
                    ImageUri3 = getImageUri(this, bm);
                    mPhoto3.setImageBitmap(bm);
                    break;
                case 4:
                    LogoUri = getImageUri(this, bm);
                    mLogo.setImageBitmap(bm);
                    break;



            }
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.M)


    public String getImageExt(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }







    public void onClickSave(final View v) {
        count();


        try {

            if (mName.getText()!=null&&mAddress.getText()!=null&&mDescription.getText()!=null) {

                MyLocation();

                if (latitude != 0 && longitude != 0) {
                    if (LogoUri != null) {
                        if (ImageUri1 != null || ImageUri3 != null || ImageUri2 != null) {


                            final DatabaseReference id = mDatabase.child("Places").push();
                            uploadLogo(LogoUri,id);

                            id.child("Place Name").setValue(mName.getText()+"");
                            id.child("Place Category").setValue(mCategory.getSelectedItem()+"");
                            id.child("Place Location").setValue(mAddress.getText()+" ");
                            id.child("Place Description").setValue(mDescription.getText()+"");
                            String[] a = mTags.getText().toString().split(" ");
                            for (String x : a) {
                                myList.add(x.toUpperCase());
                            }

                            id.child("Place Tags").setValue(myList);
                            myList.clear();
                            id.child("Place Website").setValue(mETweb.getText() + "");
                            myList.add(mETphone.getText() + "");
                            myList.add(mETphone2.getText() + "");
                            id.child("Place Phone").setValue(myList);
                            myList.clear();
                            id.child("Place Own").setValue(PublicParamaters.UserRootId);
                            id.child("Place City").setValue(getCompleteAddressString(latitude, longitude));
                            id.child("Place Lat").setValue(latitude);
                            id.child("Place Lng").setValue(longitude);
                            // Location.clear();


                            StorageReference Ref = null;




                            if (ImageUri1 != null) {
                                upload(ImageUri1,id,"URL-1");




                            }
                            if (ImageUri2 != null) {
                                upload(ImageUri2,id,"URL-2");



                            }
                            if (ImageUri3 != null) {


                                upload(ImageUri3,id,"URL-3");


                            }



                            try{
                                Intent i=new Intent(this,HomeActivityProfile.class);
                                startActivity(i);}
                            catch (Exception c){

                            }
                        } else {
                            Toast.makeText(this, "Please Select at least one image", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(this, "Please Select Logo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(gps, "Open Gps", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.e("Error", e.getMessage());
        }



    }

    public void uploadLogo(Uri logoUri, final DatabaseReference id) {
        Ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(logoUri));


        Ref.putFile(logoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



//                                    dialog.dismiss();


                ImageUpload imageUpload = new ImageUpload(null, taskSnapshot.getDownloadUrl().toString());


                id.child("Place Logo").setValue(imageUpload);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {



                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    StorageReference Ref = null;
    public void upload(Uri uri, final DatabaseReference id, final String URL){
        Ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri));


        Ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



//                                    dialog.dismiss();


                ImageUpload imageUpload = new ImageUpload(null, taskSnapshot.getDownloadUrl().toString());


                id.child("images").child(URL).setValue(imageUpload);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {



                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    public void MyLocation()
    {

        gps = new TrackGPS(AddPlaceActivity.this);
        if(gps.canGetLocation()){
            longitude = gps.getLongitude();
            latitude = gps .getLatitude();
        }
        else
        {

            gps.showSettingsAlert();
        }




    }
    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < 1; i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(2));
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public void count(){

        ArrayList<Uri>images=new ArrayList<Uri>();
        images.add(LogoUri);
        images.add(ImageUri1);
        images.add(ImageUri2);
        images.add(ImageUri3);

        for (Uri img:images){
            if(img!=null){
                imageCounter++;
            }
        }


    }

}


