package com.example.opcv.localDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseFormsHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database_Offline_Forms.db";
    private static final int DATABASE_VERSION = 1;

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

    String createTable_CIH = "CREATE TABLE " + TABLE_CIH +"(" +
            "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idForm INTEGER," +
            "nameForm TEXT," +
            "tool TEXT," +
            "concept TEXT," +
            "incomingOutgoing TEXT," +
            "toolQuantity INTEGER," +
            "toolStatus TEXT," +
            "existenceQuantity INTEGER" +
            ")";

    String createTable_CPS = "CREATE TABLE " + TABLE_CPS + "(" +
            "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idForm INTEGER," +
            "nameForm TEXT," +
            "personResponsable TEXT," +
            "processPhase TEXT," +
            "phaseDuration TEXT," +
            "plantsOrSeeds TEXT," +
            "commentsObservations TEXT" +
            ")";

    String createTable_IMP = "CREATE TABLE " + TABLE_IMP + "(" +
            "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idForm INTEGER," +
            "nameForm TEXT," +
            "personResponsable TEXT," +
            "processPhase TEXT," +
            "phaseDuration TEXT," +
            "plantsOrSeeds TEXT," +
            "commentsObservations TEXT" +
            ")";

    String createTable_RAC = "CREATE TABLE " + TABLE_RAC + "(" +
            "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idForm INTEGER," +
            "nameForm TEXT," +
            "containerSize TEXT," +
            "wormsWeight TEXT," +
            "humidity TEXT," +
            "amountOfWaste TEXT," +
            "collectedHumus TEXT," +
            "amountLeached TEXT" +
            ")";

    String createTable_RCC = "CREATE TABLE " + TABLE_RCC + "(" +
            "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idForm INTEGER," +
            "nameForm TEXT," +
            "areaRecipient TEXT," +
            "areaDescription TEXT," +
            "residueQuantity TEXT," +
            "fertilizerQuantity TEXT," +
            "leachedQuantity TEXT" +")";

    String createTable_RE = "CREATE TABLE IF NOT EXISTS "+ TABLE_RE +"(" +
            "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idForm INTEGER," +
            "nameForm TEXT," +
            "date TEXT," +
            "eventName TEXT," +
            "totalPerson INTEGER," +
            "womenNumber INTEGER," +
            "menNumber INTEGER," +
            "noSpcNumber INTEGER," +
            "infantNumber INTEGER," +
            "childhoodNumber INTEGER," +
            "teenNumber INTEGER," +
            "youthNumber INTEGER," +
            "adultNumber INTEGER," +
            "elderlyNumber INTEGER," +
            "afroNumber INTEGER," +
            "nativeNumber INTEGER," +
            "lgtbiNumber INTEGER," +
            "romNumber INTEGER," +
            "victimNumber INTEGER," +
            "disabilityNumber INTEGER," +
            "demobilizedNumber INTEGER," +
            "mongrelNumber INTEGER," +
            "foreignNumber INTEGER," +
            "peasantNumber INTEGER," +
            "otherNumber INTEGER" +
            ")";

    String createTable_RRH = "CREATE TABLE " + TABLE_RRH +"("
            + "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "idForm INTEGER,"
            + "nameForm TEXT,"
            + "description TEXT,"
            + "toolQuantity INTEGER,"
            + "concept TEXT,"
            + "performedBy TEXT,"
            + "toolStatus TEXT)";

    String createTable_SCMPH = "CREATE TABLE IF NOT EXISTS " + TABLE_SCMPH +
            " (ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "idForm INTEGER,"
            + "nameForm TEXT,"
            + "itemName TEXT,"
            + "item TEXT,"
            + "units TEXT,"
            + "quantity INTEGER,"
            + "total REAL)";

    String createTable_RSMP = "CREATE TABLE IF NOT EXISTS " + TABLE_RSMP + " ("
            + "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "idForm INTEGER, "
            + "nameForm TEXT, "
            + "description TEXT, "
            + "units TEXT, "
            + "quantity INTEGER, "
            + "total INTEGER, "
            + "concept TEXT, "
            + "state TEXT)";

    String createTable_RHC = "CREATE TABLE IF NOT EXISTS " + TABLE_RHC + " ("
            + "ID_Form_database INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "idForm INTEGER, "
            + "nameForm TEXT, "
            + "responsable TEXT, "
            + "incomeExpense TEXT, "
            + "type TEXT, "
            + "code TEXT, "
            + "itemName TEXT, "
            + "measurement TEXT, "
            + "totalCost INTEGER, "
            + "comments TEXT, "
            + "units TEXT, "
            + "state TEXT)";

    public DatabaseFormsHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable_CIH);
        sqLiteDatabase.execSQL(createTable_CPS);
        sqLiteDatabase.execSQL(createTable_IMP);
        sqLiteDatabase.execSQL(createTable_RAC);
        sqLiteDatabase.execSQL(createTable_RCC);
        sqLiteDatabase.execSQL(createTable_RE);
        sqLiteDatabase.execSQL(createTable_RHC);
        sqLiteDatabase.execSQL(createTable_RRH);
        sqLiteDatabase.execSQL(createTable_RSMP);
        sqLiteDatabase.execSQL(createTable_SCMPH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Elimina las tablas antiguas si existen
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CIH); 
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RAC);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RCC);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RHC);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RRH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RSMP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SCMPH);

        // Crea las tablas nuevas
        sqLiteDatabase.execSQL(createTable_CIH);
        sqLiteDatabase.execSQL(createTable_CPS);
        sqLiteDatabase.execSQL(createTable_IMP);
        sqLiteDatabase.execSQL(createTable_RAC);
        sqLiteDatabase.execSQL(createTable_RCC);
        sqLiteDatabase.execSQL(createTable_RE);
        sqLiteDatabase.execSQL(createTable_RHC);
        sqLiteDatabase.execSQL(createTable_RRH);
        sqLiteDatabase.execSQL(createTable_RSMP);
        sqLiteDatabase.execSQL(createTable_SCMPH);
    }

    public boolean checkDatabaseExists(Context context) {
        SQLiteDatabase checkDB = null;
        try {
            String databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
            checkDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // la base de datos no existe todavía
        }
        return checkDB != null;
    }

    public boolean allTablesExist() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add(TABLE_CIH);
        tableNames.add(TABLE_CPS);
        tableNames.add(TABLE_IMP);
        tableNames.add(TABLE_RAC);
        tableNames.add(TABLE_RCC);
        tableNames.add(TABLE_RE);
        tableNames.add(TABLE_RHC);
        tableNames.add(TABLE_RRH);
        tableNames.add(TABLE_RSMP);
        tableNames.add(TABLE_SCMPH);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                if (tableNames.contains(tableName)) {
                    count++;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count == tableNames.size();
    }

}
