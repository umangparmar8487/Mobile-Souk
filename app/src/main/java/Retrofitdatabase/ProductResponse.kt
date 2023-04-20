package Retrofitdatabase

data class ProductResponse(
    val `data`: List<DataX>?,
    val msg: String?,
    val staus: Int?,
    val totalProduct: Int?
)