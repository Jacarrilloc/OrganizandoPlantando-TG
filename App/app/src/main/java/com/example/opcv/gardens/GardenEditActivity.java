package com.example.opcv.gardens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opcv.HomeActivity;
import com.example.opcv.MapsActivity;
import com.example.opcv.R;
import com.example.opcv.auth.EditUserActivity;
import com.example.opcv.ludificationScreens.DictionaryHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class GardenEditActivity extends AppCompatActivity {
    private EditText gardenName, comunity, description;
    private Button acceptChanges, gardens, myGardens, profile, deleteGarden, addForm, changeImage, ludification;
    private ImageView addParticipants,gardenImage;
    private Switch switchGardenTypeModified;
    private CheckBox publicGarden, privateGarden;
    private FloatingActionButton backButtom;
    private FirebaseAuth autentication;
    private FirebaseFirestore database, database2;
    private FirebaseUser userLog;

    private Boolean IsChangedPhoto = false;
    private TextView adminMembersGarden;

    private CollectionReference gardensRef;
    private String idUser, idGarden, nameGarden, infoGarden, name;
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_edit);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idGarden = extras.getString("idGarden");
        }

        gardenImage = findViewById(R.id.gardenImageEditActivity);
        changeImage = findViewById(R.id.ChangeImageEditGarden);

        switchGardenTypeModified = findViewById(R.id.switchGardenTypeModified);
        backButtom = findViewById(R.id.returnArrowButtomEditToGarden);
        adminMembersGarden = (TextView) findViewById(R.id.adminMembers);
        autentication = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        gardensRef = database.collection("Gardens");
        gardenName = (EditText) findViewById(R.id.gardenName);
        description = (EditText) findViewById(R.id.gardenDescription);
        comunity = (EditText) findViewById(R.id.gardenInfo);
        gardens = (Button) findViewById(R.id.gardens);
        myGardens = (Button) findViewById(R.id.myGardens);
        acceptChanges = (Button) findViewById(R.id.acceptChanges);
        deleteGarden = (Button) findViewById(R.id.deleteGarden);
        addForm = (Button) findViewById(R.id.addForm);

        IsChangedPhoto = true;

        getImageGarden(idGarden);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(GardenEditActivity.this)
                        .setMessage("¿Deseas Tomar una foto o elegir desde la galeria?")
                        .setNegativeButton("Tomar Foto", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                takePhoto();
                            }
                        })
                        .setPositiveButton("Seleccionar desde la Galeria", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                selectInGalery();
                            }
                        })
                        .show();
            }
        });

        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ludification = (Button) findViewById(R.id.ludification);

        ludification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(GardenEditActivity.this, DictionaryHome.class);
                startActivity(edit);
            }
        });


        adminMembersGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent requests = new Intent(GardenEditActivity.this, GardenMembersActivity.class);
                System.out.println("es id es : " + idGarden);
                requests.putExtra("idGarden", idGarden);
                startActivity(requests);
                finish();
            }
        });

        gardensRef.document(idGarden).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nameGarden = documentSnapshot.getString("GardenName");
                gardenName.setText(nameGarden);
                infoGarden = documentSnapshot.getString("InfoGarden");
                description.setText(infoGarden);
                String gardenInfoType = documentSnapshot.getString("GardenType");
                if(gardenInfoType.equals("Public")){
                    switchGardenTypeModified.setChecked(false);
                } else if (gardenInfoType.equals("Private")) {
                    switchGardenTypeModified.setChecked(true);
                }
            }
        });

        gardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, GardensAvailableActivity.class));
            }
        });

        myGardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, HomeActivity.class));
            }
        });

        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GardenEditActivity.this, EditUserActivity.class));
            }
        });

        acceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editGardenInfo()){
                    if(IsChangedPhoto){
                        changePhoto();
                    }
                    Intent start = new Intent(GardenEditActivity.this,HomeActivity.class);
                    String id = autentication.getCurrentUser().getUid().toString();
                    //System.out.println("El id es:.  "+id);
                    start.putExtra("ID", id);
                    start.putExtra("idGarden", idGarden);
                    start.putExtra("gardenName", name);

                    startActivity(start);
                }

            }
        });

        deleteGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("¿Estás seguro de que deseas eliminar esta huerta?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteGarden(idGarden);
                                Intent exit = new Intent(GardenEditActivity.this, HomeActivity.class);
                                exit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(exit);
                            }
                        }).create().show();


            }
        });

        addForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoForms = new Intent(GardenEditActivity.this, GardenForms.class);
                String idGardenFirebase = extras.getString("idGarden");
                infoForms.putExtra("idGardenFirebaseDoc",idGardenFirebase);
                startActivity(infoForms);
            }
        });

        addParticipants = (ImageView) findViewById(R.id.addPersons);
        addParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void deleteGarden(String idGarden) {
        database2 = FirebaseFirestore.getInstance();
        database2.collection("Gardens")
                .document(idGarden)
                .collection("Forms")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                database2.collection("Gardens")
                                        .document(idGarden)
                                        .collection("Forms")
                                        .document(document.getId())
                                        .delete();
                            }
                        }
                    }
                });
        database2.collection("Gardens")
                .document(idGarden)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GardenEditActivity.this, "Se borro exitosamente tu huerta", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GardenEditActivity.this, "Error al borrar la huerta", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private Boolean editGardenInfo(){
        name = gardenName.getText().toString();
        String info = description.getText().toString();
        Boolean gardenType = switchGardenTypeModified.isChecked();
        if(validateField(name, info)){
            FirebaseUser user = autentication.getCurrentUser();
            //CollectionReference collectionRef = database.collection("Gardens");

            idUser = user.getUid();
            searchGarden(idUser, name, info, gardenType);
            Toast.makeText(GardenEditActivity.this, "Se modificó exitosamente tu huerta", Toast.LENGTH_SHORT).show();
            return true;

            //System.out.println("El id es: "+collectionRef.getPath().toString());
        }
        else{
            return false;
        }
    }

    private void searchGarden(String idUser, String name, String info, Boolean gardenType) {
        database.collection("Gardens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String userId;
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                userId = document.getData().get("ID_Owner").toString();
                                if(userId.equals(idUser) ){
                                    //String idCollection = document.getId().toString();
                                    final DocumentReference docRef = database.collection("Gardens").document(idGarden);
                                    database.runTransaction(new Transaction.Function<Void>() {
                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentSnapshot snapshot = transaction.get(docRef);
                                            if(!gardenType){
                                                transaction.update(docRef, "GardenName", name, "InfoGarden", info, "GardenType", "Public");
                                            }
                                            if(gardenType){
                                                transaction.update(docRef, "GardenName", name, "InfoGarden", info, "GardenType", "Private");
                                            }

                                            return null;
                                        }
                                    });

                                    /*database.collection("Gardens").document(idCollection)
                                            .update("GardenName", name);
*/
                                    //System.out.println("El id es: "+idCollection);
                                }

                            }
                        }
                        else{
                            Toast.makeText(GardenEditActivity.this, "No tienes permiso para actualizar la huerta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean validateField(String name,String info){

        if(name.isEmpty() || info.isEmpty()){
            Toast.makeText(this, "Es necesario Ingresar el nombre e información de la Huerta", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getImageGarden(String idGarden){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageName = idGarden + ".jpg";
        StorageReference imageRef = storageRef.child("gardenMainPhoto/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                gardenImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                gardenImage.setImageResource(R.drawable.im_logo_ceres_green);
            }
        });
    }

    private void takePhoto(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                openCamaraAndTakePhoto();
                IsChangedPhoto = true;
            } else {
                Toast.makeText(this, "No hay una Camara en tu Dispositivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void openCamaraAndTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private void selectInGalery(){
        if(ContextCompat.checkSelfPermission(GardenEditActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GardenEditActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
        }else{
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            IsChangedPhoto = true;
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap photoI = (Bitmap) data.getExtras().get("data");
            gardenImage.setImageBitmap(photoI);
        }
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE && data != null) {
            Uri imageUri = data.getData();
            gardenImage.setImageURI(imageUri);
        }
    }

    private void changePhoto(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("gardenMainPhoto/" + idGarden + ".jpg");
        gardenImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = gardenImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream stream = new ByteArrayInputStream(baos.toByteArray());
        UploadTask uploadTask = storageRef.putStream(stream);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(GardenEditActivity.this, "Se Cambio la Imagen de la Huerta Exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // La carga de la foto ha fallado, manejar el error aquí
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
        super.attachBaseContext(newBase);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        adjustFontScale(getApplicationContext(), config);
    }
    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }
}