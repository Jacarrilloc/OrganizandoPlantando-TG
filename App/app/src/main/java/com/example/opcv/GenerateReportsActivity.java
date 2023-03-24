package com.example.opcv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.opcv.gardens.GardenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;


import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenerateReportsActivity extends AppCompatActivity {
    private Button cancel, generate;
    private String id, idGarden, garden, ownerName, nameU, gardenU, type, info, group, idCollab;;

    private ArrayList<String> collection1Data;
    private List<String> collection2Data;
    private FirebaseFirestore db;
    private int count=0;

    private PdfDocument document;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_reports);

        cancel = (Button) findViewById(R.id.cancelReport);
        generate = (Button) findViewById(R.id.generateReportButton);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            garden = extras.getString("garden");
            if(garden.equals("true")){
                id = extras.getString("idUser");
                idGarden = extras.getString("idGardenFirebaseDoc");
                ownerName = extras.getString("ownerName");
            }
        }
        System.out.println("El id loggeado: "+ ownerName);
        System.out.println("El id garden: "+ idGarden);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInfo(idGarden, id);
                Toast.makeText(GenerateReportsActivity.this, "Se generó el reporte correctamente", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void searchInfo(String idGarden, String idUser){
        final int NUM_DOCUMENTS_TO_RETRIEVE = 1;
        final int[] NUM_DOCUMENTS_RETRIEVED = {0};
        DocumentReference collectionRef = FirebaseFirestore.getInstance().collection("Gardens").document(idGarden);
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    gardenU = task.getResult().getString("GardenName");
                    if(Objects.equals(task.getResult().getString("GardenType"), "Public")){
                        type = "Publica";
                    }
                    else{
                        type = "Privada";
                    }
                    if(task.getResult().getString("Garden_Chat_Link") != null){
                        group = "Si";
                    }
                    else{
                        group = "No";
                    }

                    info = task.getResult().getString("InfoGarden");

                    CollectionReference collRef = collectionRef.collection("Forms");
                    collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                collection1Data = new ArrayList<>();
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    String field = document.getString("nameForm");
                                    collection1Data.add(field);
                                }
                                NUM_DOCUMENTS_RETRIEVED[0]++;
                                try {
                                    checkIfAllDataRetrieved(NUM_DOCUMENTS_TO_RETRIEVE, NUM_DOCUMENTS_RETRIEVED[0]);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkIfAllDataRetrieved(int numDocumentsToRetrieve, int numDocumentsRetrieved) throws IOException {

        if (numDocumentsRetrieved == numDocumentsToRetrieve) {
            createPDF(gardenU, ownerName, collection1Data, type, info, group, count);
        }
    }

    public void createPDF( String name, String nameUser, ArrayList<String> list, String type, String info, String group, int count) throws IOException{
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        int x = 10, y = 150, width=50;
        float left = 50, top = 50;
        //pa la imagen
        AssetManager assetManager = getAssets();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.im_logo_ceres);
        int maxWidth = 200;   // Maximum width of the image
        int maxHeight = 200;  // Maximum height of the image
        int imageWidth = bitmap.getWidth();   // Original width of the image
        int imageHeight = bitmap.getHeight(); // Original height of the image
        float aspectRatio = (float) imageWidth / imageHeight;
        int destWidth = 0, destHeight = 0;
        if (aspectRatio > 1) {
            // Landscape image
            destWidth = maxWidth;
            destHeight = (int) (maxWidth / aspectRatio);
        } else {
            // Portrait or square image
            destHeight = maxHeight;
            destWidth = (int) (maxHeight * aspectRatio);
        }
        int destLeft = 55;  // Left coordinate of the rectangle
        int destTop = 20;   // Top coordinate of the rectangle
        int destRight = destLeft + destWidth;   // Right coordinate of the rectangle
        int destBottom = destTop + destHeight;
        Rect destRect = new Rect(destLeft, destTop, destRight, destBottom);
        page.getCanvas().drawBitmap(bitmap, null, destRect, null);

        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;

        page.getCanvas().drawText("Huerta: "+name, x, y, paint);
        page.getCanvas().drawText("Dueño de la huerta: "+ownerName, x, y+25, paint);
        page.getCanvas().drawText("Tipo de huerta: "+type, x, y+50, paint);
        page.getCanvas().drawText("Información de la huerta: "+info, x, y+75, paint);
        page.getCanvas().drawText("La huerta tiene chat grupal: "+group, x, y+100, paint);
        page.getCanvas().drawText("Numero de colaboradores de la huerta: "+count, x, y+125, paint);
        page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, y+150, paint);
        page.getCanvas().drawText("Los formularios que tiene la huerta->", x, y+190, paint);
        int now = y+205, i=1;
        if(!list.isEmpty()){
            for(String element : list){
                page.getCanvas().drawText(i+": "+element, x, now, paint);
                i++;
                now = now+15;
            }
        }
        else{
            page.getCanvas().drawText("No hay formularios en esta huerta", x, now, paint);
        }

        page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, now+25, paint);
        page.getCanvas().drawText("Fin del reporte", x, now+50, paint);


        document.finishPage(page);

        String path = Environment.getExternalStorageDirectory().getPath() + "/"+name+".pdf";
        File file = new File(path);

        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
    }
    public void searchInfoUser(String idGarden, String idUser){
        final int NUM_DOCUMENTS_TO_RETRIEVE =1;
        final int[] NUM_DOCUMENTS_RETRIEVED = {0};
        CollectionReference collectionRef2 = FirebaseFirestore.getInstance().collection("UserInfo");
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("Gardens").document(idGarden).collection("Collaborators");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int a=0, b=0;
                    ArrayList<String> ids = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        idCollab = document.getString("idCollaborator");
                        ids.add(idCollab);
                        a++;
                    }
                    collectionRef2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(String ele : ids){


                                    Query query = collectionRef2.whereEqualTo("ID", ele);
                                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            collection2Data = new ArrayList<>();
                                            if (task.isSuccessful()) {

                                                String name = null;

                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //name = document.getData().get("Name").toString();
                                                    //System.out.println("name: " + name);
                                                    count++;
                                                    //collection2Data.add(name);
                                                }



                                            }

                                        }
                                    });


                                }
                            }
                        }
                    });

                }
            }
        });
    }

}