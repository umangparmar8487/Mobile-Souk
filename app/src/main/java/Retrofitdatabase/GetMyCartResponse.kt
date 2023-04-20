package Retrofitdatabase

data class GetMyCartResponse(
    var cartTotal: Double?,
    val `data`: List<DataXXX>?,
    val msg: String?,
    val status: Int?
)