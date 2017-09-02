package archivosmultimedia.eam.archivosmultimedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import archivosmultimedia.eam.archivosmultimedia.modelo.Reunion;

public class TomarFoto extends AppCompatActivity {

    ArrayList<ImageView> fotos;
    ListView lvFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);


        lvFotos = (ListView) findViewById(R.id.lvFotos);
        fotos = new ArrayList<>();
        listarFotos();
        recuperarFotos();
        cargarFotos();

    }

    public void tomarfoto (View v){
        int tamanio = fotos.size();
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        i.putExtra(MediaStore.EXTRA_OUTPUT, getExternalFilesDir(null) +
                "/" + Reunion.getActual().getNombre() + tamanio);
        startActivity(i);
        cargarFotos();
    }

    public void recuperarFotos (){

        if (fotos.size() != 0) {
            int tam = fotos.size();
            for (int i = 0; i < tam; i++) {
                ImageView imagen = new ImageView(this);
                Bitmap bitmap1 =
                        BitmapFactory.decodeFile(
                                getExternalFilesDir(null) +
                                        "/" + Reunion.getActual().getNombre() + i + 1
                        );
                //Se añade la foto al ImageView
                imagen.setImageBitmap(bitmap1);
                fotos.add(imagen);
            }
        } else {
            Toast.makeText(this, "La galería de fotos esta vacía", Toast.LENGTH_SHORT).show();
        }

    }

    public void listarFotos() {
        try {
            String[] a = fileList();
            if (existeArchivo(a, Reunion.getNomArchivo())) {
                ObjectInputStream reader = new ObjectInputStream(openFileInput(Reunion.getNomArchivo()));
                fotos = (ArrayList<ImageView>) reader.readObject();
                reader.close();
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException c) {

        }
    }

    public void cargarFotos(){
        ArrayAdapter<ImageView> adapter = new ArrayAdapter<ImageView>(this,
                android.R.layout.simple_list_item_1, fotos);
        lvFotos.setAdapter(adapter);
    }

    public void guardar(ArrayList<Reunion> re) {
        try {
            ObjectOutputStream writer = new ObjectOutputStream
                    (openFileOutput(Reunion.getNomArchivo(), Activity.MODE_PRIVATE));
            writer.writeObject(re);
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
