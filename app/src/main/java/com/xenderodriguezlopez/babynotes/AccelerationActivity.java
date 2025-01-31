package com.xenderodriguezlopez.babynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.widget.TextView;

import com.xenderodriguezlopez.babynotes.database.BabyNotesDatabase;
import com.xenderodriguezlopez.babynotes.models.Aceleration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccelerationActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor linearAccelerationSensor;

    // Views para mostrar valores en tiempo real
    private TextView textViewCurrentX;
    private TextView textViewCurrentY;
    private TextView textViewCurrentZ;

    // Views para mostrar valores finales
    private TextView textViewAverage;
    private TextView textViewDuration;
    private TextView textViewTimestamp;

    // Variables para detectar movimiento
    private boolean isMoving = false;          // Indica si el dispositivo está en movimiento
    private static final float THRESHOLD = 0.5f;  // Umbral para considerar que hay movimiento
    private long startTime = 0;               // Momento en que comienza el movimiento

    // Variables para calcular aceleración promedio
    private float sumX = 0f;
    private float sumY = 0f;
    private float sumZ = 0f;
    private int countSamples = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);

        // Inicializa SensorManager y el sensor de aceleración lineal
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager != null) {
            linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }

        // Referencia a TextViews
        textViewCurrentX = findViewById(R.id.textViewCurrentX);
        textViewCurrentY = findViewById(R.id.textViewCurrentY);
        textViewCurrentZ = findViewById(R.id.textViewCurrentZ);

        textViewAverage = findViewById(R.id.textViewAverage);
        textViewDuration = findViewById(R.id.textViewDuration);
        textViewTimestamp = findViewById(R.id.textViewTimestamp);

        // Ajustes de preferencias (color y tamaño de texto)
        applyUserPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar el listener del sensor con SENSOR_DELAY_NORMAL
        if(linearAccelerationSensor != null) {
            sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // Vuelve a aplicar preferencias en caso de que hayan cambiado
        applyUserPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar el sensor para ahorrar batería y no consumir recursos
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Mostrar valores actuales en la UI
            textViewCurrentX.setText(String.format(Locale.getDefault(), "X: %.2f", x));
            textViewCurrentY.setText(String.format(Locale.getDefault(), "Y: %.2f", y));
            textViewCurrentZ.setText(String.format(Locale.getDefault(), "Z: %.2f", z));

            // Calcular la magnitud total de la aceleración
            float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

            if(!isMoving && magnitude > THRESHOLD) {
                // Comienza movimiento
                isMoving = true;
                startTime = System.currentTimeMillis();
                // Reiniciar sumas y conteos
                sumX = 0f;
                sumY = 0f;
                sumZ = 0f;
                countSamples = 0;
            }

            if(isMoving) {
                // Acumular valores para promedio
                sumX += x;
                sumY += y;
                sumZ += z;
                countSamples++;


                if(magnitude < THRESHOLD) {
                    isMoving = false;

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime; // tiempo en movimiento (ms)

                    // Calcular promedio
                    float avgX = sumX / countSamples;
                    float avgY = sumY / countSamples;
                    float avgZ = sumZ / countSamples;

                    // Mostrar resultados
                    textViewAverage.setText(String.format(
                            Locale.getDefault(),
                            "Promedio X=%.2f, Y=%.2f, Z=%.2f",
                            avgX, avgY, avgZ
                    ));

                    textViewDuration.setText(String.format(
                            Locale.getDefault(),
                            "Duración (ms): %d",
                            duration
                    ));

                    // Generar timestamp de inicio (formato YYYY-MM-DD HH:mm:ss)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String startTimestamp = sdf.format(new Date(startTime));
                    textViewTimestamp.setText("Inicio: " + startTimestamp);

                    // Guardar en la base de datos
                    saveAccelerationDataToDB(startTimestamp, duration, avgX, avgY, avgZ);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void saveAccelerationDataToDB(String startTimestamp, long duration, float avgX, float avgY, float avgZ) {

        BabyNotesDatabase db = BabyNotesDatabase.getInstance(getApplicationContext());

        Aceleration acel = new Aceleration();
        acel.startTimestamp = startTimestamp;
        acel.durationMillis = duration;
        acel.avgX = avgX;
        acel.avgY = avgY;
        acel.avgZ = avgZ;

        db.acelerationDao().insertAceleration(acel);
    }



    private void applyUserPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String textSize = prefs.getString("pref_text_size", "medium"); // "small", "medium", "large"
        String textColor = prefs.getString("pref_text_color", "#000000"); // valor por defecto: negro

        float sizeInSP;
        switch (textSize) {
            case "small":
                sizeInSP = 14f;
                break;
            case "large":
                sizeInSP = 24f;
                break;
            case "medium":
            default:
                sizeInSP = 18f;
                break;
        }

        try {

            int color = Color.parseColor(textColor);

            // Aplicar a los TextViews
            textViewCurrentX.setTextSize(sizeInSP);
            textViewCurrentX.setTextColor(color);

            textViewCurrentY.setTextSize(sizeInSP);
            textViewCurrentY.setTextColor(color);

            textViewCurrentZ.setTextSize(sizeInSP);
            textViewCurrentZ.setTextColor(color);

            textViewAverage.setTextSize(sizeInSP);
            textViewAverage.setTextColor(color);

            textViewDuration.setTextSize(sizeInSP);
            textViewDuration.setTextColor(color);

            textViewTimestamp.setTextSize(sizeInSP);
            textViewTimestamp.setTextColor(color);

        } catch (IllegalArgumentException e) {
            // Si el color no es válido, usar negro por defecto
            textViewCurrentX.setTextColor(Color.BLACK);
            textViewCurrentY.setTextColor(Color.BLACK);
            textViewCurrentZ.setTextColor(Color.BLACK);
            textViewAverage.setTextColor(Color.BLACK);
            textViewDuration.setTextColor(Color.BLACK);
            textViewTimestamp.setTextColor(Color.BLACK);
        }
    }
}
