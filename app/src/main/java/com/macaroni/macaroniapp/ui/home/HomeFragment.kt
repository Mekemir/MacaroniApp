package com.macaroni.macaroniapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.databinding.FragmentHomeBinding
import com.macaroni.macaroniapp.ui.aboutpasta.AboutPastaFragment
import com.macaroni.macaroniapp.ui.cooking.intro.CookingIntroFragment
import com.macaroni.macaroniapp.ui.recipes.RecipesFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.cook.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, CookingIntroFragment(), "CookingIntroFragment::class.java")
                ?.commit()
        }

        binding.aLittleAbout.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, AboutPastaFragment(), "AboutPastaFragment::class.java")
                ?.commit()
        }

        binding.recipes.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, RecipesFragment(), "RecipesFragment::class.java")
                ?.commit()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}