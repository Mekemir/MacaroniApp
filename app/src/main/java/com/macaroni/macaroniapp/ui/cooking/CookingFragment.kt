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


class CookingFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var binding: FragmentGalleryBinding? = null
    var angle = 0.0
    var shouldStop = false
    var cookingData: CookingItemData? = null
    var viewLevelPositionStack: ArrayList<ImageView?>? = null
    var levelAngleList: ArrayList<Int> = arrayListOf()
    var levelCount = 0
    var currentLevel = 0
    var numberOfCorrectAnswers = 0
    var startTime: Long = 0
    var endTime: Long = 0


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
            endTime = System.currentTimeMillis()
            val newAngle = (endTime - startTime)/20
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

            checkIfAnswerIsValid()
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
                    startTime = System.currentTimeMillis()
                    angle = 0.0
                }

                override fun onAnimationEnd(p0: Animation?) {
                    endTime = System.currentTimeMillis()
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
        if (levelCount == 0) {
            if (numberOfCorrectAnswers == levelAngleList.size) {
                val toast = Toast.makeText(this.context, "wow", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val toast = Toast.makeText(this.context, numberOfCorrectAnswers.toString(), Toast.LENGTH_SHORT)
                toast.show()
            }
            return
        }
        binding?.pointer?.rotation = 0f
        binding?.pauseBtn?.visibility = View.VISIBLE

        viewLevelPositionStack?.get(currentLevel)?.visibility = View.VISIBLE
        if (currentLevel != 0) {
            viewLevelPositionStack?.get(currentLevel -1 )?.visibility = View.GONE
        }
        currentLevel++
        levelCount--

        slidePointerAnimation()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun setLevelsData() {
        levelCount = cookingData?.levelTaps ?: 0
        if (cookingData?.levelTaps == 3) {
            viewLevelPositionStack = arrayListOf(binding?.positionOne, binding?.positionTwo, binding?.positionThree)
            levelAngleList =  arrayListOf(Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"), Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"), Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"))
        }
        if (cookingData?.levelTaps == 4) {
            viewLevelPositionStack = arrayListOf(binding?.positionEight, binding?.positionFour, binding?.positionSix, binding?.positionFive)
            levelAngleList =  arrayListOf(Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"), Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"), Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"), Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"))
        }
        if (cookingData?.levelTaps == 5) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionFive,
                binding?.positionOne,
                binding?.positionSeven
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 6) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionTwo,
                binding?.positionSix,
                binding?.positionSeven,
                binding?.positionThree
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 7) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionFive,
                binding?.positionTwo,
                binding?.positionFour
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 8) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionOne,
                binding?.positionFive,
                binding?.positionSeven,
                binding?.positionFour,
                binding?.positionTwo,
                binding?.positionEight
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1")
                )
        }
        if (cookingData?.levelTaps == 9) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionFive,
                binding?.positionSix,
                binding?.positionTwo,
                binding?.positionSeven,
                binding?.positionOne,
                binding?.positionFour,
                binding?.positionEight,
                binding?.positionThree,
                binding?.positionOne
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1")
                )
        }
        if (cookingData?.levelTaps == 10) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionTwo,
                binding?.positionSix,
                binding?.positionEight,
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionFive,
                binding?.positionOne,
                binding?.positionSeven
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1")
                )
        }
        if (cookingData?.levelTaps == 11) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionFive,
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionTwo,
                binding?.positionSix,
                binding?.positionSeven,
                binding?.positionThree
            )
            levelAngleList = arrayListOf(
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1")
            )

        }
        if (cookingData?.levelTaps == 12) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionFive,
                binding?.positionTwo,
                binding?.positionFour,
                binding?.positionFour,
                binding?.positionTwo,
                binding?.positionEight
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 13) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionTwo,
                binding?.positionSix,
                binding?.positionEight,
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionFive,
                binding?.positionOne,
                binding?.positionSeven
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 14) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionFive,
                binding?.positionTwo,
                binding?.positionFour,
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionFive,
                binding?.positionTwo,
                binding?.positionFour
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 15) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionFive,
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionOne,
                binding?.positionFive,
                binding?.positionSeven,
                binding?.positionFour,
                binding?.positionTwo,
                binding?.positionEight,
                binding?.positionThree,
                binding?.positionSix
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 16) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionOne,
                binding?.positionFive,
                binding?.positionSeven,
                binding?.positionFour,
                binding?.positionTwo,
                binding?.positionEight,
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionOne,
                binding?.positionFive,
                binding?.positionSeven,
                binding?.positionFour,
                binding?.positionTwo,
                binding?.positionEight
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1")
            )
        }
        if (cookingData?.levelTaps == 17) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionTwo,
                binding?.positionSix,
                binding?.positionEight,
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionFive,
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionTwo,
                binding?.positionSix,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionSix
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1")
                )
        }
        if (cookingData?.levelTaps == 18) {
            viewLevelPositionStack = arrayListOf(
                binding?.positionThree,
                binding?.positionEight,
                binding?.positionFive,
                binding?.positionOne,
                binding?.positionSeven,
                binding?.positionThree,
                binding?.positionSix,
                binding?.positionOne,
                binding?.positionFive,
                binding?.positionSeven,
                binding?.positionFour,
                binding?.positionTwo,
                binding?.positionEight,
                binding?.positionOne,
                binding?.positionFour,
                binding?.positionEight,
                binding?.positionThree,
                binding?.positionOne
            )
            levelAngleList =  arrayListOf(
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSix?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFive?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionSeven?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionTwo?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionFour?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionEight?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionThree?.tag.toString() ?: "-1"),
                Integer.valueOf(binding?.positionOne?.tag.toString() ?: "-1")
            )
        }
    }

    private fun checkIfAnswerIsValid() {
        val tappedAngle: Double = (binding?.pointer?.rotation?.toDouble() ?: 0.0)
        val correct1 = (tappedAngle - 2).toInt()
        val correct2 = (tappedAngle - 1).toInt()
        val correct3 = tappedAngle.toInt()
        val correct4 = (tappedAngle + 1).toInt()
        val correct5 = (tappedAngle + 2).toInt()

        if (levelAngleList.contains(correct1)
            || levelAngleList.contains(correct2)
            || levelAngleList.contains(correct3)
            || levelAngleList.contains(correct4)
            || levelAngleList.contains(correct5)) {
            numberOfCorrectAnswers += 1
        }
    }
}