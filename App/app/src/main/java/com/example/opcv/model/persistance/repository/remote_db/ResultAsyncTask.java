package com.example.opcv.model.persistance.repository.remote_db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.opcv.model.persistance.repository.local_db.LocalDatabase;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

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

    public ResultAsyncTask(String idGarden,String formName, Context context){
        this.idGarden = idGarden;
        this.formName = formName;
        mContext = context;
    }

    @Override
    protected List<Map<String, Object>> doInBackground(Void... voids) {
        LocalDatabase infoForm = new LocalDatabase(mContext);
        List<Map<String, Object>> result = new ArrayList<>();

        TaskCompletionSource<List<Map<String,Object>>> taskCompletionSource = new TaskCompletionSource<>();
        FirebaseDatabase updateInfo = new FirebaseDatabase();
        try {
            updateInfo.getAllFormsDatabase(idGarden, formName, new FirebaseDatabase.onFormsLoaded() {
                @Override
                public void FormsLoad(List<Map<String, Object>> infoResult) throws IOException, JSONException {
                    List<Map<String,Object>> resultLocal = infoForm.getInfoJsonForms(idGarden,formName);
                    updateInfo(resultLocal,infoResult,idGarden);

                    taskCompletionSource.setResult(infoResult);
                }
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            result = Tasks.await(taskCompletionSource.getTask());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    @Override
    protected void onPostExecute(List<Map<String,Object>> resultI) {
        LocalDatabase info = new LocalDatabase(mContext);
        info.updateAllJson(resultI,idGarden);
    }

    private void updateInfo(List<Map<String, Object>> local,List<Map<String, Object>> firebase, String idGarden){
        List<Map<String, Object>> newInfo = new ArrayList<>();
        if (local == null) {
            newInfo.addAll(firebase);
            Log.d("TAG", "No hay Local,Info en Firebase : " + firebase.size());
        } else {
            Log.d("TAG", "Firebase : " + firebase.size() + " Local: " + local.size());
            for(Map<String,Object> firebaseData : firebase){
                boolean found = false;
                for (Map<String,Object> localData : local){
                    if(firebaseData.get("Date").equals(localData.get("Date")) &&  firebaseData.get("idForm").equals(localData.get("idForm"))  && firebaseData.get("CreatedBy").equals(localData.get("CreatedBy")) && !firebaseData.equals(localData)){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    newInfo.add(firebaseData);
                }
            }
        }
        LocalDatabase info = new LocalDatabase(mContext);
        info.updateAllJson(newInfo,idGarden);
        Log.d("ResultAsyncTask", "Diferencia: " + newInfo.size());
    }
}
