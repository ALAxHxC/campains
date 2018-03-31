package ponny.org.democampains.vistas.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ponny.org.democampains.R;

/**
 * Created by Daniel on 06/03/2018.
 */

public abstract class Popup  {
    protected Context context;
    protected final String tag=Popup.class.getName();
    public Popup(Context context) {
        this.context = context;
    }
    public Dialog generarDialogo( int r)
    {
        Dialog dialogo = new Dialog(context, R.style.AlertDialogTheme);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(r);
        Window window = dialogo.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        return dialogo;
    }
    public abstract void registrar(View view);
}
