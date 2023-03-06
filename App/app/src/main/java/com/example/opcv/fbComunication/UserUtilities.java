package com.example.opcv.fbComunication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserUtilities {

    private FirebaseAuth autentication;

    public boolean isLogeed(){
        autentication = FirebaseAuth.getInstance();
        if(autentication != null){
            return true;
        }else{
            return false;
        }
    }

    public static String getCurrentUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }
}
