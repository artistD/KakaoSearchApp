package com.will_d.tp11kakaoplacesearchapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.security.identity.CipherSuiteNotSupportedException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SearchMapFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_searchmap, container, false)
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment:SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync {
            val mainActivity:MainActivity = activity as MainActivity

            //구글지도 위도/경도
            val me : LatLng= LatLng(mainActivity.myLocation.latitude, mainActivity.myLocation.longitude)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 17F))

            //구글맵에 내 위치 표시 설정

            it.isMyLocationEnabled  = true

            //몇가지 지도 설정
            it.uiSettings.isZoomControlsEnabled = true
            it.uiSettings.isZoomGesturesEnabled = true

            val places:ArrayList<Place> =mainActivity.searchLocalApiResponse?.documents as ArrayList<Place>
            for (i in 0 until places.size){
                val place : Place = places.get(i)
                val latitude : Double = place.y.toDouble()
                val longitude : Double = place.x.toDouble()
                val position :LatLng = LatLng(latitude, longitude)

                //마커옵션 객체를 통해 마커의 설정들
                val markerOptions:MarkerOptions = MarkerOptions().position(position).title(place.place_name).snippet(place.distance + "m")
                it.addMarker(markerOptions)?.tag = place.place_url
                
            }
            
            
            //구글맵에게 마커의 정보창(infoWindow)를 클릭하는 것에 반응시키기
            it.setOnInfoWindowClickListener {
                //Toast.makeText(mainActivity, "" + it.tag.toString(), Toast.LENGTH_SHORT).show()
                //장소Url의 웹문서를 사용자에게 보여주기 위해 새로운 액티비티로 전환
                val intent:Intent = Intent(mainActivity, PlaceUrlActivity::class.java)
                intent.putExtra("placeUrl", it.tag.toString()) // place_url 정보
                startActivity(intent)
            }

        }

    }
}