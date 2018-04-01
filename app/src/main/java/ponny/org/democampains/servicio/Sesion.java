package ponny.org.democampains.servicio;

import java.text.SimpleDateFormat;

import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;

/**
 * Created by Daniel on 11/03/2018.
 */

public class Sesion {
    public static OximetriaRoom oximetriaRoom=null;
    public static PacienteRoom pacienteRoom=null;
    public static final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
