package archivosmultimedia.eam.archivosmultimedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class ListaReuniones extends AppCompatActivity {

    ListView listaReuniones;
    ArrayList<Reunion> reuniones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reuniones);

        listaReuniones = (ListView) findViewById(R.id.lvReuniones);
        reuniones = new ArrayList<>();

        listarReuniones();

        listaReuniones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Reunion reu = (Reunion) listaReuniones.getItemAtPosition(position);
                Reunion.setActual(reu);
                Intent intent = new Intent(getApplicationContext(), InfoReunion.class);
                startActivity(intent);
            }
        });

    }

    public void listarReuniones(){
        try {
            String[] a = fileList();
            if (existeArchivo(a, Reunion.getNomArchivo())) {
                ObjectInputStream reader = new ObjectInputStream(openFileInput(Reunion.getNomArchivo()));
                reuniones = (ArrayList<Reunion>) reader.readObject();
                reader.close();
            }

            if (reuniones.size() != 0){
                ArrayAdapter<Reunion> adapter = new ArrayAdapter<Reunion>(this,
                        android.R.layout.simple_list_item_1, reuniones);
                listaReuniones.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No hay reuniones registradas", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e){

        } catch (ClassNotFoundException c){

        }
    }

    public void regresarMain(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private boolean existeArchivo(String[] archivos, String archBuscar) {

        for (int f = 0; f < archivos.length; f++) {
            if (archBuscar.equals(archivos[f])) {
                return true;
            }

        }
        return false;
    }

}
