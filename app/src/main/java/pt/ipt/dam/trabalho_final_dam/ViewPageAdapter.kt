package pt.ipt.dam.trabalho_final_dam

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import pt.ipt.dam.trabalho_final_dam.fragmentos.fragmento1
import pt.ipt.dam.trabalho_final_dam.fragmentos.fragmento2
import pt.ipt.dam.trabalho_final_dam.fragmentos.fragmento3

class MyViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return fragmento1()
            1 -> return fragmento2()
            2 -> return fragmento3()
            else -> return fragmento1()
        }
    }
}