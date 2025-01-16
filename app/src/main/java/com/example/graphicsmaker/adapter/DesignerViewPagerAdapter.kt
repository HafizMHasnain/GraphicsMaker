package com.example.graphicsmaker.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.graphicsmaker.sticker_fragment.FragmentStepOne
import com.example.graphicsmaker.sticker_fragment.FragmentStepThree
import com.example.graphicsmaker.sticker_fragment.FragmentStepTwo

class DesignerViewPagerAdapter(private val _context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    private val TITLES = arrayOf("STEP 1", "STEP 2", "STEP 3")
    var bakgrndFragment: Fragment? = null
    var fragments: ArrayList<Fragment?> = ArrayList()

    init {
        val f: Fragment = FragmentStepOne()
        for (i in 0..2) {
            fragments.add(f)
        }
    }

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            this.bakgrndFragment = FragmentStepOne()
        } else if (position == 1) {
            this.bakgrndFragment = FragmentStepTwo()
        } else if (position == 2) {
            this.bakgrndFragment = FragmentStepThree()
        }
        fragments[position] = this.bakgrndFragment
        return bakgrndFragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TITLES[position]
    }

    override fun getCount(): Int {
        return TITLES.size
    }

    fun currentFragment(position: Int): Fragment? {
        return fragments[position]
    }
}
