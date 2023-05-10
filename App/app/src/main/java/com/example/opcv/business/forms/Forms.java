package com.example.opcv.business.forms;

import android.content.Context;
import android.util.Log;

import com.example.opcv.business.persistance.firebase.UserCommunication;
import com.example.opcv.business.persistance.repository.FormsRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
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
