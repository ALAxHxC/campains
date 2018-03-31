package ponny.org.democampains.negocio.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Daniel on 06/03/2018.
 */

public class Usuario {
    private Context context;
    private final String usuario = "usuario";
    private final String password = "password";
    private final String session = "session";
    private final String preferencias="preferencias";

    private SharedPreferences preferences;
    public Usuario(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(preferencias, Context.MODE_PRIVATE);

    }

    public void setUsuario(String usuarioP) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(usuario,usuarioP);
        editor.commit();
    }
    public void setPassword(String passwordP) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(password, passwordP);
        editor.commit();
    }
    public void setSession(Boolean sessionP) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(session,sessionP);
        editor.commit();
    }

    public boolean getSession()
    {
        return preferences.getBoolean(session,false);
    }

    public String getUsuario()
    {
        return preferences.getString(usuario,null);
    }
    public String getPassword()
    {
        return preferences.getString(password,null);
    }



}
