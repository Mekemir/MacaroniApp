package com.macaroni.macaroniapp.ui.cooking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.databinding.FragmentGalleryBinding
import java.util.Timer
import java.util.TimerTask


class CookingFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var timer: Timer? = Timer()
    var timerForPosition: Timer? = Timer()
    var angle = 0.0
    var shouldStop = false
    var cookingData: CookingItemData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(CookingViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pauseBtn.setOnClickListener {
            shouldStop = true
            angle = angle/2
            //working
            if (angle <= 60 && angle > 15) {
                binding.pointer.rotation = (angle - 20).toFloat()
                Log.d("Current", angle.toString())
            } else if (angle < 80 && angle > 60) {
                binding.pointer.rotation = (angle - 10).toFloat()
                Log.d("Current", angle.toString())
            }  else if (angle > 185.0) {
                val currentAngle = 180 - (angle - 185)
                binding.pointer.rotation = (currentAngle).toFloat()
                Log.d("Current", angle.toString())
            } else if (angle < 185.0 && angle > 105) {
                binding.pointer.rotation = (angle + 10).toFloat()
                Log.d("Current", angle.toString())
            } else {
                binding.pointer.rotation = (angle).toFloat()
                Log.d("Current", angle.toString())
            }
            //working
            binding.pointer.clearAnimation()
        }
        setPastaChooser()
        return root

    }

    private fun setPastaChooser() {
        val pastaOne = binding.pastaOne
        val pastaTwo = binding.pastaTwo
        val pastaThree = binding.pastaThree
        pastaOne.setImageDrawable(cookingData?.listOfPastas?.get(0))
        pastaTwo.setImageDrawable(cookingData?.listOfPastas?.get(1))
        pastaThree.setImageDrawable(cookingData?.listOfPastas?.get(2))

        pastaOne.setOnClickListener {
            val isCorrectText = if (pastaOne.tag == (cookingData?.incorrectPasta?.toLowerCase() ?: "")) { "no" } else { "yes" }
            val toast = Toast.makeText(this.context, isCorrectText, Toast.LENGTH_SHORT)
            toast.show()
            binding.choosePastaHolder.visibility = View.GONE
            binding.cookingHolder.visibility = View.VISIBLE
            executePointerMove()
        }
        pastaTwo.setOnClickListener {
            val isCorrectText = if (pastaTwo.tag == (cookingData?.incorrectPasta?.toLowerCase() ?: "")) { "no" } else { "yes" }
            val toast = Toast.makeText(this.context, isCorrectText, Toast.LENGTH_SHORT)
            toast.show()
            binding.choosePastaHolder.visibility = View.GONE
            binding.cookingHolder.visibility = View.VISIBLE
        }
        pastaThree.setOnClickListener {
            val isCorrectText = if (pastaThree.tag == (cookingData?.incorrectPasta?.toLowerCase() ?: "")) { "no" } else { "yes" }
            val toast = Toast.makeText(this.context, isCorrectText, Toast.LENGTH_SHORT)
            toast.show()
            binding.choosePastaHolder.visibility = View.GONE
            binding.cookingHolder.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //executePointerMove()
    }

    private fun slidePointerAnimation() {
        val animationBack = AnimationUtils.loadAnimation(activity, R.anim.rotate_anim_opposite)

        val animation = AnimationUtils.loadAnimation(activity, R.anim.rotation_anim)
        animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    if (!shouldStop) {
                        // binding.pointer.animation = animationBack
                        binding.pointer.rotation = 0f
                        timerForPosition?.cancel()
                        timerForPosition = null
                    }
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })

        binding.pointer.animation = animation
    }

    private fun executePointerMove() {

//        timer?.scheduleAtFixedRate(object: TimerTask() {
//            override fun run() {
//                if (!shouldStop) {
//                    //angle = 0.0
//                    slidePointerAnimation()
//                }
//            }
//
//        }, 0 , 3600)
        slidePointerAnimation()
        timerForPosition?.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                if (!shouldStop) {
                    if (angle > 360.0) {
                        angle = 0.0
                    }
                    angle++
                    Log.d("angle", angle.toString())
                }
            }

        }, 0, 10)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer?.cancel()
        timer = null
        timerForPosition?.cancel()
        timerForPosition = null
    }
}