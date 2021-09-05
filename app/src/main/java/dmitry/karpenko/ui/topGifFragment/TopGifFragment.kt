package dmitry.karpenko.ui.topGifFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dmitry.karpenko.R
import dmitry.karpenko.data.database.database.GifDatabase
import dmitry.karpenko.databinding.ViewPagerItemBinding
import dmitry.karpenko.repository.DefaultGifRepository
import dmitry.karpenko.ui.GifFragment.GifFragment
import dmitry.karpenko.ui.GifFragment.GifViewModel
import dmitry.karpenko.ui.latestGifFragment.LatestGifViewModel
import dmitry.karpenko.ui.randomGifFragment.RandomGifViewModel

class TopGifFragment : GifFragment() {
    lateinit var viewModel: TopGifViewModel

    override fun getFragmentViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = requireActivity().application
        viewModel = ViewModelProvider(this, GifViewModel.Factory(DefaultGifRepository(GifDatabase.getDatabase(app))))
            .get(TopGifViewModel::class.java)
    }
}