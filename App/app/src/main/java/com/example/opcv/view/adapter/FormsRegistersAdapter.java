package com.example.opcv.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.opcv.R;
import com.example.opcv.business.forms.Forms;
import com.example.opcv.view.forms.*;
import com.example.opcv.model.items.ItemRegistersList;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class FormsRegistersAdapter extends ArrayAdapter<ItemRegistersList> {
    private TextView dateText,procesName;
    private Button seeMoreButton;
    private ImageButton edit, delete;
    private Context context;

    //private static int idsForms = {};

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
        procesName = (TextView) view.findViewById(R.id.processMade);
        seeMoreButton = (Button) view.findViewById(R.id.seeMorButton);
        edit = (ImageButton) view.findViewById(R.id.editButton);
        delete = (ImageButton) view.findViewById(R.id.deleteButton);

        ItemRegistersList item = getItem(position);
        Object createdBy = item.getInfo().get("CreatedBy");
        String nameByForm = "Creado por: " + (createdBy != null ? createdBy.toString() : "");
        procesName.setText(nameByForm);
        dateText.setText(item.getDate());

        //Boton para ver la Información del Formulario
        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idFormString = String.valueOf(item.getInfo().get("idForm"));
                int formType = (int) Double.parseDouble(idFormString);
                Intent newForm;
                Class<?> formClass;

                switch (formType) {
                    case 1:
                        formClass = Form_RAC.class;
                        break;
                    case 2:
                        formClass = Form_SCMPH.class;
                        break;
                    case 3:
                        formClass = Form_IMP.class;
                        break;
                    case 4:
                        formClass = Form_RSMP.class;
                        break;
                    case 7:
                        formClass = Form_CPS.class;
                        break;
                    case 8:
                        formClass = Form_RCC.class;
                        break;
                    case 10:
                        formClass = Form_CIH.class;
                        break;
                    case 11:
                        formClass = Form_RHC.class;
                        break;
                    case 12:
                        formClass = Form_RE.class;
                        break;
                    default:
                        Log.i("LOCALFORM", "NO SE RECONOCE EL ID DE ESTE FORM");
                        return;
                }

                newForm = new Intent(context, formClass);
                newForm.putExtra("watch", "true");
                newForm.putExtra("idGardenFirebase", item.getIdGarden());
                newForm.putExtra("idCollecion", (Serializable) item.getInfo());
                newForm.putExtra("Name", item.getFormName());
                context.startActivity(newForm);

                /*
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
                */
            }
        });

        //Boton para editar la Informacion del Formulario
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idFormString = String.valueOf(item.getInfo().get("idForm"));
                int formType = (int) Double.parseDouble(idFormString);
                Intent newForm;
                Class<?> formClass;

                switch (formType) {
                    case 1:
                        formClass = Form_RAC.class;
                        break;
                    case 2:
                        formClass = Form_SCMPH.class;
                        break;
                    case 3:
                        formClass = Form_IMP.class;
                        break;
                    case 4:
                        formClass = Form_RSMP.class;
                        break;
                    case 7:
                        formClass = Form_CPS.class;
                        break;
                    case 8:
                        formClass = Form_RCC.class;
                        break;
                    case 10:
                        formClass = Form_CIH.class;
                        break;
                    case 11:
                        formClass = Form_RHC.class;
                        break;
                    case 12:
                        formClass = Form_RE.class;
                        break;
                    default:
                        Log.i("LOCALFORM", "NO SE RECONOCE EL ID DE ESTE FORM");
                        return;
                }

                newForm = new Intent(context, formClass);
                newForm.putExtra("watch", "edit");
                newForm.putExtra("idGardenFirebase", item.getIdGarden());
                newForm.putExtra("idCollecion", (Serializable) item.getInfo());
                newForm.putExtra("Name", item.getFormName());
                context.startActivity(newForm);

                //FU.editForms(item.idGarden, item.idFormCollection, item.getFormName());
                /*
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
                */
            }
        });

        //Boton para eliminar el Formulario
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("¿Estás seguro de que deseas eliminar este formulario?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Forms deteleInfo = new Forms(context);
                                try {
                                    deteleInfo.deleteInfo(item.getIdGarden(), item.getInfo());
                                } catch (IOException | JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                remove(item);
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
