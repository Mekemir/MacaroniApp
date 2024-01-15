package com.game.macaroniapp.ui.cooking

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.game.macaroniapp.CookingItemData
import com.game.macaroniapp.MainActivity
import com.game.macaroniapp.Player
import com.game.macaroniapp.R
import com.game.macaroniapp.databinding.FragmentGalleryBinding
import com.game.macaroniapp.getRecipesData
import com.game.macaroniapp.preferences.PreferencesRepository


class CookingFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var binding: FragmentGalleryBinding? = null
    private lateinit var viewModel: CookingViewModel
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
    var levelIndex: Int = -1
    var isCorrectText = false
    var allTimeCorrectNumber: String = "0?"
    var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(CookingViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container,  false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        this.activity?.applicationContext?.let {
            viewModel.preferencesRepository = PreferencesRepository.getInstance(it)
            viewModel.numberOfCorrectFlow = viewModel.preferencesRepository?.numberOfCorrectFlow
        }
        binding?.data = viewModel
        val root: View? = binding?.root
        setLevelsData()
        binding?.homeBtn?.setOnClickListener {
            activity?.onBackPressed()
        }

        binding?.pauseBtn?.setOnClickListener {
            shouldStop = true
            endTime = System.currentTimeMillis()
            var newAngle = (endTime - startTime)/20
            
            //working
            if (newAngle >= 0 && newAngle <= 25) {
                binding?.pointer?.rotation = (newAngle - 13).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle <= 50 && newAngle > 25) {
                binding?.pointer?.rotation = (newAngle - 20).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle <= 58 && newAngle > 50) {
                binding?.pointer?.rotation = (newAngle - 17).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle <= 60 && newAngle > 58) {
                binding?.pointer?.rotation = (newAngle - 13).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle < 73 && newAngle > 60) {
                binding?.pointer?.rotation = (newAngle - 10).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle < 80 && newAngle >= 73) {
                binding?.pointer?.rotation = (newAngle - 6).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle < 97 && newAngle >= 92) {
                binding?.pointer?.rotation = (newAngle + 2).toFloat()
                Log.d("Current", newAngle.toString())
            }  else if (newAngle < 108 && newAngle >= 97) {
                binding?.pointer?.rotation = (newAngle + 6).toFloat()
                Log.d("Current", newAngle.toString())
            }  else if (newAngle < 115 && newAngle >= 108) {
                binding?.pointer?.rotation = (newAngle + 10).toFloat()
                Log.d("Current", newAngle.toString())
            }  else if (newAngle < 140 && newAngle >= 115) {
                binding?.pointer?.rotation = (newAngle + 15).toFloat()
                Log.d("Current", newAngle.toString())
            }   else if (newAngle < 150 && newAngle >= 140) {
                binding?.pointer?.rotation = (newAngle + 20).toFloat()
                Log.d("Current", newAngle.toString())
            } else if (newAngle < 160 && newAngle >= 150) {
                binding?.pointer?.rotation = (newAngle + 15).toFloat()
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
            isCorrectText = (pastaOne.tag != (cookingData?.incorrectPasta?.toLowerCase() ?: ""))
            val toast = Toast.makeText(this.context, isCorrectText.toString(), Toast.LENGTH_SHORT)
            // toast.show()
            binding?.choosePastaHolder?.visibility = View.GONE
            binding?.cookingHolder?.visibility = View.VISIBLE
            executePointerMove()
        }
        pastaTwo.setOnClickListener {
            isCorrectText = (pastaTwo.tag != (cookingData?.incorrectPasta?.toLowerCase() ?: ""))
            val toast = Toast.makeText(this.context, isCorrectText.toString(), Toast.LENGTH_SHORT)
            // toast.show()
            binding?.choosePastaHolder?.visibility = View.GONE
            binding?.cookingHolder?.visibility = View.VISIBLE
            executePointerMove()
        }
        pastaThree.setOnClickListener {
            isCorrectText = (pastaThree.tag != (cookingData?.incorrectPasta?.toLowerCase() ?: ""))
            val toast = Toast.makeText(this.context, isCorrectText.toString(), Toast.LENGTH_SHORT)
            // toast.show()
            binding?.choosePastaHolder?.visibility = View.GONE
            binding?.cookingHolder?.visibility = View.VISIBLE
            executePointerMove()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.numberOfCorrectFlow?.asLiveData()?.observe(binding?.lifecycleOwner ?: return) {
            if (it != "0?") {
                allTimeCorrectNumber = allTimeCorrectNumber + it
            }
        }
    }

    private fun slidePointerAnimation() {

        val animation = AnimationUtils.loadAnimation(activity, R.anim.rotation_anim)
        animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    startTime = System.currentTimeMillis()
                    angle = 0.0
                    levelIndex = levelIndex + 1
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
            if (numberOfCorrectAnswers == levelAngleList.size && isCorrectText) {
                val toast = Toast.makeText(this.context, "wow", Toast.LENGTH_SHORT)
                // toast.show()
                viewModel.newCorrectAnswerAllTimeCount(allTimeCorrectNumber + (cookingData?.levelTaps ?: 0).toString() + "?")
            } else {
                val toast = Toast.makeText(this.context, numberOfCorrectAnswers.toString(), Toast.LENGTH_SHORT)
                // toast.show()
            }
            return
        }
        binding?.potImage?.setImageDrawable(context?.getDrawable(R.drawable.pot_image))
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkIfAnswerIsValid() {
        val currentCorrectAngle = levelAngleList.get(levelIndex)
        val tappedAngle: Double = (binding?.pointer?.rotation?.toDouble() ?: 0.0)
        val correct0 = (currentCorrectAngle - 3).toInt()
        val correct1 = (currentCorrectAngle - 2).toInt()
        val correct2 = (currentCorrectAngle - 1).toInt()
        val correct3 = currentCorrectAngle.toInt()
        val correct4 = (currentCorrectAngle + 1).toInt()
        val correct5 = (currentCorrectAngle + 2).toInt()

        val correctRange = correct0..correct5

        if (correctRange.contains(tappedAngle.toInt())) {
            numberOfCorrectAnswers += 1
            // delicious
            binding?.potImage?.setImageDrawable(context?.getDrawable(R.drawable.delicious_imae))
            return
        }
        // check for good cooked

        val goodRangeFirst = ((currentCorrectAngle-3)-14) until ((currentCorrectAngle-3))
        val goodRangeSecond = ((currentCorrectAngle+2)+1) until ((currentCorrectAngle+2) + 14)
        if (goodRangeFirst.contains(tappedAngle.toInt())
            || goodRangeSecond.contains(tappedAngle.toInt())) {
            // good range
            binding?.potImage?.setImageDrawable(context?.getDrawable(R.drawable.good))
        } else {
            // bad range
            binding?.potImage?.setImageDrawable(context?.getDrawable(R.drawable.notbad))
        }

    }

    override fun onPause() {
        super.onPause()
        player?.stop()
    }
}