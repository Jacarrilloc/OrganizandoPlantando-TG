package com.example.opcv.business.formsLogic;

import android.content.Context;
import android.util.Log;

import com.example.opcv.repository.FormsRepository;
import com.example.opcv.repository.interfaces.OnFormInsertedListener;
import com.example.opcv.repository.localForms.CIH;
import com.example.opcv.repository.localForms.CPS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormsLogic {

    private Context context;

    public FormsLogic(Context context) {
        this.context = context;
    }

    public void createForm(Map<String,Object> infoForm, String idGraden){
        int type = (int) infoForm.get("idForm");
        switch (type){
            case 10:
                createCIHForm(infoForm,idGraden);
                break;
            case 7:
                createCPSForm(infoForm,idGraden);
        }
    }

    public void createCIHForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String tool = (String) infoForm.get("tool");
        String concept = (String) infoForm.get("concept");
        String incomingOutgoing = (String) infoForm.get("incomingOutgoing");
        String toolQuantityS = (String) infoForm.get("toolQuantity");
        int toolQuantity = Integer.parseInt(toolQuantityS);
        String toolStatus = (String) infoForm.get("toolStatus");
        String existenceQuantityS = (String) infoForm.get("existenceQuantity");
        int existenceQuantity = Integer.parseInt(existenceQuantityS);
        String date = getDateNow();

        infoForm.put("Date",date);
        CIH cihForm = new CIH(id,name,tool,concept,incomingOutgoing,toolQuantity,toolStatus,existenceQuantity,date);

        FormsRepository info = new FormsRepository(context);
        info.insertFormCIH(cihForm,infoForm,idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_CIH","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_CIH","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    private void createCPSForm(Map<String, Object> infoForm, String idGraden) {
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String personResponsable = (String) infoForm.get("personResponsable");
        String processPhase = (String) infoForm.get("processPhase");
        String phaseDuration = (String) infoForm.get("phaseDuration");
        String plants_or_seeds = (String)  infoForm.get("plants or seeds");
        String commentsObservations = (String) infoForm.get("commentsObservations");
        String date = getDateNow();

        infoForm.put("Date",date);
        CPS cps = new CPS(id,name,personResponsable,processPhase,phaseDuration,commentsObservations,plants_or_seeds,date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormCPS(cps, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_CPS","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_CPS","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public static String getDateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Define el formato de la fecha
        Date date = new Date(); // Obtiene la fecha actual
        return dateFormat.format(date); // Convierte la fecha a string con el formato definido
    }
}
