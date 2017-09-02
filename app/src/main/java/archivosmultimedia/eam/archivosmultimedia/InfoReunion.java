package archivosmultimedia.eam.archivosmultimedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class InfoReunion extends AppCompatActivity {

    Reunion reunionActual;

    TextView nombre;
    TextView fecha;
    TextView lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_reunion);

        Bundle datos = getIntent().getExtras();
        reunionActual = (Reunion) datos.get("reu");

        nombre = (TextView) findViewById(R.id.tvNombre);
        fecha = (TextView) findViewById(R.id.tvFecha);
        lugar = (TextView) findViewById(R.id.tvLugar);

        cargarDatos();

    }

    public void cargarDatos(){
        nombre.setText("Nombre: " + reunionActual.getNombre());
        fecha.setText("Fecha: " + reunionActual.getFecha());
        lugar.setText("Lugar: " + reunionActual.getLugar());
    }


}
