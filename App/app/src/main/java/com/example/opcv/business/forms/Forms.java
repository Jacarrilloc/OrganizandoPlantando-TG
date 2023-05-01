package com.example.opcv.business.forms;

import android.content.Context;
import android.util.Log;

import com.example.opcv.business.persistance.firebase.UserCommunication;
import com.example.opcv.business.persistance.repository.FormsRepository;
import com.example.opcv.business.persistance.repository.interfaces.OnFormInsertedListener;
import com.example.opcv.business.persistance.repository.localForms.CPS;
import com.example.opcv.business.persistance.repository.localForms.IMP;
import com.example.opcv.business.persistance.repository.localForms.RAC;
import com.example.opcv.business.persistance.repository.localForms.RCC;
import com.example.opcv.business.persistance.repository.localForms.RE;
import com.example.opcv.business.persistance.repository.localForms.RHC;
import com.example.opcv.business.persistance.repository.localForms.RRH;
import com.example.opcv.business.persistance.repository.localForms.RSMP;
import com.example.opcv.business.persistance.repository.localForms.SCMPH;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Forms {

    private final Context context;

    public Forms(Context context) {
        this.context = context;
    }

    public void createForm(Map<String,Object> infoForm, String idGraden){
        int type = (int) infoForm.get("idForm");
        switch (type){
            case 1:
                createFormInfo(infoForm,idGraden);
                break;
            case 2:
                createSCMPHForm(infoForm,idGraden);
                break;
            case 3:
                createFormInfo(infoForm,idGraden);
                break;
            case 4:
                createRSMPForm(infoForm,idGraden);
                break;
            case 7:
                createFormInfo(infoForm,idGraden);
                break;
            case 8:
                createFormInfo(infoForm,idGraden);
                break;
            case 9:
                createRRHForm(infoForm,idGraden);
                break;
            case 10:
                createFormInfo(infoForm,idGraden);
                break;
            case 11:
                createFormInfo(infoForm,idGraden);
                break;
            case 12:
                createFormInfo(infoForm,idGraden);
                break;
        }
    }

    public void createFormInfo(Map<String,Object> infoForm,String idGraden){
        String date = getDateNow();
        infoForm.put("Date",date);
        String createdBy = getUserName();
        infoForm.put("CreatedBy",createdBy);

        FormsRepository info = new FormsRepository(context);
        info.insertForm(infoForm,idGraden, new OnFormInsertedListener() {
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

    public void createRRHForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String description = (String) infoForm.get("description");
        String toolQuantity = (String) infoForm.get("toolQuantity");
        String concept = (String) infoForm.get("concept");
        String performedBy = (String) infoForm.get("performedBy");
        String toolStatus = (String) infoForm.get("toolStatus");
        String date = getDateNow();

        infoForm.put("Date",date);
        RRH rrh = new RRH(id,name,description,concept,performedBy,toolStatus,toolQuantity,date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormRRH(rrh, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_RRH","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_RRH","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public void createRSMPForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String description = (String) infoForm.get("description");
        String units = (String) infoForm.get("units");
        String quantity = (String) infoForm.get("quantity");
        String total = (String) infoForm.get("total");
        String concept = (String) infoForm.get("concept");
        String state = (String) infoForm.get("state");
        String date = getDateNow();

        infoForm.put("Date",date);
        RSMP rsmp = new RSMP(id,name,description,units,concept,state,Integer.parseInt(quantity),Integer.parseInt(total),date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormRSMP(rsmp, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_RSMP","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_RSMP","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });
    }

    public void createSCMPHForm(Map<String,Object> infoForm,String idGraden){
        int id = (int) infoForm.get("idForm");
        String name = (String) infoForm.get("nameForm");
        String itemName = (String) infoForm.get("itemName");
        String item = (String) infoForm.get("item");
        String units = (String) infoForm.get("units");
        String quantity = (String) infoForm.get("quantity");
        String total = (String) infoForm.get("total");
        String date = getDateNow();

        infoForm.put("Date",date);
        SCMPH scmph = new SCMPH(id,name,itemName,item,units,total,Integer.parseInt(quantity),date);
        FormsRepository info = new FormsRepository(context);
        info.insertFormSCMPH(scmph, infoForm, idGraden, new OnFormInsertedListener() {
            @Override
            public void onFormInserted(String formId) {
                Log.i("INSERTAR_FORM_SCMPH","SE AGREGÓ");
            }

            @Override
            public void onFormInsertionError(Exception e) {
                Log.i("INSERTAR_FORM_SCMPH","ERROR, NO SE INGRESÓ:" + e.getMessage().toString());
            }
        });

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
