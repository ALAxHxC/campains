package ponny.org.democampains.vistas.popup;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ponny.org.democampains.R;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.vistas.listas.PacientesList;

public class PopupBusquedaPaciente extends Popup {
    private TextView nombres;
    private TextView identificacion;
    private Button buscar;
    private Mensajes mensajes;
    private PacientesList pacientesList;
    private  ListView listView;
    public PopupBusquedaPaciente(Context context) {
        super(context);

        mensajes=new Mensajes(context);
    }

    public void generarDialogo(ListView pacientesList) {
        this.listView=pacientesList;
        this.pacientesList= (PacientesList) pacientesList.getAdapter();
        final Dialog dialog = super.generarDialogo(R.layout.popup_busqueda_paciente);
        buscar = dialog.findViewById(R.id.buscar_paciente);
        nombres = dialog.findViewById(R.id.buscar_nombre_paciente);
        identificacion = dialog.findViewById(R.id.buscar_identificacion_paciente);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultadosBusqueda(identificacion.getText().toString(),nombres.getText().toString(),dialog);
            }
        });
        dialog.show();
    }
    public void resultadosBusqueda(String identificacion,String nombres,Dialog dialog){
        Log.println(Log.ASSERT,"sql","Inicia busqueda paciente");
        List<PacienteRoom> pacientesListList=new ArrayList<>();
        for(PacienteRoom pacienteRoom: this.pacientesList.getList()){
            if(pacienteRoom.getNombres().contains(nombres) || pacienteRoom.getNombres().equalsIgnoreCase(nombres)
                    || pacienteRoom.getApellidos().contains(nombres) || pacienteRoom.getApellidos().equalsIgnoreCase(nombres)
                    || pacienteRoom.getIdentificacion().contains(identificacion) || pacienteRoom.getIdentificacion().equalsIgnoreCase(nombres))
            {
                pacientesListList.add(pacienteRoom);
            }
        }
        Log.println(Log.ASSERT,"sql","Buscando paciente");
        if(pacientesListList.size()<=0){
            mensajes.generarToast(R.string.no_resultados);
            return;
        }
        PacientesList pacientesList=new PacientesList(context,pacientesListList);
        listView.setAdapter(pacientesList);
        dialog.dismiss();
    }

    @Override
    public void registrar(View view) {

    }
}
