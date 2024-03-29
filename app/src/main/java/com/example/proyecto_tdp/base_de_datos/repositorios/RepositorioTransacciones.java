package com.example.proyecto_tdp.base_de_datos.repositorios;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.example.proyecto_tdp.base_de_datos.AppDataBase;
import com.example.proyecto_tdp.base_de_datos.TransaccionDao;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RepositorioTransacciones {

    private TransaccionDao transaccionDao;
    private LiveData<List<Transaccion>> transacciones;

    public RepositorioTransacciones(Application application) {
        AppDataBase dataBase = Room.databaseBuilder(application, AppDataBase.class, "database_app").build();
        transaccionDao = dataBase.transaccionDao();
        transacciones = transaccionDao.getAllLiveTransacciones();
    }

    public LiveData<List<Transaccion>> getTransacciones(){
        return transacciones;
    }

    public List<Transaccion> getTransaccionesDesdeHasta(String desde, String hasta){
        try {
            return new ObtenerTransaccionesDesdeHastaAsyncTask(transaccionDao).execute(desde,hasta).get();
        }catch(ExecutionException|InterruptedException e){e.printStackTrace();}
        return null;
    }

    public LiveData<List<Transaccion>> getLiveTransaccionesDesdeHasta(String desde, String hasta){
        try {
            return new ObtenerLiveTransaccionesDesdeHastaAsyncTask(transaccionDao).execute(desde,hasta).get();
        }catch(ExecutionException|InterruptedException e){e.printStackTrace();}
        return null;
    }

    public void insertarTransaccion(Transaccion transaccion){
        new InsertTransaccionAsyncTask(transaccionDao).execute(transaccion);
    }

    public void actualizarTransaccion(Transaccion transaccion){
        new ActualizarTransaccionAsyncTask(transaccionDao).execute(transaccion);
    }

    public void eliminarTransaccion(Transaccion transaccion){
        new EliminarTransaccionAsyncTask(transaccionDao).execute(transaccion);
    }

    public void eliminarTransaccionPorID(String id){
        new EliminarTransaccionIDAsyncTask(transaccionDao).execute(id);
    }

    public void eliminarTransaccionesHijas(String idPadre){
        new EliminarTransaccionesHijasAsyncTask(transaccionDao).execute(idPadre);
    }

    public static class InsertTransaccionAsyncTask extends AsyncTask<Transaccion,Void,Void> {
        private TransaccionDao transaccionDao;

        private InsertTransaccionAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(Transaccion... transaccion) {
            transaccionDao.insertTransaccion(transaccion[0]);
            return null;
        }
    }

    public static class ActualizarTransaccionAsyncTask extends AsyncTask<Transaccion,Void,Void> {
        private TransaccionDao transaccionDao;

        private ActualizarTransaccionAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(Transaccion... transaccion) {
            transaccionDao.upDateTransaccion(transaccion[0]);
            return null;
        }
    }

    public static class EliminarTransaccionAsyncTask extends AsyncTask<Transaccion,Void,Void> {
        private TransaccionDao transaccionDao;

        private EliminarTransaccionAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(Transaccion... transaccion) {
            transaccionDao.deleteTransaccion(transaccion[0]);
            return null;
        }
    }

    public static class EliminarTransaccionIDAsyncTask extends AsyncTask<String,Void,Void> {
        private TransaccionDao transaccionDao;

        private EliminarTransaccionIDAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(String... id) {
            transaccionDao.eliminarTransaccion(id[0]);
            return null;
        }
    }

    public static class EliminarTransaccionesHijasAsyncTask extends AsyncTask<String,Void,Void> {
        private TransaccionDao transaccionDao;

        private EliminarTransaccionesHijasAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(String... idPadre) {
            transaccionDao.eliminarTransaccionesHijas(idPadre[0]);
            return null;
        }
    }

    public static class ObtenerTransaccionesDesdeHastaAsyncTask extends AsyncTask<String,Void,List<Transaccion>> {
        private TransaccionDao transaccionDao;

        private ObtenerTransaccionesDesdeHastaAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected List<Transaccion> doInBackground(String... periodo) {
            return transaccionDao.getTransaccionesDesdeHasta(periodo[0],periodo[1]);
        }
    }

    public static class ObtenerLiveTransaccionesDesdeHastaAsyncTask extends AsyncTask<String,Void,LiveData<List<Transaccion>>> {
        private TransaccionDao transaccionDao;

        private ObtenerLiveTransaccionesDesdeHastaAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected LiveData<List<Transaccion>> doInBackground(String... periodo) {
            return transaccionDao.getLiveTransaccionesDesdeHasta(periodo[0],periodo[1]);
        }
    }
}
