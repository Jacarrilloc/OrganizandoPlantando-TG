package com.example.opcv.model.persistance.repository.remote_db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.opcv.model.persistance.repository.local_db.LocalDatabase;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ResultAsyncTask extends AsyncTask<Void, Void, List<Map<String,Object>>> {
    private String idGarden;
    private String formName;
    private Context mContext;
    private OnResultLoadedListener mListener;

    public ResultAsyncTask(String idGarden, String formName, Context context, OnResultLoadedListener listener) {
        this.idGarden = idGarden;
        this.formName = formName;
        mContext = context;
        mListener = listener;
    }

    @Override
    protected List<Map<String, Object>> doInBackground(Void... voids) {
        List<Map<String, Object>> result = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference formsCollection = db.collection("Gardens").document(idGarden).collection("Forms");

        try {
            QuerySnapshot querySnapshot = Tasks.await(formsCollection.get());

            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Map<String, Object> data = documentSnapshot.getData();
                result.add(data);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Map<String, Object>> result) {
        // Actualiza la interfaz de usuario o realiza otras operaciones con el resultado aquí

        if (mListener != null) {
            mListener.onResultLoaded(result);
        }
    }

    public interface OnResultLoadedListener {
        void onResultLoaded(List<Map<String, Object>> result);
    }

}
