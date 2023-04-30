package com.example.opcv.business.persistance.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.opcv.business.notifications.Notifications;
import com.example.opcv.business.persistance.repository.interfaces.OnFormInsertedListener;
import com.example.opcv.business.persistance.repository.localForms.CIH;
import com.example.opcv.business.persistance.repository.localForms.CPS;
import com.example.opcv.business.persistance.repository.localForms.IMP;
import com.example.opcv.business.persistance.repository.localForms.RAC;
import com.example.opcv.business.persistance.repository.localForms.RCC;
import com.example.opcv.business.persistance.repository.localForms.RE;
import com.example.opcv.business.persistance.repository.localForms.RHC;
import com.example.opcv.business.persistance.repository.localForms.RRH;
import com.example.opcv.business.persistance.repository.localForms.RSMP;
import com.example.opcv.business.persistance.repository.localForms.SCMPH;
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

    public void insertFormCIH(Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Crear la base de datos local si no existe
            LocalDatabase localDb = new LocalDatabase(mContext);
            localDb.createJsonForm(idGarden,infoForm);

            // Insertar en la base de datos local siempre
/*
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
                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);

            }).start();
            */
        });
    }

    public void insertFormCPS(final CPS cps, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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
            }*/

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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormIMP(final IMP imp, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.impDao().insert(imp);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormRAC(final RAC rac, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*if (isOnline) {
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
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.racDao().insert(rac);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormRCC(final RCC rcc, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.rccDao().insert(rcc);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormRE(final RE re, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.reDao().insertForm(re);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormRHC(final RHC rhc, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.rhcDao().insert(rhc);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormRRH(final RRH rrh, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.rrhDao().insert(rrh);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormRSMP(final RSMP rsmp, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.rsmpDao().insert(rsmp);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
            }).start();
        });
    }

    public void insertFormSCMPH(final SCMPH scmph, Map<String,Object> infoForm, String idGarden, final OnFormInsertedListener listener) {
        mExecutor.execute(() -> {
            // Comprobar la conectividad a Internet
            boolean isOnline = isOnline();
            /*
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
            }*/

            // Insertar en la base de datos local siempre
            mLocalDatabase.runInTransaction(() -> {
                mLocalDatabase.scmphDao().insert(scmph);
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

                Notifications notifications = new Notifications();
                notifications.notification("Formulario creado", "Tienes Conexion, Formulario Subido", mContext);
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
