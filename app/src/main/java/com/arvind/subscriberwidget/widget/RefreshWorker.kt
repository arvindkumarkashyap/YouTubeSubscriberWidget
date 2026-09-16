package com.arvind.subscriberwidget.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.*
import com.arvind.subscriberwidget.network.ChannelRepository
import java.util.concurrent.TimeUnit

class RefreshWorker(ctx:Context, params:WorkerParameters):CoroutineWorker(ctx,params){ override suspend fun doWork():Result { ChannelRepository(applicationContext).refresh(); SubscriberWidget().updateAll(applicationContext); return Result.success() }
companion object { fun schedule(context:Context){ val r=PeriodicWorkRequestBuilder<RefreshWorker>(30,TimeUnit.MINUTES).build(); WorkManager.getInstance(context).enqueueUniquePeriodicWork("subscriber_refresh",ExistingPeriodicWorkPolicy.UPDATE,r) } } }
