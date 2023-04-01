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
import com.example.opcv.objects.*;
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

    private Map<String,Object> InsertLocalDate(Map<String,Object> i,String idGardenFb){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Se agrega 1 porque el primer mes es 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String date = String.format("%02d/%02d/%d", day, month, year);
        i.put("Date",date);
        i.put("Gardenid",idGardenFb);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        i.put("Time",time);
        return i;
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
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_CPS(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("CPS", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String personResponsable = cursor.getString(cursor.getColumnIndex("personResponsable"));
                    String processPhase = cursor.getString(cursor.getColumnIndex("processPhase"));
                    String phaseDuration = cursor.getString(cursor.getColumnIndex("phaseDuration"));
                    String plantsOrSeeds = cursor.getString(cursor.getColumnIndex("plantsOrSeeds"));
                    String commentsObservations = cursor.getString(cursor.getColumnIndex("commentsObservations"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    CPS_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(CPS_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getPersonResponsable().equals(personResponsable) ||
                                            !registroFirestore.getProcessPhase().equals(processPhase) ||
                                            !registroFirestore.getPhaseDuration().equals(phaseDuration) ||
                                            !registroFirestore.getPlantsOrSeeds().equals(plantsOrSeeds) ||
                                            !registroFirestore.getCommentsObservations().equals(commentsObservations)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "personResponsable", personResponsable,
                                                "processPhase", processPhase,
                                                "phaseDuration", phaseDuration,
                                                "plantsOrSeeds", plantsOrSeeds,
                                                "commentsObservations", commentsObservations
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    CPS_Element registroFirestore = new CPS_Element(
                                            idForm, nameForm, personResponsable, processPhase, phaseDuration,
                                            plantsOrSeeds, commentsObservations);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_IMP(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("IMP", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String personResponsable = cursor.getString(cursor.getColumnIndex("personResponsable"));
                    String processPhase = cursor.getString(cursor.getColumnIndex("processPhase"));
                    String phaseDuration = cursor.getString(cursor.getColumnIndex("phaseDuration"));
                    String plantsOrSeeds = cursor.getString(cursor.getColumnIndex("plantsOrSeeds"));
                    String commentsObservations = cursor.getString(cursor.getColumnIndex("commentsObservations"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    IMP_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(IMP_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getPersonResponsable().equals(personResponsable) ||
                                            !registroFirestore.getProcessPhase().equals(processPhase) ||
                                            !registroFirestore.getPhaseDuration().equals(phaseDuration) ||
                                            !registroFirestore.getPlantsOrSeeds().equals(plantsOrSeeds) ||
                                            !registroFirestore.getCommentsObservations().equals(commentsObservations)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "personResponsable", personResponsable,
                                                "processPhase", processPhase,
                                                "phaseDuration", phaseDuration,
                                                "plantsOrSeeds", plantsOrSeeds,
                                                "commentsObservations", commentsObservations
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    IMP_Element registroFirestore = new IMP_Element(
                                            idForm, nameForm, personResponsable, processPhase, phaseDuration,
                                            plantsOrSeeds, commentsObservations);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_RAC(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("RAC", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String containerSize = cursor.getString(cursor.getColumnIndex("containerSize"));
                    String wormsWeight = cursor.getString(cursor.getColumnIndex("wormsWeight"));
                    String humidity = cursor.getString(cursor.getColumnIndex("humidity"));
                    String amountOfWaste = cursor.getString(cursor.getColumnIndex("amountOfWaste"));
                    String collectedHumus = cursor.getString(cursor.getColumnIndex("collectedHumus"));
                    String amountLeached = cursor.getString(cursor.getColumnIndex("amountLeached"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    RAC_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(RAC_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getContainerSize().equals(containerSize) ||
                                            !registroFirestore.getWormsWeight().equals(wormsWeight) ||
                                            !registroFirestore.getHumidity().equals(humidity) ||
                                            !registroFirestore.getAmountOfWaste().equals(amountOfWaste) ||
                                            !registroFirestore.getCollectedHumus().equals(collectedHumus) ||
                                            !registroFirestore.getAmountLeached().equals(amountLeached)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "containerSize", containerSize,
                                                "wormsWeight", wormsWeight,
                                                "humidity", humidity,
                                                "amountOfWaste", amountOfWaste,
                                                "collectedHumus", collectedHumus,
                                                "amountLeached", amountLeached
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    RAC_Element registroFirestore = new RAC_Element(
                                            idForm, nameForm, containerSize, wormsWeight, humidity,
                                            amountOfWaste, collectedHumus, amountLeached);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_RCC(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("RCC", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String containerSize = cursor.getString(cursor.getColumnIndex("containerSize"));
                    String wormsWeight = cursor.getString(cursor.getColumnIndex("wormsWeight"));
                    String humidity = cursor.getString(cursor.getColumnIndex("humidity"));
                    String amountOfWaste = cursor.getString(cursor.getColumnIndex("amountOfWaste"));
                    String collectedHumus = cursor.getString(cursor.getColumnIndex("collectedHumus"));
                    String amountLeached = cursor.getString(cursor.getColumnIndex("amountLeached"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    RCC_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(RCC_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getLeachedQuantity().equals(containerSize) ||
                                            !registroFirestore.getAreaDescription().equals(wormsWeight) ||
                                            !registroFirestore.getResidueQuantity().equals(humidity) ||
                                            !registroFirestore.getFertilizerQuantity().equals(amountOfWaste) ||
                                            !registroFirestore.getLeachedQuantity().equals(collectedHumus)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "leachedQuantity", containerSize,
                                                "areaDescription", wormsWeight,
                                                "residueQuantity", humidity,
                                                "fertilizerQuantity", amountOfWaste,
                                                "leachedQuantity", collectedHumus
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    RCC_Element registroFirestore = new RCC_Element(idForm, nameForm,containerSize,wormsWeight,humidity,amountOfWaste,collectedHumus);

                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_RE(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean connection = isOnline(context);
        if(connection){
            Cursor cursor = db.query("RE", null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
                    String totalPerson = cursor.getString(cursor.getColumnIndex("totalPerson"));
                    String womenNumber = cursor.getString(cursor.getColumnIndex("womenNumber"));
                    String menNumber = cursor.getString(cursor.getColumnIndex("menNumber"));
                    String noSpcNumber = cursor.getString(cursor.getColumnIndex("noSpcNumber"));
                    String infantNumber = cursor.getString(cursor.getColumnIndex("infantNumber"));
                    String childhoodNumber = cursor.getString(cursor.getColumnIndex("childhoodNumber"));
                    String teenNumber = cursor.getString(cursor.getColumnIndex("teenNumber"));
                    String youthNumber = cursor.getString(cursor.getColumnIndex("youthNumber"));
                    String adultNumber = cursor.getString(cursor.getColumnIndex("adultNumber"));
                    String elderlyNumber = cursor.getString(cursor.getColumnIndex("elderlyNumber"));
                    String afroNumber = cursor.getString(cursor.getColumnIndex("afroNumber"));
                    String nativeNumber = cursor.getString(cursor.getColumnIndex("nativeNumber"));
                    String lgtbiNumber = cursor.getString(cursor.getColumnIndex("lgtbiNumber"));
                    String romNumber = cursor.getString(cursor.getColumnIndex("romNumber"));
                    String victimNumber = cursor.getString(cursor.getColumnIndex("victimNumber"));
                    String disabilityNumber = cursor.getString(cursor.getColumnIndex("disabilityNumber"));
                    String demobilizedNumber = cursor.getString(cursor.getColumnIndex("demobilizedNumber"));
                    String mongrelNumber = cursor.getString(cursor.getColumnIndex("mongrelNumber"));
                    String foreignNumber = cursor.getString(cursor.getColumnIndex("foreignNumber"));
                    String peasantNumber = cursor.getString(cursor.getColumnIndex("peasantNumber"));
                    String otherNumber = cursor.getString(cursor.getColumnIndex("otherNumber"));
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()){
                                    RE_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(RE_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getDate().equals(date) ||
                                            !registroFirestore.getEventName().equals(eventName) ||
                                            !registroFirestore.getTotalPerson().equals(totalPerson) ||
                                            !registroFirestore.getWomenNumber().equals(womenNumber) ||
                                            !registroFirestore.getMenNumber().equals(menNumber) ||
                                            !registroFirestore.getNoSpcNumber().equals(noSpcNumber) ||
                                            !registroFirestore.getInfantNumber().equals(infantNumber) ||
                                            !registroFirestore.getChildhoodNumber().equals(childhoodNumber) ||
                                            !registroFirestore.getTeenNumber().equals(teenNumber) ||
                                            !registroFirestore.getYouthNumber().equals(youthNumber) ||
                                            !registroFirestore.getAdultNumber().equals(adultNumber) ||
                                            !registroFirestore.getElderlyNumber().equals(elderlyNumber) ||
                                            !registroFirestore.getAfroNumber().equals(afroNumber) ||
                                            !registroFirestore.getNativeNumber().equals(nativeNumber) ||
                                            !registroFirestore.getLgtbiNumber().equals(lgtbiNumber) ||
                                            !registroFirestore.getRomNumber().equals(romNumber) ||
                                            !registroFirestore.getVictimNumber().equals(victimNumber) ||
                                            !registroFirestore.getDisabilityNumber().equals(disabilityNumber) ||
                                            !registroFirestore.getDemobilizedNumber().equals(demobilizedNumber) ||
                                            !registroFirestore.getMongrelNumber().equals(mongrelNumber) ||
                                            !registroFirestore.getForeignNumber().equals(foreignNumber) ||
                                            !registroFirestore.getPeasantNumber().equals(peasantNumber) ||
                                            !registroFirestore.getOtherNumber().equals(otherNumber))
                                    {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "date", date,
                                                "eventName", eventName,
                                                "totalPerson", totalPerson,
                                                "womenNumber", womenNumber,
                                                "menNumber", menNumber,
                                                "noSpcNumber", noSpcNumber,
                                                "infantNumber", infantNumber,
                                                "childhoodNumber", childhoodNumber,
                                                "teenNumber", teenNumber,
                                                "youthNumber", youthNumber,
                                                "adultNumber", adultNumber,
                                                "elderlyNumber", elderlyNumber,
                                                "afroNumber", afroNumber,
                                                "nativeNumber", nativeNumber,
                                                "lgtbiNumber", lgtbiNumber,
                                                "romNumber", romNumber,
                                                "victimNumber", victimNumber,
                                                "disabilityNumber", disabilityNumber,
                                                "demobilizedNumber", demobilizedNumber,
                                                "mongrelNumber", mongrelNumber,
                                                "foreignNumber", foreignNumber,
                                                "peasantNumber", peasantNumber,
                                                "otherNumber", otherNumber
                                        );
                                    }
                                } else {
                                    RE_Element registroFirestore = new RE_Element(idForm,nameForm,date,eventName,totalPerson,womenNumber,menNumber,noSpcNumber,infantNumber,childhoodNumber,teenNumber,youthNumber,adultNumber,elderlyNumber,afroNumber,nativeNumber,lgtbiNumber,romNumber,victimNumber,disabilityNumber,demobilizedNumber,mongrelNumber,foreignNumber,peasantNumber,otherNumber);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
                                    usersCollection.add(infoForm);
                                }
                            } else {
                                Log.d("Off_On", "Error al obtener el registro de Firestore", task.getException());
                            }
                        }
                    });
                } while (cursor.moveToNext());
            }
        }
    }

    public void syncFirestore_RRH(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("RRH", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    int toolQuantity = cursor.getInt(cursor.getColumnIndex("toolQuantity"));
                    String concept = cursor.getString(cursor.getColumnIndex("concept"));
                    String performedBy = cursor.getString(cursor.getColumnIndex("performedBy"));
                    String toolStatus = cursor.getString(cursor.getColumnIndex("toolStatus"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    RRH_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(RRH_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getDescription().equals(description) ||
                                            registroFirestore.getToolQuantity() != String.valueOf(toolQuantity) ||
                                            !registroFirestore.getConcept().equals(concept) ||
                                            !registroFirestore.getPerformedBy().equals(performedBy) ||
                                            !registroFirestore.getToolStatus().equals(toolStatus)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "description", description,
                                                "toolQuantity", toolQuantity,
                                                "concept", concept,
                                                "performedBy", performedBy,
                                                "toolStatus", toolStatus
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    RRH_Element registroFirestore = new RRH_Element(
                                            idForm, nameForm, description, String.valueOf(toolQuantity), concept,
                                            performedBy, toolStatus);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_SCMPH(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("SCMPH", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String itemName = cursor.getString(cursor.getColumnIndex("itemName"));
                    String item = cursor.getString(cursor.getColumnIndex("item"));
                    String units = cursor.getString(cursor.getColumnIndex("units"));
                    int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                    double total = cursor.getDouble(cursor.getColumnIndex("total"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    SCMPH_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(SCMPH_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getItemName().equals(itemName) ||
                                            !registroFirestore.getItem().equals(item) ||
                                            !registroFirestore.getUnits().equals(units) ||
                                            registroFirestore.getQuantity() != quantity ||
                                            registroFirestore.getTotal() != String.valueOf(total)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "itemName", itemName,
                                                "item", item,
                                                "units", units,
                                                "quantity", quantity,
                                                "total", total
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    SCMPH_Element registroFirestore = new SCMPH_Element(
                                            idForm, nameForm, itemName, item, units,
                                            String.valueOf(quantity), (int) total);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_RSMP(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean connection = isOnline(context);
        if (connection) {
            Cursor cursor = db.query("RSMP", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String units = cursor.getString(cursor.getColumnIndex("units"));
                    int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                    int total = cursor.getInt(cursor.getColumnIndex("total"));
                    String concept = cursor.getString(cursor.getColumnIndex("concept"));
                    String state = cursor.getString(cursor.getColumnIndex("state"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    RSMP_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(RSMP_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getDescription().equals(description) ||
                                            !registroFirestore.getUnits().equals(units) ||
                                            registroFirestore.getQuantity() != quantity ||
                                            registroFirestore.getTotal() != total ||
                                            !registroFirestore.getConcept().equals(concept) ||
                                            !registroFirestore.getState().equals(state)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "description", description,
                                                "units", units,
                                                "quantity", quantity,
                                                "total", total,
                                                "concept", concept,
                                                "state", state
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    RSMP_Element registroFirestore = new RSMP_Element(
                                            idForm, nameForm, description, units, quantity,
                                            total, concept, state);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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

    public void syncFirestore_RHC(String idGardenFb) {
        DB_User info = new DB_User(context);
        SQLiteDatabase db = context.openOrCreateDatabase("database_Offline_Forms.db", Context.MODE_PRIVATE, null);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = dbFirestore.collection("Gardens").document(idGardenFb).collection("Forms");
        boolean conection = isOnline(context);
        if (conection) {
            Cursor cursor = db.query("RHC", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idFormDatabase = cursor.getInt(cursor.getColumnIndex("ID_Form_database"));
                    int idForm = cursor.getInt(cursor.getColumnIndex("idForm"));
                    String nameForm = cursor.getString(cursor.getColumnIndex("nameForm"));
                    String responsable = cursor.getString(cursor.getColumnIndex("responsable"));
                    String incomeExpense = cursor.getString(cursor.getColumnIndex("incomeExpense"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String code = cursor.getString(cursor.getColumnIndex("code"));
                    String itemName = cursor.getString(cursor.getColumnIndex("itemName"));
                    String measurement = cursor.getString(cursor.getColumnIndex("measurement"));
                    int totalCost = cursor.getInt(cursor.getColumnIndex("totalCost"));
                    String comments = cursor.getString(cursor.getColumnIndex("comments"));
                    String units = cursor.getString(cursor.getColumnIndex("units"));
                    String state = cursor.getString(cursor.getColumnIndex("state"));

                    // Consulta el registro en Firestore
                    Query query = usersCollection.whereEqualTo("ID_Form_database", idFormDatabase);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    // El registro existe en Firestore, compara los valores
                                    RHC_Element registroFirestore = querySnapshot.getDocuments().get(0).toObject(RHC_Element.class);
                                    if (registroFirestore.getIdForm() != idForm ||
                                            !registroFirestore.getNameForm().equals(nameForm) ||
                                            !registroFirestore.getResponsable().equals(responsable) ||
                                            !registroFirestore.getIncomeExpense().equals(incomeExpense) ||
                                            !registroFirestore.getType().equals(type) ||
                                            !registroFirestore.getCode().equals(code) ||
                                            !registroFirestore.getItemName().equals(itemName) ||
                                            !registroFirestore.getMeasurement().equals(measurement) ||
                                            registroFirestore.getTotalCost() != totalCost ||
                                            !registroFirestore.getComments().equals(comments) ||
                                            !registroFirestore.getUnits().equals(units) ||
                                            !registroFirestore.getState().equals(state)) {
                                        // El registro ha cambiado, actualiza en Firestore
                                        usersCollection.document(String.valueOf(idFormDatabase)).update(
                                                "idForm", idForm,
                                                "nameForm", nameForm,
                                                "responsable", responsable,
                                                "incomeExpense", incomeExpense,
                                                "type", type,
                                                "code", code,
                                                "itemName", itemName,
                                                "measurement", measurement,
                                                "totalCost", totalCost,
                                                "comments", comments,
                                                "units", units,
                                                "state", state
                                        );
                                    }
                                } else {
                                    // El registro no existe en Firestore, agrégalo
                                    RHC_Element registroFirestore = new RHC_Element(
                                            idForm, nameForm, responsable, incomeExpense, type,
                                            code, itemName, measurement, totalCost, comments, units, state);
                                    Map<String,Object> infoForm = registroFirestore.toMap();
                                    infoForm = InsertLocalDate(infoForm,idGardenFb);
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
}