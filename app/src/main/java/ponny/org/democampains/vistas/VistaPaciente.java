package ponny.org.democampains.vistas;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ponny.org.democampains.R;
import ponny.org.democampains.error.ExceptionHandler;
import ponny.org.democampains.negocio.modelos.bd.OximetriaRoom;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.servicio.ControlerBLE;
import ponny.org.democampains.servicio.Sesion;
import ponny.org.democampains.vistas.popup.Mensajes;
import ponny.org.democampains.vistas.popup.PopRegistro;

public class VistaPaciente extends AppCompatActivity {


    private PacienteRoom pacienteRoom;
    private View parentView;
    private static final long SCAN_PERIOD = 100000;
    private Handler mHandlerScan;
    private ControlerBLE controlerBLE;
    private PopRegistro popRegistro;
    private Mensajes mensajes;
    private TextView textViewOximetriaLista;
    private ImageView oximetro, glucometro, tensiometro, bascula;
    private FloatingActionButton guardarBtn;
    private OximetriaRoom oximetriaRoom;
    private ImageView imageView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//startActivity(new Intent(VistaPaciente.this,PerfilPaciente.class));
                 irPaciente();
                    return true;
                case R.id.navigation_dashboard:
                 irRegistros();
       //        irRegistros();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_paciente);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        guardarBtn = (FloatingActionButton) findViewById(R.id.imageViewSave);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        parentView = findViewById(R.id.containerVistaPaciente);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
    try {
        pacienteRoom = (PacienteRoom) bundle.getSerializable(getString(R.string.paciente_rom));
        if(pacienteRoom!=null){
            Sesion.pacienteRoom=pacienteRoom;
        }

    }catch (NullPointerException ex){
        ex.printStackTrace();
        Log.println(Log.ASSERT,"SQL",ex.toString());
        pacienteRoom=Sesion.pacienteRoom;
    }
        imageView=findViewById(R.id.imageView2);
        loadProfiles(imageView);
        textViewOximetriaLista = (TextView) findViewById(R.id.textViewOximetriaLista);
        textViewOximetriaLista.setText("");
        TextView nombres = (TextView) findViewById(R.id.TextNombresPaciente);

        nombres.setText(pacienteRoom.getNombres() + " " + pacienteRoom.getApellidos());
        TextView descripccion = (TextView) findViewById(R.id.textViewDescripccionPaciente);
        descripccion.setText(pacienteRoom.getDescripccion());
        mensajes = new Mensajes(this);
        mHandlerScan = new Handler();
        controlerBLE = new ControlerBLE(this, mensajes, pacienteRoom);

     activarBluetooth();

     IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        loadImages();
       loadTextos();

    }
    private void irRegistros(){

        try {
            //Intent intent = new Intent(this, OximeterList.class);
            Intent intent = new Intent(this, OximeterList.class);
            intent.putExtras(getExtras());
            startActivity(intent);
        } catch (Exception ex) {
            Log.println(Log.ASSERT, "sql", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Bundle getExtras() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(this.getString(R.string.paciente_rom), pacienteRoom);
        return bundle;
    }


    private void loadImages() {
        oximetro = (ImageView) findViewById(R.id.imageViewPulso);
        tensiometro = (ImageView) findViewById(R.id.imageViewTensiometro);
        glucometro = (ImageView) findViewById(R.id.imageViewGlucometro);
        bascula = (ImageView) findViewById(R.id.imageViewBascula);
        oximetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDipositivoBusqueda(R.string.buscando_oxi);
                scanearDispositivo(true);
            }
        });
        tensiometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanearDispositivo(true);
            }
        });
        glucometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanearDispositivo(true);
            }
        });
        bascula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanearDispositivo(true);
            }
        });
        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogoDeRegistro();
            }
        });

    }

    private void loadTextos() {
        try {
            if(Sesion.oximetriaRoom==null){return;}
            textViewOximetriaLista = (TextView) findViewById(R.id.textViewOximetriaLista);
            oximetriaRoom=Sesion.oximetriaRoom;
            textViewOximetriaLista.setText(oximetriaRoom.getSpo2() + "%");
            Log.println(Log.ASSERT,"SQL","Genernado texto"+oximetriaRoom.getPulse());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.println(Log.ASSERT, "SQL", ex.getMessage());
        }
    }


    private void irPaciente() {
        try {
            Intent intent = new Intent(this, PerfilPaciente.class);
             intent.putExtras(getExtras());
            startActivity(intent);
        } catch (Exception ex) {
            Log.println(Log.ASSERT, "sql", ex.getMessage());
            ex.printStackTrace();
        }

    }

    private void cargarDialogoDeRegistro() {

        try {
            if(Sesion.oximetriaRoom==null){
                mensajes.generarSnack(parentView,R.string.no_hay_registros);
                return;
            }
            popRegistro = new PopRegistro(this, Sesion.oximetriaRoom, pacienteRoom,textViewOximetriaLista);
            popRegistro.dialogoRegistroPaciente(parentView);
        } catch (Exception ex)
        {
            ex.printStackTrace();

            Log.println(Log.ASSERT,"SQL",ex.getMessage());

            mensajes.generarSnack(parentView,R.string.no_registro);
        }
        }

    private void irOximetria() {
        try {
            Intent intent = new Intent(this, OximeterView.class);
            intent.putExtras(getExtras());
            startActivity(intent);
        } catch (Exception ex) {
            Log.println(Log.ASSERT, "sql", ex.getMessage());
            ex.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        scanearDispositivo(false);
        unregisterReceiver(mReceiver);
    }

    private void activarBluetooth() {
        Intent bleEnable = controlerBLE.verificacionBluetooth();
        if (bleEnable != null)
            startActivityForResult(bleEnable, ControlerBLE.REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ControlerBLE.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            activarBluetooth();
            return;
        }

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        // Bluetooth has been turned off;
                        mensajes.generarSnack(parentView, R.string.bluetooth_activado_no);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        // Bluetooth is turning off;
                        break;
                    case BluetoothAdapter.STATE_ON:
                        // Bluetooth has been on
                        // startActivityForResult(bleEnable, ControlerBLE.REQUEST_ENABLE_BT);
                        mensajes.generarSnack(parentView, R.string.bluetooth_activado);
                        //  activarBluetooth();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        // Bluetooth is turning on
                        mensajes.generarSnack(parentView, R.string.bluetooth_activando);
                        break;
                }
            }
        }
    };
    public BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Log.println(Log.ASSERT, "BLE", "Escaneando");
                            try {
                                if (device.getName() == null) {
                                    return;
                                }
                                Log.println(Log.ASSERT, "BLE", device.getName());

                                if (device.getName().equalsIgnoreCase(getString(R.string.name_device))) {
                                    Log.println(Log.ASSERT, "BLE", "Conectara");

                                    controlerBLE.conectarDevice(device.getAddress(), device.getName());
                                }
                            } catch (Exception ex) {
                                Log.println(Log.ASSERT, "BLE", ex.toString());
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            };

    private void scanearDispositivo(final boolean enable) {

        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandlerScan.postDelayed(controlerBLE.hiloScan, SCAN_PERIOD);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                Log.println(Log.ASSERT, "ble", "asignando scan");
                controlerBLE.getAdapter().startLeScan(mLeScanCallback);
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                controlerBLE.getAdapter().getBluetoothLeScanner().startScan(controlerBLE.scanCallback());
            }
        } else {
            controlerBLE.getAdapter().stopLeScan(mLeScanCallback);
        }

        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {

        try {
//            controlerBLE.desconnectar();
            scanearDispositivo(false);
            startActivity(new Intent(this,MainActivity.class));
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.println(Log.ASSERT, "BLE", ex.toString());
        }

    }
    private void loadProfiles(ImageView imageView){
        if(pacienteRoom.getNombres().toLowerCase().contains("juan")){
            imageView.setImageResource(R.drawable.profile67);
        }
        else if(pacienteRoom.getNombres().toLowerCase().contains("eliana")){
            imageView.setImageResource(R.drawable.profile66);
        }
        else
        {
            imageView.setImageResource(R.drawable.profile65);
        }
    }
    private void mostrarDipositivoBusqueda(int id){
        mensajes.generarSnack(parentView,id);
    }

}
