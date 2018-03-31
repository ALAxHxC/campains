package ponny.org.democampains.vistas.popup;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.controlador.OximetriaController;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;

/**
 * Created by Daniel on 10/03/2018.
 */

public class PopRegistro extends Popup {
private OximetriaRoom oximetriaRoom;
private PacienteRoom pacienteRoom;
EditText nombres,descripccion,identificacion;
private OximetriaController oximetriaController;
Button guardar;
    private Dialog dialog;
    public PopRegistro(Context context,OximetriaRoom oximetriaRoom,PacienteRoom pacienteRoom) {
        super(context);
        this.oximetriaController=new OximetriaController(context,null);
        this.oximetriaRoom=oximetriaRoom;
        this.pacienteRoom=pacienteRoom;
    }

    @Override
    public void registrar(View view) {
        try {
            oximetriaRoom.setAnotaciones(descripccion.getText().toString() + "");
            oximetriaRoom.setIdPaciente(pacienteRoom.getIdentificacion());
            oximetriaController.insertar(oximetriaRoom, view, dialog);
        }catch (Exception ex){
            ex.printStackTrace();
            Log.println(Log.ASSERT,"sql",ex.getMessage());

        }
        }
    public void dialogoRegistroPaciente(final View view) {
        dialog = super.generarDialogo(R.layout.popup_registro_oximetria);
        identificacion=(AutoCompleteTextView) dialog.findViewById(R.id.id_paciente_registro);
        identificacion.setText(pacienteRoom.getIdentificacion());
        nombres = (AutoCompleteTextView)dialog.findViewById(R.id.id_nombre_registro);
        nombres.setText(pacienteRoom.getNombres()+" "+pacienteRoom.getApellidos());
        nombres.setEnabled(false);
        identificacion.setEnabled(false);
        descripccion = (AutoCompleteTextView)dialog.findViewById(R.id.anotaciones_registro);
        guardar=(Button)dialog.findViewById(R.id.btnoxi);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar(view);
            }
        });

        dialog.show();
    }

}
