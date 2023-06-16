package keep.pixbrazil.windows.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import keep.pixbrazil.MainActivity
import keep.pixbrazil.R
import keep.pixbrazil.databinding.FragmentMenuBinding
import keep.pixbrazil.utils.Parametres.MUSIC_STATE

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        val mediaPlayer = (activity as MainActivity).mediaPlayer
        if (MUSIC_STATE){
            mediaPlayer.start()
        }else{
            mediaPlayer.pause()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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