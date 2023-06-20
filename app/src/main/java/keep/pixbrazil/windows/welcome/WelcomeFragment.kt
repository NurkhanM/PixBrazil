package keep.pixbrazil.windows.welcome

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import keep.pixbrazil.R
import keep.pixbrazil.databinding.FragmentWelcomeBinding
import keep.pixbrazil.utils.Parametres.LINK_REMOTE

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var remoteConfig: FirebaseRemoteConfig


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        fullScreen()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        remoteFireBaseConf()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("ObsoleteSdkInt")
    fun fullScreen() {

        val uiOptions = (activity as AppCompatActivity).window.decorView.systemUiVisibility
        var newUiOptions = uiOptions

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        (activity as AppCompatActivity).window.decorView.systemUiVisibility = newUiOptions
    }


    private fun remoteFireBaseConf() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // Установите интервал минимального обновления в 0 секунд
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val defaultConfigValues = mapOf(
            "123" to "hei",
            "show_ads" to true
        )
        remoteConfig.setDefaultsAsync(defaultConfigValues)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                val map = remoteConfig.all
                val value = remoteConfig.getString("jungle")

                if (value.isNotEmpty()) {
                    LINK_REMOTE = value
                    findNavController().navigate(R.id.action_welcomeFragment_to_webFragment)
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_welcomeFragment_to_SplashFragment)
                }


            } else {
                Log.d("RemoteConfig", "Failure")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}