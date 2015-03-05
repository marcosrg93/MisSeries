package es.marcosfrancisco.misseries;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by MarcosFrancisco on 05/03/2015.
 */
public class Series implements Parcelable{

    String nombre;
    int annio;
    int temporadas;

    public Series (){
        this.nombre = "";
        this.annio = 0;
        this.temporadas = 0;
    }
    public Series(String nombre, int annio, int temporadas) {
        this.nombre = nombre;
        this.annio = annio;
        this.temporadas = temporadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnnio() {
        return annio;
    }

    public void setAnnio(int annio) {
        this.annio = annio;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Series)) return false;

        Series series = (Series) o;

        if (annio != series.annio) return false;
        if (temporadas != series.temporadas) return false;
        if (nombre != null ? !nombre.equals(series.nombre) : series.nombre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nombre != null ? nombre.hashCode() : 0;
        result = 31 * result + annio;
        result = 31 * result + temporadas;
        return result;
    }

    @Override
    public String toString() {
        return "Series{" +
                "nombre='" + nombre + '\'' +
                ", año=" + annio +
                ", temporadas=" + temporadas +
                '}';
    }



    /**************************** Para hacerlo Parcelable ************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //El orden es muy importante, porque a la hora de coger los elementos, se cogen por orden.
        parcel.writeSerializable(this.nombre);
        parcel.writeInt(this.annio);
        parcel.writeInt(this.temporadas);
    }

    public Series (Parcel p){
        this.nombre = String.valueOf(p.readSerializable());
        this.annio = p.readInt();
        this.temporadas = p.readInt();
    }

    public static final Parcelable.Creator<Series> CREATOR =
            new Parcelable.Creator<Series>(){

                @Override
                public Series createFromParcel(Parcel parcel) {
                    return new Series(parcel);
                }

                @Override
                public Series[] newArray(int i) {
                    return new Series[i];
                }
            };
}
