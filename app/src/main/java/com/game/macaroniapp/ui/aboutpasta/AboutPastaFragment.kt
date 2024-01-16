package com.game.macaroniapp.ui.aboutpasta

import android.app.ActionBar.LayoutParams
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnScrollChangeListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.game.macaroniapp.databinding.AboutPastaFragmentBinding

class AboutPastaFragment: Fragment() {
    private var _binding: AboutPastaFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(AboutPastaViewModel::class.java)

        _binding = AboutPastaFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _binding?.homeBtn?.setOnClickListener {
            activity?.onBackPressed()
        }
        val scroll: NestedScrollView? = _binding?.scrolling
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val scrolxxx = scrollX
                val scrolold = oldScrollX
                val deviderFactor = (_binding?.scrolHolder?.height ?: 1)/(_binding?.scrollBackgroundPos?.height ?: 1)
                val layoutParams = RelativeLayout.LayoutParams(_binding?.macaroniView?.width ?: 50, _binding?.macaroniView?.height ?: 50)
                var newMarginTop = (_binding?.macaroniView?.marginTop ?: 0) + (scrollY - oldScrollY)/deviderFactor
                if (newMarginTop < 275) {
                    newMarginTop = 275
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParams.setMargins((_binding?.macaroniView?.marginStart ?: 0), newMarginTop, (_binding?.macaroniView?.marginRight ?: 0), (_binding?.macaroniView?.marginBottom ?: 0))
                _binding?.macaroniView?.layoutParams = layoutParams
            }
            _binding?.scrollBackgroundPos?.setOnTouchListener(object : OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    if (p1?.action == MotionEvent.ACTION_MOVE
                        || p1?.action == MotionEvent.ACTION_UP) {
                        val y = p1?.y ?: return false
                        //Log.d("touchPositin", y.toString())
                        val multiplicationFactor = (_binding?.scrolHolder?.height ?: 1)/(_binding?.scrollBackgroundPos?.height ?: 1)
                        if ((y.plus((multiplicationFactor* (y?.toInt() ?: 0))) <= ((binding?.scrolHolder?.height ?: 1) - 100))) {
                            val layoutParams = RelativeLayout.LayoutParams(
                                _binding?.macaroniView?.width ?: 50,
                                _binding?.macaroniView?.height ?: 50
                            )
                            //val newMarginTop = (_binding?.macaroniView?.marginTop ?: 0) + y?.toInt() ?: 0
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                            val marginTop = if (((y?.toInt() ?: 0) + 180) < 275) {
                                275
                            } else {
                                ((y?.toInt() ?: 0) + 180)
                            }
                            layoutParams.setMargins(
                                (_binding?.macaroniView?.marginStart ?: 0),
                                marginTop,
                                (_binding?.macaroniView?.marginRight ?: 0),
                                (_binding?.macaroniView?.marginBottom ?: 0)
                            )

                            Log.d("touchPositin", y.toString())
                            _binding?.macaroniView?.layoutParams = layoutParams

                            //find comparation value
                            scroll?.smoothScrollTo(
                                (scroll?.scrollX ?: 0),
                                multiplicationFactor * (y?.toInt() ?: 0)
                            )
//                            Log.d("scrolHolder?.height", (_binding?.scrolHolder?.height).toString())
//                            Log.d(
//                                "multiplicationFactor",
//                                (multiplicationFactor * (y?.toInt() ?: 0)).toString()
//                            )
                        }
                    }
                    return false
                }
            })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}