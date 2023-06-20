package keep.pixbrazil

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.NavController
import keep.pixbrazil.databinding.ActivityMainBinding
import keep.pixbrazil.utils.Parametres.CURRENT_NUMBER_GAME
import keep.pixbrazil.utils.Parametres.CURRENT_NUMBER_GAME_MAX
import keep.pixbrazil.utils.Parametres.MUSIC_STATE

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = (application as MyApp).mediaPlayer

        controller = findNavController(R.id.nav_host_fragment_content_main)
    }

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        if (destination.id == R.id.MenuFragment) {
            if (CURRENT_NUMBER_GAME >= CURRENT_NUMBER_GAME_MAX) {
                CURRENT_NUMBER_GAME = 0
            } else {
                CURRENT_NUMBER_GAME++
            }
        }
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(listener)
        if (MUSIC_STATE) {
            mediaPlayer.start()
        } else {
            mediaPlayer.pause()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }
}
