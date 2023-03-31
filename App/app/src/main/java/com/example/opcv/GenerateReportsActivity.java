package com.example.opcv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.graphics.Typeface;
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
import android.widget.Switch;
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
import com.itextpdf.layout.Style;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenerateReportsActivity extends AppCompatActivity {
    private Button cancel, generate;
    private String id, idGarden, garden, ownerName, nameU, gardenU, type, info, group, idCollab, answer, name, idStored;

    private ArrayList<String> collection1Data;
    private ArrayList<String> collection2Data;
    private ArrayList<String> collectionCrops;
    private ArrayList<String> gardensNames;
    private ArrayList<String> contColl;
    private ArrayList<String> crops = new ArrayList<>();
    private ArrayList<Integer> conts;
    private List<HashMap<Object, String>> mapsArray;
    private HashMap<Object, String> collection3, collection4;
    private Map<String, List<String>> collectionCols;
    private FirebaseFirestore db;
    private int count=0, countForms=0, countEvents=0, countCollabs=0, count1=0, count2=0, countGardens=0, STORAGE_PERMISSION_CODE = 1;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_reports);

        cancel = (Button) findViewById(R.id.cancelReport);
        generate = (Button) findViewById(R.id.generateReportButton);

        context = this;
        Bundle extras = getIntent().getExtras();
        if(ContextCompat.checkSelfPermission(GenerateReportsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            System.out.println("se hizo");
        }
        else{
            requestStoragePermission();
        }
        if(extras != null){
            garden = extras.getString("garden");
            if(garden.equals("true")){
                id = extras.getString("idUser");
                idGarden = extras.getString("idGardenFirebaseDoc");
                ownerName = extras.getString("ownerName");
                generate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(context)
                                .setMessage("¿Deseas enviar el archivo generado por correo electronico?")
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        answer = "false";
                                        searchInfo(idGarden, id);
                                        searchInfoUser(idGarden, id);
                                        Toast.makeText(GenerateReportsActivity.this, "Se generó el reporte correctamente", Toast.LENGTH_SHORT).show();
                                        GenerateReportsActivity.super.onBackPressed();
                                        //finishAffinity();
                                    }
                                })
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
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
            }
            else if(garden.equals("false")){
                generate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(context)
                                .setMessage("¿Deseas enviar el archivo generado por correo electronico?")
                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //finishAffinity();
                                        answer = "false";
                                        searchGeneralInfo();

                                        Toast.makeText(GenerateReportsActivity.this, "Se generó el reporte correctamente", Toast.LENGTH_SHORT).show();
                                        GenerateReportsActivity.super.onBackPressed();
                                    }
                                })
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //finishAffinity();
                                        answer = "true";
                                        searchGeneralInfo();

                                        Toast.makeText(GenerateReportsActivity.this, "Se generó el reporte correctamente", Toast.LENGTH_SHORT).show();
                                        GenerateReportsActivity.super.onBackPressed();

                                    }
                                }).create().show();

                        //onBackPressed();
                    }
                });
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permiso de escritura")
                    .setMessage("El permiso de escritura es necesario para esta tarea, desea otorgarlo?")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(GenerateReportsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(GenerateReportsActivity.this, "Se dio el permiso", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(GenerateReportsActivity.this, "No se dio el permiso", Toast.LENGTH_SHORT).show();
            }
        }
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
                                collectionCrops = new ArrayList<>();
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

                                    if(field.equals("Control de Procesos de Siembra")){

                                        String name = document.getString("plants or seeds");
                                        collectionCrops.add(name);
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
            createPDF(gardenU, ownerName, collection1Data, type, info, group, count, countForms, answer, mapsArray, collectionCrops);
        }
    }

    public void createPDF( String name, String nameUser, ArrayList<String> list, String type, String info, String group, int count, int countForms, String answer, List<HashMap<Object, String>> map, ArrayList<String> crops) throws IOException{
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        int x = 10, y = 125, width=50;
        float left = 50, top = 50;
        //pa la imagen
        AssetManager assetManager = getAssets();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.im_logo_ceres_green);
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

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        String monthU="";
        switch (month){
            case 1:
                monthU = "Enero";
                break;
            case 2:
                monthU = "Febrero";
                break;
            case 3:
                monthU = "Marzo";
                break;
            case 4:
                monthU = "Abril";
                break;
            case 5:
                monthU = "Mayo";
                break;
            case 6:
                monthU = "Junio";
                break;
            case 7:
                monthU = "Julio";
                break;
            case 8:
                monthU = "Agosto";
                break;
            case 9:
                monthU = "Septiembre";
                break;
            case 10:
                monthU = "Octubre";
                break;
            case 11:
                monthU = "Noviembre";
                break;
            case 12:
                monthU = "Diciembre";
                break;
        }

        int year = calendar.get(Calendar.YEAR);
        Paint paintDate = new Paint();
        Paint paintBold = new Paint();
        Typeface italics = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
        paintDate.setTypeface(italics);
        Typeface bold = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        paintBold.setTypeface(bold);
        page.getCanvas().drawText("Fecha: "+day+" de "+monthU+" del "+year, x+130, y, paintDate);
        String firstPart, secondpart;
        int index = 23, len=info.length();
        firstPart = info.substring(0, index);
        secondpart = info.substring(index);
        page.getCanvas().drawText("Reporte de Huerta: "+name, x, y+20, paintBold);
        page.getCanvas().drawText("Dueño de la huerta: "+ownerName, x, y+40, paint);
        page.getCanvas().drawText("Tipo de huerta: "+type, x, y+60, paint);
        if(len<=index){
            page.getCanvas().drawText("Información de la huerta: "+info, x, y+80, paint);
            page.getCanvas().drawText("La huerta tiene chat grupal: "+group, x, y+100, paint);
            page.getCanvas().drawText("Numero de colaboradores de la huerta: "+count, x, y+120, paint);
            String cropsNames = "";
            if(!crops.isEmpty()){
                for(int i =0; i<crops.size();i++){
                    cropsNames = cropsNames + crops.get(i);
                    if(i != crops.size()-1){
                        cropsNames = cropsNames+", ";
                    }

                }
                page.getCanvas().drawText("Cultivos de la huerta: ", x, y+140, paint);
                page.getCanvas().drawText(cropsNames, x+20, y+160, paint);
            }
            else{
                page.getCanvas().drawText("No hay cultivos en la huerta.", x, y+140, paint);
            }

            page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, y+170, paint);

            page.getCanvas().drawText("Los formularios que tiene la huerta->", x, y+190, paintBold);
            int now = y+215, i=1;
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

            page.getCanvas().drawText("En el último mes se realizarón "+countForms+" registros", x, now+25, paintBold);

            page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, now+40, paint);
            document.finishPage(page);
        }
        else{
            page.getCanvas().drawText("Información de la huerta: "+firstPart, x, y+80, paint);
            page.getCanvas().drawText(secondpart, x, y+100, paint);
            page.getCanvas().drawText("La huerta tiene chat grupal: "+group, x, y+120, paint);
            page.getCanvas().drawText("Numero de colaboradores de la huerta: "+count, x, y+140, paint);
            String cropsNames = "";
            if(!crops.isEmpty()){
                for(int i =0; i<crops.size();i++){
                    cropsNames = cropsNames + crops.get(i);
                    if(i != crops.size()-1){
                        cropsNames = cropsNames+", ";
                    }

                }
                page.getCanvas().drawText("Cultivos de la huerta: ", x, y+160, paint);
                page.getCanvas().drawText(cropsNames, x+20, y+180, paint);
            }
            else{
                page.getCanvas().drawText("No hay cultivos en la huerta.", x, y+160, paint);
            }

            page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, y+190, paint);

            page.getCanvas().drawText("Los formularios que tiene la huerta->", x, y+210, paintBold);
            int now = y+240, i=1;
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

            page.getCanvas().drawText("En el último mes se realizarón "+countForms+" registros", x, now+25, paintBold);

            page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, now+40, paint);
            document.finishPage(page);
        }


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
                    page.getCanvas().drawText("Eventos realizados en la huerta: ", x, z+20, paintBold);
                    page.getCanvas().drawText("Evento: " + maps.get("eventName"), x, z + 35, paintBold);
                    page.getCanvas().drawText("Estadisticas del evento: ", x, z + 50, paintBold);
                    page.getCanvas().drawText("Fecha del evento: " + maps.get("date"), x+5, z + 65, paintDate);
                    page.getCanvas().drawText("Asistentes al evento según género: ", x+5, z + 80, paintBold);
                    page.getCanvas().drawText("Hombres: " + maps.get("menNumber")+", Mujeres: "+maps.get("womenNumber")+", No especificado: "+maps.get("noSpcNumber"), x+15, z + 95, paint);
                    page.getCanvas().drawText("Asistentes al evento según rango de edad: ", x+5, z + 110, paintBold);
                    page.getCanvas().drawText("Primera infancia (0-5 años): " + maps.get("infantNumber"), x+15, z + 125, paint);
                    page.getCanvas().drawText("Infancia (6-11 años): "+maps.get("childhoodNumber"), x+15, z + 140, paint);
                    page.getCanvas().drawText("Adolescencia (12-18 años): "+maps.get("teenNumber"), x+15, z + 155, paint);
                    page.getCanvas().drawText("Jueventud (19-26 años): "+maps.get("youthNumber"), x+15, z + 170, paint);
                    page.getCanvas().drawText("Adultez (27-59 años): "+maps.get("adultNumber"), x+15, z + 185, paint);
                    page.getCanvas().drawText("Persona mayor (60 años o más): "+maps.get("elderlyNumber"), x+15, z + 200, paint);
                    page.getCanvas().drawText("Asistentes al evento por grupo étnico: ", x+5, z + 215, paintBold);
                    page.getCanvas().drawText("Población afro: "+maps.get("afroNumber"), x+15, z + 230, paint);
                    page.getCanvas().drawText("Población Indígena: "+maps.get("nativeNumber"), x+15, z + 245, paint);
                    page.getCanvas().drawText("Población LGBTI+: "+maps.get("lgtbiNumber"), x+15, z + 260, paint);
                    page.getCanvas().drawText("Población Rom+: "+maps.get("romNumber"), x+15, z + 275, paint);
                    page.getCanvas().drawText("Población Víctima del Conflicto Armado: "+maps.get("victimNumber"), x+15, z + 290, paint);
                    page.getCanvas().drawText("Población en Condición de Discapacidad: "+maps.get("disabilityNumber"), x+15, z + 305, paint);
                    page.getCanvas().drawText("Población Desmovilizada: "+maps.get("demobilizedNumber"), x+15, z + 320, paint);
                    page.getCanvas().drawText("Población Mestiza: "+maps.get("mongrelNumber"), x+15, z + 335, paint);
                    page.getCanvas().drawText("Extranjero(a): "+maps.get("foreignNumber"), x+15, z + 350, paint);
                    page.getCanvas().drawText("Campesino(a): "+maps.get("peasantNumber"), x+15, z + 365, paint);
                    page.getCanvas().drawText("Otros: "+maps.get("otherNumber"), x+15, z + 380, paint);

                    o++;
                    if(con == map.size()-1){
                        page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, z+400, paint);
                        page.getCanvas().drawText("Fin del reporte", x, z+415, paintBold);
                    }

                    document.finishPage(page);
                }
                if(map.size() == 1){
                    pageInfo = new PdfDocument.PageInfo.Builder(300, 600, o).create();
                    page = document.startPage(pageInfo);
                    page.getCanvas().drawBitmap(bitmap, null, destRect, null);
                    page.getCanvas().drawText("No hubo eventos en la huerta.", x, z+20, paintBold);
                    page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, z+40, paint);
                    page.getCanvas().drawText("Fin del reporte", x, z+60, paintBold);
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
        final int NUM_DOCUMENTS_TO_RETRIEVE =22;
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
    private void searchGeneralInfo(){
        final int[] NUM_DOCUMENTS_TO_RETRIEVE = new int[1];// se realizo el cambio con respecto a lo anterior debido al problem con los gardens
        final int[] NUM_DOCUMENTS_RETRIEVED = {0};
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("Gardens");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    NUM_DOCUMENTS_TO_RETRIEVE[0] =task.getResult().size();
                    gardensNames = new ArrayList<>();
                    contColl = new ArrayList<>();
                    conts = new ArrayList<>();


                    for(QueryDocumentSnapshot q : task.getResult()){

                        name = q.getData().get("GardenName").toString();
                        contColl.add(q.getId());
                        gardensNames.add(name);
                        countGardens++;
                        collectionRef.document(q.getId()).collection("Forms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){

                                    String field, nameCrop, date, idR;
                                    int cont = 0;
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.add(Calendar.MONTH, -1);
                                    Date startDate = calendar.getTime();
                                    Date endDate = new Date();
                                    collectionCols = new HashMap<>();

                                    for(QueryDocumentSnapshot document : task.getResult()){

                                        field = document.getString("nameForm");
                                        date = document.getString("Date");
                                        idR= document.getString("Gardenid");
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

                                        if(field.equals("Control de Procesos de Siembra")){
                                            List<String> listCrop = new ArrayList<>();

                                            nameCrop = document.getString("plants or seeds");
                                            listCrop.add(nameCrop);
                                            crops.add(nameCrop);
                                            collectionCols.put("name", listCrop);
                                            count1++;

                                        }
                                        if(field.equals("Control de Procesos de Siembra") && count1 == 1){
                                            countGardens++;
                                        }

                                        count2++;
                                    }
                                   // System.out.println("el contador: "+countEvents);
                                    countEvents++;
                                }

                                collectionRef.document(q.getId()).collection("Collaborators").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            int cont=task.getResult().size();
                                            //System.out.println("el tamano "+task.getResult().size());
                                            conts.add(cont);
                                            //collectionCols.put(name, cont);
                                        }
                                        NUM_DOCUMENTS_RETRIEVED[0]++;
                                        try {
                                            checkIfAllGeneralDataRetrieved(NUM_DOCUMENTS_TO_RETRIEVE[0], NUM_DOCUMENTS_RETRIEVED[0]);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            }
                        });


                    }
                }
            }
        });

    }
    private void checkIfAllGeneralDataRetrieved(int numDocumentsToRetrieve, int numDocumentsRetrieved) throws IOException {

        if (numDocumentsRetrieved == numDocumentsToRetrieve ) {
            createGeneralPDF(gardensNames, conts, collectionCols, crops, countEvents, count1, count2, countForms);
        }
    }
    private void createGeneralPDF(ArrayList<String> list, ArrayList<Integer> list2, Map<String, List<String>> map, ArrayList<String> crops, Integer countEvents, Integer count1, Integer count2, Integer countForms) {

       /* int cont=0;
        for (int i = 0; i<list.size(); i++){
            System.out.println("Garden: "+list.get(i)+"Colabs: "+list2.get(i));
            cont = cont+list2.get(i);
        }System.out.println("Cont: "+cont);


        System.out.println("Map: "+crops.size());
        for (int i = 0; i<crops.size(); i++){
            System.out.println("Cultivos: "+crops.get(i));
        }
        int x=count1+count2;
        int y=x-countEvents;
        System.out.println("Counteventos: "+y);//numero de huertas trabajando los cultivos
        System.out.println("Contador de registros ultimo mes: "+countForms);*/


        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        int x = 10, y = 135, width = 50;
        float left = 50, top = 50;
        //pa la imagen
        AssetManager assetManager = getAssets();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.im_logo_ceres_green);
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
        int destTop = 10;   // Top coordinate of the rectangle
        int destRight = destLeft + destWidth;   // Right coordinate of the rectangle
        int destBottom = destTop + destHeight;
        Rect destRect = new Rect(destLeft, destTop, destRight, destBottom);
        page.getCanvas().drawBitmap(bitmap, null, destRect, null);
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        String monthU="";
        switch (month){
            case 1:
                monthU = "Enero";
                break;
            case 2:
                monthU = "Febrero";
                break;
            case 3:
                monthU = "Marzo";
                break;
            case 4:
                monthU = "Abril";
                break;
            case 5:
                monthU = "Mayo";
                break;
            case 6:
                monthU = "Junio";
                break;
            case 7:
                monthU = "Julio";
                break;
            case 8:
                monthU = "Agosto";
                break;
            case 9:
                monthU = "Septiembre";
                break;
            case 10:
                monthU = "Octubre";
                break;
            case 11:
                monthU = "Noviembre";
                break;
            case 12:
                monthU = "Diciembre";
                break;
        }

        int year = calendar.get(Calendar.YEAR);
        Paint paintDate = new Paint();
        Paint paintBold = new Paint();
        Typeface italics = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
        paintDate.setTypeface(italics);
        Typeface bold = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        paintBold.setTypeface(bold);
        page.getCanvas().drawText("Fecha: "+day+" de "+monthU+" del "+year, x+130, y, paintDate);
        page.getCanvas().drawText("Reporte General de Ceres", x, y + 20, paintBold);
        //page.getCanvas().drawText("Actualmente Ceres cuenta con " + list.size() + " huertas", x, y + 40, paint);
        page.getCanvas().drawText("Actualmente Ceres cuenta con " + list.size() + " huertas", x, y + 40, paint);
        page.getCanvas().drawText("Nombres de las huertas en pagina siguiente.", x, y + 60, paint);
        int cont = 0;
        for (int i = 0; i < list.size(); i++) {
            cont = cont + list2.get(i);
        }
        page.getCanvas().drawText("Numero total de colaboradores en Ceres: " + cont, x, y + 80, paint);
        page.getCanvas().drawText("Actualmente se existen los siguientes cultivos: ", x, y + 100, paint);
        int z=y+120;
        for (int i = 0; i<crops.size(); i++){
            page.getCanvas().drawText(i+1+": "+crops.get(i), x+10, z, paintDate);
            z=z+20;
        }
        int c1=countEvents-count2;
        int prove=0;
        if(c1%2!=0){
            prove=2;
        }
        else{
            prove=3;
        }
        //prove = prove*2;

        int c2=count1-c1-prove;
        System.out.println("c1: "+count1+" c2: "+count2+" countevents: "+countEvents);
        page.getCanvas().drawText("De las "+list.size()+" huertas "+c2+" estan trabajando en estos cultivos.", x, z , paintDate);
        //page.getCanvas().drawText("estos cultivos.", x, z + 10, paintDate);
        page.getCanvas().drawText("Se han realizado "+countForms+" registros de formularios en el ", x, z + 30, paint);
        page.getCanvas().drawText("último mes.", x, z + 40, paint);
        document.finishPage(page);

        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        page.getCanvas().drawBitmap(bitmap, null, destRect, null);

        page.getCanvas().drawText("Las huertas existentes a dia de hoy son: ", x+5, y, paintBold);
        int o=y+40;
        for (int i = 0; i<list.size(); i++){
            page.getCanvas().drawText(i+1+": "+list.get(i), x+10, o, paintDate);
            o=o+14;

        }
        page.getCanvas().drawText("---------------------------------------------------------------------------------------------", x, o+20, paint);
        page.getCanvas().drawText("Fin del reporte", x, o+40, paintBold);
        document.finishPage(page);

            String path = Environment.getExternalStorageDirectory().getPath() + "/ReporteGeneralCeres.pdf";
            File file = new File(path);
            try {
                document.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.close();
            if (answer.equals("true")) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String userEmail = user.getEmail();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + userEmail));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte General de Ceres " + name);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Mensaje del correo...");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }


        }



}