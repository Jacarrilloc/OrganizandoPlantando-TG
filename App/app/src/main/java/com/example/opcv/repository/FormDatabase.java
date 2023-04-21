package com.example.opcv.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.opcv.repository.localForms.*;
import com.example.opcv.repository.localDaoForms.*;

@Database(entities = {CIH.class, CPS.class, IMP.class, RAC.class, RCC.class, RE.class, RHC.class, RRH.class, RSMP.class, SCMPH.class}, version = 1)
public abstract class FormDatabase extends RoomDatabase {

    // Define aquí tus Dao para acceder a las tablas de la base de datos
    public abstract CIHDao cihDao();
    public abstract CPSDao cpsDao();
    public abstract IMPDao impDao();
    public abstract RACDao racDao();
    public abstract RCCDao rccDao();
    public abstract REDao reDao();
    public abstract RHCDao rhcDao();
    public abstract RRHDao rrhDao();
    public abstract RSMPDao rsmpDao();
    public abstract SCMPHDao scmphDao();

    // Define aquí una instancia estática de tu base de datos
    private static volatile FormDatabase INSTANCE;

    public static FormDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FormDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FormDatabase.class, "form_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

