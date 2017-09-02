package archivosmultimedia.eam.archivosmultimedia.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Didier_Narváez on 1/09/2017.
 */

public class Reunion implements Serializable {

    private String nombre;
    private String fecha;
    private String lugar;

    public static final String nomArchivo = "reuniones.txt";

    public Reunion() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public static String getNomArchivo() {
        return nomArchivo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
