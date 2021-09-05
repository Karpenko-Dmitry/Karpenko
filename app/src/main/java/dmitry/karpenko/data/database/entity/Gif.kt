package dmitry.karpenko.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    Сущность описывающая гиф-файл. Является кешем ответа на запрос
    id - уникальный идентификатор. Определяет последовательность отображаемых пользователю гиф-изображений
    category - уникальный идентификатор категории из которой была получена гиф-изображение. Связана с полем "value"
            enum класса Category
    description - полученное из запроса описание гиф-изображения
    gifURL - полученный из запроса Url для загрузки гиф-изображения
 */
@Entity(primaryKeys = ["id","category"])
data class Gif(
    val id : Long,
    val category: String,
    val description : String,
    val gifURL : String
)
