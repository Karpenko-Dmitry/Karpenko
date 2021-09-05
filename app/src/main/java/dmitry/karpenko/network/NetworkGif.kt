package dmitry.karpenko.network

import dmitry.karpenko.data.database.entity.Gif

//Сущность Гиф-изображения, получаемая при парсинге JSON
data class NetworkGif(
    val description : String,
    val gifURL : String
)

//Конвертация сущности гиф-изображения в сущность пригодную для загрузки в БД
fun NetworkGif.toGif(newPos : Long,category : String) = Gif(
    id = newPos,
    category = category,
    description = this.description,
    gifURL = this.gifURL
)

//Конвертация массива сущностей гиф-изображений в масиив сущностей пригодных для загрузки в БД
fun List<NetworkGif>.toGifList(newPos : Long,category : String) : List<Gif> {
    val newList = ArrayList<Gif>(this.size)
    var pos = newPos
    this.forEach {
        newList.add(it.toGif(pos,category))
        pos++
    }
    return newList
}