package Retrofitdatabase


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL="https://shopping-app-backend-t4ay.onrender.com/"

private val moshi= Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit= Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

public interface Api {
    @POST("/user/registerUser")
    fun registerUser(@Body userRegisterRequest: UserRegisterRequest): Call<UserRegisterResponse>

    @POST("/user/verifyOtpOnRegister")
    fun registerOtp(@Body userRegisterOtpRequest: UserRegisterOtpRequest):Call<UserRegisterResponse>

    @POST("/user/login")
    fun login(@Body loginRequest: UserLoginRequest):Call<UserRegisterResponse>

    @POST("/user/forgotPassword")
    fun forgotpassword(@Body forgotPasswordRequest: ForgotPasswordRequest):Call<UserRegisterResponse>

    @POST("/user/verifyOtpOnForgotPassword")
    fun forgotpasswordotp(@Body fogotPasswordOtpRequest: ForgotOtpRequest):Call<UserRegisterResponse>

    @POST("/user/resendOtp")
    fun resendOtp(@Body resendOtpRequest: ResendOtpRequest):Call<UserRegisterResponse>

//    @POST("/user/setFcmToken")
//   fun fcmtoken(@Body fcMtokenRequest: FCMtokenRequest):Call<UserRegisterResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/product/getAllProduct")
    fun productresponse(@Header("Authorization") jwttoken:String):Call<ProductResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/addToCart")
    fun addtocart(@Header("Authorization") jwttoken:String,@Body addToCartRequest: AddToCartRequest):Call<AddToCartResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/cart/getMyCart")
    fun getmycart(@Header("Authorization") jwttoken: String):Call<GetMyCartResponse>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/cart/removeProductFromCart")
    fun removetocart(@Header("Authorization") jwttoken: String,@Body removeProductFromCartRequest: RemoveProductFromCartRequest):Call<RemoveProductFromCartResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/watchList/addToWatchList")
    fun addtowatchlist(@Header("Authorization") jwttoken: String,@Body addToWatchListRequest: AddToWatchListRequest):Call<AddToWatchListResponse>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("/watchList/getWatchList")
    fun getwatchlist(@Header("Authorization") jwttoken: String):Call<GetWatchListResponse>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/watchList/removeFromWatchList")
    fun removefromwatchlist(@Header("Authorization") jwttoken: String,@Body removeFromWatchListRequest: RemoveFromWatchListRequest):Call<RemoveFromWatchListResponse>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/cart/increaseProductQuantity")
    fun increaseproductquantity(@Header("Authorization")jwttoken: String,@Body increaseProductQuantityRequest: IncreaseProductQuantityRequest):Call<IncreaseProductQuantityResponse>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/cart/decreaseProductQuantity")
    fun decreaseproductquantity(@Header("Authorization")jwttoken: String,@Body decreaseProductQuantityRequeast: DecreaseProductQuantityRequeast):Call<DecreaseProductQuantityResponses>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/order/placeOrder")
    fun placeorder(@Header("Authorization")jwttoken:String,@Body placeOrderRequest: PlaceOrderRequest):Call<PlaceOrderResponse>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("/order/getOrderHistory")
    fun getorderhistory(@Header("Authorization")jwttoken: String):Call<GetOrderHistoryResponse>

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/user/changePassword")
    fun changepassword(@Header("Authorization")jwttoken: String,@Body changepasswordRequest: changepasswordRequest):Call<changepasswordResponse>

}

object applicationApi{
    val retrofitService:Api by lazy { retrofit.create(Api::class.java) }
}