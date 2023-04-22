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
            case 11:
                createRHCForm(infoForm,idGraden);
                break;
            case 12:
                createREForm(infoForm,idGraden);
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

    public void createREForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String date = (String) infoForm.get("date");
        String eventName = (String) infoForm.get("eventName");
        String totalPerson = (String) infoForm.get("totalPerson");
        String womenNumber = (String) infoForm.get("womenNumber");
        String menNumber = (String) infoForm.get("menNumber");
        String noSpcNumber = (String) infoForm.get("noSpcNumber");
        String infantNumber = (String) infoForm.get("infantNumber");
        String childhoodNumber = (String) infoForm.get("childhoodNumber");
        String teenNumber = (String) infoForm.get("teenNumber");
        String youthNumber = (String) infoForm.get("youthNumber");
        String adultNumber = (String) infoForm.get("adultNumber");
        String elderlyNumber = (String) infoForm.get("elderlyNumber");
        String afroNumber = (String) infoForm.get("afroNumber");
        String nativeNumber = (String) infoForm.get("nativeNumber");
        String lgtbiNumber = (String) infoForm.get("lgtbiNumber");
        String romNumber = (String) infoForm.get("romNumber");
        String victimNumber = (String) infoForm.get("victimNumber");
        String disabilityNumber = (String) infoForm.get("disabilityNumber");
        String demobilizedNumber = (String) infoForm.get("demobilizedNumber");
        String mongrelNumber = (String) infoForm.get("mongrelNumber");
        String foreignNumber = (String) infoForm.get("foreignNumber");
        String peasantNumber = (String) infoForm.get("peasantNumber");
        String otherNumber = (String) infoForm.get("otherNumber");
        String dateCreated = getDateNow();

        infoForm.put("Date",dateCreated);
        RE re = new RE(id,name,date,eventName,totalPerson,womenNumber,menNumber,noSpcNumber,infantNumber,childhoodNumber,teenNumber,youthNumber,adultNumber,elderlyNumber,afroNumber,nativeNumber,lgtbiNumber,romNumber,victimNumber,disabilityNumber,demobilizedNumber,mongrelNumber,foreignNumber,peasantNumber,otherNumber,dateCreated);
        FormsRepository info = new FormsRepository(context);
        info.insertFormRE(re, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_RE","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_RE","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public void createRHCForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String responsable = (String) infoForm.get("responsable");
        String incomeExpense = (String) infoForm.get("incomeExpense");
        String type = (String) infoForm.get("type");
        String code = (String) infoForm.get("code");
        String itemName = (String) infoForm.get("itemName");
        String measurement = (String) infoForm.get("measurement");
        String totalCost = (String) infoForm.get("totalCost");
        String comments = (String) infoForm.get("comments");
        String units = (String) infoForm.get("units");
        String date = getDateNow();

        infoForm.put("Date",date);
        RHC rhc = new RHC(id,name,responsable,incomeExpense,type,code,itemName,measurement,comments,units,"",Integer.parseInt(totalCost),date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormRHC(rhc, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_RHC","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_RHC","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public static String getDateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Define el formato de la fecha
        Date date = new Date(); // Obtiene la fecha actual
        return dateFormat.format(date); // Convierte la fecha a string con el formato definido
    }
}
