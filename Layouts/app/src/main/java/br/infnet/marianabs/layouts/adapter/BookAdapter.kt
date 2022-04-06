package br.infnet.marianabs.layouts.adapter

import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.infnet.marianabs.layouts.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import br.infnet.marianabs.layouts.ui.viewmodel.ViewModelPattern
import br.infnet.marianabs.layouts.data.VolumeInfo
import br.infnet.marianabs.layouts.model.Favorite
import br.infnet.marianabs.layouts.ui.user.HomeDirections
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookAdapter(

    private val booksData: List<VolumeInfo> ,
    val viewModelgrading: ViewModelPattern ,
    val getAnimations: Animation ,


    ) : RecyclerView.Adapter<CustomHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.gride_book_item, parent, false)
        return CustomHolder(view)
    }

    @DelicateCoroutinesApi
    override fun onBindViewHolder(holder: CustomHolder , position: Int) {
        val userIdentity = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseFirestore.getInstance()

        val book = booksData[position]

        holder.bookTitleTV.text = book.volumeInfo.title.toString()

        var bookPublisherDatePrint = ""

        book.volumeInfo.publishedDate
            .let {
            bookPublisherDatePrint = book
                .volumeInfo
                .publishedDate
        }
        holder.bookPublisherDateTV.text = bookPublisherDatePrint

        Glide.with(holder.itemView)
            .load(book.volumeInfo.imageLinks?.smallThumbnail)
            .placeholder(R.drawable.imagenotfound2)
            .error(R.drawable.placeholder)
            .into(holder.bookImageIV)

        database.collection("Users").document("$userIdentity").collection("Favorite").document(book.id).get()
            .addOnCompleteListener {
                when {
                    it.result?.exists()!! -> {
                        book.isFavBook=true
                        holder.likeIV.setImageResource(R.drawable.img_2)
                    }
                    else -> {
                        holder.likeIV.setImageResource(R.drawable.img)

                    }
                }
            }

        holder.likeIV.setOnClickListener {
            GlobalScope.launch {
                holder.likeIV.startAnimation(getAnimations)
                delay(500)
            }

            when {
                book.isFavBook -> {
                    holder.likeIV.setImageResource(R.drawable.img)
                    book.isFavBook = !book.isFavBook
                    viewModelgrading.delBooksBulb(book.id)

                }
                else -> {
                    holder.likeIV.setImageResource(R.drawable.img_2)
                    book.isFavBook = !book.isFavBook

                    val favInfo = Favorite(book.id, book.volumeInfo.title,book.volumeInfo.authors,
                        book.volumeInfo.publishedDate,book.volumeInfo.description,book.volumeInfo.pageCount,
                        book.volumeInfo.previewLink, book.volumeInfo.categories,
                        book.volumeInfo.imageLinks?.thumbnail.toString(),book.isFavBook

                    )

                    viewModelgrading.saveBookFavorite(favInfo)

                }
            }
        }

        holder.itemView.setOnClickListener { view ->
            val action =
                HomeDirections.actionBookFragmentToBookDetailsFragment(book.volumeInfo)
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return booksData.size
    }
}

class CustomHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val bookTitleTV: TextView = itemView.findViewById(R.id.tvBookTitle)
    val bookPublisherDateTV: TextView = itemView.findViewById(R.id.tvPublishDate)
    val bookImageIV: ImageView = itemView.findViewById(R.id.ivbook)
    val likeIV: ImageView = itemView.findViewById(R.id.ivLike)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
    }

}

