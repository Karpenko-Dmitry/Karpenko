package dmitry.karpenko.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dmitry.karpenko.data.database.entity.Category
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object DevelopersLiveServiceGenerator {

    private val DEVELOPERS_BASE_URL = "https://developerslife.ru/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(DEVELOPERS_BASE_URL)
        .build()

    private fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    fun randomGifClient() : RandomGifClientWrap {
        return RandomGifClientWrap(createService(RandomGifClient::class.java))
    }

    fun categoryGifClient(category : Category) : CategoryGifClientWrap {
        return CategoryGifClientWrap(createService(CategoryGifClient::class.java),category.value)
    }
}

class RandomGifClientWrap(private val client : RandomGifClient) {

    suspend fun getRandomGif() : SafeResponse<NetworkGif> {
        return try {
            SafeResponse.success(client.getRandomGif())
        } catch (e : Exception) {
            SafeResponse.failure(e)
        }
    }
}

class CategoryGifClientWrap(
    private val client : CategoryGifClient,
    private val category : String
) {

    private val PAGE_SIZE = 50

    suspend fun getListGif(pos : Long) : SafeResponse<NetworkListGif> {
        return try {
            SafeResponse.success(client.getGifList(
                category = category,
                page = pos / PAGE_SIZE,
                pageSize = PAGE_SIZE
            ))
        } catch (e : Exception) {
            SafeResponse.failure(e)
        }
    }
}