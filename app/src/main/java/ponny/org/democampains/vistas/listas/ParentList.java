package ponny.org.democampains.vistas.listas;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Daniel on 07/03/2018.
 */

public abstract class ParentList<T> extends BaseAdapter {
    //testing
    //testing
    protected final String tag=ParentList.class.getName();
    protected Context context;
    protected List<T> lista;
    protected Intent intent;
    public ParentList(Context context, List<T> lista,Class clase) {
        this.context = context;
        this.lista = lista;
        this.intent=new Intent(context,clase);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }
    protected abstract void irActividad();

}
