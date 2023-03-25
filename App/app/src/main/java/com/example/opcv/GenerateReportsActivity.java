package com.example.opcv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;

import java.text.SimpleDateFormat;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenerateReportsActivity extends AppCompatActivity {
    private Button cancel, generate;
    private String id, idGarden, garden, ownerName, nameU, gardenU, type, info, group, idCollab, answer;

    private ArrayList<String> collection1Data;
    private ArrayList<String> collection2Data;
    private List<HashMap<Object, String>> mapsArray;
    private HashMap<Object, String> collection3, collection4;
    private FirebaseFirestore db;
    private int count=0, countForms=0, countEvents=0;

    private Context context;
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
        context = this;

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("¿Deseas enviar el archivo generado por correo electronico?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //finishAffinity();
                                answer = "false";
                                searchInfo(idGarden, id);
                                searchInfoUser(idGarden, id);

                                Toast.makeText(GenerateReportsActivity.this, "Se generó el reporte correctamente", Toast.LENGTH_SHORT).show();
                                GenerateReportsActivity.super.onBackPressed();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                //finishAffinity();
                                answer = "true";
                                searchInfo(idGarden, id);
                                searchInfoUser(idGarden, id);

                                Toast.makeText(GenerateReportsActivity.this, "Se generó el reporte correctamente", Toast.LENGTH_SHORT).show();
                                GenerateReportsActivity.super.onBackPressed();
                            }
                        }).create().show();

                //onBackPressed();
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
        final int NUM_DOCUMENTS_TO_RETRIEVE =1;
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

                                collection4 = new HashMap<>();
                                mapsArray = new ArrayList<>();
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.MONTH, -1);
                                Date startDate = calendar.getTime();
                                Date endDate = new Date();
                                String field, date;
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    field = document.getString("nameForm");
                                    date = document.getString("Date");
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    try{
                                        Date createForm = formatter.parse(date);
                                        calendar.setTime(createForm);
                                        if(createForm.after(startDate) && createForm.before(endDate)){
                                            countForms++;
                                        }
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    if(field.equals("Registro de evento")){
                                        collection3 = new HashMap<>();
                                        String name = document.getString("eventName");
                                        String lgbt = document.getString("lgtbiNumber");
                                        String dateEvent = document.getString("date");
                                        String adults = document.getString("adultNumber");
                                        String afros = document.getString("afroNumber");
                                        String childs = document.getString("childhoodNumber");
                                        String demobilized = document.getString("demobilizedNumber");
                                        String disability = document.getString("disabilityNumber");
                                        String elders = document.getString("elderlyNumber");
                                        String infant = document.getString("infantNumber");
                                        String foreign = document.getString("foreignNumber");
                                        String men = document.getString("menNumber");
                                        String mongrel = document.getString("mongrelNumber");
                                        String natives = document.getString("nativeNumber");
                                        String noSpc = document.getString("noSpcNumber");
                                        String others = document.getString("otherNumber");
                                        String peasant = document.getString("peasantNumber");
                                        String rom = document.getString("romNumber");
                                        String teen = document.getString("teenNumber");
                                        String total = document.getString("totalPerson");
                                        String victims = document.getString("victimNumber");
                                        String women = document.getString("womenNumber");
                                        String youth = document.getString("youthNumber");

                                        collection3.put("lgtbiNumber", lgbt);
                                        collection3.put("date", dateEvent);
                                        collection3.put("adultNumber", adults);
                                        collection3.put("afroNumber", afros);
                                        collection3.put("childhoodNumber", childs);
                                        collection3.put("demobilizedNumber", demobilized);
                                        collection3.put("disabilityNumber", disability);
                                        collection3.put("elderlyNumber", elders);
                                        collection3.put("eventName", name);
                                        collection3.put("foreignNumber", foreign);
                                        collection3.put("menNumber", men);
                                        collection3.put("mongrelNumber", mongrel);
                                        collection3.put("nativeNumber", natives);
                                        collection3.put("noSpcNumber", noSpc);
                                        collection3.put("peasantNumber", peasant);
                                        collection3.put("romNumber", rom);
                                        collection3.put("teenNumber", teen);
                                        collection3.put("totalPerson", total);
                                        collection3.put("victimNumber", victims);
                                        collection3.put("womenNumber", women);
                                        collection3.put("youthNumber", youth);
                                        collection3.put("otherNumber", others);
                                        collection3.put("infantNumber", infant);
                                        countEvents++;
                                        mapsArray.add(collection3);
                                    }
                                    collection1Data.add(field);
                                }
                                mapsArray.add(collection4);

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
            createPDF(gardenU, ownerName, collection1Data, type, info, group, count, countForms, answer, mapsArray);
        }
    }

    public void createPDF( String name, String nameUser, ArrayList<String> list, String type, String info, String group, int count, int countForms, String answer, List<HashMap<Object, String>> map) throws IOException{
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
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

        page.getCanvas().drawText("Los formularios que tiene la huerta->", x, y+170, paint);
        int now = y+195, i=1;
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

        page.getCanvas().drawText("En el último mes se realizarón "+countForms+" registros", x, now+25, paint);

        page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, now+40, paint);
        document.finishPage(page);

        int z = y, o=2, con=0;
        Boolean bo=false;
        String later = "";

        if(!map.isEmpty()){
            for(HashMap<Object, String> maps : map) {
                con++;
                later = maps.get("eventName");
                if(later != null){
                    pageInfo = new PdfDocument.PageInfo.Builder(300, 600, o).create();
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                    paint = new Paint();
                    page.getCanvas().drawBitmap(bitmap, null, destRect, null);
                    page.getCanvas().drawText("Eventos realizados en la huerta: ", x, z, paint);
                    page.getCanvas().drawText("Evento: " + maps.get("eventName"), x, z + 15, paint);
                    page.getCanvas().drawText("Estadisticas del evento: ", x, z + 30, paint);
                    page.getCanvas().drawText("Fecha del evento: " + maps.get("date"), x+5, z + 45, paint);
                    page.getCanvas().drawText("Asistentes al evento según género: ", x+5, z + 60, paint);
                    page.getCanvas().drawText("Hombres: " + maps.get("menNumber")+", Mujeres: "+maps.get("womenNumber")+", No especificado: "+maps.get("noSpcNumber"), x+15, z + 75, paint);
                    page.getCanvas().drawText("Asistentes al evento según rango de edad: ", x+5, z + 90, paint);
                    page.getCanvas().drawText("Primera infancia (0-5 años): " + maps.get("infantNumber"), x+15, z + 105, paint);
                    page.getCanvas().drawText("Infancia (6-11 años): "+maps.get("childhoodNumber"), x+15, z + 120, paint);
                    page.getCanvas().drawText("Adolescencia (12-18 años): "+maps.get("teenNumber"), x+15, z + 135, paint);
                    page.getCanvas().drawText("Jueventud (19-26 años): "+maps.get("youthNumber"), x+15, z + 150, paint);
                    page.getCanvas().drawText("Adultez (27-59 años): "+maps.get("adultNumber"), x+15, z + 165, paint);
                    page.getCanvas().drawText("Persona mayor (60 años o más): "+maps.get("elderlyNumber"), x+15, z + 180, paint);
                    page.getCanvas().drawText("Asistentes al evento por grupo étnico: ", x+5, z + 195, paint);
                    page.getCanvas().drawText("Población afro: "+maps.get("afroNumber"), x+15, z + 210, paint);
                    page.getCanvas().drawText("Población Indígena: "+maps.get("nativeNumber"), x+15, z + 225, paint);
                    page.getCanvas().drawText("Población LGBTI+: "+maps.get("lgtbiNumber"), x+15, z + 240, paint);
                    page.getCanvas().drawText("Población Rom+: "+maps.get("romNumber"), x+15, z + 255, paint);
                    page.getCanvas().drawText("Población Víctima del Conflicto Armado: "+maps.get("victimNumber"), x+15, z + 270, paint);
                    page.getCanvas().drawText("Población en Condición de Discapacidad: "+maps.get("disabilityNumber"), x+15, z + 285, paint);
                    page.getCanvas().drawText("Población Desmovilizada: "+maps.get("demobilizedNumber"), x+15, z + 300, paint);
                    page.getCanvas().drawText("Población Mestiza: "+maps.get("mongrelNumber"), x+15, z + 315, paint);
                    page.getCanvas().drawText("Extranjero(a): "+maps.get("foreignNumber"), x+15, z + 330, paint);
                    page.getCanvas().drawText("Campesino(a): "+maps.get("peasantNumber"), x+15, z + 345, paint);
                    page.getCanvas().drawText("Otros: "+maps.get("otherNumber"), x+15, z + 360, paint);

                    o++;
                    if(con == map.size()-1){
                        page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, z+380, paint);
                        page.getCanvas().drawText("Fin del reporte", x, z+395, paint);
                    }

                    document.finishPage(page);
                }
                if(map.size() == 1){
                    pageInfo = new PdfDocument.PageInfo.Builder(300, 600, o).create();
                    page = document.startPage(pageInfo);
                    page.getCanvas().drawBitmap(bitmap, null, destRect, null);
                    page.getCanvas().drawText("No hubo eventos en la huerta.", x, z, paint);
                    page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, z+20, paint);
                    page.getCanvas().drawText("Fin del reporte", x, z+40, paint);
                    document.finishPage(page);
                }
            }
        }

        String path = Environment.getExternalStorageDirectory().getPath() + "/"+name+".pdf";
        File file = new File(path);
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
        if(answer.equals("true")){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String userEmail = user.getEmail();

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + userEmail));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte de huerta "+name);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Mensaje del correo...");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        }
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
                        count++;
                    }
                    System.out.println("name: " + count);
                    collectionRef2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(String ele : ids){


                                    Query query = collectionRef2.whereEqualTo("ID", ele);
                                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                            if (task.isSuccessful()) {

                                                String name = null;

                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    collection2Data = new ArrayList<>();
                                                    name = document.getData().get("Name").toString();

                                                    //count++;
                                                    collection2Data.add(name);
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