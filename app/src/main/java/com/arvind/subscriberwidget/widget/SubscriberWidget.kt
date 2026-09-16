package com.arvind.subscriberwidget.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.*
import androidx.glance.action.actionRunCallback
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.*
import androidx.glance.text.*
import com.arvind.subscriberwidget.MainActivity
import com.arvind.subscriberwidget.data.ChannelStore
import com.arvind.subscriberwidget.network.ChannelRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SubscriberWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) { provideContent { Content(context) } }
    @Composable private fun Content(context:Context) {
        val s=ChannelStore(context).load()
        Column(GlanceModifier.fillMaxSize().background(ColorProvider(android.graphics.Color.WHITE)).padding(14.dp).clickable(actionStartActivity<MainActivity>()), verticalAlignment=Alignment.Vertical.CenterVertically) {
            Text(s.title, style=TextStyle(fontWeight=FontWeight.Bold, fontSize=15.sp), maxLines=1)
            Spacer(GlanceModifier.height(7.dp))
            Text(if(s.updatedAt==0L) "Set up channel" else NumberFormat.getIntegerInstance().format(s.subscribers), style=TextStyle(fontWeight=FontWeight.Bold,fontSize=30.sp))
            Text(if(s.updatedAt==0L) "Open app to configure" else "SUBSCRIBERS", style=TextStyle(fontSize=11.sp))
            Spacer(GlanceModifier.height(7.dp))
            Row(verticalAlignment=Alignment.Vertical.CenterVertically) {
                Text(if(s.updatedAt==0L) "Not updated" else "Updated ${SimpleDateFormat("h:mm a",Locale.getDefault()).format(Date(s.updatedAt))}", style=TextStyle(fontSize=10.sp))
                Spacer(GlanceModifier.defaultWeight())
                Button("↻", onClick=actionRunCallback<RefreshAction>())
            }
        }
    }
}
class SubscriberWidgetReceiver: GlanceAppWidgetReceiver(){ override val glanceAppWidget:GlanceAppWidget=SubscriberWidget() }
class RefreshAction: ActionCallback { override suspend fun onAction(context:Context, glanceId:GlanceId, parameters:ActionParameters){ ChannelRepository(context).refresh(); SubscriberWidget().update(context,glanceId) } }
