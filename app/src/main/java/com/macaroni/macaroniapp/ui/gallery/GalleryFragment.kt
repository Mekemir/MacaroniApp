package com.macaroni.macaroniapp.ui.gallery

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.databinding.FragmentGalleryBinding
import java.util.Timer
import java.util.TimerTask


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val timer = Timer()
    val timerForPosition = Timer()
    var angle = 0.0
    var shouldStop = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.pauseBtn.setOnClickListener {
            shouldStop = true
            //working
            if (angle < 40 && angle > 15) {
                binding.pointer.rotation = (angle - 20).toFloat()
                Log.d("Current", angle.toString())
            } else if (angle < 70 && angle > 40) {
                binding.pointer.rotation = (angle - 10).toFloat()
                Log.d("Current", angle.toString())
            }
            else if (angle > 185.0) {
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
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executePointerMove()
    }

    private fun slidePointerAnimation() {
        val animationBack = AnimationUtils.loadAnimation(activity, R.anim.rotate_anim_opposite)

        val animation = AnimationUtils.loadAnimation(activity, R.anim.rotation_anim)
        animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    if (!shouldStop) {
                        binding.pointer.animation = animationBack
                    }
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })

        binding.pointer.animation = animation


//        val anim: ObjectAnimator = ObjectAnimator.ofFloat<View>(binding.pointer, View.ROTATION, 0f, 180f)
//            .setDuration(2000)
//        anim.interpolator = LinearInterpolator()
//        anim.addListener()
//        anim.start()
    }

    private fun executePointerMove() {

        timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                if (!shouldStop) {
                    //angle = 0.0
                    slidePointerAnimation()
                }
            }

        }, 0 , 3600)
        timerForPosition.scheduleAtFixedRate(object: TimerTask() {
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
    }
}