package com.example.pockerguide.map;


import static android.content.Context.MODE_PRIVATE;

import static com.example.pockerguide.MainActivity.fmm;

import android.content.Context;
import android.content.SharedPreferences;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pockerguide.data.DATA;
import com.example.pockerguide.MainActivity;
import com.example.pockerguide.R;
import com.example.pockerguide.map.GPS.Clustering;
import com.example.pockerguide.museumactivity.MuseumActivity;
import com.example.pockerguide.recycler.MainFrag;
import com.example.pockerguide.recycler.homepagerecycler.State;
import com.example.pockerguide.server.UserService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Frag1 extends Fragment implements UserLocationObjectListener, LocationListener{
    private MapView mapView;
    private UserLocationLayer userLocationLayer;
    protected static MapObjectCollection mapObjects;
    protected static DrivingRouter drivingRouter;
    private LocationRequest locationRequest;
    private double latitude, longitude;
    private LocationListener locationListener;
    private double myLocation;
    private Object data = "ded";
    public static double museumLatitude, museumLongitude;
    String MAPKIT_API_KEY = "2ca51eab-b0a9-467d-981c-adc9f2aa3d08";
    public static String museum_name = "";
    SharedPreferences sp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (DATA.MapFrag1 == 0) {
            MapKitFactory.setApiKey(MAPKIT_API_KEY);
            DATA.MapFrag1 = 1;
            MapKitFactory.initialize(getContext());
        }
        sp = getActivity().getSharedPreferences("Test", Context.MODE_WORLD_READABLE);

        getCurrentLocation();

        View view = inflater.inflate(R.layout.frag1_layout, container, false);
        mapView = (com.yandex.mapkit.mapview.MapView) view.findViewById(R.id.mapview);
        mapView.getMap().setNightModeEnabled(true);




        Clustering clustering = new Clustering();
        ImageProvider imageProvider = ImageProvider.fromResource(
                getContext(), R.drawable.clustering_image);

        ClusterizedPlacemarkCollection clusterizedCollection =
                mapView.getMap().getMapObjects().addClusterizedPlacemarkCollection(clustering);

        List<Point> points = clustering.createPoints(Clustering.PLACEMARKS_NUMBER);
        clusterizedCollection.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                double latitude = point.getLatitude();
                double longitude = point.getLongitude();
                Toast.makeText(getContext(),"Открываем",Toast.LENGTH_SHORT).show();
                getCurrentLocation();
                getMuseum(latitude, longitude);

                return true;
            }
        });
        clusterizedCollection.addPlacemarks(points, imageProvider, new IconStyle());
        clusterizedCollection.setUserData(clustering);
        clusterizedCollection.clusterPlacemarks(60, 15);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                myLocation = location.getLatitude();
            }
        };
        if(DATA.CHECK_GPS_MUSEUM==1){
            Point TARGET_LOCATION = new Point(museumLatitude, museumLongitude);
            mapView.getMap().move(
                    new CameraPosition(TARGET_LOCATION, 17.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 1),
                    null);
            DATA.CHECK_GPS_MUSEUM=0;
        }

        if(DATA.CHECK_GPS==1) getCurrentLocation();

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        LocationProvider provider =
                MainActivity.locationManager2.getProvider(LocationManager.GPS_PROVIDER);

        userLocationLayer.setObjectListener(this);
        return view;
    }


    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isGPSEnabled()) {

                        getCurrentLocation();

                    } else {

                        turnOnGPS();
                    }
                }
            }
        }catch (Exception e){
            getCurrentLocation();
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(getActivity())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(getActivity())
                                            .removeLocationUpdates(this);

                                    if (locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();
                                        Log.d("MyLog", String.valueOf(latitude));
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(getContext(), "Включите GPS на телефоне", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;



        isEnabled = MainActivity.locationManager2.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                getContext(), R.drawable.thumbain));



        if(DATA.MUSEUM_ACTIVITY ==1){ new RouteMapPeople(userLocationView.getPin().getGeometry().getLatitude(),userLocationView.getPin().getGeometry().getLongitude(),museumLatitude,museumLongitude);}
        if(DATA.CHECK_GPS == 1) check(userLocationView.getPin().getGeometry().getLatitude(),userLocationView.getPin().getGeometry().getLongitude());
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
        if(DATA.MUSEUM_ACTIVITY ==1){
            new RouteMapPeople(view.getPin().getGeometry().getLatitude(),view.getPin().getGeometry().getLongitude(),museumLatitude,museumLongitude);
            Point TARGET_LOCATION = new Point(museumLatitude, museumLongitude);
            mapView.getMap().move(
                    new CameraPosition(TARGET_LOCATION, 17.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 1),
                    null);
        }
        if(DATA.CHECK_GPS == 1) check(view.getPin().getGeometry().getLatitude(),view.getPin().getGeometry().getLongitude());
    }

    public void check(double startLatitude, double startLongitude){
        if(museumLatitude == 0.0);
        else {
            if (startLatitude <= museumLatitude + 0.005500 && startLatitude >= museumLatitude - 0.005500 && startLongitude >= museumLongitude - 0.005500 && startLongitude <= museumLongitude + 0.005500 && startLatitude != 0.0) {
                Toast.makeText(getContext(), "Вы в зоне!", Toast.LENGTH_SHORT).show();
                DATA.CHECK_GPS=0;

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("nameMuseum", museum_name);
                editor.apply();
                Log.i("MyLog",sp.getString("nameMuseum" , ""));

                Toast.makeText(getContext(), "Пожалуйста оцените музей в его карточке", Toast.LENGTH_SHORT).show();


                UserService serv = DATA.retrofit.create(UserService.class);
                Call<Void> call = serv.updatePerson(DATA.id,DATA.statisticsMuseums + 1, DATA.statisticsPoints + 50, DATA.statisticsComment);
                class MyThread extends Thread {
                    @Override
                    public void run() {
                        try {
                            call.execute();
                            DATA.statisticsMuseums += 1;
                            DATA.statisticsPoints += 50;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                new MyThread().start();
                getActivity().finish();

            } else if (startLatitude == 0.0) {
                Toast.makeText(getContext(), "Подождите немного", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
                Log.i("MyLog", String.valueOf(myLocation + " d"));
            } else {
                DATA.CHECK_GPS=0;
                Toast.makeText(getContext(), "Вы не в зоне действия этого объекта", Toast.LENGTH_SHORT).show();
            }
        }

    }

    void getMuseum(double latitudeMap, double longitudeMap){
        for(State state: MainFrag.states_country){
            String a = state.getCoords();
            double latitude = Double.parseDouble(a.substring(0,9));
            double longitude = Double.parseDouble(a.substring(11,20));
            if(latitudeMap <= latitude+00.001000&&latitudeMap>=latitude-00.001000 && longitudeMap<=longitude+00.001000&&longitudeMap>=longitude-00.001000){
                Intent i = new Intent(getContext(), MuseumActivity.class);
                i.putExtra("name", state.getName());
                i.putExtra("inform", state.getInform());
                i.putExtra("image", state.getCapital());
                i.putExtra("coords", state.getCoords());
                i.putExtra("idMuseum",state.getId());
                Log.d("MyLog",state.getId());
                startActivity(i);
                break;
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(
                getContext(),
                "Location changed: Lat: " + location.getLatitude() + " Lng: "
                        + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }
}

