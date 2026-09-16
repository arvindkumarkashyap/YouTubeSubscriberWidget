package com.arvind.subscriberwidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.glance.appwidget.updateAll
import com.arvind.subscriberwidget.data.ChannelStore
import com.arvind.subscriberwidget.network.ChannelRepository
import com.arvind.subscriberwidget.widget.RefreshWorker
import com.arvind.subscriberwidget.widget.SubscriberWidget
import kotlinx.coroutines.launch

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState); RefreshWorker.schedule(this); setContent { MaterialTheme { Screen() } }}
 @Composable fun Screen(){ var input by remember{ mutableStateOf(ChannelStore(this).load().handle) }; var status by remember{ mutableStateOf("Enter a YouTube @handle, channel ID, or channel URL.") }; var busy by remember{mutableStateOf(false)}
  Surface(Modifier.fillMaxSize()){ Column(Modifier.padding(24.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){ Text("YouTube Subscriber Widget",style=MaterialTheme.typography.headlineMedium); Text("Show the latest public subscriber total directly on your Android home screen."); OutlinedTextField(input,{input=it},Modifier.fillMaxWidth(),label={Text("Channel")},placeholder={Text("@YourChannel")}); Button(onClick={busy=true; lifecycleScope.launch{ val r=ChannelRepository(this@MainActivity).refresh(input); status=r.fold({"Saved ${it.title}: ${it.subscribers} subscribers"},{it.message ?: "Unable to update"}); SubscriberWidget().updateAll(this@MainActivity); busy=false}},enabled=!busy){Text(if(busy)"Checking…" else "Save & refresh")}; Text(status); HorizontalDivider(); Text("Add the widget: long-press your Android home screen → Widgets → Subscriber Widget. The widget refreshes periodically and also has a manual refresh button.",style=MaterialTheme.typography.bodyMedium); Text("Note: YouTube rounds public subscriber counts to three significant figures for channels over 1,000 subscribers.",style=MaterialTheme.typography.bodySmall) } }
 }
}
