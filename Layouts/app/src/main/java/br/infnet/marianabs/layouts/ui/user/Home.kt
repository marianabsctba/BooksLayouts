package br.infnet.marianabs.layouts.ui.user

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.infnet.marianabs.layouts.R
import br.infnet.marianabs.layouts.ui.viewmodel.ViewModelPattern
import br.infnet.marianabs.layouts.adapter.BookAdapter

class Home : Fragment() {

    private lateinit var booksRV: RecyclerView
    private lateinit var searchView: SearchView

    private val viewModels by lazy {
        ViewModelProvider(this)[ViewModelPattern::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModels.userData(viewLifecycleOwner)

        this.searchView = view.findViewById(R.id.search)
        this.booksRV = view.findViewById(R.id.rvBooks)
        this.booksRV.layoutManager =
            GridLayoutManager(context, 2)
        downloadingBooks()


        this.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(tag, "$query")
                downloadingBooks(query?.trim())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(tag, "$newText")
                return false
            }
        })
    }

    private fun downloadingBooks(query: String? = null) {
        val upBooks = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        viewModels.getBookList(query).observe(viewLifecycleOwner) {
            when {
                query.isNullOrEmpty() -> {

                    booksRV.adapter = BookAdapter(it , viewModels , upBooks)
                }
                else -> {
                    booksRV.scrollToPosition(0)
                    booksRV.swapAdapter(BookAdapter(it , viewModels , upBooks)
                        , false)
                }
            }
            Log.d("" , it.toString())
        }
    }
}

