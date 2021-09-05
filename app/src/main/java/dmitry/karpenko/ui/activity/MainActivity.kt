package dmitry.karpenko.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dmitry.karpenko.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dmitry.karpenko.R
import dmitry.karpenko.ui.hotGifFragment.HotGifFragment
import dmitry.karpenko.ui.latestGifFragment.LatestGifFragment
import dmitry.karpenko.ui.randomGifFragment.RandomGifFragment
import dmitry.karpenko.ui.topGifFragment.TopGifFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabsTitles : Array<String>
    private var NUM_PAGES : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabsTitles = arrayOf(
            getString(R.string.latest),
            getString(R.string.top),
            getString(R.string.hot),
            getString(R.string.random)
        )
        NUM_PAGES = tabsTitles.size
        binding.viewPager.adapter = PagerAdapter(this)
        val tabLayout = binding.tabLayout
        TabLayoutMediator(
            tabLayout, binding.viewPager
        ) { tab, position -> tab.setText(tabsTitles.get(position)) }.attach()
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount() = NUM_PAGES

        override fun createFragment(position: Int) : Fragment {
            val str = tabsTitles[position]
            when(str) {
                getString(R.string.latest) -> return LatestGifFragment()
                getString(R.string.top) -> return TopGifFragment()
                getString(R.string.hot) -> return HotGifFragment()
                getString(R.string.random) -> return RandomGifFragment()
                else -> throw IllegalStateException()
            }
        }
    }
}