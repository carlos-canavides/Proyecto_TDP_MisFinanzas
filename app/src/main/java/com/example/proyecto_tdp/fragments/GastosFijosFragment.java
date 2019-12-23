package com.example.proyecto_tdp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterHome;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelSubcategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GastosFijosFragment extends Fragment {

    private View vista;
    private RecyclerView gastos;
    private AdapterHome adapterHome;
    private ViewModelTransaccion viewModelTransaccion;
    private ViewModelSubcategoria viewModelSubcategoria;
    private Map<Transaccion, Integer> mapColorCategoria;
    private List<Transaccion> transacciones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_ingresos_gastos_fijos, container, false);
        gastos = vista.findViewById(R.id.recycler_ingresos_gastos_fijos);
        mapColorCategoria = new HashMap<>();
        transacciones = new ArrayList<>();
        adapterHome = new AdapterHome(transacciones,mapColorCategoria);
        gastos.setLayoutManager(new GridLayoutManager(getActivity(),1));
        gastos.setAdapter(adapterHome);
        inicializarViewModels();
        return vista;
    }

    private void inicializarViewModels(){
        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelSubcategoria = ViewModelProviders.of(getActivity()).get(ViewModelSubcategoria.class);
        recopilarDatos();
    }

    protected void recopilarDatos() {
        LiveData<List<Transaccion>> t = viewModelTransaccion.getAllTransacciones();
        if (t != null) {
            t.observe(getActivity(), new Observer<List<Transaccion>>() {
                @Override
                public void onChanged(List<Transaccion> transaccions) {
                    transacciones.clear();
                    adapterHome.notifyDataSetChanged();
                    transacciones.addAll(transaccions);
                    adapterHome.notifyDataSetChanged();
                    for(Transaccion t : transaccions){
                        Subcategoria subcategoria = viewModelSubcategoria.getSubcategoriaPorNombre(t.getCategoria());
                        mapColorCategoria.put(t,subcategoria.getColorSubcategoria());
                    }
                    adapterHome.notifyDataSetChanged();
                }
            });
        }
        else {
            mostrarMensaje("HOLAAAAAAAAA");
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}