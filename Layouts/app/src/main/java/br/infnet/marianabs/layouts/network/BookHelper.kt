package br.infnet.marianabs.layouts.network

import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BookHelper {

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val I_BOOK_API: IBookAPI = retrofit().create(IBookAPI::class.java)
}
