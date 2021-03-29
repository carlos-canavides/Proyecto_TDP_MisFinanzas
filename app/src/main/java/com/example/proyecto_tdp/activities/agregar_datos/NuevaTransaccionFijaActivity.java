package com.example.proyecto_tdp.activities.agregar_datos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.activities.SeleccionarCategoriaActivity;
import com.example.proyecto_tdp.views.CalculatorInputDialog;
import com.example.proyecto_tdp.views.CalendarioDialog;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NuevaTransaccionFijaActivity extends AppCompatActivity {

    protected TextView campoPrecio;
    protected TextView campoCategoria;
    protected TextView campoFecha;
    protected TextView campoFechaFinal;
    protected EditText campoTitulo;
    protected EditText campoEtiqueta;
    protected EditText campoInfo;
    protected RadioButton btnGasto;
    protected RadioButton btnIngreso;
    protected Button btnAceptar;
    protected Button btnCancelar;
    protected Spinner frecuencia;
    protected DateTimeFormatter formatoFecha;
    protected NumberFormat formatoNumero;
    protected CalendarioDialog calendarioDialog;
    protected CalendarioDialog calendarioDialogFinal;
    protected CalculatorInputDialog calculatorInputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_transaccion_fija);

        campoInfo = findViewById(R.id.set_tf_campo_info);
        campoFecha = findViewById(R.id.set_tf_campo_fecha);
        campoPrecio = findViewById(R.id.set_tf_campo_precio);
        campoTitulo = findViewById(R.id.set_tf_campo_titulo);
        campoEtiqueta = findViewById(R.id.set_tf_campo_etiqueta);
        campoCategoria = findViewById(R.id.set_tf_campo_categoria);
        campoFechaFinal = findViewById(R.id.set_tf_campo_fecha_final);
        btnGasto = findViewById(R.id.set_tf_radiobtn_gasto);
        btnIngreso = findViewById(R.id.set_tf_radiobtn_ingreso);
        btnAceptar = findViewById(R.id.set_tf_btn_aceptar);
        btnCancelar = findViewById(R.id.set_tf_btn_eliminar);
        frecuencia = findViewById(R.id.set_tf_frecuencia);
        formatoFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
        formatoNumero = NumberFormat.getInstance(new Locale("es", "ES"));

        inicializarValoresCampos();
        definirIngresarMonto();
        definirSeleccionarFecha();
        definirSeleccionarCategoria();
        listenerBotonesPrincipales();
    }

    protected void inicializarValoresCampos(){
        campoInfo.setText("");
        campoFecha.setText("");
        campoTitulo.setText("");
        campoPrecio.setText("0,00");
        campoEtiqueta.setText("");
        campoCategoria.setText("Seleccionar Categoria");
        campoFechaFinal.setText("Seleccionar Fecha Final");
        btnGasto.setChecked(true);
        btnIngreso.setChecked(false);
        btnCancelar.setText("Cancelar");
        ArrayList<String> opcionesFrecuencia = new ArrayList<>();
        opcionesFrecuencia.add(Constantes.SELECCIONAR_FRECUENCIA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_SOLO_UNA_VEZ);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_MES);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO);
        ArrayAdapter<String> adapterFrecuencia = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcionesFrecuencia);
        frecuencia.setAdapter(adapterFrecuencia);
    }

    protected void definirIngresarMonto(){
        calculatorInputDialog = new CalculatorInputDialog(this);
        calculatorInputDialog.setPositiveButton(new CalculatorInputDialog.OnInputDoubleListener() {
            @Override
            public boolean onInputDouble(AlertDialog dialog, Double value) {
                campoPrecio.setText(String.format( "%.2f", value));
                return false;
            }
        });
        campoPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorInputDialog.show();
            }
        });
    }

    protected void definirSeleccionarFecha(){
        calendarioDialog = new CalendarioDialog();
        calendarioDialog.setListener(new CalendarioDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(Date date) throws Exception {
                campoFecha.setText(formatoFecha.print(date.getTime()));
            }
        });
        campoFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioDialog.show(getSupportFragmentManager(), getClass().getSimpleName());
            }
        });
        calendarioDialogFinal = new CalendarioDialog();
        calendarioDialogFinal.setListener(new CalendarioDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(Date date) throws Exception {
                campoFechaFinal.setText(formatoFecha.print(date.getTime()));
            }
        });
        campoFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioDialogFinal.show(getSupportFragmentManager(), getClass().getSimpleName());
            }
        });
    }

    protected void definirSeleccionarCategoria(){
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevaTransaccionFijaActivity.this, SeleccionarCategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    protected void listenerBotonesPrincipales(){
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = campoInfo.getText().toString();
                String titulo = campoTitulo.getText().toString();
                String precio = campoPrecio.getText().toString();
                String etiqueta = campoEtiqueta.getText().toString();
                String categoria = campoCategoria.getText().toString();
                String fechaInicio = campoFecha.getText().toString();
                String fechaFinal = campoFechaFinal.getText().toString();
                String frecuenciaSeleccionada = frecuencia.getSelectedItem().toString();
                String tipo;
                if(btnGasto.isChecked()){
                    tipo = btnGasto.getText().toString();
                }
                else {
                    tipo = btnIngreso.getText().toString();
                }

                Intent intent = new Intent();
                intent.putExtra(Constantes.CAMPO_INFO, info);
                intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                intent.putExtra(Constantes.CAMPO_CATEGORIA, categoria);
                intent.putExtra(Constantes.CAMPO_FECHA, fechaInicio);
                intent.putExtra(Constantes.CAMPO_FECHA_FINAL, fechaFinal);
                intent.putExtra(Constantes.CAMPO_FRECUENCIA, frecuenciaSeleccionada);
                try {
                    if(tipo.equals(Constantes.INGRESO)){
                        intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue());
                    }
                    else {
                        intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue()*(-1));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constantes.PEDIDO_SELECCIONAR_CATEGORIA) {
            if (resultCode == RESULT_OK) {
                String idCategoriaElegida = data.getStringExtra("id_categoria_elegida");
                campoCategoria.setText(idCategoriaElegida);
            }
        }
    }
}