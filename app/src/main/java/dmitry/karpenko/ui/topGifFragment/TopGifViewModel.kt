package dmitry.karpenko.ui.topGifFragment

import android.app.Application
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.repository.GifRepository
import dmitry.karpenko.ui.GifFragment.GifViewModel

class TopGifViewModel(repository : GifRepository) : GifViewModel(repository) {
    override val category: Category = Category.TOP
}