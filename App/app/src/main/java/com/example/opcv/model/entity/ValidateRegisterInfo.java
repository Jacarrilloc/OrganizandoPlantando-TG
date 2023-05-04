package com.example.opcv.model.entity;

import android.content.Context;
import android.widget.Toast;

import com.example.opcv.view.auth.ChangePassword;

public class ValidateRegisterInfo {

    public boolean validateFirstRegisterInfo(String nameString, String lastNameString, String emailString, String passwordString, String confirmPasswordString, boolean termsBool, Context context) {
        if (!validateEmail(emailString)) {
            Toast.makeText(context, "El Correo electrónico ingresado no es válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!validatePassword(passwordString)) {
            Toast.makeText(context, "La contraseña no es válida", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Debe contener letras, numeros y tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
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

        String regex = "^(?=.*[A-Za-z])(?=.*\\d).+$";
        if(!password.matches(regex)){
            return false;
        }
        return true;
    }

    public boolean validateNewPassword(String password, Context context, String repeatPassword){
        if(!password.isEmpty() && !repeatPassword.isEmpty()){
            if(password.equals(repeatPassword)){
                boolean validateFirst = validatePassword(password);
                if(!validateFirst){
                    Toast.makeText(context, "La contraseña no es válida", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Debe contener letras, numeros y tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else{
                Toast.makeText(context, "Las contraseñas ingresadas no son iguales, por favor revisa e intenta de nuevo", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Toast.makeText(context, "Uno de los campos esta vacio. Porfavor revisa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
