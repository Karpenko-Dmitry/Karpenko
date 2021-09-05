package dmitry.karpenko.ui.latestGifFragment

import android.app.Application
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.repository.GifRepository
import dmitry.karpenko.ui.GifFragment.GifViewModel

/*class LatestGifViewModel(application: Application) : AndroidViewModel(application) {
    private val category = Category.LATEST

    private val repository = GifRepository(GifDatabase.getDatabase(application))

    private var _gif: MutableLiveData<Gif?> = MutableLiveData(null)
    val gif: LiveData<Gif?>
        get() = _gif

    private lateinit var currentPos: LiveData<Long>

    private var _hasPrevious = MutableLiveData(false)
    val hasPrevious: LiveData<Boolean>
        get() = _hasPrevious

    private var _status : MutableLiveData<Status> = MutableLiveData(Status.Download)
    val status: LiveData<Status>
        get() = _status

    init {
        getCurrentPos()
        currentPos.observeForever {
            getCurrentGif()
            checkPrevious()
        }
    }

    fun onNext() {
        viewModelScope.launch {
            var pos = currentPos.value
            pos?.let {
                (++pos)?.let { it1 -> repository.setCurruntPosition(category, it1) }
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
        _status.value = Status.GLideError
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

    fun getCurrentGif() {
        val pos = currentPos.value
        pos?.let {
            viewModelScope.launch {
                delay(3000)
                try {
                    _gif.value = repository.getGif(category, it)
                    _status.value = Status.Success
                } catch (e : Exception ) {
                    _status.value = Status.RetrofitError
                }
            }
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LatestGifViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LatestGifViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    enum class Status {
        Success,Download,GLideError,RetrofitError
    }
}*/

class LatestGifViewModel(repository : GifRepository) : GifViewModel(repository) {
    override val category: Category = Category.LATEST
}