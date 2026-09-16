package com.arvind.subscriberwidget.network

import android.content.Context
import com.arvind.subscriberwidget.BuildConfig
import com.arvind.subscriberwidget.data.ChannelSnapshot
import com.arvind.subscriberwidget.data.ChannelStore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChannelRepository(private val context:Context) {
    private val api = Retrofit.Builder().baseUrl("https://www.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build().create(YouTubeApi::class.java)
    suspend fun refresh(input:String?=null): Result<ChannelSnapshot> = runCatching {
        require(BuildConfig.YOUTUBE_API_KEY.isNotBlank()) { "Add YOUTUBE_API_KEY to gradle.properties" }
        val store=ChannelStore(context); val old=store.load(); val raw=(input ?: if(old.id.isNotBlank()) old.id else old.handle).trim()
        require(raw.isNotBlank()) { "Enter a YouTube @handle or channel ID" }
        val normalized=raw.substringAfter("youtube.com/").substringBefore('?').trimEnd('/').substringAfterLast('/')
        val response = if(normalized.startsWith("UC") && normalized.length >= 20) api.byId(id=normalized,key=BuildConfig.YOUTUBE_API_KEY)
        else api.byHandle(handle=normalized.removePrefix("@"),key=BuildConfig.YOUTUBE_API_KEY)
        val item=response.items.firstOrNull() ?: error("Channel not found")
        require(!item.statistics.hiddenSubscriberCount) { "This channel hides its subscriber count" }
        val snap=ChannelSnapshot(item.id, if(normalized.startsWith("@")) normalized else "@$normalized", item.snippet.title,item.statistics.subscriberCount?.toLongOrNull() ?: 0,System.currentTimeMillis())
        store.save(snap); snap
    }
}
