package dmitry.karpenko.network

import retrofit2.Response
import java.lang.Exception

/* Обертка над Retrofit Response.
    Если данные были получены, то имеет статус Success.
    Иначе - Failure
 */
data class SafeResponse<T> (
    val status : Status,
    val data : Response<T>?,
    val exception: Exception?
    ) {

    companion object {
        fun <T> success(data: Response<T>) : SafeResponse<T> {
            return SafeResponse(
                status = Status.Success,
                data = data,
                exception = null
            )
        }

        fun <T> failure(exception: Exception) : SafeResponse<T> {
            return SafeResponse(
                status = Status.Failure,
                data = null,
                exception = exception
            )
        }
    }

    sealed class Status {
        object Success : Status()
        object Failure : Status()
    }

    val failure : Boolean
        get() = this.status == Status.Failure

    val isSuccessful : Boolean
        get() = !failure && this.data?.isSuccessful == true

    val body : T
        get() = this.data!!.body()!!
}