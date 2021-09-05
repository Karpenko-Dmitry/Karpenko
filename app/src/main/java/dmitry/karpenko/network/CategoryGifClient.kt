package dmitry.karpenko.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryGifClient {

    //Запрос страницы данных гиф-изображений. Запрос из категорий Top,Latest,Hot.
    @GET("{category}/{page}")
    suspend fun getGifList(
        @Path("category") category : String,
        @Path("page") page : Long,
        @Query("json") json: Boolean = true,
        @Query("pageSize") pageSize : Int
    ) : Response<NetworkListGif>
}
