package com.arvind.subscriberwidget.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

import com.arvind.subscriberwidget.MainActivity
import com.arvind.subscriberwidget.data.ChannelStore
import com.arvind.subscriberwidget.network.ChannelRepository

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SubscriberWidget : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            WidgetContent(context)
        }
    }


    @Composable
    private fun WidgetContent(context: Context) {

        val channel = ChannelStore(context).load()

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(
                    ColorProvider(
                        android.graphics.Color.WHITE
                    )
                )
                .padding(16.dp)
                .clickable(
                    actionStartActivity<MainActivity>()
                ),

            verticalAlignment =
                Alignment.Vertical.CenterVertically
        ) {

            // CHANNEL NAME

            Text(
                text = if (channel.title.isBlank()) {
                    "YouTube Channel"
                } else {
                    channel.title
                },

                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),

                maxLines = 1
            )


            Spacer(
                modifier = GlanceModifier.height(10.dp)
            )


            // SUBSCRIBER COUNT

            Text(
                text = if (channel.updatedAt == 0L) {

                    "—"

                } else {

                    NumberFormat
                        .getIntegerInstance()
                        .format(channel.subscribers)
                },

                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )


            Text(
                text = "SUBSCRIBERS",

                style = TextStyle(
                    fontSize = 11.sp
                )
            )


            Spacer(
                modifier = GlanceModifier.height(10.dp)
            )


            // LAST UPDATED

            Text(
                text = if (channel.updatedAt == 0L) {

                    "Open app to configure channel"

                } else {

                    "Updated: " +
                        SimpleDateFormat(
                            "h:mm a",
                            Locale.getDefault()
                        ).format(
                            Date(channel.updatedAt)
                        )
                },

                style = TextStyle(
                    fontSize = 10.sp
                )
            )


            Spacer(
                modifier = GlanceModifier.height(8.dp)
            )


            // REFRESH ACTION

            Text(
                text = "↻  REFRESH",

                modifier = GlanceModifier
                    .padding(6.dp)
                    .clickable(
                        actionRunCallback<RefreshAction>()
                    ),

                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            )
        }
    }
}


// ANDROID WIDGET RECEIVER

class SubscriberWidgetReceiver :
    GlanceAppWidgetReceiver() {

    override val glanceAppWidget:
        GlanceAppWidget = SubscriberWidget()
}


// REFRESH BUTTON ACTION

class RefreshAction : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {

        // Get latest subscriber count
        ChannelRepository(context).refresh()

        // Re-render this widget.
        // update() is a member function of GlanceAppWidget.
        SubscriberWidget().update(
            context,
            glanceId
        )
    }
}
