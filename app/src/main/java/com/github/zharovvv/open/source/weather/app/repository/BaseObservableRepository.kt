package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.database.entity.PerishableEntity
import com.github.zharovvv.open.source.weather.app.model.DataState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call

abstract class BaseObservableRepository<Response, Entity : PerishableEntity, Model>(
    observableDataFromDatabase: Observable<Entity>,
    private val converter: BaseConverter<Response, Entity, Model>
) {

    private val behaviorSubject: BehaviorSubject<DataState<Model>> =
        BehaviorSubject.createDefault(DataState.Loading())

    init {
        @Suppress("UNUSED_VARIABLE") val connection = observableDataFromDatabase
            .filter { entity -> entity.isFresh }
            .map { entity -> converter.convertToModel(entity) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { behaviorSubject.onNext(DataState.Success(it)) },
                {
                    it.printStackTrace()
                    behaviorSubject.onNext(DataState.Error(it.message ?: ""))
                }
            )
    }

    fun observableData(): Observable<DataState<Model>> {
        return behaviorSubject
    }

    fun requestData(lat: Float, lon: Float, withLoadingStatus: Boolean = true) {
        val previousValue = behaviorSubject.value
        if (withLoadingStatus) {
            behaviorSubject.onNext(DataState.Loading())
        }
        val lastKnownEntity: Entity? = getLastKnownDataFromDatabase()
        if (shouldFetchData(lastKnownEntity, lat, lon)) {
            try {
                fetchData(lastKnownEntity, lat, lon)
            } catch (e: Exception) {
                e.printStackTrace()
                behaviorSubject.onNext(DataState.Error(e.message ?: ""))
                return
            }
        } else {
            if (withLoadingStatus) {
                behaviorSubject.onNext(previousValue!!)
            }
        }
    }

    protected abstract fun getLastKnownDataFromDatabase(): Entity?

    protected abstract fun shouldFetchData(
        lastKnownEntity: Entity?,
        newLat: Float,
        newLon: Float
    ): Boolean

    private fun fetchData(lastKnownEntity: Entity?, lat: Float, lon: Float) {
        //Thread.sleep(3000L) //test loading
        val retrofitResponse: retrofit2.Response<Response> = callApiService(lat, lon).execute()
        if (retrofitResponse.isSuccessful) {
            val response = retrofitResponse.body()!!
            val newEntity = converter.convertToEntity(
                entityId = lastKnownEntity?.id ?: 0,
                latitude = lat,
                longitude = lon,
                response = response
            )
            if (lastKnownEntity == null) {
                insertDataToDatabase(newEntity)
            } else {
                updateDataInDatabase(newEntity)
            }
        } else {
            behaviorSubject.onNext(DataState.Error(retrofitResponse.message()))
        }
    }

    protected abstract fun callApiService(lat: Float, lon: Float): Call<Response>

    protected abstract fun insertDataToDatabase(newEntity: Entity)

    protected abstract fun updateDataInDatabase(entity: Entity)
}