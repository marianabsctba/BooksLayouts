package br.infnet.marianabs.layouts.ui.book

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import br.infnet.marianabs.layouts.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FavoriteDetailsFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private lateinit var titleTV: TextView
    private lateinit var title2TV: TextView
    private lateinit var descriptionTV: TextView
    private lateinit var authorsTV: TextView
    private lateinit var authorsTV0: TextView
    private lateinit var categoryTV: TextView
    private lateinit var categoryTV0: TextView
    private lateinit var PageCountTV: TextView
    private lateinit var PageCountTV0: TextView
    private lateinit var PublishDateTV: TextView
    private lateinit var PublishDateTV0: TextView
    private lateinit var previewBtn: Button
    private lateinit var bookImgV: ImageView
    private lateinit var previewUrl: String

    private val args: FavoriteDetailsFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bookImgV = view.findViewById(R.id.ivbookD)
        previewBtn = view.findViewById(R.id.btnPreviewD)
        descriptionTV = view.findViewById(R.id.tvDescriptionD)
        titleTV = view.findViewById(R.id.tvTitle1D)
        title2TV = view.findViewById(R.id.tvTitle2D)
        authorsTV = view.findViewById(R.id.tvAuthorsD)
        authorsTV = view.findViewById(R.id.tvAuthorsD0)
        categoryTV = view.findViewById(R.id.tvCategoriesD)
        categoryTV0 = view.findViewById(R.id.tvCategoriesD0)
        PageCountTV = view.findViewById(R.id.tvPageCountD)
        PageCountTV0 = view.findViewById(R.id.tvPageCountD0)
        PublishDateTV = view.findViewById(R.id.tvPublishDateD)
        PublishDateTV0 = view.findViewById(R.id.tvPublishDateD0)

        Glide.with(view)
            .load(args.favoriteBooksKey.imageLinks)
            .placeholder(R.drawable.imagenotfound2)
            .error(R.drawable.placeholder)
            .into(bookImgV)


        titleTV.text = args.favoriteBooksKey.title
        title2TV.text = args.favoriteBooksKey.title

        if (args.favoriteBooksKey.pageCount != null) {
            PageCountTV.text = args.favoriteBooksKey.pageCount.toString()
        } else {
            PageCountTV.isVisible = false
            PageCountTV0.isVisible = false
        }
        if (args.favoriteBooksKey.publishedDate != null) {
            PublishDateTV.text = args.favoriteBooksKey.publishedDate.toString()
        } else {
            PublishDateTV.isVisible = false
            PublishDateTV0.isVisible = false
        }

        if (args.favoriteBooksKey.categories != null) {
            var categoryPrint = ""
            args.favoriteBooksKey.categories?.let {
                for (i in it) {
                    categoryPrint += if (i == it.last())
                        "$i ."
                    else
                        "$i , "
                }
            }
            categoryTV.text = categoryPrint

        } else {
            categoryTV.isVisible = false
            categoryTV0.isVisible = false

        }

        if (args.favoriteBooksKey.authors != null) {
            var authorsPrint = ""
            args.favoriteBooksKey.authors?.let {
                for (i in it) {
                    authorsPrint += if (i == it.last())
                        "$i ."
                    else
                        "$i , "
                }
            }
            authorsTV.text = authorsPrint
        } else {
            authorsTV.isVisible = false
            authorsTV0.isVisible = false

        }
        if (args.favoriteBooksKey.description != null) {
            descriptionTV.text = args.favoriteBooksKey.description.toString()
        } else {
            descriptionTV.text = getString(R.string.no_description)
        }
        args.favoriteBooksKey.previewLink.toString().also { previewUrl = it }

        this.previewBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, previewUrl.toUri())
            startActivity(intent)
        }

    }

}