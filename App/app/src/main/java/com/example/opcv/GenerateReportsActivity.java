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
import com.itextpdf.kernel.pdf.PdfWriter;


import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class GenerateReportsActivity extends AppCompatActivity{
    private Button cancel, generate;
    private String id, idGarden, garden, ownerName;

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
                try {
                    ActivityCompat.requestPermissions(GenerateReportsActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                    createPDF(idGarden, id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void createPDF( String id, String idUser) throws IOException {

        String idSearch;

        CollectionReference collectionRef2 = FirebaseFirestore.getInstance().collection("UserInfo");
        Query query = collectionRef2.whereEqualTo("ID", idUser);


        DocumentReference collectionRef = FirebaseFirestore.getInstance().collection("Gardens").document(id);
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
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
                    String name = task.getResult().getString("GardenName");
                    String type=task.getResult().getString("GardenType");
                    String info = task.getResult().getString("InfoGarden");
                    String owner = task.getResult().getString("InfoGarden");
                    //Lo siguiente sera para mejorar el estilo y que se vea todoo
                    /*
                    TextPaint textPaint = new TextPaint();
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(20);
                    textPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                    StaticLayout staticLayout = new StaticLayout( "Información de la huerta"+info, textPaint, width, alignment, 1.0f, 0.0f, false);
                    page.getCanvas().save();
                    page.getCanvas().translate(x, y);
                    staticLayout.draw(page.getCanvas());
                    page.getCanvas().restore();*/
                    page.getCanvas().drawText("Huerta: "+name, x, y, paint);
                    page.getCanvas().drawText("Dueño de la huerta: "+ownerName, x, y+25, paint);
                    page.getCanvas().drawText("Tipo de huerta: "+type, x, y+50, paint);
                    page.getCanvas().drawText("Información de la huerta: "+info, x, y+75, paint);
                    //System.out.println("xddd" + xd);

                    document.finishPage(page);

                    String path = Environment.getExternalStorageDirectory().getPath() + "/example.pdf";
                    File file = new File(path);

                    try {
                        document.writeTo(new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    document.close();
                }

            }
        });


    }

}