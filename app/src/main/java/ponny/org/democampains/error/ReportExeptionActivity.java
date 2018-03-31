package ponny.org.democampains.error;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ponny.org.democampains.R;

public class ReportExeptionActivity extends AppCompatActivity {
TextView textViewError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_exeption);
        textViewError=(TextView)findViewById(R.id.textViewError);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        textViewError.setText(getIntent().getStringExtra("error"));
    }
}
