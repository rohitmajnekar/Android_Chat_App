package com.rohit.myapplication


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position){
            0-> {
                FragmentOne()
            }
            1-> {
                FragmentTwo()
            }
            else->{
                FragmentThree()
            }
        }
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0->"Chats"
            1->"Friend"
            else->"Request"
        }
    }


}
