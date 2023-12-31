package keep.pixbrazil.windows.game

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import keep.pixbrazil.MainActivity
import keep.pixbrazil.MyApp
import keep.pixbrazil.R
import keep.pixbrazil.databinding.FragmentGameBinding
import keep.pixbrazil.utils.AnswerTrue.ANSWERS_ALL
import keep.pixbrazil.utils.AnswerTrue.QUESTIONS_ALL
import keep.pixbrazil.utils.ArrayFacts.ARRAY_FACTS
import keep.pixbrazil.utils.ArrayGameImage.ARRAY_IMAGE
import keep.pixbrazil.utils.ArrayGameList.ARRAY_LIST_FOOTBALL
import keep.pixbrazil.utils.Parametres.CURRENT_NUMBER_GAME
import keep.pixbrazil.utils.Parametres.CURRENT_NUMBER_GAME_MAX
import keep.pixbrazil.utils.Parametres.GAME_LIVE
import keep.pixbrazil.utils.Parametres.GAME_TOTAL
import keep.pixbrazil.utils.Parametres.MUSIC_STATE
import keep.pixbrazil.utils.RandomWordFinish.FINISH_WORD
import keep.pixbrazil.utils.RandomWordFinish.FINISH_WORD_SMALL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    private var remainingTime: Long = 0
    private val totalTime: Long = 15000 // Общее время в миллисекундах


    private var isAnswerCorrect = false

    private var _binding: FragmentGameBinding? = null

    private var randomGame = ArrayList<String>()

    private lateinit var dialog: Dialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy { requireNotNull(_binding) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGameBinding.inflate(inflater, container, false)

        dialog = Dialog(requireContext())

//        CURRENT_NUMBER_GAME = Random.nextInt(0, QUESTIONS_ALL.size - 1)

        CURRENT_NUMBER_GAME_MAX = (QUESTIONS_ALL.size - 1)
        binding.nextTitle.text = QUESTIONS_ALL[CURRENT_NUMBER_GAME]
        randomGame = ARRAY_LIST_FOOTBALL[CURRENT_NUMBER_GAME]
        binding.gameImage.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                ARRAY_IMAGE[CURRENT_NUMBER_GAME]
            )
        )


        binding.nextA.text = randomGame[0]
        binding.nextB.text = randomGame[1]
        binding.nextC.text = randomGame[2]
        binding.nextD.text = randomGame[3]

        return binding.root

    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resumeTimer()

        buttonClicked()

        binding.menuSetting.setOnClickListener {
            alertDialogMessage()
            pauseTimer()
        }

        binding.botShare.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Get ready for a soccer intellectual explosion with ${resources.getString(R.string.app_name)}, the fiercest soccer quiz ever!")
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }


        binding.backStack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.textLive.text = GAME_LIVE.toString()

    }


    private fun buttonClicked() {

        binding.nextA.setOnClickListener {
            checkAnswer(randomGame[0], binding.nextA)
            binding.nextA.isClickable = false
            binding.nextB.isClickable = false
            binding.nextC.isClickable = false
            binding.nextD.isClickable = false
        }

        binding.nextB.setOnClickListener {
            checkAnswer(randomGame[1], binding.nextB)
            binding.nextA.isClickable = false
            binding.nextB.isClickable = false
            binding.nextC.isClickable = false
            binding.nextD.isClickable = false
        }

        binding.nextC.setOnClickListener {
            checkAnswer(randomGame[2], binding.nextC)
            binding.nextA.isClickable = false
            binding.nextB.isClickable = false
            binding.nextC.isClickable = false
            binding.nextD.isClickable = false
        }

        binding.nextD.setOnClickListener {
            checkAnswer(randomGame[3], binding.nextD)
            binding.nextA.isClickable = false
            binding.nextB.isClickable = false
            binding.nextC.isClickable = false
            binding.nextD.isClickable = false
        }

        binding.botNext.setOnClickListener {
            nextLvL()
        }

        binding.botHelp.setOnClickListener {
            dialogFacts()
            pauseTimer()
        }
    }


    private fun checkAnswer(selectedOption: String, textView: TextView) {

        isAnswerCorrect = if (selectedOption == ANSWERS_ALL[CURRENT_NUMBER_GAME]) {
            // Ответ правильный
//            Toast.makeText(requireContext(), "Правильный ответ!", Toast.LENGTH_SHORT).show()
            true
        } else {
            // Ответ неправильный
            Toast.makeText(requireContext(), "Wrong answer!", Toast.LENGTH_SHORT).show()
            false
        }
        colorTrue(textView)

        if (GAME_LIVE <= 0){
            dialogGameOver("You've wasted all your lives")
        }else{
            CoroutineScope(Dispatchers.Main).launch {
                delay(800)
                nextLvL()
            }
        }


    }

    private fun colorTrue(textView: TextView) {
        if (isAnswerCorrect) {
            textView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            GAME_TOTAL += 1
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
            GAME_LIVE -= 1
            binding.textLive.text = GAME_LIVE.toString()
        }
    }

    private fun nextLvL() {
        countPlus()
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_gameFragment_self)
    }

    private fun countPlus() {

        if (CURRENT_NUMBER_GAME >= CURRENT_NUMBER_GAME_MAX) {
            CURRENT_NUMBER_GAME = 0
        } else {
            CURRENT_NUMBER_GAME++
        }
    }


    private fun alertDialogMessage() {
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val buttonBack = dialog.findViewById<ImageView>(R.id.backStack)
        val buttonMusic = dialog.findViewById<TextView>(R.id.nextMusic)
        val buttonRules = dialog.findViewById<TextView>(R.id.nextRules)
        val buttonPrivacy = dialog.findViewById<TextView>(R.id.nextPrivacy)

        if (MUSIC_STATE) {
            buttonMusic.text = "Music: on"
        } else {
            buttonMusic.text = "Music: off"
        }

        buttonPrivacy.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_privacyFragment)
            dialog.dismiss()
        }

        buttonRules.setOnClickListener{
            findNavController().navigate(R.id.action_gameFragment_to_rulesFragment)
            dialog.dismiss()
        }

        buttonMusic.setOnClickListener {
            if (!MUSIC_STATE) {
                buttonMusic.text = "Music: on"
                true
            } else {
                buttonMusic.text = "Music: off"
                false
            }

            val mediaPlayer = (requireActivity().application as MyApp).mediaPlayer

            MUSIC_STATE = if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                true
            } else {
                mediaPlayer.pause()
                false
            }
        }

        buttonBack.setOnClickListener {
            dialog.dismiss()
            resumeTimer()
        }
        dialog.show()

    }

    private fun dialogGameOver(str: String) {
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_game_over)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val textGameOver = dialog.findViewById<TextView>(R.id.titleGameOver)
        val textResult = dialog.findViewById<TextView>(R.id.gmResult)
        val textFinish = dialog.findViewById<TextView>(R.id.textFinishWord)
        val buttonAgain = dialog.findViewById<TextView>(R.id.gmAgain)
        val buttonExit = dialog.findViewById<TextView>(R.id.gmExit)
        textGameOver.text = str
        textResult.text = GAME_TOTAL.toString()
        if (GAME_TOTAL >= 5){
            textFinish.text = FINISH_WORD.random()
        }else{
            textFinish.text = FINISH_WORD_SMALL.random()
        }
        buttonExit.setOnClickListener {
            dialog.dismiss()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_gameFragment_to_MenuFragment)
        }
        buttonAgain.setOnClickListener {
            GAME_TOTAL = 0
            GAME_LIVE = 3
            nextLvL()
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun dialogFacts() {
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val buttonExit = dialog.findViewById<ImageView>(R.id.backStack)
        val imageFact = dialog.findViewById<ImageView>(R.id.factImage)
        val textFact = dialog.findViewById<TextView>(R.id.factText)

        imageFact.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                ARRAY_IMAGE[CURRENT_NUMBER_GAME]
            )
        )

        textFact.text = ARRAY_FACTS[CURRENT_NUMBER_GAME]

        buttonExit.setOnClickListener {
            dialog.dismiss()
            resumeTimer()
        }
        dialog.show()

    }


    private fun resumeTimer() {
        timer = object : CountDownTimer(totalTime - remainingTime, 1000) {
            override fun onTick(remaining: Long) {
                remainingTime = totalTime - remaining
                val minutes = (remaining / 1000) / 60
                val seconds = (remaining / 1000) % 60
                val formattedTime = "%02d:%02d".format(minutes, seconds)
                binding.gameTimer.text = formattedTime
            }

            override fun onFinish() {
                binding.gameTimer.text = "Game over!"
                dialogGameOver("Time's up")
            }
        }.start()
    }

    // Поставить таймер на паузу
    private fun pauseTimer() {
        timer.cancel()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}