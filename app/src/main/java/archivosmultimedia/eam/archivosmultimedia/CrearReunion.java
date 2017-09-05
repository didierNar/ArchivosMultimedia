package archivosmultimedia.eam.archivosmultimedia;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class CrearReunion extends AppCompatActivity implements View.OnClickListener {

    EditText nombre;
    EditText lugar;
    EditText fecha;

    ArrayList<Reunion> reuniones;

    private int dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reunion);

        fecha = (EditText) findViewById(R.id.etFecha);
        lugar = (EditText) findViewById(R.id.etLugar);
        nombre = (EditText) findViewById(R.id.etNombre);

        fechaActual();

        reuniones = new ArrayList<>();

        listarReuniones();

    }

    public void regresar(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Registra una reunión
     *
     * @param v la vista
     */
    public void registrarReunion(View v) {

        String fe = fecha.getText().toString();
        String lug = lugar.getText().toString();
        String nom = nombre.getText().toString();

        boolean entro = false;

        if (fe.isEmpty() || lug.isEmpty() || nom.isEmpty()) {
            Toast.makeText(this, "Debe diligenciar " +
                    "todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (reuniones.size() != 0) {
                for (Reunion re : reuniones) {
                    if (re.getNombre().equalsIgnoreCase(nom)) {
                        Toast.makeText(this, "Esta reunión ya existe", Toast.LENGTH_SHORT).show();
                        entro = true;
                        break;
                    }
                }
            }
            if (!entro) {
                Reunion r = new Reunion();
                r.setFecha(fe);
                r.setLugar(lug);
                r.setNombre(nom);
                reuniones.add(r);
                guardar(reuniones);
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void listarReuniones() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, Reunion.getNomArchivo())) {
                ObjectInputStream reader = new ObjectInputStream(openFileInput(Reunion.getNomArchivo()));
                reuniones = (ArrayList<Reunion>) reader.readObject();
                reader.close();
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException c) {

        }
    }

    public void guardar(ArrayList<Reunion> re) {
        try {
            ObjectOutputStream writer = new ObjectOutputStream
                    (openFileOutput(Reunion.getNomArchivo(), Activity.MODE_PRIVATE));
            writer.writeObject(re);
            writer.flush();
            writer.close();
            fechaActual();
            lugar.setText("");
            nombre.setText("");
        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void fechaActual() {
        Calendar c = Calendar.getInstance();
        int dd = c.get(Calendar.DAY_OF_MONTH);
        int mm = c.get(Calendar.MONTH)+1;
        int yy = c.get(Calendar.YEAR);
        fecha.setText(dd + "/" + mm + "/" + yy);
    }

    private boolean existeArchivo(String[] archivos, String archBuscar) {

        for (int f = 0; f < archivos.length; f++) {
            if (archBuscar.equals(archivos[f])) {
                return true;
            }

        }
        return false;


    }


    @Override
    public void onClick(View v) {

        if (v == fecha) {
            final Calendar c = Calendar.getInstance();

            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH) + 1;
            anio = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
                    , anio, mes, dia);
            datePickerDialog.show();
        }

    }

}
