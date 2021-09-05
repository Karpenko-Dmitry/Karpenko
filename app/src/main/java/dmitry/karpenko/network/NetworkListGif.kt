package dmitry.karpenko.network

//Сущность страницы Гиф-изображений, получаемой при парсинге JSON
data class NetworkListGif(
    val result : List<NetworkGif>,
    val totalCount : Int
)