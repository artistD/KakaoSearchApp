package com.will_d.tp11kakaoplacesearchapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    var choiceID:Int=R.id.choice_wv

    val tabLayout : TabLayout by lazy { findViewById(R.id.layout_tab) }


    //kakao 키워드 로컬 검색 api의 요청 파라미터
    //1. 장소명(검색어)
    var searchQuery:String = "화장실"
    
    //2. 내 위치정보객체(위도, 경도, 정보 보유한 객체)
    lateinit var myLocation:Location
    
    //Fused Location 관리 객체
    lateinit var locationProviderClient: FusedLocationProviderClient

    //검색결과 데이터를 가진 객체
    public var searchLocalApiResponse : SearchLocalApiResponse? = null
    
    val etSearch:EditText by lazy { findViewById(R.id.et_search) }



    //progressbar 제어
    val blur : RelativeLayout by lazy { findViewById(R.id.blur) }
    val pg : ProgressBar by lazy { findViewById(R.id.pg) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pg.visibility = View.VISIBLE
        blur.visibility = View.VISIBLE
        
        //소프트키패드의 검색버튼(액션버튼) 눌렀을때
        etSearch.setOnEditorActionListener { v, actionId, event ->
            searchQuery = etSearch.text.toString()
            searchPlace()

            return@setOnEditorActionListener false
        }
        
        //툴바를 제목줄(actionbar)로 설정하기 [그럼 옵션도 가질수 있는겨]
        val toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //SearchListFragment를 우선 화면에 붙이기
        supportFragmentManager.beginTransaction().add(R.id.container, SearchListFragment()).commit()

        //탭레이아웃의 탭버튼을 클릭하는 것을 처리

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text?.equals("LIST") == true){
                    supportFragmentManager.beginTransaction().replace(R.id.container, SearchListFragment()).commit()

                }else if (tab?.text?.equals("MAP") == true){
                    supportFragmentManager.beginTransaction().replace(R.id.container, SearchMapFragment()).commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        
        //우선 내 위치 사용에 대한 허용 동적 퍼미션[다이얼로그로 허락을 하는 퍼미션]
        val permissions: Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        val checkResult:Int = checkSelfPermission(permissions[0])
        if (checkResult == PackageManager.PERMISSION_DENIED) {
            //이게 다이얼로그 띄어주는 명령임
            requestPermissions(permissions, 100)
        }else{
            requestMyLocation()
        }
        
        
    }//onCreate method....
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // 내 위치 얻어오기
            requestMyLocation()
        }else{
            Toast.makeText(this, "내 위치정보를 제공하지 않아 검색기능을 온전히 사용할수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    //내 위치 얻어내는 기능 메소드
    //이거 그냥 무시하라는 뜻
    @SuppressLint("MissingPermission")
    val requestMyLocation :()->Unit = {
        //Google Map에서 사용하고 있는 내위치 검색 API 라이브러리 적용 [ Fused Location API : play-service-location]
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val request:LocationRequest = LocationRequest.create()
        request.setInterval(1000)//위치정보 갱신 간격 
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) //우선순위 결정
        //실시간 위치검색 조건 설정


        locationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

        
    }
    
    //위치정보 받았을때 반응하는 객체
    val locationCallback : LocationCallback = object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            
            //위치결과 객체로 부터 내위치 정보 얻기
            myLocation = p0.lastLocation

            //위치 얻어왔으면 더이상 업데이트 하지마세요
            locationProviderClient.removeLocationUpdates(this)
            
            //위치정보를 얻었으니 kakao keyword local검색 시작하기
            searchPlace()

        }
    }
    
    //카카오 키워드 local검색 API 호출 기능 메소드
    val searchPlace:()->Unit = {

        //retrofit 역할 inputstream, outputstream, thread, onui... try catch이런것들 레트로핏이 다해버림 : 서버의 작업을 다해줌
        val baseUrl: String = "https://dapi.kakao.com/"
        val builder : Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl(baseUrl)
        builder.addConverterFactory(ScalarsConverterFactory.create())
        builder.addConverterFactory(GsonConverterFactory.create())
        val retrofit:Retrofit = builder.build()


        val retrofitService:RetrofitService = retrofit.create(RetrofitService::class.java)
        //방향성대로 니가써야할 수천줄을 만들어줘

        val call : Call<SearchLocalApiResponse> = retrofitService.searchPlace(searchQuery, ""+myLocation.longitude, ""+myLocation.latitude)
        call.enqueue(object : Callback<SearchLocalApiResponse>{
            override fun onResponse(
                call: Call<SearchLocalApiResponse>,
                response: Response<SearchLocalApiResponse>
            ) {
                searchLocalApiResponse = response.body() as SearchLocalApiResponse
                supportFragmentManager.beginTransaction().replace(R.id.container, SearchListFragment()).commit()

                tabLayout.getTabAt(0)?.select()

                pg.visibility = View.INVISIBLE
                blur.visibility = View.INVISIBLE

            }

            override fun onFailure(call: Call<SearchLocalApiResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "서버에 오류가 있습니다.\n 잠시뒤에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }

        })

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_main, menu)
        return super.onCreateOptionsMenu(menu)

    }

    @SuppressLint("ResourceAsColor")
    fun clickChoice(view: View) {

        findViewById<CardView>(choiceID).setBackgroundResource(R.drawable.bg_choice)
        view.setBackgroundResource(R.drawable.bg_choice_select)
        choiceID = view.id

        val llchoiceWv : LinearLayout = findViewById(R.id.ll_chice_wv)
        var isMvCheched =false

        when(choiceID){
            R.id.choice_wv -> {
                searchQuery = "화장실"
                isMvCheched = true
            }
            R.id.choice_movie -> searchQuery = "영화관"
            R.id.choice_gas -> searchQuery = "주유소"
            R.id.chice_ev -> searchQuery = "전기"
        }

        if (isMvCheched){
            llchoiceWv.setBackgroundResource(R.drawable.bg_choice_select)
            isMvCheched =false
        }else{
            llchoiceWv.setBackgroundResource(R.drawable.bg_choice)
            isMvCheched =false
        }

        searchPlace()

        etSearch.setText("")
        etSearch.clearFocus()
    }
}