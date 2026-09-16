# YouTube Subscriber Widget

Android app + home-screen widget showing a YouTube channel's latest public subscriber count.

## Setup
1. Open this folder in Android Studio.
2. Create/choose a Google Cloud project, enable **YouTube Data API v3**, and create an API key.
3. Add this line to the project-level `gradle.properties` (do not commit a real key):
   `YOUTUBE_API_KEY=YOUR_KEY_HERE`
4. Sync Gradle, run on an Android device/emulator.
5. Enter a YouTube `@handle`, channel ID, or channel URL and tap **Save & refresh**.
6. Long-press Android home screen → Widgets → **Subscriber Widget**.

## Behavior
- Widget displays latest public subscriber count, channel name and update time.
- Manual refresh button is included.
- WorkManager requests a periodic refresh every 30 minutes; Android may defer background work for battery optimization.
- YouTube Data API public `subscriberCount` is rounded down to three significant figures above 1,000 subscribers.
