package keep.pixbrazil

import android.app.Application
import android.media.MediaPlayer
import com.onesignal.OneSignal

// Replace the below with your own ONESIGNAL_APP_ID
  const val ONESIGNAL_APP_ID = "11144864-66cc-4f2e-bb32-4cac4c7c0b96"


class MyApp: Application() {

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        mediaPlayer = MediaPlayer.create(this, R.raw.fon_muz)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
    }

}