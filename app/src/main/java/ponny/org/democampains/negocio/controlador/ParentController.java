package ponny.org.democampains.negocio.controlador;

import android.content.Context;
import android.widget.ListView;

import java.util.List;

import ponny.org.democampains.negocio.manager.AppDatabase;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.vistas.popup.Mensajes;

/**
 * Created by Daniel on 09/03/2018.
 */

public abstract class ParentController<T> {
    protected Context context;
    protected AppDatabase mDb;
    protected Mensajes mensajes;
    protected List<T> list;
    protected ListView lista;

    public ParentController(Context context, AppDatabase mDb, Mensajes mensajes, List<T> list, ListView lista) {
        this.context = context;
        this.mDb = mDb;
        this.mensajes = mensajes;
        this.list = list;
        this.lista = lista;
    }
}
