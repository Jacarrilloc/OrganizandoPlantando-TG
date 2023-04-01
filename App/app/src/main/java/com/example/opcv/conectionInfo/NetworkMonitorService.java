package com.example.opcv.conectionInfo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opcv.fbComunication.AuthUtilities;
import com.example.opcv.localDatabase.DB_User;
import com.example.opcv.localDatabase.DatabaseHelper;
import com.example.opcv.objects.CIH_Element;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class NetworkMonitorService extends Service {

    private DatabaseHelper dbHelper;
    private String UserID;
    Context context;
    private ConnectivityManager connectivityManager;
    private BroadcastReceiver networkReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            // Comprueba si hay conexión a Internet
            if (isOnline(context)) {
                // Actualiza los datos de Firestore a partir de los datos de SQLite
                syncFirestoreWithSQLite();
            }
        }
    };

    public NetworkMonitorService(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Obtiene una instancia del sistema de conectividad
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Registra un receptor de difusión para detectar cambios en la conexión a Internet
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Anula el registro del receptor de difusión
        unregisterReceiver(networkReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void syncFirestore_CIH(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("CIH", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String tool = cursor.getString(cursor.getColumnIndex("tool"));
                    String concept = cursor.getString(cursor.getColumnIndex("concept"));
                    String incomingOutgoing = cursor.getString(cursor.getColumnIndex("incomingOutgoing"));
                    int toolQuantity = cursor.getInt(cursor.getColumnIndex("toolQuantity"));
                    String toolStatus = cursor.getString(cursor.getColumnIndex("toolStatus"));
                    int existenceQuantity = cursor.getInt(cursor.getColumnIndex("existenceQuantity"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    CIH_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(CIH_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getTool().equals(tool) ||
                                            !registroFirestore.getConcept().equals(concept) ||
                                            !registroFirestore.getIncomingOutgoing().equals(incomingOutgoing) ||
                                            registroFirestore.getToolQuantity() != toolQuantity ||
                                            !registroFirestore.getToolStatus().equals(toolStatus) ||
                                            registroFirestore.getExistenceQuantity() != existenceQuantity) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "tool", tool,
                                                "concept", concept,
                                                "incomingOutgoing", incomingOutgoing,
                                                "toolQuantity", toolQuantity,
                                                "toolStatus", toolStatus,
                                                "existenceQuantity", existenceQuantity
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    CIH_Element registroFirestore = new CIH_Element(
                                            idForm, nameForm, tool, concept, incomingOutgoing,
                                            toolQuantity, toolStatus, existenceQuantity);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
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

                                    usersCollection.add(infoForm);
                                }
                            } else {
                                Log.d("Off_On", "Error al obtener el registro de Firestore", task.getException());
                            }
                        }
                    });
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }


    private void syncFirestoreWithSQLite() {

        /*

        // Obtener una instancia de la clase DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Obtener una instancia de FirebaseFirestore
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();

        // Obtener la colección "UserInfo" de Firestore
        CollectionReference usersCollection = dbFirestore.collection("UserInfo");

        // Obtener el estado de conexión a internet
        boolean isNetworkConnected = isOnline();

        // Si hay conexión a internet
        if (isNetworkConnected) {
            // Obtener todos los documentos de la colección "UserInfo" de Firestore
            usersCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    // Para cada documento de la colección
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Obtener el ID del documento
                        String documentId = document.getId();

                        // Obtener los datos del documento en un Map
                        Map<String, Object> userInfo = document.getData();

                        // Actualizar la tabla "UserInfo" en SQLite con los datos del Map
                        dbHelper.updateUserInfo(userInfo);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Persistence", "Error getting documents.", e);
                }
            });
        } else { // Si no hay conexión a internet
            // Obtener el ID del usuario actual
            AuthUtilities info = new AuthUtilities();
            String id = info.getCurrentUserUid();

            // Obtener los datos del usuario actual en la tabla "UserInfo" de SQLite
            Map<String, Object> userInfo = dbHelper.getUserInfo(id);

            // Si los datos no son nulos
            if (userInfo != null) {
                // Actualizar el documento correspondiente en la colección "UserInfo" de Firestore
                usersCollection.document(id).set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Persistence", "DocumentSnapshot successfully written!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Persistence", "Error writing document", e);
                    }
                });
            }
        }
    */}
}

