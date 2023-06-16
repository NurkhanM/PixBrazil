package keep.pixbrazil.windows.ruletka

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.animation.ValueAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import keep.pixbrazil.databinding.FragmentRuletkaBinding
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import keep.pixbrazil.R

class RuletkaFragment : Fragment() {

    private var _binding: FragmentRuletkaBinding? = null
    private val binding get() = _binding!!
    private var animationEnded = true
    private var randomRotation = 0f

    private lateinit var dialog: Dialog

    private val sectorDegrees = arrayOf(
        0f,     // Участок 1: от 0 до 59 градусов
        60f,    // Участок 2: от 60 до 119 градусов
        120f,   // Участок 3: от 120 до 179 градусов
        180f,   // Участок 4: от 180 до 239 градусов
        240f,   // Участок 5: от 240 до 299 градусов
        300f    // Участок 6: от 300 до 359 градусов
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRuletkaBinding.inflate(inflater, container, false)
        dialog = Dialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backStack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.ruletPlay.setOnClickListener {
            if (animationEnded) {
                startRotation()
            }
        }

        binding.roulette.post {
            resetPosition()
        }
    }

    private fun resetPosition() {
        binding.roulette.rotation = 0f
    }

    private fun startRotation() {
        animationEnded = false
        randomRotation = (720..1080).random().toFloat()

        val currentRotation = binding.roulette.rotation % 360

        val duration = 3000L // Длительность анимации (3 секунды)
        val rotationSpeed = 1800f // Скорость вращения (градусы в секунду)

        val rotateAnimator = ValueAnimator.ofFloat(currentRotation, randomRotation)
        rotateAnimator.duration = duration
        rotateAnimator.interpolator = LinearInterpolator()
        rotateAnimator.addUpdateListener { valueAnimator ->
            val rotationValue = valueAnimator.animatedValue as Float
            binding.roulette.rotation = rotationValue
        }

        rotateAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                animationEnded = true
                val rotation = binding.roulette.rotation % 360
                checkResult(rotation)

            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        rotateAnimator.start()
    }

    private fun checkResult(rotation: Float) {
        for (i in sectorDegrees.indices) {
            val startDegree = sectorDegrees[i]
            val endDegree = if (i < sectorDegrees.size - 1) sectorDegrees[i + 1] else 360f

            if (rotation >= startDegree && rotation < endDegree) {
                val result = i + 1 // Текущий участок
                dialogFacts(result.toString())
                // Здесь вы можете выполнить необходимые действия с результатом
                break
            }
        }
    }



    private fun dialogFacts(res: String) {
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_result)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val buttonExit = dialog.findViewById<ImageView>(R.id.backStack)
//        val imageFact = dialog.findViewById<ImageView>(R.id.factImage)
        val textResult = dialog.findViewById<TextView>(R.id.resultRulet)

//        imageFact.setImageDrawable(
//            ContextCompat.getDrawable(
//                requireContext(),
//                ArrayGameImage.ARRAY_IMAGE[Parametres.CURRENT_NUMBER_GAME]
//            )
//        )
//
        textResult.text = res

        buttonExit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
