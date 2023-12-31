package com.example.contactosysensores;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.contactosysensores.Contact;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class AppActivity extends AppCompatActivity {

    private Contact contact;
    private Button btnIrAcelerometro;
    private boolean mostrarAcelerometro = true;
    private TextView textViewTipoDeSensor;
    private Button btnSensor;
    private FragmentManager fragmentManager;
    private Button btnAnadir;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        textViewTipoDeSensor = findViewById(R.id.textViewTipoDeSensor);
        btnSensor = findViewById(R.id.btnSensor);
        fragmentManager = getSupportFragmentManager();
        btnAnadir = findViewById(R.id.btnAnadir);
        btnAnadir.setOnClickListener(v -> addRandomContactToActiveFragment());
        progressBar = findViewById(R.id.progressBar);


        loadMagnetometerFragment();

        btnSensor.setOnClickListener(v -> {
            if (textViewTipoDeSensor.getText().equals("Magnetómetro")) {
                loadAccelerometerFragment();
            } else {
                loadMagnetometerFragment();
            }
        });

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> showSensorDescriptionDialog());

    }

    private void showSensorDescriptionDialog() {
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        String message;
        String titulo;

        if (activeFragment instanceof AcelerometroFragment) {
            titulo ="Detalles - Acelerómetro";
            message = "Haga CLICK en Añadir para agregar contactos a su lista. Esta aplicación está utilizando el ACELEROMETRO de su dispositivo. De esta forma, la lista hará scroll hacia abajo, cuando agite su dispositivo.";
        } else if (activeFragment instanceof MagnetometroFragment) {
            titulo ="Detalles - MAGNETÓMETRO";
            message = "Haga CLICK on Añadir para agregar contactos a su lista. Esta aplicación está utilizando el MAGNETÓMETRO de su dispositivo. De esta forma, la lista se mostrara al 100% cuando se apurte al NORTE. Caso contrario se desvanecerá";
        } else {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void loadMagnetometerFragment() {
        MagnetometroFragment fragment = new MagnetometroFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();

        // Limpia el BackStack
        clearBackStack();

        textViewTipoDeSensor.setText("Magnetómetro");
        btnSensor.setText("Ir a Acelerómetro");
    }

    private void loadAccelerometerFragment() {
        AcelerometroFragment fragment = new AcelerometroFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();

        // Limpia el BackStack
        clearBackStack();

        textViewTipoDeSensor.setText("Acelerómetro");
        btnSensor.setText("Ir a Magnetómetro");
    }

    private void clearBackStack() {
        int backStackEntry = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackEntry; i++) {
            fragmentManager.popBackStack();
        }
    }

    private void addRandomContactToActiveFragment() {
        // Llama a la API de randomuser.me para obtener un contacto aleatorio.
        fetchRandomContact();
    }

    private void fetchRandomContact() {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            btnAnadir.setEnabled(false);
            btnSensor.setEnabled(false);
            btnAnadir.setAlpha(0.5f);
            btnSensor.setAlpha(0.5f);
        });
        new Thread(() -> {
            try {
                URL url = new URL("https://randomuser.me/api/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    JSONObject contactJson = resultsArray.getJSONObject(0);

                    // Parsea los datos del contacto desde el JSON
                    JSONObject nameObj = contactJson.getJSONObject("name");
                    String name = nameObj.getString("title") + " " +
                            nameObj.getString("first") + " " +
                            nameObj.getString("last");

                    String gender = contactJson.getString("gender");
                    String city = contactJson.getJSONObject("location").getString("city");
                    String country = contactJson.getJSONObject("location").getString("country");
                    String email = contactJson.getString("email");
                    String phone = contactJson.getString("phone");
                    String imageUrl = contactJson.getJSONObject("picture").getString("large");


                    // Crea un objeto Contact con los datos
                    Contact contact = new Contact(imageUrl, name, gender, city, country, email, phone);


                    runOnUiThread(() -> {
                        // Agrega el contacto al fragmento activo
                        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

                        if (activeFragment instanceof AcelerometroFragment) {
                            AcelerometroFragment acelerometroFragment = (AcelerometroFragment) activeFragment;
                            acelerometroFragment.addContact(contact);
                        } else if (activeFragment instanceof MagnetometroFragment) {
                            MagnetometroFragment magnetometroFragment = (MagnetometroFragment) activeFragment;
                            magnetometroFragment.addContact(contact);
                        }
                        btnAnadir.setEnabled(true);
                        btnSensor.setEnabled(true);
                        btnAnadir.setAlpha(1f); // 100% de opacidad
                        btnSensor.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(AppActivity.this, "Ocurrió un error al agregar contacto", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(AppActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    btnAnadir.setEnabled(true);
                    btnSensor.setEnabled(true);
                    btnAnadir.setAlpha(1f);
                    btnSensor.setAlpha(1f);
                    progressBar.setVisibility(View.GONE);
                });
            }
        }).start();
    }



}






