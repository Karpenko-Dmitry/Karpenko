package dmitry.karpenko.network

import dmitry.karpenko.data.database.entity.Gif
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomGifClient {

    //Запрос случайного гиф-изображения.
    @GET("random")
    suspend fun getRandomGif(
        @Query("json") json: Boolean = true
    ) : Response<NetworkGif>
}