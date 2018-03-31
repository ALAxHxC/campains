package ponny.org.democampains.vistas.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;

import static ponny.org.democampains.servicio.Sesion.sdfCompleta;

/**
 * Created by Daniel on 11/03/2018.
 */

public class PopupOximetria extends  Popup {
    Dialog dialog;
    TextView fecha,puslo,pi,spo2,anotaciones;
    public PopupOximetria(Context context) {
        super(context);

    }

    @Override
    public void registrar(View view) {

    }
    public void dialogoRegistroPaciente(OximetriaRoom oximetriaRoom) {
        dialog = super.generarDialogo(R.layout.pop_up_detalle);
        fecha=(TextView) dialog.findViewById(R.id.textViewIdItemRegistroDialog);
        fecha.setText(sdfCompleta.format(oximetriaRoom.getDatatime()));
        puslo=(TextView)dialog.findViewById(R.id.textViewPulsoItemDialog);
        puslo.setText(oximetriaRoom.getPulse()+"");
        pi=(TextView)dialog.findViewById(R.id.textViewPIItemDialog);
        pi.setText(oximetriaRoom.getPi()+"");
        spo2=(TextView)dialog.findViewById(R.id.textViewSPO2ItemDialog);
        spo2.setText(oximetriaRoom.getPi()+"");
        anotaciones=(TextView)dialog.findViewById(R.id.editTextPacienteAnotaciones);
        anotaciones.setEnabled(false);
        if(oximetriaRoom.getAnotaciones().isEmpty() || oximetriaRoom.getAnotaciones().equals("")){
            anotaciones.setText(R.string.no_anotaciones);

        }else{
            anotaciones.setText(oximetriaRoom.getAnotaciones());
        }


        dialog.show();
    }
}
