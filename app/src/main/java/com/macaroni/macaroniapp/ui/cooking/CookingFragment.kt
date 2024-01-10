package com.macaroni.macaroniapp.ui.cooking

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.databinding.FragmentGalleryBinding
import java.util.Timer
import java.util.TimerTask


class CookingFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var binding: FragmentGalleryBinding? = null
    var angle = 0.0
    var shouldStop = false
    var cookingData: CookingItemData? = null
    var viewLevelPositionStack: ArrayList<ImageView?>? = null
    var levelCount = 0
    var currentLevel = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val galleryViewModel =
            ViewModelProvider(this).get(CookingViewModel::class.java)

        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View? = binding?.root
        setLevelsData()

        binding?.pauseBtn?.setOnClickListener {
            shouldStop = true
            val newAngle = angle/2
            //working
            if (newAngle <= 60 && newAngle > 15) {
                binding?.pointer?.rotation = (newAngle - 20).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle < 80 && newAngle > 60) {
                binding?.pointer?.rotation = (newAngle - 10).toFloat()
                Log.d("Current", newAngle.toString())
            }  else if (newAngle > 185.0) {
                val currentAngle = 180 - (newAngle - 185)
                binding?.pointer?.rotation = (currentAngle).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle < 185.0 && newAngle > 105) {
                binding?.pointer?.rotation = (newAngle + 10).toFloat()
                Log.d("Current", newAngle.toString())
            } else {
                binding?.pointer?.rotation = (newAngle).toFloat()
                Log.d("Current", newAngle.toString())
            }
            //working
            binding?.pointer?.clearAnimation()
            binding?.pauseBtn?.visibility = View.GONE
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    shouldStop = false

                    executePointerMove()
                }
            }, 1500)
        }
        setPastaChooser()
        return root

    }

    private fun setPastaChooser() {
        val pastaOne = binding?.pastaOne ?: return
        val pastaTwo = binding?.pastaTwo ?: return
        val pastaThree = binding?.pastaThree ?: return
        pastaOne.setImageDrawable(cookingData?.listOfPastas?.get(0))
        pastaTwo.setImageDrawable(cookingData?.listOfPastas?.get(1))
        pastaThree.setImageDrawable(cookingData?.listOfPastas?.get(2))

        pastaOne.setOnClickListener {
            val isCorrectText = if (pastaOne.tag == (cookingData?.incorrectPasta?.toLowerCase() ?: "")) { "no" } else { "yes" }
            val toast = Toast.makeText(this.context, isCorrectText, Toast.LENGTH_SHORT)
            toast.show()
            binding?.choosePastaHolder?.visibility = View.GONE
            binding?.cookingHolder?.visibility = View.VISIBLE
            executePointerMove()
        }
        pastaTwo.setOnClickListener {
            val isCorrectText = if (pastaTwo.tag == (cookingData?.incorrectPasta?.toLowerCase() ?: "")) { "no" } else { "yes" }
            val toast = Toast.makeText(this.context, isCorrectText, Toast.LENGTH_SHORT)
            toast.show()
            binding?.choosePastaHolder?.visibility = View.GONE
            binding?.cookingHolder?.visibility = View.VISIBLE
        }
        pastaThree.setOnClickListener {
            val isCorrectText = if (pastaThree.tag == (cookingData?.incorrectPasta?.toLowerCase() ?: "")) { "no" } else { "yes" }
            val toast = Toast.makeText(this.context, isCorrectText, Toast.LENGTH_SHORT)
            toast.show()
            binding?.choosePastaHolder?.visibility = View.GONE
            binding?.cookingHolder?.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getLevelsData()
    }

    private fun slidePointerAnimation() {

        val animation = AnimationUtils.loadAnimation(activity, R.anim.rotation_anim)
        animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                        angle = 0.0
                }

                override fun onAnimationEnd(p0: Animation?) {
                    if (!shouldStop) {
                        // binding?.pointer?.animation = animationBack
                        binding?.pointer?.rotation = 0f
                        executePointerMove()
                    }
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    angle = 0.0
                }

            })

        binding?.pointer?.animation = animation
    }

    private fun executePointerMove() {
        activity ?: return
        if (levelCount == 0) return
        binding?.pointer?.rotation = 0f
        binding?.pauseBtn?.visibility = View.VISIBLE

        viewLevelPositionStack?.get(currentLevel)?.visibility = View.VISIBLE
        if (currentLevel != 0) {
            viewLevelPositionStack?.get(currentLevel -1 )?.visibility = View.GONE
        }
        currentLevel++
        levelCount--

        slidePointerAnimation()

        object : CountDownTimer(3600, 10) {
            override fun onTick(millisUntilFinished: Long) {
                angle = (3600 - millisUntilFinished)/10.toDouble()
                // logic to set the EditText could go here
                activity ?: return
                Log.d("angle", angle.toString())
            }

            override fun onFinish() {
                angle = 0.0
            }
        }.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun setLevelsData() {
        if (cookingData?.levelTaps == 3) {
            viewLevelPositionStack = arrayListOf(binding?.positionOne, binding?.positionTwo, binding?.positionThree)
            levelCount = 3
        }
    }
}