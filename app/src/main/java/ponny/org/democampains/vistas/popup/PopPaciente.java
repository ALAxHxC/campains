package ponny.org.democampains.vistas.popup;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.controlador.PacienteController;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;

/**
 * Created by Daniel on 06/03/2018.
 */

public class PopPaciente extends Popup {

    private Dialog dialog;
    private AutoCompleteTextView identificacion, nombres, apellidos, descripccion;
    private Button btnRegistrar;
    private PacienteController pacienteController;

    public PopPaciente(Context context,ListView listView) {

        super(context);
        pacienteController = new PacienteController(context,listView);
    }

    @Override
    public void registrar(View view) {
        PacienteRoom paciente = new PacienteRoom(
                identificacion.getText().toString(),
                nombres.getText().toString(),
                apellidos.getText().toString(),
                descripccion.getText().toString()
        );
        pacienteController.insertar(paciente,view,dialog);


    }

    public void dialogoRegistroPaciente(final View view) {
        dialog = super.generarDialogo(R.layout.popup_registro_paciente);
        identificacion = dialog.findViewById(R.id.identificacion_paciente);
        nombres = dialog.findViewById(R.id.nombre_paciente);
        apellidos = dialog.findViewById(R.id.apellidos_paciente);
        descripccion = dialog.findViewById(R.id.descripccion_paciente);
        btnRegistrar = dialog.findViewById(R.id.register_paciente);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar(view);
            }
        });
        dialog.show();
    }


}
