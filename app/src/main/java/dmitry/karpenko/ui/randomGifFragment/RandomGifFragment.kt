package dmitry.karpenko.ui.randomGifFragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import dmitry.karpenko.ui.latestGifFragment.LatestGifViewModel
import dmitry.karpenko.ui.topGifFragment.TopGifViewModel

/*class RandomGifFragment : Fragment(R.layout.view_pager_item) {

    private val viewModel: RandomGifViewModel by lazy {
        val activity = requireActivity()
        ViewModelProvider(this, RandomGifViewModel.Factory(activity.application))
            .get(RandomGifViewModel::class.java)
    }

    private lateinit var binding: ViewPagerItemBinding

    private val requestListener : RequestListener<Drawable> = object : RequestListener<Drawable> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            viewModel.onError()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            viewModel.onSuccess()
            return false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ViewPagerItemBinding.bind(view)
        viewModel.hasPrevious.observe(viewLifecycleOwner) {
            binding.back.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        viewModel.gif.observe(viewLifecycleOwner) {
            if (it != null) {
                showGif(it)
            }
        }
        viewModel.status.observe(viewLifecycleOwner) {
            when(it) {
                RandomGifViewModel.Status.Success -> binding.progress.hide()
                RandomGifViewModel.Status.Download -> binding.progress.show()
                RandomGifViewModel.Status.Error -> {
                    binding.progress.hide()
                    onError()
                }
            }
        }

        binding.next.setOnClickListener {
            onDownload()
            viewModel.onNext()
        }
        binding.back.setOnClickListener {
            onDownload()
            viewModel.onPrev()
        }
        binding.retry.setOnClickListener {
            onDownload()
            viewModel.getCurrentGif()
        }
    }

    private fun onDownload() {
        binding.retry.visibility = View.INVISIBLE
        viewModel.onDownload()
        binding.description.text = ""
        binding.gif.setImageResource(android.R.color.transparent)
    }

    private fun onError() {
        binding.description.text = ""
        binding.retry.visibility = View.VISIBLE
        Glide.with(this).load(R.drawable.warning_message).into(binding.gif)
    }

    private fun showGif(gif : Gif) {
        Glide.with(this)
            .load(gif.gifURL)
            .addListener(requestListener)
            .into(binding.gif)
        binding.description.text = gif.description
    }
}*/

class RandomGifFragment : GifFragment() {
    lateinit var viewModel: RandomGifViewModel

    override fun getFragmentViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = requireActivity().application
        viewModel = ViewModelProvider(this, GifViewModel.Factory(DefaultGifRepository(GifDatabase.getDatabase(app))))
            .get(RandomGifViewModel::class.java)
    }
}