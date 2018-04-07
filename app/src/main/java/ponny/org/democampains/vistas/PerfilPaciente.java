package ponny.org.democampains.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ponny.org.democampains.R;
import ponny.org.democampains.error.ExceptionHandler;
import ponny.org.democampains.negocio.controlador.PacienteController;
import ponny.org.democampains.negocio.modelos.bd.PacienteRoom;
import ponny.org.democampains.servicio.ControlerBLE;
import ponny.org.democampains.vistas.popup.Mensajes;

public class PerfilPaciente extends AppCompatActivity {
    private PacienteRoom pacienteRoom;
    private PacienteController pacienteController;
    private Mensajes mensajes;
    private boolean actualiza = false;
    private TextView nombres;
    private EditText descripccion;
    View parentView;
    private ProgressBar progressBar;
    FloatingActionButton fab;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_perfil_paciente);

        try {
            mensajes = new Mensajes(this);
            pacienteController = new PacienteController(this, null);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            progressBar = findViewById(R.id.perfilprogress);
            parentView = findViewById(R.id.contPefil);
            pacienteRoom = (PacienteRoom) bundle.getSerializable(getString(R.string.paciente_rom));
            imageView = findViewById(R.id.imageView3);
            loadProfiles(imageView);
            cargarDatosPaciente();
            fab = (FloatingActionButton) findViewById(R.id.fabIconPerfil);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarPaciente();
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizarPaciente();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.println(Log.ASSERT, "PERFIL", ex.getMessage());
        }
    }

    private void cargarDatosPaciente() {
        nombres = (TextView) findViewById(R.id.textNombresPacientePerfil);
        nombres.setText(pacienteRoom.getNombres() + " " + pacienteRoom.getApellidos());
        descripccion = (EditText) findViewById(R.id.editTextDescripccionPacientePerfil);
        descripccion.setText(pacienteRoom.getDescripccion());
        descripccion.setEnabled(false);

    }

    private void actualizarPaciente() {
        if (actualiza) {
            pacienteRoom.setDescripccion(descripccion.getText().toString());
            pacienteController.actualizarPacientePefil(this, pacienteRoom, parentView);
            actualiza = false;
            updateButtonImage(R.drawable.edit);
            descripccion.setEnabled(false);
        } else {
            actualiza = true;
            descripccion.setEnabled(true);
            updateButtonImage(R.drawable.save);
            mensajes.generarSnack(parentView, R.string.puede_actualizar);
        }


    }

    private void updateButtonImage(int r) {
        fab.setImageResource(r);

    }


    @Override
    public void onBackPressed() {

        Bundle bundle = new Bundle();
        bundle.putSerializable(this.getString(R.string.paciente_rom), pacienteRoom);
        Intent intent = new Intent(this, VistaPaciente.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loadProfiles(ImageView imageView) {
        if (pacienteRoom.getNombres().toLowerCase().contains("juan")) {
            imageView.setImageResource(R.drawable.profile67);
        } else if (pacienteRoom.getNombres().toLowerCase().contains("eliana")) {
            imageView.setImageResource(R.drawable.profile66);
        } else {
            imageView.setImageResource(R.drawable.profile65);
        }
    }

    public void mostrarProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        parentView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
