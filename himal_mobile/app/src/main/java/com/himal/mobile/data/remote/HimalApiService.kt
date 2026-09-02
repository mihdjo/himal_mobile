package com.himal.mobile.data.remote

import com.himal.mobile.data.remote.dto.EkspedicijaResponse
import com.himal.mobile.data.remote.dto.LoginRequest
import com.himal.mobile.data.remote.dto.LoginResponse
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.MojPlanResponse
import com.himal.mobile.data.remote.dto.AgregiranaOpremaResponse
import com.himal.mobile.data.remote.dto.GrupisanaOpremaResponse
import com.himal.mobile.data.remote.dto.UpdateMojPlanStatusRequest
import com.himal.mobile.data.remote.dto.KorisnikResponse
import com.himal.mobile.data.remote.dto.UpdateKorisnikRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface HimalApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/expeditions")
    suspend fun getExpeditions(
        @Header("Authorization") authorization: String
    ): Response<List<EkspedicijaResponse>>

    @GET("api/expeditions/{id}")
    suspend fun getExpeditionById(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String
    ): Response<EkspedicijaResponse>

    @GET("api/expeditions/{id}/equipment")
    suspend fun getExpeditionEquipment(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String
    ): Response<List<EkspedicijaOpremaResponse>>

    @GET("api/users/me/saved-expeditions")
    suspend fun getSavedExpeditions(
        @Header("Authorization") authorization: String
    ): Response<List<EkspedicijaResponse>>

    @POST("api/expeditions/{id}/save")
    suspend fun saveExpedition(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String
    ): Response<Unit>

    @DELETE("api/expeditions/{id}/save")
    suspend fun removeSavedExpedition(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String
    ): Response<Unit>


    @GET("api/users/me/plan")
    suspend fun getMyPlan(
        @Header("Authorization") authorization: String
    ): Response<List<MojPlanResponse>>

    @POST("api/users/me/plan/{id}")
    suspend fun addToPlan(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String
    ): Response<Unit>

    @DELETE("api/users/me/plan/{id}")
    suspend fun removeFromPlan(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String
    ): Response<Unit>

    @GET("api/users/me/plan/equipment")
    suspend fun getAggregatedPlanEquipment(
        @Header("Authorization") authorization: String
    ): Response<List<AgregiranaOpremaResponse>>

    @GET("api/users/me/plan/equipment/grouped")
    suspend fun getGroupedPlanEquipment(
        @Header("Authorization") authorization: String
    ): Response<List<GrupisanaOpremaResponse>>

    @PUT("api/users/me/plan/{id}/status")
    suspend fun updatePlanStatus(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String,
        @Body request: UpdateMojPlanStatusRequest
    ): Response<MojPlanResponse>

    @GET("api/users/me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorization: String
    ): Response<KorisnikResponse>

    @PUT("api/users/me")
    suspend fun updateCurrentUser(
        @Header("Authorization") authorization: String,
        @Body request: UpdateKorisnikRequest
    ): Response<KorisnikResponse>
}