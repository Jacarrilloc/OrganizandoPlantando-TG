package com.example.opcv.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.opcv.repository.interfaces.OnFormInsertedListener;
import com.example.opcv.repository.localForms.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FormsRepository {
    private final FormDatabase mLocalDatabase;
    private final FirebaseFirestore mFirestore;
    private final Executor mExecutor;
    private final Context mContext;

    public FormsRepository(Context context) {
        mContext = context;
        mLocalDatabase = FormDatabase.getDatabase(context);
        mFirestore = FirebaseFirestore.getInstance();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public void insertFormCIH(final CIH cih, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            if (isOnline) {
                mFirestore.collection("Gardens").document(idGarden).collection("Forms").add(infoForm).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) {
                            listener.onFormInserted(task.getResult().getId());
                        }
                    } else {
                        if (listener != null) {
                            listener.onFormInsertionError(task.getException());
                        }
                    }
                });
            }

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.cihDao().insert(cih);
            });

            // Ejecutar un hilo en segundo plano para verificar la conectividad a Internet
            new Thread(() -> {
                while (!isOnline()) {
                    try {
                        Thread.sleep(1000); // Esperar 1 segundo antes de verificar de nuevo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Si se detecta conectividad, subir los datos a Firebase
                mFirestore.collection("Gardens").document(idGarden).collection("Forms").add(infoForm).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) {
                            listener.onFormInserted(task.getResult().getId());
                        }
                    } else {
                        if (listener != null) {
                            listener.onFormInsertionError(task.getException());
                        }
                    }
                });
            }).start();
        });
    }

    public void insertFormCPS(final CPS cps, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            if (isOnline) {
                mFirestore.collection("Gardens").document(idGarden).collection("Forms").add(infoForm).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) {
                            listener.onFormInserted(task.getResult().getId());
                        }
                    } else {
                        if (listener != null) {
                            listener.onFormInsertionError(task.getException());
                        }
                    }
                });
            }

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.cpsDao().insertCPS(cps);
            });

            // Ejecutar un hilo en segundo plano para verificar la conectividad a Internet
            new Thread(() -> {
                while (!isOnline()) {
                    try {
                        Thread.sleep(1000); // Esperar 1 segundo antes de verificar de nuevo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Si se detecta conectividad, subir los datos a Firebase
                mFirestore.collection("Gardens").document(idGarden).collection("Forms").add(infoForm).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) {
                            listener.onFormInserted(task.getResult().getId());
                        }
                    } else {
                        if (listener != null) {
                            listener.onFormInsertionError(task.getException());
                        }
                    }
                });
            }).start();
        });
    }


    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
