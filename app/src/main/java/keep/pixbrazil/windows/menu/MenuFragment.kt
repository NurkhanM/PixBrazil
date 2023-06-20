package keep.pixbrazil.windows.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import keep.pixbrazil.MainActivity
import keep.pixbrazil.MyApp
import keep.pixbrazil.R
import keep.pixbrazil.databinding.FragmentMenuBinding
import keep.pixbrazil.utils.Parametres.GAME_LIVE
import keep.pixbrazil.utils.Parametres.GAME_TOTAL
import keep.pixbrazil.utils.Parametres.MUSIC_STATE

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        val mediaPlayer = (requireActivity().application as MyApp).mediaPlayer
        if (MUSIC_STATE){
            mediaPlayer.start()
        }else{
            mediaPlayer.pause()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GAME_TOTAL = 0
        GAME_LIVE = 3

        binding.nextStart.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_gameFragment)
        }

        binding.nextSetting.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_settingsFragment)
        }
        binding.nextExit.setOnClickListener {
            activity?.finishAffinity()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}