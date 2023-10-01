package com.example.contactosysensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class MagnetometroFragment extends Fragment implements SensorEventListener {

    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private SensorManager sensorManager;
    private Sensor magneticFieldSensor;
    private float[] mGeomagnetic = new float[3];

    public MagnetometroFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_acelerometro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactAdapter(getContext(), contactList);
        recyclerView.setAdapter(adapter);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void addContact(Contact contact) {
        if (contactList != null && adapter != null) {
            contactList.add(contact);
            adapter.notifyDataSetChanged();
        }
    }

    public void updateContactList(List<Contact> contacts) {
        this.contactList.clear();
        this.contactList.addAll(contacts);
        adapter.notifyDataSetChanged();
    }

    public void setContactList(List<Contact> contacts) {
        if (this.contactList != null) {
            this.contactList.clear();
            this.contactList.addAll(contacts);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void removeContact(int position) {
        contactList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
            float azimut = computeAzimuth(mGeomagnetic);
            adjustVisibilityBasedOnAzimuth(azimut);
        }
    }

    private float computeAzimuth(float[] geomagnetic) {
        float azimuth = (float) Math.toDegrees(geomagnetic[0]);
        return (azimuth + 360) % 360;
    }

    private void adjustVisibilityBasedOnAzimuth(float azimuth) {
        // Calcular visibilidad
        float visibilityPercentage;
        if (azimuth <= 180) {  // Norte
            visibilityPercentage = Math.min(1, 1 - (azimuth / 180));
        } else {  // Sur
            visibilityPercentage = 0;
        }

        recyclerView.setAlpha(visibilityPercentage);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}


