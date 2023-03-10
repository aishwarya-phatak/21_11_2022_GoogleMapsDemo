package com.example.a21_11_2022_googlemapsdemo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.a21_11_2022_googlemapsdemo.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bitcodeMarker: Marker
    private lateinit var shardaCenterMarker : Marker
    private lateinit var puneMarker : Marker
    private lateinit var naviMumbaiMarker : Marker

    private lateinit var circle: Circle
    private lateinit var polygon: Polygon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        initMapSettings()
        addMarkers()
        setInfoWindowAdapter()
        onMarkerDragClickListener()
        onMarkerClickListener()
        onInfoWindowClickListener()
        addShapes()
        miscellaneous()

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun miscellaneous(){
        mMap.setOnPoiClickListener {
            Log.e("tag","${it.latLng} --- ${it.name}") }

    }

    @SuppressLint("NewApi")
    private fun addShapes(){

        circle = mMap.addCircle(
            CircleOptions()
                .center(LatLng(19.033,73.0297))
                .fillColor(Color.argb(90,255,0,0))
                .radius(2000.0)
                .strokeColor(Color.GRAY)
                .strokeWidth(3.0F)
        )

        polygon = mMap.addPolygon(
            PolygonOptions()
                .add(LatLng(19.0332,73.0297))
                .add(LatLng(19.0948,74.7480))
                .add(LatLng(17.6806,75.3155))
                .add(LatLng(16.9902,73.3120))
                .fillColor(Color.argb(50,0,255,0))
                .strokeColor(Color.CYAN)
                .strokeWidth(4.0F)
        )
    }

    private fun onInfoWindowClickListener(){
        mMap.setOnInfoWindowClickListener {
            Log.e("tag","${it.title}")
            mt("${it.title} -- ${it.position.longitude} -- ${it.position.latitude}")
        }
    }

    private fun onMarkerDragClickListener(){
        mMap.setOnMarkerDragListener(
            object : GoogleMap.OnMarkerDragListener{
                override fun onMarkerDrag(marker: Marker) {
                    Log.e("tag","${marker.position.longitude} -- ${marker.position.latitude} -- ${marker.title}")
                    mt("${marker.title} -- ${marker.position.longitude} -- ${marker.position.latitude}")
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    Log.e("tag","${marker.position.longitude} -- ${marker.position.latitude} -- ${marker.title}")
                    mt("${marker.title} -- ${marker.position.longitude} -- ${marker.position.latitude}")
                }

                override fun onMarkerDragStart(marker: Marker) {
                    Log.e("tag","${marker.position.longitude} -- ${marker.position.latitude} -- ${marker.title}")
                    mt("${marker.title} -- ${marker.position.longitude} -- ${marker.position.latitude}")
                }
            }
        )
    }

    private fun onMarkerClickListener(){
        mMap.setOnMarkerClickListener(
            object  : GoogleMap.OnMarkerClickListener{
                override fun onMarkerClick(marker: Marker): Boolean {
                    mt("marker clicked -- ${marker.title}")
                    Log.e("tag","${marker.position.longitude} -- ${marker.position.latitude} -- ${marker.title}")
                    return true
                }
            }
        )
    }

    private fun setInfoWindowAdapter(){
        mMap.setInfoWindowAdapter(MyInfoWindowAdapter())
    }

    private inner class MyInfoWindowAdapter : GoogleMap.InfoWindowAdapter{
        override fun getInfoContents(marker: Marker): View? {
         var infoWindowView = layoutInflater.inflate(R.layout.info_window,null)
            var infoWindowBinding = com.example.a21_11_2022_googlemapsdemo.
            databinding.
            InfoWindowBinding.bind(infoWindowView)
            infoWindowBinding.txtView1.text = marker.title
            infoWindowBinding.imgView.setImageResource(R.mipmap.ic_launcher)
            return infoWindowView
        }

        override fun getInfoWindow(marker: Marker): View? {
            return null
        }
    }

    private fun addMarkers(){
        var markerOptionsForBitcodeMarker = MarkerOptions().title("Bitcode")
            .snippet("Bitcode Office")
            .position(LatLng(18.5091,73.8326))
            .visible(true)
            .rotation(30F)
            .draggable(true)
            .anchor(0.2F,0.2F)
            .zIndex(60F)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        bitcodeMarker = mMap.addMarker(
            markerOptionsForBitcodeMarker
        )!!

        var scaledImage = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource
                (resources,R.drawable.city_icon),100,100,false)
        
        var shardaCenterIcon = BitmapDescriptorFactory.fromBitmap(scaledImage)
        shardaCenterMarker = mMap.addMarker(
            MarkerOptions().snippet("Sharda Center in Pune")
                .title("Sharda Center")
                .zIndex(45F)
                .visible(true)
                .position(LatLng(18.507,73.8343))
                .icon(shardaCenterIcon)
        )!!
    }

    private fun mt(text : String){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingPermission")
    private fun initMapSettings(){
        mMap.isIndoorEnabled = true
        mMap.isTrafficEnabled = true
        mMap.isBuildingsEnabled = true
        mMap.isMyLocationEnabled = true

        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
        //mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
    }
}