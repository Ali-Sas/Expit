package com.example.pockerguide.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pockerguide.data.DATA;
import com.example.pockerguide.R;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.places.PlacesFactory;
import com.yandex.mapkit.places.panorama.NotFoundError;
import com.yandex.mapkit.places.panorama.PanoramaService;
import com.yandex.mapkit.places.panorama.PanoramaView;
import com.yandex.runtime.Error;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

public class Panorama extends Activity implements PanoramaService.SearchListener {
    /**
     * Replace "your_api_key" with a valid developer key.
     * You can get it at the https://developer.tech.yandex.ru/ website.
     */
    private final String MAPKIT_API_KEY = "2ca51eab-b0a9-467d-981c-adc9f2aa3d08";
    private Point SEARCH_LOCATION = new Point();

    private PanoramaView panoramaView;
    private PanoramaService panoramaService;
    private PanoramaService.SearchSession searchSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DATA.MapFrag1 == 0) {
            MapKitFactory.setApiKey(MAPKIT_API_KEY);
            DATA.MapFrag1 = 1;
        }
        Intent intent = getIntent();

        String names = intent.getStringExtra("coords");
        Double museumLatitude = Double.parseDouble(names.substring(0,9));
        Double museumLongitude = Double.parseDouble(names.substring(11,20));

        SEARCH_LOCATION = new Point(museumLatitude,museumLongitude);

        MapKitFactory.initialize(this);
        PlacesFactory.initialize(this);
        setContentView(R.layout.activity_panorama);
        super.onCreate(savedInstanceState);
        panoramaView = (PanoramaView)findViewById(R.id.panoview);

        panoramaService = PlacesFactory.getInstance().createPanoramaService();
        searchSession = panoramaService.findNearest(SEARCH_LOCATION, this);
    }

    @Override
    protected void onStop() {
        panoramaView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        panoramaView.onStart();
    }

    @Override
    public void onPanoramaSearchResult(String panoramaId) {
        panoramaView.getPlayer().openPanorama(panoramaId);
        panoramaView.getPlayer().enableMove();
        panoramaView.getPlayer().enableRotation();
        panoramaView.getPlayer().enableZoom();
        panoramaView.getPlayer().enableMarkers();
    }

    @Override
    public void onPanoramaSearchError(Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof NotFoundError) {
            errorMessage = getString(R.string.not_found_error_message);
        } else if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}