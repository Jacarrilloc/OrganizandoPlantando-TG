package com.example.opcv.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.opcv.HomeActivity;
import com.example.opcv.R;
import com.example.opcv.fbComunication.FormsUtilities;
import com.example.opcv.formsScreen.Form_CIH;
import com.example.opcv.formsScreen.Form_CPS;
import com.example.opcv.formsScreen.Form_IMP;
import com.example.opcv.formsScreen.Form_RAC;
import com.example.opcv.formsScreen.Form_RCC;
import com.example.opcv.formsScreen.Form_RHC;
import com.example.opcv.formsScreen.Form_RRH;
import com.example.opcv.formsScreen.Form_RSMP;
import com.example.opcv.formsScreen.Form_SCMPH;
import com.example.opcv.formsScreen.Form_SVH;
import com.example.opcv.formsScreen.FormsRegistersActivity;
import com.example.opcv.gardens.GardenForms;
import com.example.opcv.item_list.ItemCollaboratorsRequest;
import com.example.opcv.item_list.ItemRegistersList;

import java.util.List;
import java.util.Objects;

public class FormsRegistersAdapter extends ArrayAdapter<ItemRegistersList> {
    private TextView dateText;
    private Button seeMoreButton;
    private ImageButton edit, delete;
    private Context context;

    public FormsRegistersAdapter(Context context, List<ItemRegistersList> objects) {
        super(context, 0, objects);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_list_registro_lombricultura, parent, false);
        }
        dateText = (TextView) view.findViewById(R.id.dateRegister);
        seeMoreButton = (Button) view.findViewById(R.id.seeMorButton);
        edit = (ImageButton) view.findViewById(R.id.editButton);
        delete = (ImageButton) view.findViewById(R.id.deleteButton);

        ItemRegistersList item = getItem(position);
        ItemRegistersList IRL = new ItemRegistersList(item.getIdGarden(), item.getFormName(), item.getIdFormCollection(), item.getDate());
        FormsUtilities FU = new FormsUtilities();
        dateText.setText(item.getDate());
        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int form;
                if(Objects.equals(item.getFormName(), "Registro y Actualización de Compostaje")){
                    form = 1;
                    Intent newForm = new Intent(context, Form_RAC.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Solicitud de visita a la huerta")){
                    form = 2;
                    Intent newForm = new Intent(context, Form_SVH.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Control de Inventario de Materia Prima")){
                    form = 3;
                    Intent newForm = new Intent(context, Form_IMP.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Solicitud de compra de materia prima y herramientas")){
                    form = 4;
                    Intent newForm = new Intent(context, Form_SCMPH.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Recepción y Requisición de Materia Prima")){
                    form = 5;
                    Intent newForm = new Intent(context, Form_RSMP.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Revisión de Asistencia a la Huerta")){
                    form = 6;
                    //Aun esta en duda
                }
                else if(Objects.equals(item.getFormName(), "Control de Procesos de Siembra")){
                    form = 7;
                    Intent newForm = new Intent(context, Form_CPS.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Registro y Control de Compostaje")){
                    form = 8;
                    Intent newForm = new Intent(context, Form_RCC.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Recepción y Requisición de Herramientas")){
                    form = 9;
                    Intent newForm = new Intent(context, Form_RRH.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Control de Inventarios de Herramientas")){
                    form = 10;
                    Intent newForm = new Intent(context, Form_CIH.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Registro Histórico Caja")){
                    form = 11;
                    Intent newForm = new Intent(context, Form_RHC.class);
                    newForm.putExtra("watch","true");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FU.editForms(item.idGarden, item.idFormCollection, item.getFormName());
                int form;
                if(Objects.equals(item.getFormName(), "Registro y Actualización de Compostaje")){
                    form = 1;
                    Intent newForm = new Intent(context, Form_RAC.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Solicitud de visita a la huerta")){
                    form = 2;
                    Intent newForm = new Intent(context, Form_SVH.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Control de Inventario de Materia Prima")){
                    form = 3;
                    Intent newForm = new Intent(context, Form_IMP.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Solicitud de compra de materia prima y herramientas")){
                    form = 4;
                    Intent newForm = new Intent(context, Form_SCMPH.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Recepción y Requisición de Materia Prima")){
                    form = 5;
                    Intent newForm = new Intent(context, Form_RSMP.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Revisión de Asistencia a la Huerta")){
                    form = 6;
                    //Aun esta en duda
                }
                else if(Objects.equals(item.getFormName(), "Control de Procesos de Siembra")){
                    form = 7;
                    Intent newForm = new Intent(context, Form_CPS.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Registro y Control de Compostaje")){
                    form = 8;
                    Intent newForm = new Intent(context, Form_RCC.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Recepción y Requisición de Herramientas")){
                    form = 9;
                    Intent newForm = new Intent(context, Form_RRH.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Control de Inventarios de Herramientas")){
                    form = 10;
                    Intent newForm = new Intent(context, Form_CIH.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
                else if(Objects.equals(item.getFormName(), "Registro Histórico Caja")){
                    form = 11;
                    Intent newForm = new Intent(context, Form_RHC.class);
                    newForm.putExtra("watch","edit");
                    newForm.putExtra("idGardenFirebase",item.getIdGarden());
                    newForm.putExtra("idCollecion",item.getIdFormCollection());
                    newForm.putExtra("Name",item.getFormName());
                    context.startActivity(newForm);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("¿Estás seguro de que deseas eliminar este formulario?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                remove(item);
                                FU.deleteForm(item.getIdGarden(), item.getIdFormCollection());
                                notifyDataSetChanged();
                            }
                        }).create().show();
            }
        });



        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        if(view != null){
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }
}
