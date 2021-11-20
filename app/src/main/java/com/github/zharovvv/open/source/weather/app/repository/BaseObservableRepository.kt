package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.database.entity.PerishableEntity
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.LocationModel
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
                    behaviorSubject.onNext(
                        DataState.Error.buildUnexpectedError(
                            errorMessage = it.message ?: ""
                        )
                    )
                }
            )
    }

    /**
     * Обозреваемые данные репозитория.
     */
    fun observableData(): Observable<DataState<Model>> {
        return behaviorSubject
    }

    /**
     * Запрос данных из репозитория.
     * Данные придут в [observableData].
     */
    fun requestData(locationModel: LocationModel, withLoadingStatus: Boolean = true) {
        val previousValue = behaviorSubject.value
        if (withLoadingStatus) {
            behaviorSubject.onNext(DataState.Loading())
        }
        val lastKnownEntity: Entity? = getLastKnownDataFromDatabase()
        if (shouldFetchData(lastKnownEntity, locationModel)) {
            try {
                fetchData(lastKnownEntity, locationModel)
            } catch (e: Exception) {
                e.printStackTrace()
                behaviorSubject.onNext(
                    DataState.Error.buildNetworkError(errorMessage = e.message ?: "")
                )
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
        newLocationModel: LocationModel
    ): Boolean

    private fun fetchData(lastKnownEntity: Entity?, locationModel: LocationModel) {
        //Thread.sleep(3000L) //test loading
        val retrofitResponse: retrofit2.Response<Response> =
            callApiService(lat = locationModel.latitude, lon = locationModel.longitude).execute()
        if (retrofitResponse.isSuccessful) {
            val response = retrofitResponse.body()!!
            val newEntity = converter.convertToEntity(
                entityId = lastKnownEntity?.id ?: 0,
                locationModel = locationModel,
                response = response
            )
            if (lastKnownEntity == null) {
                insertDataToDatabase(newEntity)
            } else {
                updateDataInDatabase(newEntity)
            }
        } else {
            behaviorSubject.onNext(
                DataState.Error.buildNetworkError(errorMessage = retrofitResponse.message())
            )
        }
    }

    protected abstract fun callApiService(lat: Float, lon: Float): Call<Response>

    protected abstract fun insertDataToDatabase(newEntity: Entity)

    protected abstract fun updateDataInDatabase(entity: Entity)

    /**
     * Блокирующий запрос данных из репозитория.
     * Возвращает объект [DataState].
     * Если данные будут получены с сервера, то полученный результат сохранится в БД.
     */
    fun requestDataSync(locationModel: LocationModel): DataState<Model> {
        val result: DataState<Model>
        val lastKnownEntity: Entity? = getLastKnownDataFromDatabase()
        result = if (shouldFetchData(lastKnownEntity, locationModel)) {
            try {
                fetchDataSync(lastKnownEntity, locationModel)
            } catch (e: Exception) {
                e.printStackTrace()
                DataState.Error.buildNetworkError(errorMessage = e.message ?: "")
            }
        } else {
            DataState.Success(converter.convertToModel(lastKnownEntity!!))
        }
        return result
    }

    private fun fetchDataSync(
        lastKnownEntity: Entity?,
        locationModel: LocationModel
    ): DataState<Model> {
        val result: DataState<Model>
        val retrofitResponse: retrofit2.Response<Response> =
            callApiService(lat = locationModel.latitude, lon = locationModel.longitude).execute()
        result = if (retrofitResponse.isSuccessful) {
            val response = retrofitResponse.body()!!
            val newEntity = converter.convertToEntity(
                entityId = lastKnownEntity?.id ?: 0,
                locationModel = locationModel,
                response = response
            )
            if (lastKnownEntity == null) {
                insertDataToDatabase(newEntity)
            } else {
                updateDataInDatabase(newEntity)
            }
            DataState.Success(converter.convertToModel(newEntity))
        } else {
            DataState.Error.buildNetworkError(errorMessage = retrofitResponse.message())
        }
        return result
    }
}