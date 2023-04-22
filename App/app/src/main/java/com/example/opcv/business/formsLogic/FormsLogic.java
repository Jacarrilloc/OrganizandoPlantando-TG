package com.example.opcv.business.formsLogic;

import android.content.Context;
import android.util.Log;

import com.example.opcv.repository.FormsRepository;
import com.example.opcv.repository.interfaces.OnFormInsertedListener;
import com.example.opcv.repository.localForms.*;

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
            case 1:
                createRACForm(infoForm,idGraden);
                break;
            case 3:
                createIMPForm(infoForm,idGraden);
                break;
            case 7:
                createCPSForm(infoForm,idGraden);
                break;
            case 8:
                createRCCForm(infoForm,idGraden);
                break;
            case 10:
                createCIHForm(infoForm,idGraden);
                break;
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

    private void createIMPForm(Map<String, Object> infoForm, String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String rawMaterial = (String) infoForm.get("rawMaterial");
        String concept = (String) infoForm.get("concept");
        String movement = (String) infoForm.get("movement");
        String quantityRawMaterial = (String) infoForm.get("quantityRawMaterial");
        String units = (String) infoForm.get("units");
        String existenceQuantity = (String) infoForm.get("existenceQuantity");
        String date = getDateNow();

        infoForm.put("Date",date);
        IMP imp = new IMP(id,name,rawMaterial,concept,movement,quantityRawMaterial,units,existenceQuantity,date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormIMP(imp, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_IMP","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_IMP","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public void createRACForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String containerSize = (String) infoForm.get("containerSize");
        String wormsWeight = (String) infoForm.get("wormsWeight");
        String humidity = (String) infoForm.get("humidity");
        String amount_of_waste = (String) infoForm.get("amount of waste");
        String collected_humus = (String) infoForm.get("collected humus");
        String amount_leached = (String) infoForm.get("amount leached");
        String date = getDateNow();

        infoForm.put("Date",date);
        RAC rac = new RAC(id,name,containerSize,wormsWeight,humidity,amount_of_waste,collected_humus,amount_leached,date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormRAC(rac, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_RAC","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_RAC","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public void createRCCForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String areaRecipient = (String) infoForm.get("areaRecipient");
        String areaDescription = (String) infoForm.get("areaDescription");
        String residueQuantity = (String) infoForm.get("residueQuantity");
        String fertilizerQuantity = (String) infoForm.get("fertilizerQuantity");
        String leachedQuantity = (String) infoForm.get("leachedQuantity");
        String date = getDateNow();

        infoForm.put("Date",date);
        RCC rcc = new RCC(id,name,areaRecipient,areaDescription,residueQuantity,fertilizerQuantity,leachedQuantity,date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormRCC(rcc, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_RCC","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_RCC","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public static String getDateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Define el formato de la fecha
        Date date = new Date(); // Obtiene la fecha actual
        return dateFormat.format(date); // Convierte la fecha a string con el formato definido
    }
}
