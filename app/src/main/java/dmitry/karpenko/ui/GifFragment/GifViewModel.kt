package dmitry.karpenko.ui.GifFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dmitry.karpenko.data.database.database.GifDatabase
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.data.database.entity.Gif
import dmitry.karpenko.repository.DefaultGifRepository
import dmitry.karpenko.repository.GifRepository
import dmitry.karpenko.ui.hotGifFragment.HotGifViewModel
import dmitry.karpenko.ui.latestGifFragment.LatestGifViewModel
import dmitry.karpenko.ui.randomGifFragment.RandomGifViewModel
import dmitry.karpenko.ui.topGifFragment.TopGifViewModel
import kotlinx.coroutines.launch

abstract class GifViewModel(private val repository : GifRepository) : ViewModel() {

    abstract val category : Category

    private var _gif: MutableLiveData<Gif?> = MutableLiveData(null)
    val gif: LiveData<Gif?>
        get() = _gif

    lateinit var currentPos: LiveData<Long>

    private var _hasPrevious = MutableLiveData(false)
    val hasPrevious: LiveData<Boolean>
        get() = _hasPrevious

    private var _status : MutableLiveData<Status> = MutableLiveData(Status.Download)
    val status: LiveData<Status>
        get() = _status

    /* Метод, который необходимо вызвать перед началом работы с viewModel
     для получение текущей позиции для категории
     */
    fun findCurrentPos() {
        getCurrentPos()
        currentPos.observeForever {
            getCurrentGif()
            checkPrevious()
        }
    }

    /* Методы onNext(), onPrev(), onError(), onDownload(), onSuccess() -
        методы обратного вызова вызываемые Фрагментом при наступлении соответствующих событий
     */
    fun onNext() {
        viewModelScope.launch {
            var pos = currentPos.value
            pos?.let {
                repository.setCurruntPosition(category, ++pos)
                checkPrevious()
            }
        }
    }

    fun onPrev() {
        viewModelScope.launch {
            var pos = currentPos.value
            if (pos != null) {
                if (pos > 0) {
                    repository.setCurruntPosition(category, --pos)
                    checkPrevious()
                }
            }
        }
    }

    fun onError() {
        _status.value = Status.GlideError
    }

    fun onDownload() {
        _status.value = Status.Download
    }

    fun onSuccess() {
        _status.value = Status.Success
    }

    private fun getCurrentPos() {
        viewModelScope.launch {
            currentPos = repository.getCurrentPosition(category)
            checkPrevious()
        }
    }

    private fun checkPrevious() {
        val pos = currentPos.value
        pos?.let {
            _hasPrevious.value = repository.hasPrevious(it)
        }
    }

    //Получение из Репозитория информации о текущем гиф-изображении для текущей категории
    fun getCurrentGif() {
        val pos = currentPos.value
        pos?.let {
            viewModelScope.launch {
                try {
                    _gif.value = repository.getGif(category, it)
                    _status.value = Status.Success
                }  catch (e : Exception ) {
                    _status.value = Status.RetrofitError
                }
            }
        }
    }


    class Factory(val repository : GifRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            when {
                modelClass.isAssignableFrom(LatestGifViewModel::class.java) -> {
                    @Suppress("UNCHECKED_CAST")
                    return LatestGifViewModel(repository) as T
                }
                modelClass.isAssignableFrom(TopGifViewModel::class.java) -> {
                    @Suppress("UNCHECKED_CAST")
                    return TopGifViewModel(repository) as T
                }
                modelClass.isAssignableFrom(HotGifViewModel::class.java) -> {
                    @Suppress("UNCHECKED_CAST")
                    return HotGifViewModel(repository) as T
                }
                modelClass.isAssignableFrom(RandomGifViewModel::class.java) -> {
                    @Suppress("UNCHECKED_CAST")
                    return RandomGifViewModel(repository) as T
                }
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    enum class Status {
        Success,Download,RetrofitError,GlideError
    }
}