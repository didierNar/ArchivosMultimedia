package archivosmultimedia.eam.archivosmultimedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirRegistroReunion (View v){
        Intent i = new Intent(this, CrearReunion.class);
        startActivity(i);
    }

    public void abrirListaReuniones (View v){
        Intent i = new Intent(this, ListaReuniones.class);
        startActivity(i);
    }
}
