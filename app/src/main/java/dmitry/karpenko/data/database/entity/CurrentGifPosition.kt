package dmitry.karpenko.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*сущность для хранения последней позиции для каждой категории
    id - уникальный идентификатор категории. Связан с id enum Category
    curPosition - последняя просмотренная позиция пользователем в последовательности
     хранимых для каждой категории гиф-файлов(сущность Gif)
 */
@Entity
data class CurrentUserGifPosition(
    @PrimaryKey
    val id : Int,
    val curPosition : Long
)

enum class Category( val id: Int,val value: String) {
    LATEST(0,"latest"),
    TOP(1,"top"),
    HOT(2,"hot"),
    RANDOM(3,"random")
}