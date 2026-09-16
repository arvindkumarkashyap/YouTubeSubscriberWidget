package com.arvind.subscriberwidget.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {
    @GET("youtube/v3/channels") suspend fun byHandle(@Query("part") part:String="snippet,statistics", @Query("forHandle") handle:String, @Query("key") key:String): ChannelResponse
    @GET("youtube/v3/channels") suspend fun byId(@Query("part") part:String="snippet,statistics", @Query("id") id:String, @Query("key") key:String): ChannelResponse
}
data class ChannelResponse(val items:List<ChannelItem> = emptyList())
data class ChannelItem(val id:String, val snippet:Snippet, val statistics:Statistics)
data class Snippet(val title:String)
data class Statistics(@SerializedName("subscriberCount") val subscriberCount:String? = null, val hiddenSubscriberCount:Boolean=false)
