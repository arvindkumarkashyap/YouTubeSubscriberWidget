package com.arvind.subscriberwidget.data

import android.content.Context

data class ChannelSnapshot(val id:String="", val handle:String="", val title:String="YouTube Channel", val subscribers:Long=0, val updatedAt:Long=0)

class ChannelStore(context: Context) {
    private val p = context.getSharedPreferences("channel", Context.MODE_PRIVATE)
    fun save(s: ChannelSnapshot) = p.edit().putString("id",s.id).putString("handle",s.handle).putString("title",s.title).putLong("subs",s.subscribers).putLong("updated",s.updatedAt).apply()
    fun load() = ChannelSnapshot(p.getString("id","")!!,p.getString("handle","")!!,p.getString("title","YouTube Channel")!!,p.getLong("subs",0),p.getLong("updated",0))
}
