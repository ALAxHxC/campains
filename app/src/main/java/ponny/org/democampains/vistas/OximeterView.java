package ponny.org.democampains.vistas;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import ponny.org.democampains.R;
import ponny.org.democampains.error.ExceptionHandler;
import ponny.org.democampains.negocio.controlador.OximetriaController;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.negocio.session.Oximetria;
import ponny.org.democampains.servicio.BluetoothLeService;
import ponny.org.democampains.servicio.ControlerBLE;
import ponny.org.democampains.servicio.Sesion;
import ponny.org.democampains.vistas.popup.Mensajes;

import static android.content.Context.BIND_AUTO_CREATE;

public class OximeterView extends AppCompatActivity {


    private int datos;
    private ControlerBLE controlerBLE;
    private String address, name;
    private TextView txtSPO2, txtPI, txtPulse;
    private Mensajes mensajes;
    private OximetriaController oximetriaController;
    private Oximetria oximetria;
    private View parentView;
    private final int datosvalidos = 8;
    private PacienteRoom pacienteRoom;
    private TabHost tabshost;
    private TabHost.TabSpec pulso, spo2, registros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oximeter_view);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        mensajes = new Mensajes(this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

       /* pacienteRoom = (PacienteRoom) bundle.getSerializable(getString(R.string.paciente_rom));
        Log.println(Log.ASSERT,"paciente",pacienteRoom.getNombres());*/
        controlerBLE = new ControlerBLE(this, mensajes, Sesion.pacienteRoom);
        cargarVistas();
        cargarExtras();
        datos = 0;
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }

    private void cargarVistas() {
        txtPI = (TextView) findViewById(R.id.txtPI);
        txtPulse = (TextView) findViewById(R.id.txtPulse);
        txtSPO2 = (TextView) findViewById(R.id.txtSPO2);
        parentView = findViewById(R.id.oximetria_view);
    }

    private void cargarExtras() {
        address = getIntent().getStringExtra(getString(R.string.address));
        name = getIntent().getStringExtra(getString(R.string.name_device));
    }

    private void irPaciente() {
        //Bundle bundle=new Bundle();
        // bundle.putSerializable(getString(R.string.paciente_rom),pacienteRoom);
        Intent intent = new Intent(this, VistaPaciente.class);
        //   intent.putExtras(bundle);
        startActivity(intent);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.println(Log.ASSERT,"BLE","rECIBIENDO");
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.println(Log.ASSERT, "BLE", "conectado");
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.println(Log.ASSERT, "BLE", "Desconectado");
                controlerBLE.desconnectar();
                irPaciente();
                //  servicio.volverAinicio();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                controlerBLE.displayGattServicesService(controlerBLE.getmBluetoothLeService().getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                try {//Limpia salida
                    //       Log.println(Log.ASSERT, "BLE", "Datos");
                    ///String recibido = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    byte[] bytes = intent.getExtras().getByteArray("data");
                    OximetriaRoom oximetriaRoom = controlerBLE.tratarDatos(bytes, txtSPO2, txtPI, txtPulse);
                    Log.println(Log.ASSERT, "BLE", "Datos2");
                    if (oximetriaRoom == null) {
                        return;
                    }
                    configurarDatos(oximetriaRoom);

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    Log.println(Log.ASSERT, "BLE", ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.println(Log.ASSERT, "BLE", ex.getMessage());
                }

            }


        }
    };

    private void configurarDatos(OximetriaRoom oximetriaRoom) {
        datos++;
        if (datos > datosvalidos) {
            Log.println(Log.ASSERT, "SQL", "Cargando registros");
            controlerBLE.desconnectar();
            oximetria = new Oximetria(oximetriaRoom, this);
            Sesion.oximetriaRoom = oximetriaRoom;
            Log.println(Log.ASSERT, "SQL", "Oximetria temp" + Sesion.oximetriaRoom.getPulse());
            mensajes.generarToast(R.string.exito);
            irPaciente();
           /*if( oximetria.guardarOximetriaTemp()){


           }*/

        }

    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            controlerBLE.setmBluetoothLeService(((BluetoothLeService.LocalBinder) service).getService());
            if (!controlerBLE.getmBluetoothLeService().initialize()) {
                finish();
            }
            Log.println(Log.ASSERT, "BLE", "cONECTANDO");

            controlerBLE.getmBluetoothLeService().connect(address);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            controlerBLE.setmBluetoothLeService(null);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mGattUpdateReceiver, controlerBLE.makeGattUpdateIntentFilter());

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        //mBluetoothLeService = null;
    }

    @Override
    public void onBackPressed() {
        irPaciente();
        unbindService(mServiceConnection);
        registerReceiver(mGattUpdateReceiver, controlerBLE.makeGattUpdateIntentFilter());
    }
}
