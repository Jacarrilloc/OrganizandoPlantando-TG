package com.example.opcv.fbComunication;

import com.google.firebase.auth.FirebaseAuth;

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
}
