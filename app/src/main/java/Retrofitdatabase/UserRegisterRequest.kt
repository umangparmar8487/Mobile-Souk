package Retrofitdatabase

data class UserRegisterRequest(
    val emailId: String?,
    val mobileNo: String?,
    val name: String?,
    val password: String?
)