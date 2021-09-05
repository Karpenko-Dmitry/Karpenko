package dmitry.karpenko.ui.GifFragment

import androidx.test.ext.junit.runners.AndroidJUnit4
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.data.database.entity.Gif
import dmitry.karpenko.getOrAwaitValue
import dmitry.karpenko.repository.FakeRepository
import dmitry.karpenko.ui.hotGifFragment.HotGifViewModel
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HotGifViewModelTest {
    private lateinit var gifViewModel: GifViewModel
    private lateinit var repository: FakeRepository
    private val gifs : Array<Gif> = arrayOf(
        Gif(0,Category.HOT.value,"description1","url"),
        Gif(0,Category.HOT.value,"description2","url")
    )

    @Before
    fun setupViewModel() {
        repository = FakeRepository(gifs,2)
        gifViewModel = HotGifViewModel(repository)
        gifViewModel.findCurrentPos()
    }

    @Test
    fun category_HotCategory() {
        assertThat(gifViewModel.category, `is`( Category.HOT))
    }

    @Test
    fun getCurrentPos_equalsCurPosRepository() {
        val pos = gifViewModel.currentPos.getOrAwaitValue()
        assertThat(pos, `is`(repository.curPosByCategory[gifViewModel.category.value]))
    }

    @Test
    fun getFirstGif_equalsGifRepositoryStateHasPrevious() {
        val pos = gifViewModel.currentPos.getOrAwaitValue()
        gifViewModel.getCurrentGif()
        val gif = gifViewModel.gif.getOrAwaitValue()
        assertThat(gif,`is`(repository.gifs[0]))
        val hasPrev = gifViewModel.hasPrevious.getOrAwaitValue()
        assertThat(hasPrev, `is`(repository.hasPrevious(pos)))
        val status = gifViewModel.status.getOrAwaitValue()
        assertThat(status, `is`(GifViewModel.Status.Success))
    }

    @Test
    fun onNext_equalsGifRepositoryStateHasPrevious() {
        var pos = gifViewModel.currentPos.getOrAwaitValue()
        gifViewModel.onNext()
        assertThat(repository.curPosByCategory[gifViewModel.category.value],`is`(++pos))
        gifViewModel.findCurrentPos()
        assertThat(repository.curPosByCategory[gifViewModel.category.value],`is`(gifViewModel.currentPos.getOrAwaitValue()))
        assertThat(gifViewModel.gif.getOrAwaitValue(),`is`(repository.gifs[1]))
        assertThat(gifViewModel.hasPrevious.getOrAwaitValue(),`is`(true))
        assertThat(gifViewModel.status.getOrAwaitValue(),`is`(GifViewModel.Status.Success))
    }

    @Test
    fun onNextOnNext_Error() {
        gifViewModel.onNext()
        gifViewModel.findCurrentPos()
        assertThat(gifViewModel.currentPos.getOrAwaitValue(),`is`(1))
        gifViewModel.onNext()
        gifViewModel.findCurrentPos()
        assertThat(gifViewModel.currentPos.getOrAwaitValue(),`is`(2))
        gifViewModel.getCurrentGif()
        assertThat(gifViewModel.status.getOrAwaitValue(),`is`(GifViewModel.Status.RetrofitError))
    }

    @Test
    fun onNextOnPrev_StateSuccessHasPrevNot() {
        gifViewModel.onNext()
        gifViewModel.findCurrentPos()
        assertThat(gifViewModel.currentPos.getOrAwaitValue(),`is`(1))
        gifViewModel.onPrev()
        gifViewModel.findCurrentPos()
        assertThat(gifViewModel.currentPos.getOrAwaitValue(),`is`(0))
        gifViewModel.getCurrentGif()
        assertThat(gifViewModel.status.getOrAwaitValue(),`is`(GifViewModel.Status.Success))
        assertThat(gifViewModel.hasPrevious.getOrAwaitValue(),`is`(false))
    }

    @Test
    fun onSuccess_SuccessState() {
        gifViewModel.onSuccess()
        val value = gifViewModel.status.getOrAwaitValue()
        assertThat(value, `is`( GifViewModel.Status.Success))
    }

    @Test
    fun onDownload_DownloadState() {
        gifViewModel.onDownload()
        val value = gifViewModel.status.getOrAwaitValue()
        assertThat(value, `is`( GifViewModel.Status.Download))
    }

    @Test
    fun onError_GlideErrorState() {
        gifViewModel.onError()
        val value = gifViewModel.status.getOrAwaitValue()
        assertThat(value, `is`( GifViewModel.Status.GlideError))
    }
}