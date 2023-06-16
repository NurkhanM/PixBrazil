package keep.pixbrazil.windows.settings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import keep.pixbrazil.MainActivity
import keep.pixbrazil.R
import keep.pixbrazil.databinding.FragmentSettingsBinding
import keep.pixbrazil.utils.Parametres.MUSIC_STATE


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)



        return binding.root

    }

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backStack.setOnClickListener {
            activity?.onBackPressed()
        }

        if (MUSIC_STATE){
            binding.nextMusic.text = "Music: on"
        }else{
            binding.nextMusic.text = "Music: off"
        }

        binding.nextMusic.setOnClickListener {
            musicBoolean()

            val mediaPlayer = (activity as MainActivity).mediaPlayer

            MUSIC_STATE = if (!mediaPlayer.isPlaying){
                mediaPlayer.start()
                true
            }else{
                mediaPlayer.pause()
                false
            }
        }

        binding.nextRules.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_rulesFragment)
        }
        binding.nextPrivacy.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_privacyFragment)
        }


    }

    private fun musicBoolean(){
        MUSIC_STATE = if (!MUSIC_STATE){
            binding.nextMusic.text = "Music: on"
            true
        }else{
            binding.nextMusic.text = "Music: off"
            false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}