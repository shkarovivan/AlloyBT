package com.example.alloybt.viewpager

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentViewPagerBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager), AddBadge {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var pages: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openPages()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pages = listOf(
            resources.getString(R.string.page_head_text_monitor),
            resources.getString(R.string.page_head_text_control),
            resources.getString(R.string.page_head_text_errors),
            resources.getString(R.string.page_head_text_stat)
        )
    }

    override fun onResume() {
        super.onResume()
        checkScreenOrientation()

    }

    // open pages, filtered by Tags,  with TabLayout
    private fun openPages() {
        //  val filteredPages = filteredPagesByTag(pages, checkedTags)
        //  val pageIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        //  val viewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        val adapter = PageAdapter(pages.size, this)
        binding.viewPager.adapter = adapter
        binding.wormDotsIndicator.setViewPager2(binding.viewPager)
        //  pageIndicator.setViewPager2(viewPager2)
        binding.viewPager.offscreenPageLimit = 1

        binding.viewPager.setPageTransformer(ZoomOutTransformation())

        // set TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = pages[position]
        }.attach()
    }

    override fun addBadge() {
        val badgePosition = (pages.indices).random()
        binding.tabLayout.getTabAt(badgePosition)?.orCreateBadge?.apply {
            number += 1
            badgeGravity = BadgeDrawable.TOP_END
        }
    }

    private fun checkScreenOrientation(){
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            else -> (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        }
    }

}