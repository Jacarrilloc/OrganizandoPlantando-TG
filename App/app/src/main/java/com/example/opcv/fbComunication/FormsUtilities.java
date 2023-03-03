package com.example.opcv.fbComunication;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Map;

public class FormsUtilities {

    private FirebaseFirestore database;
    private FirebaseAuth autentication;
    private Calendar calendar;
    public boolean statusCreateGarden;

    public void createForm(Context context, Map<String,Object> infoForm, String idGardenFb) {
        database = FirebaseFirestore.getInstance();
        autentication = FirebaseAuth.getInstance();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Se agrega 1 porque el primer mes es 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String date = String.format("%02d/%02d/%d", day, month, year);
        infoForm.put("Date",date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        infoForm.put("Time",time);


        database.collection("Gardens").document(idGardenFb).collection("Forms").add(infoForm)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            statusCreateGarden = true;
                        } else {
                            statusCreateGarden = false;
                        }
                    }
                });
    }
}

