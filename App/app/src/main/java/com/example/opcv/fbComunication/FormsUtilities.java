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
import com.google.firebase.firestore.Transaction;

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
//Aca se haran los metodos para editar cada formulario
    public void editInfoCIH(Context context, String idGarden, String idCollection, String tools, String quantityTools, String statusTools, String toolExistance, String conceptSelectedItem, String selectedItem){
        database = FirebaseFirestore.getInstance();
        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "tool", tools, "toolQuantity", quantityTools, "toolStatus", statusTools, "existenceQuantity", toolExistance,
                                            "concept", conceptSelectedItem , "incomingOutgoing", selectedItem);
                                    return null;
                                }
                            });
                        }
                    }
                });


    }

    public void editInfoRE(Context context, String idGarden, String idCollection, String date, String eventN, String totalP, String femaleN, String maleN,
                           String noSpcN, String infantN, String childhoodN, String teenN, String youthN, String adultN, String elderlyN, String afroN, String nativeN,
                           String lgtbiN, String romN, String victimN, String disabilityN, String demobilizedN, String mongrelN, String foreignN, String peasantN, String otherN){
        database = FirebaseFirestore.getInstance();
        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "date", date, "eventName", eventN, "totalPerson", totalP, "womenNumber", femaleN, "menNumber", maleN,
                                            "noSpcNumber", noSpcN, "infantNumber", infantN, "childhoodNumber", childhoodN, "teenNumber", teenN, "youthNumber", youthN,
                                            "adultNumber", adultN, "elderlyNumber", elderlyN, "afroNumber", afroN, "nativeNumber", nativeN, "lgtbiNumber", lgtbiN,
                                            "romNumber", romN,"victimNumber", victimN, "disabilityNumber", disabilityN, "demobilizedNumber", demobilizedN,
                                            "mongrelNumber", mongrelN, "foreignNumber", foreignN, "peasantNumber", peasantN, "otherNumber", otherN);
                                    return null;
                                }
                            });
                        }
                    }
                });


    }

    public void editInfoCPS(Context context, String idGarden, String idCollection, String personResp, String duration, String plantsSeeds, String comments, String phase){
        database = FirebaseFirestore.getInstance();
        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "personResponsable", personResp, "phaseDuration", duration, "plants or seeds", plantsSeeds, "commentsObservations", comments,
                                            "processPhase", phase);
                                    return null;
                                }
                            });
                        }
                    }
                });


    }
    public void editInfoIMP(Context context, String idGarden, String idCollection, String rawMaterial, String concept, String movement, String quantityMaterial, String units, String existenceQuantity){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "rawMaterial", rawMaterial, "concept", concept, "movement", movement, "quantityRawMaterial", quantityMaterial,
                                            "units", units, "existenceQuantity", existenceQuantity);
                                    return null;
                                }
                            });
                        }
                    }
                });


    }
    public void editInfoRAC(Context context, String idGarden, String idCollection, String containerSize, String wormsWeight, String humidity, String wasteAmount, String humusCollected, String amountLeached){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "containerSize", containerSize, "wormsWeight", wormsWeight, "humidity", humidity, "amount of waste", wasteAmount,
                                            "collected humus", humusCollected, "amount leached", amountLeached);
                                    return null;
                                }
                            });
                        }
                    }
                });
    }
    public void editInfoRCC(Context context, String idGarden, String idCollection, String areaRecipient, String description, String quantityResidue, String fertilizer, String leached){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "areaRecipient", areaRecipient, "areaDescription", description, "residueQuantity", quantityResidue, "fertilizerQuantity", fertilizer,
                                            "leachedQuantity", leached);
                                    return null;
                                }
                            });
                        }
                    }
                });
    }

    public void editInfoRHC(Context context, String idGarden, String idCollection, String responsable, String code, String itemName, String units, String measurement, String totalCost, String comments, String incomeExpense, String type){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "responsable", responsable, "code", code, "itemName", itemName, "units", units,
                                            "measurement", measurement, "totalCost", totalCost, "comments", comments, "incomeExpense", incomeExpense, "type", type);
                                    return null;
                                }
                            });
                        }
                    }
                });
    }
    public void editInfoRRH(Context context, String idGarden, String idCollection, String description, String quantity, String performedBy, String status, String concept){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "processDescription", description, "toolQuantity", quantity, "performedBy", performedBy, "toolStatus", status,
                                            "concept", concept);
                                    return null;
                                }
                            });
                        }
                    }
                });
    }
    public void editInfoRSMP(Context context, String idGarden, String idCollection, String description, String quantity, String total, String state, String concept, String units){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "description", description, "quantity", quantity, "total", total, "state", state,
                                            "concept", concept, "units", units);
                                    return null;
                                }
                            });
                        }
                    }
                });
    }
    public void editInfoSCMPH(Context context, String idGarden, String idCollection, String itemName, String item, String units, String quantity, String total){
        database = FirebaseFirestore.getInstance();

        database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final DocumentReference docRef = database.collection("Gardens").document(idGarden).collection("Forms").document(idCollection);
                            database.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(docRef, "itemName", itemName, "item", item, "units", units, "quantity", quantity,
                                            "total", total);
                                    return null;
                                }
                            });
                        }
                    }
                });
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

