package com.mazinger.masterdelivery;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mazinger.masterdelivery.modelo.Usuariodirecciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.mazinger.masterdelivery.Muestraproductosporempresa.CONNECTION_TIMEOUT;
import static com.mazinger.masterdelivery.Registrodeusuario.READ_TIMEOUT;

public class Mapacitonulo extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final int REQUEST_FINE_LOCATION = 0;
    String FileName = "myfile";
    SharedPreferences prefs;
    String apiKey = "MY API KEY";
    private GoogleMap mapa;
    private final LatLng Sopdapop = new LatLng(-11.495692495131408, -77.208248);
    private FusedLocationProviderClient mFusedLocationClient;
    TextView direccion;
    EditText referencia;
    Button listo, aregistro;
    //private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    //private long FASTEST_INTERVAL = 2000; /* 2 sec */

    public Double longituduser;
    public Double latituduser;


    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapacitonulo);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa1);
        mapFragment.getMapAsync(this);
        referencia = (EditText) findViewById(R.id.referencia1);

        direccion = (TextView) findViewById(R.id.direc1);
        listo = (Button) findViewById(R.id.listo1);
        aregistro = (Button) findViewById(R.id.aregistro1);
        mFusedLocationClient = getFusedLocationProviderClient(this);

//        checkRunTimePermission();


        aregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (direccion.getText().toString().equals("")) {


                    Toast.makeText(getApplication(), "Debes seleccionar una direccion y activar tu gps.", Toast.LENGTH_LONG).show();


                } else {

                    String hh = String.valueOf(longituduser).toString();
                    String pp = String.valueOf(latituduser).toString();
                    Toast.makeText(getApplication(), hh + pp, Toast.LENGTH_LONG).show();
                    guardardireccionlatitudylongitud(direccion.getText().toString(), referencia.getText().toString(), latituduser, longituduser);

                    prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
                    String idfirebase=prefs.getString("idfirebase","");
                    Usuariodirecciones ml=new Usuariodirecciones(0,idfirebase,direccion.getText().toString(),hh,
                            pp,referencia.getText().toString());

                    new grabardireccionusuario().execute(ml);

///guardardireccionnueva



                    Intent intent = new Intent(getApplicationContext(), Activitycuentausuario.class);

                    startActivity(intent);


                }


            }

        });
        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();


            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        checkPermissions();
        Toast.makeText(this, "Activa tu gps por favor", Toast.LENGTH_LONG).show();
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(Sopdapop, 13));
        //  mapa.setMyLocationEnabled(true);
        // mapa.getUiSettings().setCompassEnabled(true);

        Marker marker = mapa.addMarker(new MarkerOptions()

                .position(Sopdapop)
                .title("mi casita")
                .snippet("aqui recibire mis pedidos :)")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_directions))
                .anchor(0.5f, 0.5f)
        );

        mapa.setInfoWindowAdapter(new Marketclaselocal(LayoutInflater.from(getApplicationContext())));
        marker.showInfoWindow();


    }


    @Override
    public void onMapClick(LatLng latLng) {
        mapa.clear();

        Marker markerName = null;
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latLng.latitude,
                    latLng.longitude, 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getAddressLine(0));
                longituduser = latLng.longitude;
                latituduser = latLng.latitude;
                cityName = addresses.get(0).getAddressLine(0);
                direccion.setText(cityName);
                if (direccion.getText().equals("")) {
                    Toast.makeText(this, "Activa tu gps para ubicarte por favor", Toast.LENGTH_LONG).show();
                } else {


                    Marker marker = mapa.addMarker(new MarkerOptions()

                            .position(latLng)
                            .title("mi casita")
                            .snippet("aqui recibire mis pedidos :)")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(android.R.drawable.ic_menu_directions))
                            .anchor(0.5f, 0.5f)
                    );

                    mapa.setInfoWindowAdapter(new markerclase(LayoutInflater.from(getApplicationContext())));
                    marker.showInfoWindow();


                }

            }
        } catch (IOException e) {
            // Toast.makeText(this,"presionaste"+e.toString().trim(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        //////////////////////////////////7777
        //////con que frecuencia se actualiza la ubicacion /////
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // mLocationRequest.setInterval(UPDATE_INTERVAL);
        // mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }


    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Toast.makeText(this,"longitud"+String.valueOf(latLng.latitude)+"longitud:  "+String.valueOf(latLng.longitude),Toast.LENGTH_LONG).show();

        mapa.clear();
        ;

        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        // mapa.setMyLocationEnabled(true);
        //mapa.getUiSettings().setCompassEnabled(true);

        longituduser = latLng.longitude;
        latituduser = latLng.latitude;
        Marker marker = mapa.addMarker(new MarkerOptions()

                .position(latLng)
                .title("mi casita")
                .snippet("aqui recibire mis pedidos :)")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_directions))
                .anchor(0.5f, 0.5f)
        );

        mapa.setInfoWindowAdapter(new markerclase(LayoutInflater.from(getApplicationContext())));
        marker.showInfoWindow();

        mapa.setOnMapClickListener(this);
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {

            addresses = gcd.getFromLocation(latLng.latitude,
                    latLng.longitude, 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getAddressLine(0));

                String cityName = addresses.get(0).getAddressLine(0);

                direccion.setText(cityName);

                if (direccion.getText().equals("")) {
                    Toast.makeText(this, "Activa tu gps para ubicarte por favor", Toast.LENGTH_LONG).show();
                } else {


                }

            }
        } catch (IOException e) {
            //  Toast.makeText(this,"presionaste"+e.toString().trim(),Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }


    }


    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }


    private boolean checkPermissions() {

        if ((ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) ){


            return true;

        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION);
            return false;

        }

    }

    private void revaamapa() {
        Intent intent = new Intent(getApplicationContext(), Mapa.class);

        startActivity(intent);

    }


    public void guardardireccionlatitudylongitud(String direccion, String referencia, Double latitud, Double longitud) {
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("direccionusuario", direccion);
        editor.putString("referenciausuario", referencia);
        editor.putString("latitudusuario", String.valueOf(latitud));
        editor.putString("longitudusuario", String.valueOf(longitud));
        editor.commit();

    }

    private class grabardireccionusuario extends AsyncTask<Usuariodirecciones, Void, String> {
        String resultado;
        HttpURLConnection conne;
        URL url = null;
        Usuariodirecciones ped;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Usuariodirecciones... params) {
            ped=params[0];
            try {
                url = new URL("https://sodapop.pe/sugest/apigrabardirecciondeusuario.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                // Append parameters to URL


                Log.d("valor",String.valueOf(ped.getLongitud()));
                Uri.Builder builder = new Uri.Builder()

                          .appendQueryParameter("idfirebase",String.valueOf(ped.getIdfirebase()))
                        .appendQueryParameter("direccion",String.valueOf(ped.getDireccion()))
                        .appendQueryParameter("longitud", String.valueOf(ped.getLongitud()))
                        .appendQueryParameter("latitud", String.valueOf(ped.getLatitud()))
                        .appendQueryParameter("referencia", String.valueOf(ped.getReferencia()))

                        ;



                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conne.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conne.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
            try {
                int response_code = conne.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conne.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);

                    }
                    resultado=result.toString();
                    Log.d("paso",resultado.toString());
                    return resultado;

                } else {

                }
            } catch (IOException e) {
                e.printStackTrace()                ;
                Log.d("valorito",e.toString());
                return null;
            } finally {
                conne.disconnect();
            }
            return resultado;
        }
        @Override
        protected void onPostExecute(String resultado) {

            super.onPostExecute(resultado);

            if(resultado.equals("true")){
                Log.d("ii", resultado);


            }else{
                String ii =resultado.toString();
                Log.d("jj", "usuario valido");


                // lanzarsistema();
            }



        }
    }

}

