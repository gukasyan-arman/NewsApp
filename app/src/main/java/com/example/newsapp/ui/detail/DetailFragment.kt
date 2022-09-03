package com.example.newsapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.net.URL

@AndroidEntryPoint
class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    private val bundleArgs: DetailFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articleArg = bundleArgs.article

        articleArg.let {article ->
            article.urlToImage.let {
                Glide.with(this).load(article.urlToImage).into(binding.headerImage)
            }
            binding.headerImage.clipToOutline = true
            binding.articleDetailTitle.text = article.title
            binding.articleDetailDescriptionTitle.text = article.description

            binding.articleDetailButton.setOnClickListener {
                try {
                    Intent()
                        .setAction(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(takeIf { URLUtil.isValidUrl(article.url) }
                            ?.let {
                                article.url
                            } ?: "https://google.com"))
                            .let {
                                ContextCompat.startActivity(requireContext(), it, null)
                            }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "The device doesn't have any browser to view the document!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            binding.iconFavorite.setOnClickListener {
                viewModel.saveFavoriteArticle(article)

            }

        }
    }

}