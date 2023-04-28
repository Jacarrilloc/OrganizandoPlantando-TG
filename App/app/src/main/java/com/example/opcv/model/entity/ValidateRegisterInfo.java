package com.example.opcv.model.entity;

import android.content.Context;
import android.widget.Toast;

public class ValidateRegisterInfo {

    public boolean validateFirstRegisterInfo(String nameString, String lastNameString, String emailString, String passwordString, String confirmPasswordString, boolean termsBool, Context context) {
        if (!validateEmail(emailString)) {
            Toast.makeText(context, "El Correo electrónico ingresado no es válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!validatePassword(passwordString)) {
            Toast.makeText(context, "La contraseña ingresada no es válida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nameString.isEmpty() || lastNameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || confirmPasswordString.isEmpty()) {
            Toast.makeText(context, "Alguno de los campos está vacío, por favor revisa los campos e intenta de nuevo", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!passwordString.equals(confirmPasswordString)) {
            Toast.makeText(context, "Las contraseñas ingresadas no son iguales, por favor revisa e intenta de nuevo", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!termsBool) {
            Toast.makeText(context, "Debe aceptar los términos y condiciones para continuar", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private boolean validateEmail(String email){
        if(!email.contains("@") || !email.contains(".") || email.length() < 5)
            return false;
        return true;
    }

    private boolean validatePassword(String password){
        if(password.length() < 6)
            return false;
        return true;
    }
}
