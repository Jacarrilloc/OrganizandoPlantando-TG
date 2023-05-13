package com.example.opcv.business.forms;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.opcv.model.persistance.firebase.UserCommunication;
import com.example.opcv.model.persistance.repository.FormsRepository;
import com.example.opcv.model.items.ItemRegistersList;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Forms {

    private final Context context;

    public Forms(Context context) {
        this.context = context;
    }

    public void createFormInfo(Map<String,Object> infoForm,String idGraden){
        String date = getDateNow();
        infoForm.put("Date",date);
        String createdBy = getUserName();
        infoForm.put("CreatedBy",createdBy);
        FormsRepository info = new FormsRepository(context);
        info.insertForm(infoForm,idGraden);
    }

    public List<ItemRegistersList> getInfoForms(String idGarden, String formName) throws IOException, JSONException {
        FormsRepository info = new FormsRepository(context);
        List<Map<String,Object>> infoResult = info.getInfoForms(idGarden, formName);
        List<ItemRegistersList> itemRegistersList = new ArrayList<>();
        if(infoResult != null) {
            for (Map<String, Object> infoForm : infoResult) {
                String register_name = (String) infoForm.get("register_name");
                String date = (String) infoForm.get("Date");
                ItemRegistersList item = new ItemRegistersList(idGarden, register_name, infoForm, date);
                itemRegistersList.add(item);
            }
            Log.d("Forms", "Tamaño en Forms: " + infoResult.size());
        }else{
            Log.d("Forms", "No hay Informacion en la Base de datos local");
        }
        Toast.makeText(context, "Tamaño en Forms: "+ +itemRegistersList.size(), Toast.LENGTH_SHORT).show();
        return itemRegistersList;
    }

    public void updateInfoForm(Map<String,Object> oldInfo,Map<String,Object> newInfo,String idGraden) throws JSONException, IOException {
        boolean areEqual = oldInfo.equals(newInfo);
        if(!areEqual){
            Log.i("Update-Form","Existen Cambios en el Formulario, se Procede a Actualizar");
            FormsRepository updateInfo = new FormsRepository(context);
            updateInfo.updateInfoDatabase(idGraden,newInfo);
        }else{
            Log.i("Update-Form","No hay Cambios en el formulario, no se Actualiza la Información");
        }
    }

    public void deleteInfo(String idGarden, Map<String, Object> infoForm) throws IOException, JSONException {
        if(idGarden != null){
            if(infoForm != null){
                FormsRepository deleteFormsInfo = new FormsRepository(context);
                deleteFormsInfo.deleteInfoDatabase(idGarden,infoForm);
            }else{
                Log.i("Database-Delete","No existe Informacion para borrar");
            }
        }else{
            Log.w("Database-Delete","No existe Id de la Huerta");
        }
    }

    private static String getDateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static String getUserName() {
        UserCommunication user = new UserCommunication();
        return user.getUserFullName();
    }
}
