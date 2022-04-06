package br.infnet.marianabs.layouts.ui.book

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.infnet.marianabs.layouts.R
import br.infnet.marianabs.layouts.ui.viewmodel.ViewModelPattern
import br.infnet.marianabs.layouts.adapter.FavoriteAdapter

class Favorite : Fragment() {
    private lateinit var booksRVFav: RecyclerView
    private lateinit var favAdapter: FavoriteAdapter
    private lateinit var favIcon: ImageView
    private lateinit var favText: TextView

    private val viewModelStyle by lazy {
        ViewModelProvider(this)[ViewModelPattern::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksRVFav = view.findViewById(R.id.rvFavBooks)
        favIcon = view.findViewById(R.id.empty_fav_list)
        favText = view.findViewById(R.id.empty_text_list)
        booksRVFav.layoutManager = GridLayoutManager(context, 2)
        favIcon.isVisible = false
        favText.isVisible = false
        loadBooks()

    }


    private fun loadBooks() {

        val getBulbAnimations = AnimationUtils.loadAnimation(context, R.anim.scale_up)

        viewModelStyle.favoriteList(viewLifecycleOwner).observe(viewLifecycleOwner) {
            if (! it.isNullOrEmpty()) {
                favIcon.visibility = View.GONE
                favText.visibility = View.GONE
            } else {
                favIcon.visibility = View.VISIBLE
                favText.visibility = View.VISIBLE
                booksRVFav.visibility = View.GONE
            }
            favAdapter = FavoriteAdapter(it , viewModelStyle , getBulbAnimations , favIcon , favText)
            booksRVFav.adapter = favAdapter

        }
    }
}
