package com.example.shopgame.Activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.shopgame.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoreMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // Tọa độ vị trí cửa hàng
    private final LatLng storeLocation = new LatLng(10.762622, 106.660172); // Thay đổi tọa độ theo vị trí của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        // Tìm SupportMapFragment và thiết lập bản đồ khi sẵn sàng
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Thêm marker cho vị trí cửa hàng và zoom tới vị trí đó
        mMap.addMarker(new MarkerOptions().position(storeLocation).title("Cửa hàng của bạn"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15));
    }
}
