package pt.ipt.dam.galeria_virtual

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import pt.ipt.dam.galeria_virtual.fragmentos.fragmento1
import pt.ipt.dam.galeria_virtual.fragmentos.fragmento2
import pt.ipt.dam.galeria_virtual.fragmentos.fragmento3

class ViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {


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