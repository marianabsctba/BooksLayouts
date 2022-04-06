package br.infnet.marianabs.layouts.network


import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.API_KEY
import br.infnet.marianabs.layouts.data.BooksData
import retrofit2.http.GET
import retrofit2.http.Query

interface IBookAPI {

    @GET("?q=ebook&key=$API_KEY")
    suspend fun listingBooks(): BooksData

    @GET("?printType=books&$API_KEY")
    suspend fun searchBook(@Query("q") searchKey: String): BooksData

}
