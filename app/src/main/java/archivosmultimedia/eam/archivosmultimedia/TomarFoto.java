package archivosmultimedia.eam.archivosmultimedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class TomarFoto extends AppCompatActivity {

    ArrayList<ImageView> fotos;
    ListView lvFotos;
    String archivoCantidad = "archivoCantidad.txt";
    int cantidadFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);

        lvFotos = (ListView) findViewById(R.id.lvFotos);
        fotos = new ArrayList<>();
        cargarNumero();
        recuperarFotos();
        cargarFotos();

    }

    public void tomarfoto(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cantidadFotos == 0) {
            //"android.resource://archivosmultimedia.eam.archivosmultimedia/drawable"
            i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
                    "/" + Reunion.getActual().getNombre() + "1");
        } else {
            i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
                    "/" + Reunion.getActual().getNombre() + cantidadFotos + 1 + ".jpg");
        }
        startActivity(i);
        cantidadFotos++;
        guardar();
        recuperarFotos();
        cargarFotos();
    }

    public void recuperarFotos() {

        fotos.clear();

        if (cantidadFotos != 0) {
            for (int i = 1; i <= cantidadFotos; i++) {
                ImageView imagen = new ImageView(this);
                Bitmap bitmap1 =
                        BitmapFactory.decodeFile(
                                getExternalFilesDir(null) +
                                        "/" + Reunion.getActual().getNombre() + i + ".jpg"
                        );
                imagen.setImageBitmap(bitmap1);
                fotos.add(imagen);
            }
        } else {
            Toast.makeText(this, "La galería de fotos esta vacía", Toast.LENGTH_SHORT).show();
        }

    }

    public void cargarNumero() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, Reunion.getNomArchivo())) {
                InputStreamReader reader = new InputStreamReader(
                        openFileInput(archivoCantidad));
                BufferedReader br = new BufferedReader(reader);
                String linea = br.readLine();
                if (linea == null){
                    cantidadFotos = 0;
                } else {
                    cantidadFotos = Integer.parseInt(linea.toString());
                }
                br.close();
                reader.close();
            }
        } catch (IOException e) {

        }
    }

    public void cargarFotos() {
        ArrayAdapter<ImageView> adapter = new ArrayAdapter<ImageView>(this,
                android.R.layout.simple_list_item_1, fotos);
        lvFotos.setAdapter(adapter);
    }

    public void guardar() {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(
                    openFileOutput(archivoCantidad, Activity.MODE_PRIVATE));

            writer.write(cantidadFotos+"");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
