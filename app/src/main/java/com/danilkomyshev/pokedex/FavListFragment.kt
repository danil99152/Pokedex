package com.danilkomyshev.pokedex

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.danilkomyshev.pokedex.adapters.PokeAdapter
import com.danilkomyshev.pokedex.adapters.PokeListClickListener
import com.danilkomyshev.pokedex.database.PokemonRepository
import com.danilkomyshev.pokedex.databinding.FragmentFavListBinding
import com.danilkomyshev.pokedex.entity.PokeListEntry
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FavListFragment : Fragment(), PokeListClickListener {
    companion object {

        const val TAG = "FavListFragment"
    }
    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private lateinit var binding: FragmentFavListBinding
    private lateinit var listAdapter: PokeAdapter
    private lateinit var alertDialog: AlertDialog
    private lateinit var searchView: SearchView
    private val disposables = CompositeDisposable()

    init {
        PokeApp.getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        listAdapter = PokeAdapter(this)
        binding = FragmentFavListBinding.inflate(inflater, container, false)
        binding.favList.adapter = listAdapter

        firstEntryLoad()
        return binding.root
    }

    private fun firstEntryLoad() {
        Log.i(TAG, "first entry load")
        startLoading()

        disposables.add(
            pokemonRepository.getFavPokemons(true)
                .applySchedulers()
                .subscribe({
                    Log.i(TAG, "on load finish. Items loaded: ${it.size}")
                    searchFieldEnabled(true)
                    setEntries(it)
                },
                    {
                        Log.e(TAG, it.toString())
                        showError(it.toString())
                    })
        )
    }

    private fun showError(error: String) {
        finishLoading(false)

        alertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.pokedex))
            .setMessage(error)
            .setOnCancelListener { cancelError() }
            .show()
    }

    private fun startLoading() {
        Log.i(TAG, "on start loading")
        binding.favList.visibility = View.GONE
        binding.probarFavList.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
    }

    private fun searchFieldEnabled(enabled: Boolean) {
        searchView.inputType = when(enabled){
            true -> InputType.TYPE_CLASS_TEXT
            false -> InputType.TYPE_NULL
        }
    }

    private fun setEntries(entries: List<PokeListEntry>) {
        listAdapter.submitList(entries)
        finishLoading(entries.isEmpty())
    }


    override fun onClick(entry: Int) {
        Log.i(TAG, "clicked on pokemon: $entry")
        this.findNavController().navigate(
            FavListFragmentDirections.actionFavListFragmentToPokeDetailsFragment(entry)
        )
    }

    private fun cancelError() {
        if (::alertDialog.isInitialized && alertDialog.isShowing) {
            alertDialog.hide()
        }
    }

    private fun finishLoading(emptyList: Boolean) {
        Log.i(TAG, "on finish loading")
        binding.favList.visibility = View.VISIBLE
        binding.probarFavList.visibility = View.GONE
        if (emptyList) {
            binding.emptyView.visibility = View.VISIBLE
        }
    }
}