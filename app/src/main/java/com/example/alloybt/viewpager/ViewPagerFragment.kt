package com.example.alloybt.viewpager

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.alloybt.BtDevice
import com.example.alloybt.BtDeviceControlFragmentArgs
import com.example.alloybt.R
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_view_pager.*

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager), AddBadge {



    private var btDeviceInformation: BtDevice? = null

    private var checkedTags = booleanArrayOf(true, true, true)
    private lateinit var pages: List<String>


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
            resources.getString(R.string.page_head_text_stat))
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
        viewPager.adapter = adapter
        worm_dots_indicator.setViewPager2(viewPager)
        //  pageIndicator.setViewPager2(viewPager2)
        viewPager.offscreenPageLimit = 1

        viewPager.setPageTransformer(ZoomOutTransformation())

        // set TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pages[position]
        }.attach()
    }

    override fun addBadge() {
        val badgePosition = (pages.indices).random()
        tabLayout.getTabAt(badgePosition)?.orCreateBadge?.apply {
            number += 1
            badgeGravity = BadgeDrawable.TOP_END
        }
    }

    override fun selectTags(tags: BooleanArray) {
        checkedTags = tags
        openPages()
    }

    private fun checkScreenOrientation(){
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            else -> (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        }
    }

}