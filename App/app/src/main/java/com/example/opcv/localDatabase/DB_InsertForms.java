package com.example.opcv.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.Map;

public class DB_InsertForms extends DatabaseFormsHelper {

    Context context;

    private static final String TABLE_CIH = "CIH";
    private static final String TABLE_CPS = "CPS";
    private static final String TABLE_IMP = "IMP";
    private static final String TABLE_RAC = "RAC";
    private static final String TABLE_RCC = "RCC";
    private static final String TABLE_RE = "RE";
    private static final String TABLE_RHC = "RHC";
    private static final String TABLE_RRH = "RRH";
    private static final String TABLE_RSMP = "RSMP";
    private static final String TABLE_SCMPH = "SCMPH";

    public DB_InsertForms(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public void insertInto_CIH(Map<String, Object> info) {
        DatabaseFormsHelper Info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = Info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("tool", info.get("tool").toString());
        values.put("concept", info.get("concept").toString());
        values.put("incomingOutgoing", info.get("incomingOutgoing").toString());
        values.put("toolQuantity", Integer.parseInt(info.get("toolQuantity").toString()));
        values.put("toolStatus", info.get("toolStatus").toString());
        values.put("existenceQuantity", Integer.parseInt(info.get("existenceQuantity").toString()));
        db.insert(TABLE_CIH, null, values);
        db.close();
    }

    public void insertInto_CPS(Map<String, Object> info) {
        DatabaseFormsHelper Info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = Info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("personResponsable", info.get("personResponsable").toString());
        values.put("processPhase", info.get("processPhase").toString());
        values.put("phaseDuration", info.get("phaseDuration").toString());
        values.put("plantsOrSeeds", info.get("plants or seeds").toString());
        values.put("commentsObservations", info.get("commentsObservations").toString());
        db.insert(TABLE_CPS, null, values);
        db.close();
    }

    public void insertInto_IMP(Map<String, Object> info) {
        DatabaseFormsHelper Info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = Info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("personResponsable", info.get("personResponsable").toString());
        values.put("processPhase", info.get("processPhase").toString());
        values.put("phaseDuration", info.get("phaseDuration").toString());
        values.put("plantsOrSeeds", info.get("plantsOrSeeds").toString());
        values.put("commentsObservations", info.get("commentsObservations").toString());
        db.insert(TABLE_IMP, null, values);
        db.close();
    }

    public void insertInto_RAC(Map<String, Object> info) {
        DatabaseFormsHelper Info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = Info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("containerSize", info.get("containerSize").toString());
        values.put("wormsWeight", info.get("wormsWeight").toString());
        values.put("humidity", info.get("humidity").toString());
        values.put("amountOfWaste", info.get("amountOfWaste").toString());
        values.put("collectedHumus", info.get("collectedHumus").toString());
        values.put("amountLeached", info.get("amountLeached").toString());
        db.insert(TABLE_RAC, null, values);
        db.close();
    }

    public void insertInto_RCC(Map<String, Object> info) {
        DatabaseFormsHelper infoHelper = new DatabaseFormsHelper(context);
        SQLiteDatabase db = infoHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("containerSize", info.get("containerSize").toString());
        values.put("wormsWeight", info.get("wormsWeight").toString());
        values.put("humidity", info.get("humidity").toString());
        values.put("amountOfWaste", info.get("amountOfWaste").toString());
        values.put("collectedHumus", info.get("collectedHumus").toString());
        values.put("amountLeached", info.get("amountLeached").toString());
        db.insert(TABLE_RCC, null, values);
        db.close();
    }

    public void insertInto_RE(Map<String, Object> info) {
        DatabaseFormsHelper Info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = Info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("date", info.get("date").toString());
        values.put("eventName", info.get("eventName").toString());
        values.put("totalPerson", Integer.parseInt(info.get("totalPerson").toString()));
        values.put("womenNumber", Integer.parseInt(info.get("womenNumber").toString()));
        values.put("menNumber", Integer.parseInt(info.get("menNumber").toString()));
        values.put("noSpcNumber", Integer.parseInt(info.get("noSpcNumber").toString()));
        values.put("infantNumber", Integer.parseInt(info.get("infantNumber").toString()));
        values.put("childhoodNumber", Integer.parseInt(info.get("childhoodNumber").toString()));
        values.put("teenNumber", Integer.parseInt(info.get("teenNumber").toString()));
        values.put("youthNumber", Integer.parseInt(info.get("youthNumber").toString()));
        values.put("adultNumber", Integer.parseInt(info.get("adultNumber").toString()));
        values.put("elderlyNumber", Integer.parseInt(info.get("elderlyNumber").toString()));
        values.put("afroNumber", Integer.parseInt(info.get("afroNumber").toString()));
        values.put("nativeNumber", Integer.parseInt(info.get("nativeNumber").toString()));
        values.put("lgtbiNumber", Integer.parseInt(info.get("lgtbiNumber").toString()));
        values.put("romNumber", Integer.parseInt(info.get("romNumber").toString()));
        values.put("victimNumber", Integer.parseInt(info.get("victimNumber").toString()));
        values.put("disabilityNumber", Integer.parseInt(info.get("disabilityNumber").toString()));
        values.put("demobilizedNumber", Integer.parseInt(info.get("demobilizedNumber").toString()));
        values.put("mongrelNumber", Integer.parseInt(info.get("mongrelNumber").toString()));
        values.put("foreignNumber", Integer.parseInt(info.get("foreignNumber").toString()));
        values.put("peasantNumber", Integer.parseInt(info.get("peasantNumber").toString()));
        values.put("otherNumber", Integer.parseInt(info.get("otherNumber").toString()));
        db.insert(TABLE_RE, null, values);
        db.close();
    }

    public void insertInto_RHC(Map<String, Object> info) {
        DatabaseFormsHelper dbHelper = new DatabaseFormsHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("responsable", info.get("responsable").toString());
        values.put("incomeExpense", info.get("incomeExpense").toString());
        values.put("type", info.get("type").toString());
        values.put("code", info.get("code").toString());
        values.put("itemName", info.get("itemName").toString());
        values.put("measurement", info.get("measurement").toString());
        values.put("totalCost", Integer.parseInt(info.get("totalCost").toString()));
        values.put("comments", info.get("comments").toString());
        values.put("units", info.get("units").toString());
        values.put("state", info.get("state").toString());
        db.insert(TABLE_RHC, null, values);
        db.close();
    }

    public void insertInto_RRH(Map<String, Object> info) {
        DatabaseFormsHelper Info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = Info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameFormRRH", info.get("nameForm").toString());
        values.put("description", info.get("description").toString());
        values.put("toolQuantityRRH", Integer.parseInt(info.get("toolQuantity").toString()));
        values.put("concept", info.get("concept").toString());
        values.put("performedBy", info.get("performedBy").toString());
        values.put("toolStatus", info.get("toolStatus").toString());
        db.insert(TABLE_RRH, null, values);
        db.close();
    }

    public void insertInto_SCMPH(Map<String, Object> info) {
        // Obtener instancia de la base de datos y crear objeto ContentValues
        DatabaseFormsHelper Info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = Info.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Agregar los valores de los campos a ContentValues
        values.put("idForm", Integer.parseInt(info.get("idForm").toString()));
        values.put("nameForm", info.get("nameForm").toString());
        values.put("itemName", info.get("itemName").toString());
        values.put("item", info.get("item").toString());
        values.put("units", info.get("units").toString());
        values.put("quantity", Integer.parseInt(info.get("quantity").toString()));
        values.put("total", Double.parseDouble(info.get("total").toString()));

        // Insertar los valores en la tabla
        db.insert(TABLE_SCMPH, null, values);

        // Cerrar la conexión de la base de datos
        db.close();
    }


    public void insertInto_RSMP(Map<String, Object> Info) {
        DatabaseFormsHelper info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", (int) Info.get("idForm"));
        values.put("nameForm", (String) Info.get("nameForm"));
        values.put("description", (String) Info.get("description"));
        values.put("units", (String) Info.get("units"));
        values.put("quantity", (int) Info.get("quantity"));
        values.put("total", (int) Info.get("total"));
        values.put("concept", (String) Info.get("concept"));
        values.put("state", (String) Info.get("state"));
        db.insert(TABLE_RSMP, null, values);
        db.close();
    }
}