package br.infnet.marianabs.layouts.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LifecycleOwner
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.EMAIL
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.NAME
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.PREFERENCE
import br.infnet.marianabs.layouts.data.VolumeInfo
import br.infnet.marianabs.layouts.model.Favorite
import br.infnet.marianabs.layouts.model.Users
import br.infnet.marianabs.layouts.network.Repository
import kotlinx.coroutines.launch

class ViewModelPattern(context: Application) : AndroidViewModel(context) {
    private val repository = Repository()

    val preferred = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)


    fun getBookList(searchKeyWord: String? = null): LiveData<List<VolumeInfo>> {
        val books = MutableLiveData<List<VolumeInfo>>()
        viewModelScope.launch {
            ->
            try {
                when {
                    searchKeyWord.isNullOrEmpty() -> {
                        books.postValue(repository.getBooksList())
                    }
                    else -> {
                        books.postValue(repository.findBooks(searchKeyWord))
                    }
                }
            } catch (e: Throwable) {
                Log.e("Pesquisando..", "falha no download ${e.localizedMessage}")
            }
        }
        return books
    }

    fun userData(viewLifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            this@ViewModelPattern.repository.retrieveUserData().observe(viewLifecycleOwner) {
                this@ViewModelPattern.preferred.edit().putString(NAME , it.username).apply()
                this@ViewModelPattern.preferred.edit().putString(EMAIL , it.email).apply()
            }
        }
    }

    fun favoriteList(viewLifecycleOwner: LifecycleOwner): MutableLiveData<MutableList<Favorite>> {
        val books: MutableLiveData<MutableList<Favorite>> = MutableLiveData<MutableList<Favorite>>()
        this.viewModelScope.launch {
            this@ViewModelPattern.repository.getFavoriteBook().observe(viewLifecycleOwner) {
                books.value = it
            }
        }
        return books
    }



    fun saveDataBooker(user: Users) {
        this.viewModelScope.launch {
            repository.saveClientData(user)
        }
    }

    fun saveBookFavorite(favBook: Favorite) {
        viewModelScope.launch {
            repository.saveFavoriteBooks(favBook)
        }
    }

    fun delBooksBulb(favBookId: String) {
        viewModelScope.launch {
            repository.excludeFavBooks(favBookId)
        }
    }

    fun refreshBookerName(userName: String) {
        viewModelScope.launch {
            repository.updateUserName(userName)
            repository.retrieveUserData()
        }
    }
}
