package com.danilkomyshev.pokedex

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.danilkomyshev.pokedex.database.PokemonRepository
import com.danilkomyshev.pokedex.databinding.FragmentPokeDetailsBinding
import com.danilkomyshev.pokedex.entity.Pokemon
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PokeDetailsFragment : Fragment() {

    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private val disposables = CompositeDisposable()

    init {
        PokeApp.getAppComponent().inject(this)
    }

    companion object {
        const val TAG = "PokeDetailsFragment"
    }

    private lateinit var binding: FragmentPokeDetailsBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokeDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val entry = PokeDetailsFragmentArgs.fromBundle(arguments!!).pokemonEntry
        Log.i(TAG, "entry received: $entry")
        loadPokemon(entry)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    private fun loadPokemon(entry: Int) {
        Log.i(TAG, "loading a pokemon with entry: $entry ...")
        disposables.add(pokemonRepository.getPokemon(entry)
            .applySchedulers()
            .subscribe ({ pokemon ->
                Log.i(TAG, "a pokemon loaded: $pokemon")
                bindPokemon(pokemon)
                if (pokemon.isFavourite == 1) {
                    binding.fabFavOff.visibility = View.GONE
                    binding.fabFavOn.visibility = View.VISIBLE
                    binding.fabFavOn.setOnClickListener {
                        binding.fabFavOn.hide()
                        binding.fabFavOff.show()
                        pokemonRepository.toUsual(entry)
                        Log.i(TAG, "Pokemon $entry deleted from favourites now")
                    }
                }
                else {
                    binding.fabFavOn.visibility = View.GONE
                    binding.fabFavOff.visibility = View.VISIBLE
                    binding.fabFavOff.setOnClickListener {
                        binding.fabFavOff.hide()
                        binding.fabFavOn.show()
                        pokemonRepository.toFavourite(entry)
                        Log.i(TAG,"Pokemon $entry is favourite now")
                    }
                }
            },{
                displayError(it)
            })
        )
    }

    private fun displayError(throwable: Throwable) {
        alertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.pokedex))
            .setMessage(throwable.toString())
            .setOnCancelListener {cancelError()}
            .show()
    }

    private fun cancelError() {
        if(::alertDialog.isInitialized && alertDialog.isShowing){
            alertDialog.hide()
        }
    }

    private fun bindPokemon(pokemon: Pokemon) {
        Log.i(TAG, "Binded pokemon: $pokemon")
        binding.pokemon = pokemon
    }
}
