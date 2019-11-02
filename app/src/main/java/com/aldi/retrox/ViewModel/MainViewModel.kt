package com.aldi.retrox.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aldi.retrox.Interface.JokeApi
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel(){

    private val jokeApiService by lazy{
        JokeApi.createService()
    }

    private val singleRandomJoke by lazy{
        jokeApiService.randomJoke().map{it.joke}
    }

    enum class LoadingStatus {LOADING, NOT_LOADING}

    //Single
    private val _joke = MutableLiveData<String>()
    val joke: LiveData<String> = _joke
    private val _jokeLoadingStatus = MutableLiveData<LoadingStatus>()
    val jokeLoadingStatus: LiveData<LoadingStatus> = _jokeLoadingStatus

    //Observable
    private val _jokes = MutableLiveData<Collection<String>>()
    val jokes: LiveData<Collection<String>> = _jokes
    private val _jokesLoadingStatus = MutableLiveData<LoadingStatus>()
    val jokesLoadingStatus: LiveData<LoadingStatus> = _jokesLoadingStatus

    //Completable
    enum class InstallationStatus { SUCCESS, ERROR }
    private val _installation = MutableLiveData<InstallationStatus>()
    val installation: LiveData<InstallationStatus> = _installation
    private val _installLoadingStatus = MutableLiveData<LoadingStatus>()
    val installLoadingStatus: LiveData<LoadingStatus> = _installLoadingStatus

    init{
        _jokes.postValue(emptyList())
    }

    fun onJokeRequest() = singleRandomJoke
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _jokeLoadingStatus.postValue(LoadingStatus.LOADING) }
        .doFinally { _jokeLoadingStatus.postValue(LoadingStatus.NOT_LOADING) }
        .subscribeBy(
            onSuccess = {_joke.postValue(it.jokeText)},
            onError =  {_joke.postValue(it.message)}
        )

    fun onJokesRequest() = singleRandomJoke.toObservable()
        .repeat(10)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe{
            _jokes.postValue(emptyList())
            _jokesLoadingStatus.postValue(LoadingStatus.LOADING)
        }
        .doFinally{_jokesLoadingStatus.postValue(LoadingStatus.NOT_LOADING)}
        .subscribeBy(
            onNext = {_jokes.postValue(_jokes.value?.plus(it.jokeText))},
            onError = {
                _jokes.postValue(
                    it.message?.let{
                        _jokes.value?.plus(it)
                    } ?: emptyList()
                )
            },
            onComplete = {}
        )

    fun onInstall() = Completable.timer(3, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _installLoadingStatus.postValue(LoadingStatus.LOADING) }
        .doFinally{_installLoadingStatus.postValue(LoadingStatus.NOT_LOADING)}
        .subscribeBy (
            onComplete = {_installation.postValue(InstallationStatus.SUCCESS)},
            onError = {_installation.postValue(InstallationStatus.ERROR)}
        )
}