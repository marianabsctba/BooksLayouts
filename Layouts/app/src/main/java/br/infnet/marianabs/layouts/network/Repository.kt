package br.infnet.marianabs.layouts.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.infnet.marianabs.layouts.data.VolumeInfo
import br.infnet.marianabs.layouts.model.Favorite
import br.infnet.marianabs.layouts.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {

    private var db = FirebaseFirestore.getInstance()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    private val apis = BookHelper.I_BOOK_API

    suspend fun getBooksList(): List<VolumeInfo>
    = withContext(Dispatchers.IO) {
        apis.listingBooks().items
    }

    suspend fun findBooks(searchKeyWord: String): List<VolumeInfo>
    = withContext(Dispatchers.IO) {
        apis.searchBook(searchKeyWord).items
    }


    suspend fun getFavoriteBook(): MutableLiveData<MutableList<Favorite>> {

        var favList: MutableLiveData<MutableList<Favorite>> = MutableLiveData()
        withContext(Dispatchers.IO) {
            var favoriteListIn = mutableListOf<Favorite>()
            return@withContext withContext(Dispatchers.IO) {

                db.collection("Users").document("$currentUserId").collection("Favorite").get()
                    .addOnCompleteListener() {
                        it.addOnSuccessListener { snapshot ->
                            snapshot?.let { docSnap ->
                                var documents = docSnap.documents

                                documents.forEach {
                                    var FavObj = it.toObject(Favorite::class.java)
                                    FavObj?.let {
                                        //favList.add(it)
                                        Log.d("books favObj", FavObj.toString())
                                        favoriteListIn.add(FavObj)
                                        Log.d("books favoriteListIn", favoriteListIn.toString())
                                    }
                                }
                                favList.value = favoriteListIn
                                Log.d("books favList", favList.toString())
                            }
                        }
                    }
            }
        }
        return favList
    }


    suspend fun excludeFavBooks(favBookId: String) = withContext(Dispatchers.IO) {
        db.collection("Users").document("$currentUserId").collection("Favorite").document(favBookId)
            .delete()
            .addOnSuccessListener {
                Log.d("TAG", "")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "", e)
            }
    }

    suspend fun saveClientData(user: Users)
    = withContext(Dispatchers.IO) {
        db.collection("Users")
            .document("$currentUserId").set(user)
    }

    suspend fun saveFavoriteBooks(favBook: Favorite)
    = withContext(Dispatchers.IO) {
        db.collection("Users")
            .document("$currentUserId")
            .collection("Favorite")
            .document(favBook.id)
            .set(favBook)
            .addOnSuccessListener {
                Log.d("TAG", "")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "", e)
            }

    }



    suspend fun retrieveUserData(): MutableLiveData<Users> {
        val userName = MutableLiveData<Users>()
        withContext(Dispatchers.IO) {
            db.collection("Users").document("$currentUserId").get().addOnCompleteListener() {
                it.addOnSuccessListener { snapshot ->
                    snapshot?.let { docSnap ->
                        val user = docSnap.toObject(Users::class.java)
                        user?.let {
                            userName.postValue(user!!)
                        }

                    }
                }
            }
        }
        return userName
    }

    suspend fun updateUserName(Name: String) =
        withContext(Dispatchers.IO) {
            db.collection("Users").document("$currentUserId")
                .update("username", Name)

        }


}


