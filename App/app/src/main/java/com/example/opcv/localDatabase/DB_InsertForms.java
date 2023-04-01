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

    public long insertInto_CIH(Map<String, Object> info) {
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
        long i = db.insert(TABLE_CIH, null, values);
        db.close();
        return i;
    }

    public void insertInto_CPS(Map<String, Object> Info){
        DatabaseFormsHelper dbHelper = new DatabaseFormsHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Info.get("idForm").toString());
        values.put("nameForm", Info.get("nameForm").toString());
        values.put("personResponsable", Info.get("personResponsable").toString());
        values.put("processPhase", Info.get("processPhase").toString());
        values.put("phaseDuration", Info.get("phaseDuration").toString());
        values.put("plantsOrSeeds", Info.get("plantsOrSeeds").toString());
        values.put("commentsObservations", Info.get("commentsObservations").toString());
        db.insert(TABLE_CPS, null, values);
        db.close();
    }

    public void insertInto_IMP(Map<String, Object> info){
        DatabaseFormsHelper dbHelper = new DatabaseFormsHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", info.get("idForm").toString());
        values.put("nameForm", info.get("nameForm").toString());
        values.put("personResponsable", info.get("personResponsable").toString());
        values.put("processPhase", info.get("processPhase").toString());
        values.put("phaseDuration", info.get("phaseDuration").toString());
        values.put("plantsOrSeeds", info.get("plantsOrSeeds").toString());
        values.put("commentsObservations", info.get("commentsObservations").toString());
        db.insert(TABLE_IMP, null, values);
        db.close();
    }

    public void insertInto_RAC(Map<String, Object> Info){
        DatabaseFormsHelper dbHelper = new DatabaseFormsHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Info.get("idForm").toString());
        values.put("nameForm", Info.get("nameForm").toString());
        values.put("containerSize", Info.get("containerSize").toString());
        values.put("wormsWeight", Info.get("wormsWeight").toString());
        values.put("humidity", Info.get("humidity").toString());
        values.put("amountOfWaste", Info.get("amountOfWaste").toString());
        values.put("collectedHumus", Info.get("collectedHumus").toString());
        values.put("amountLeached", Info.get("amountLeached").toString());
        db.insert(TABLE_RAC, null, values);
        db.close();
    }


    public void insertInto_RCC(Map<String, Object> Info){
        DatabaseFormsHelper dbHelper = new DatabaseFormsHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Info.get("idForm").toString());
        values.put("nameForm", Info.get("nameForm").toString());
        values.put("containerSize", Info.get("containerSize").toString());
        values.put("wormsWeight", Info.get("wormsWeight").toString());
        values.put("humidity", Info.get("humidity").toString());
        values.put("amountOfWaste", Info.get("amountOfWaste").toString());
        values.put("collectedHumus", Info.get("collectedHumus").toString());
        values.put("amountLeached", Info.get("amountLeached").toString());
        db.insert(TABLE_RCC, null, values);
        db.close();
    }

    public void insertInto_RE(Map<String, Object> Info){
        DatabaseFormsHelper info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", (Integer) Info.get("idForm"));
        values.put("nameForm", (String) Info.get("nameForm"));
        values.put("date", (String) Info.get("date"));
        values.put("eventName", (String) Info.get("eventName"));
        values.put("totalPerson", (Integer) Info.get("totalPerson"));
        values.put("womenNumber", (Integer) Info.get("womenNumber"));
        values.put("menNumber", (Integer) Info.get("menNumber"));
        values.put("noSpcNumber", (Integer) Info.get("noSpcNumber"));
        values.put("infantNumber", (Integer) Info.get("infantNumber"));
        values.put("childhoodNumber", (Integer) Info.get("childhoodNumber"));
        values.put("teenNumber", (Integer) Info.get("teenNumber"));
        values.put("youthNumber", (Integer) Info.get("youthNumber"));
        values.put("adultNumber", (Integer) Info.get("adultNumber"));
        values.put("elderlyNumber", (Integer) Info.get("elderlyNumber"));
        values.put("afroNumber", (Integer) Info.get("afroNumber"));
        values.put("nativeNumber", (Integer) Info.get("nativeNumber"));
        values.put("lgtbiNumber", (Integer) Info.get("lgtbiNumber"));
        values.put("romNumber", (Integer) Info.get("romNumber"));
        values.put("victimNumber", (Integer) Info.get("victimNumber"));
        values.put("disabilityNumber", (Integer) Info.get("disabilityNumber"));
        values.put("demobilizedNumber", (Integer) Info.get("demobilizedNumber"));
        values.put("mongrelNumber", (Integer) Info.get("mongrelNumber"));
        values.put("foreignNumber", (Integer) Info.get("foreignNumber"));
        values.put("peasantNumber", (Integer) Info.get("peasantNumber"));
        values.put("otherNumber", (Integer) Info.get("otherNumber"));
        db.insert(TABLE_RE, null, values);
        db.close();
    }


    public void insertInto_RHC(Map<String, Object> Info){
        DatabaseFormsHelper info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", Info.get("idForm").toString());
        values.put("nameForm", Info.get("nameForm").toString());
        values.put("responsable", Info.get("responsable").toString());
        values.put("incomeExpense", Info.get("incomeExpense").toString());
        values.put("type", Info.get("type").toString());
        values.put("code", Info.get("code").toString());
        values.put("itemName", Info.get("itemName").toString());
        values.put("measurement", Info.get("measurement").toString());
        values.put("totalCost", Info.get("totalCost").toString());
        values.put("comments", Info.get("comments").toString());
        values.put("units", Info.get("units").toString());
        values.put("state", Info.get("state").toString());
        db.insert(TABLE_RHC, null, values);
        db.close();
    }

    public void insertInto_RRH(Map<String, Object> Info){
        DatabaseFormsHelper info = new DatabaseFormsHelper(context);
        SQLiteDatabase db = info.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", (Integer)Info.get("idForm"));
        values.put("nameForm", (String)Info.get("nameForm"));
        values.put("description", (String)Info.get("description"));
        values.put("toolQuantity", (Integer)Info.get("toolQuantity"));
        values.put("concept", (String)Info.get("concept"));
        values.put("performedBy", (String)Info.get("performedBy"));
        values.put("toolStatus", (String)Info.get("toolStatus"));
        db.insert(TABLE_RRH, null, values);
        db.close();
    }

    public void insertInto_SCMPH(Map<String, Object> info) {
        DatabaseFormsHelper dbHelper = new DatabaseFormsHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idForm", (int) info.get("idForm"));
        values.put("nameForm", (String) info.get("nameForm"));
        values.put("itemName", (String) info.get("itemName"));
        values.put("item", (String) info.get("item"));
        values.put("units", (String) info.get("units"));
        values.put("quantity", (int) info.get("quantity"));
        values.put("total", (double) info.get("total"));
        db.insert(TABLE_SCMPH, null, values);
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