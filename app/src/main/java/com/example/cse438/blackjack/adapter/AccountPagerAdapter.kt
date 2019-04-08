package com.example.cse438.blackjack.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.cse438.blackjack.fragment.LoginFragment
import com.example.cse438.blackjack.fragment.SignupFragment

//pager adapter for login / signup
class AccountPagerAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm) {
    //sets fragments for the pager
    override fun getItem(p0: Int): Fragment {
        return if (p0 == 0) {
            LoginFragment(context)
        } else {
            SignupFragment(context)
        }
    }

    //sets size of pager
    override fun getCount(): Int {
        return 2
    }

    //sets the title of the pager
    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            //context.getString(R.string.create_account)
            return "Login"
        } else {
            //context.getString(R.string.sign_in)
            return "Sign Up"
        }
    }
}