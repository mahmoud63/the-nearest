package com.example.mohammed.withoutname;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private Button mNext, mPrevious;
    private ViewFlipper mFlipper;
    int imageNum=0;
    List myList = new ArrayList();
    int PageNum=1;
    private EditText mName , mDescription , mTags , mAddress , mETweb ,mETphone,mETphone2 ;
    private Spinner mCategory;
    private ImageView mPhoto , mPhoto1 , mPhoto2 , mPhoto3 , mLogo;
    private Uri ImageUri , ImageUri1 , ImageUri2 , ImageUri3 , LogoUri;
    final List<String> areas = new ArrayList<String>();
    ArrayAdapter<String> areasAdapter ;

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
        setContentView(R.layout.activity_edit);

        Init();

        RetriveData();


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PageNum==2)
                {
                    mFlipper.showNext();
                    PageNum++;
                    mNext.setText("Finish");
                }
                else if(PageNum==3)
                {
                    //Method Submit

                    onClickSave();
                }
                else if(PageNum==1)
                {
                    PageNum++;
                    mFlipper.showNext();
                    mPrevious.setEnabled(true);
                }
            }
        });
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PageNum==1){

                }else if(PageNum==2)
                {
                    mPrevious.setEnabled(false);
                    PageNum--;
                    mFlipper.showPrevious();
                }
                else if(PageNum==3) {
                    mNext.setText("Next");
                    PageNum--;
                    mFlipper.showPrevious();
                }
            }
        });
    }
    private void Init()
    {
        areasAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_spinner_item, areas);




        mNext= (Button)findViewById(R.id.BTnext);
        mPrevious= (Button)findViewById(R.id.BTprevious);
        mFlipper=(ViewFlipper)findViewById(R.id.Flipper);
        mPrevious.setEnabled(false);
        mFlipper.setInAnimation(this, R.anim.fab_slide_in_from_left);
        mFlipper.setOutAnimation(this, R.anim.fab_slide_in_from_left);


        mName=(EditText) findViewById(R.id.ET_Name);
        mDescription=(EditText) findViewById(R.id.ET_Description);
        mTags=(EditText) findViewById(R.id.ET_Tags);
        mAddress=(EditText) findViewById(R.id.ET_Address);
        mETphone=(EditText) findViewById(R.id.ET_Phone1);
        mETweb=(EditText) findViewById(R.id.ET_Website);
        mETphone2=(EditText) findViewById(R.id.ET_Phone2);


        mPhoto = (ImageView) findViewById(R.id.IVimage0);
        mPhoto1=(ImageView) findViewById(R.id.IVimage);
        mPhoto2=(ImageView) findViewById(R.id.IVimage2);
        mPhoto3=(ImageView) findViewById(R.id.IVimage3);
        mLogo=(ImageView) findViewById(R.id.IVlogo);

        mCategory=(Spinner) findViewById(R.id.SpCategory);

        mFlipper=(ViewFlipper)findViewById(R.id.Flipper);

        firebasedatabase=FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = firebasedatabase.getInstance().getReferenceFromUrl("https://without-name.firebaseio.com/");
        mDatabaseCategory=firebasedatabase.getReferenceFromUrl("https://without-name.firebaseio.com/");
        fillSpinnerCategory();


    }
    void fillSpinnerCategory(){

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
    int b=0;

    public void selectImage(View v) {
        imageNum=1;
        selectImage();
        b=1;

    }
    int n=0;
    public void selectImage2(View v) {


        if (b==0){
            Toast.makeText(this, "fill first image first", Toast.LENGTH_SHORT).show();
        }else{
            imageNum=2;
            selectImage();
            n=1;
        }
    }
    public void selectImage3(View v) {

        if (n==0){
            Toast.makeText(this, "fill above images first ", Toast.LENGTH_SHORT).show();
        }else{

            imageNum=3;
            selectImage();}
    }
    public void selectLogo(View v){

        imageNum=4;
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
        new AlertDialog.Builder(EditActivity.this)
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
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
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
        final CharSequence[] items = { "Take Photo", "Choose from Library"
                ,"Delete","Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(EditActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();


                }
                else if (items[item].equals("Delete")) {

                    switch (imageNum) {
                        case 1:
                            mPhoto1.setImageBitmap(null);
                            ImageUri1=null;
                            b=0;
                            break;
                        case 2:

                            mPhoto2.setImageBitmap(null);
                            ImageUri2=null;
                            n=0;
                            break;


                        case 3:

                            mPhoto3.setImageBitmap(null);
                            ImageUri3=null;
                            // Do something here related to button 2
                            break;

                        case 4:
                            mLogo.setImageBitmap(null);
                            LogoUri=null;
                    }




                }

                else if (items[item].equals("Cancel")) {
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
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
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
                    Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Thread.sleep(2000);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
                startActivityForResult(intent, REQUEST_CAMERA);}
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
                ImageUri = data.getData();}
            else if (requestCode == REQUEST_CAMERA){

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
                ImageUri=getImageUri(this,thumbnail);
                switch (imageNum){
                    case 1:
                        ImageUri1=ImageUri;
                        mPhoto1.setImageBitmap(thumbnail);
                        break;
                    case 2:
                        ImageUri2=ImageUri;
                        mPhoto2.setImageBitmap(thumbnail);
                        break;
                    case 3:
                        ImageUri3=ImageUri;
                        mPhoto3.setImageBitmap(thumbnail);
                        break;
                    case 4:
                        LogoUri=ImageUri;
                        mLogo.setImageBitmap(thumbnail);
                        break;


                }

                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception r) {
                Toast.makeText(this, r.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }catch (Exception c){
            Toast.makeText(this, c.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{mPhoto.setImageBitmap(bm);
            switch (imageNum){
                case 1:
                    ImageUri1=ImageUri;
                    mPhoto1.setImageBitmap(bm);
                    break;
                case 2:
                    ImageUri2=ImageUri;
                    mPhoto2.setImageBitmap(bm);
                    break;
                case 3:
                    ImageUri3=ImageUri;
                    mPhoto3.setImageBitmap(bm);
                    break;
                case 4:
                    LogoUri=ImageUri;
                    mLogo.setImageBitmap(bm);
                    break;
                default:
                    Log.e("error","error");


            }}catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }}

    @TargetApi(Build.VERSION_CODES.M)


    public String getImageExt(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    ArrayList<String > convert(String s){
        ArrayList<String> List = new ArrayList<String>(Arrays.asList(s.split(" ")));
        return  List;
    }


    void RetriveData()
    {
        mDatabase.child("Places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    mName.setText(dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Name").getValue(String.class));
                    mAddress.setText(dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Location").getValue(String.class));
                    mDescription.setText(dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Description").getValue(String.class));
                    mETweb.setText(dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Website").getValue(String.class));
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                    };
                    ArrayList<String> list2 = dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Phone").getValue(t);
                    mETphone.setText(list2.get(0).toString());
                    mETphone2.setText(list2.get(1).toString());
                    ArrayList<String> list = dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Tags").getValue(t);
                    String y = "";
                    for (String x : list) {
                        y += x + " ";
                    }
                    mTags.setText(y);
                    mCategory.setSelection(areasAdapter.getPosition(dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Category").getValue(String.class)));
                    Picasso.with(EditActivity.this)
                            .load(dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Logo").getValue(String.class) + "").placeholder(R.mipmap.ic_launcher_round)
                            .into(mLogo);
                    ArrayList<String> list3 = dataSnapshot.child(PublicParamaters.PlaceRootId).child("Place Images").getValue(t);
                    Picasso.with(EditActivity.this)
                            .load("" + list3.get(0)).placeholder(R.mipmap.ic_launcher_round)
                            .into(mPhoto1);
                    Picasso.with(EditActivity.this)
                            .load("" + list3.get(1)).placeholder(R.mipmap.ic_launcher_round)
                            .into(mPhoto2);
                    Picasso.with(EditActivity.this)
                            .load("" + list3.get(2)).placeholder(R.mipmap.ic_launcher_round)
                            .into(mPhoto3);
                }catch (Exception ex)
                {

                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickSave() {


        final DatabaseReference id = mDatabase.child("places").child(PublicParamaters.PlaceRootId);

        id.child("Name").setValue(mName.getText().toString());
        id.child("Category").setValue(mCategory.getSelectedItem().toString());
        id.child("Address").setValue(mAddress.getText().toString());
        id.child("Description").setValue(mDescription.getText().toString());
        String []a=mTags.getText().toString().split(" ");
        for (String x:a)
        {
            myList.add(x);
        }
        id.child("Tags").setValue(myList);
        myList.clear();
        id.child("Place Website").setValue(mETweb.getText()+"");
        myList.add(mETphone.getText()+"");
        myList.add(mETphone2.getText()+"");
        id.child("Phone").setValue(myList);
        myList.clear();





            // Location.clear();


        StorageReference Ref = null;
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("uploading image");
        try {



            if (ImageUri1 != null || ImageUri3 != null || ImageUri2 != null) {







                if (ImageUri1 != null) {
                    Ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(ImageUri1));

                    Ref.putFile(ImageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();


                            ImageUpload imageUpload = new ImageUpload(null, taskSnapshot.getDownloadUrl().toString());

                            id.child("images").child("URL-1").setValue(imageUpload);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {


                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            dialog.setMessage("uploading " + (int) progress + "s");
                        }
                    });
                }
                if (ImageUri2 != null) {
                    Ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(ImageUri2));

                    Ref.putFile(ImageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();


                            ImageUpload imageUpload = new ImageUpload(null, taskSnapshot.getDownloadUrl().toString());

                            id.child("images").child("URL-2").setValue(imageUpload);
                            //String uploadID=mDatabaseRef.push().getKey();
                            // mDatabase.child(uploadID).child("images").push().setValue(imageUpload);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {


                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            dialog.setMessage("uploading " + (int) progress + "s");
                        }
                    });
                }
                if (ImageUri3 != null) {
                    Ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(ImageUri3));

                    Ref.putFile(ImageUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();


                            ImageUpload imageUpload = new ImageUpload(null, taskSnapshot.getDownloadUrl().toString());

                            id.child("images").child("URL-3").setValue(imageUpload);

                            //mDatabase.child(uploadID).child("images").push().setValue(imageUpload);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {


                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            dialog.setMessage("uploading " + (int) progress + "s");
                        }
                    });
                }

            } else {
                Toast.makeText(getApplicationContext(), "please select image", Toast.LENGTH_SHORT).show();
            }

            if (LogoUri != null) {
                Ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(LogoUri));

                Ref.putFile(LogoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();


                        ImageUpload imageUpload = new ImageUpload(null, taskSnapshot.getDownloadUrl().toString());

                        id.child("Logo").child("URL").setValue(imageUpload);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {


                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        dialog.setMessage("uploading " + (int) progress + "s");
                    }
                });
            }


        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}