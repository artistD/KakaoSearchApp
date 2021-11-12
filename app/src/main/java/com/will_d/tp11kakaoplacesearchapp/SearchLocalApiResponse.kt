package com.will_d.tp11kakaoplacesearchapp

class SearchLocalApiResponse(var meta:PlaceMeta, var documents:ArrayList<Place>) {

}

class PlaceMeta(var total_count:Int, var pageable_count:Int, var is_end:Boolean){

}
class Place(var place_name:String, var phone:String, var address_name:String, var road_address_name:String, var x:String, var y:String, var place_url:String, var distance:String){

}