package dmitry.karpenko.ui.GifFragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dmitry.karpenko.R
import dmitry.karpenko.data.database.entity.Gif
import dmitry.karpenko.databinding.ViewPagerItemBinding
import dmitry.karpenko.ui.GifFragment.GifViewModel.Status.*

abstract class GifFragment : Fragment(R.layout.view_pager_item) {

    private lateinit var viewModel: GifViewModel
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

    abstract fun getFragmentViewModel() : GifViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ViewPagerItemBinding.bind(view)
        viewModel = getFragmentViewModel()
        viewModel.findCurrentPos()
        viewModel.hasPrevious.observe(viewLifecycleOwner) {
            binding.back.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        viewModel.gif.observe(viewLifecycleOwner) {
            if (it != null) {
                showGif(it)
            }
        }
        viewModel.status.observe(viewLifecycleOwner) {
            binding.retry.visibility = View.INVISIBLE
            when(it) {
                Success -> binding.progress.hide()
                Download -> binding.progress.show()
                GlideError -> {
                    binding.progress.hide()
                    onError()
                }
                RetrofitError -> {
                    binding.progress.hide()
                    onError()
                    Glide.with(this).load(R.drawable.warning_message).into(binding.gif)
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
    }

    private fun showGif(gif : Gif) {
        Glide.with(this)
            .load(gif.gifURL)
            .error(
                Glide.with(this)
                .load(R.drawable.warning_message))
            .addListener(requestListener)
            .into(binding.gif)
        binding.description.text = gif.description
    }
}