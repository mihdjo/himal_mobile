package com.himal.mobile.data.remote

import com.himal.mobile.data.remote.dto.AddEkspedicijaOpremaRequest
import com.himal.mobile.data.remote.dto.EkspedicijaResponse
import com.himal.mobile.data.remote.dto.LoginRequest
import com.himal.mobile.data.remote.dto.LoginResponse
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.MojPlanResponse
import com.himal.mobile.data.remote.dto.AgregiranaOpremaResponse
import com.himal.mobile.data.remote.dto.CreateOpremaRequest
import com.himal.mobile.data.remote.dto.GrupisanaOpremaResponse
import com.himal.mobile.data.remote.dto.UpdateMojPlanStatusRequest
import com.himal.mobile.data.remote.dto.KorisnikResponse
import com.himal.mobile.data.remote.dto.UpdateKorisnikRequest
import com.himal.mobile.data.remote.dto.RegisterRequest
import com.himal.mobile.data.remote.dto.RegisterResponse
import com.himal.mobile.data.remote.dto.EkspedicijaRequest
import com.himal.mobile.data.remote.dto.OpremaResponse
import com.himal.mobile.data.remote.dto.TipEkspedicijeResponse
import com.himal.mobile.data.remote.dto.UpdateEkspedicijaOpremaRequest
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

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("api/users/me/expeditions")
    suspend fun getMyExpeditions(
        @Header("Authorization") authorization: String
    ): Response<List<EkspedicijaResponse>>

    @POST("api/expeditions")
    suspend fun createExpedition(
        @Header("Authorization") authorization: String,
        @Body request: EkspedicijaRequest
    ): Response<EkspedicijaResponse>

    @PUT("api/expeditions/{id}")
    suspend fun updateExpedition(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String,
        @Body request: EkspedicijaRequest
    ): Response<EkspedicijaResponse>

    @DELETE("api/expeditions/{id}")
    suspend fun deleteExpedition(
        @Path("id") id: Long,
        @Header("Authorization") authorization: String
    ): Response<Unit>

    @GET("api/expedition-types")
    suspend fun getExpeditionTypes(
        @Header("Authorization") authorization: String
    ): Response<List<TipEkspedicijeResponse>>

    @GET("api/equipment")
    suspend fun getEquipmentCatalog(
        @Header("Authorization") authorization: String
    ): Response<List<OpremaResponse>>

    @POST("api/equipment")
    suspend fun createEquipment(
        @Header("Authorization") authorization: String,
        @Body request: CreateOpremaRequest
    ): Response<OpremaResponse>

    @POST("api/expeditions/{id}/equipment")
    suspend fun addExpeditionEquipment(
        @Path("id") expeditionId: Long,
        @Header("Authorization") authorization: String,
        @Body request: AddEkspedicijaOpremaRequest
    ): Response<EkspedicijaOpremaResponse>

    @PUT("api/expeditions/{id}/equipment/{equipmentId}")
    suspend fun updateExpeditionEquipment(
        @Path("id") expeditionId: Long,
        @Path("equipmentId") equipmentId: Long,
        @Header("Authorization") authorization: String,
        @Body request: UpdateEkspedicijaOpremaRequest
    ): Response<EkspedicijaOpremaResponse>

    @DELETE("api/expeditions/{id}/equipment/{equipmentId}")
    suspend fun deleteExpeditionEquipment(
        @Path("id") expeditionId: Long,
        @Path("equipmentId") equipmentId: Long,
        @Header("Authorization") authorization: String
    ): Response<Unit>
}