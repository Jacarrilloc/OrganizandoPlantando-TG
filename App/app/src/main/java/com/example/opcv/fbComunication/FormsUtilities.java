package com.example.opcv.fbComunication;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.adapter.FormsRegistersAdapter;
import com.example.opcv.formsScreen.Form_CPS;
import com.example.opcv.formsScreen.FormsRegistersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

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
        infoForm.put("Gardenid",idGardenFb);

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
    public void editForms(String idGarden, String idFormCollection, String formName){//este metodo sera el que se utilizara cuando se le de a editar en FormsRegisterActivity

    }

    public void deleteForm(String idGarden, String idFormCollection){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idFormCollection).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    database.collection("Gardens").document(idGarden).collection("Forms").document(idFormCollection).delete();
                }
            }
        });
    }



}

