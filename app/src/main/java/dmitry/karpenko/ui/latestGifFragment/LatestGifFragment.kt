package dmitry.karpenko.ui.latestGifFragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dmitry.karpenko.R
import dmitry.karpenko.data.database.database.GifDatabase
import dmitry.karpenko.data.database.entity.Gif
import dmitry.karpenko.databinding.ViewPagerItemBinding
import dmitry.karpenko.repository.DefaultGifRepository
import dmitry.karpenko.ui.GifFragment.GifFragment
import dmitry.karpenko.ui.GifFragment.GifViewModel
import dmitry.karpenko.ui.hotGifFragment.HotGifViewModel
import dmitry.karpenko.ui.randomGifFragment.RandomGifViewModel

class LatestGifFragment : GifFragment() {
    lateinit var viewModel: LatestGifViewModel

    override fun getFragmentViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = requireActivity().application
        viewModel = ViewModelProvider(this, GifViewModel.Factory(DefaultGifRepository(GifDatabase.getDatabase(app))))
            .get(LatestGifViewModel::class.java)
    }
}