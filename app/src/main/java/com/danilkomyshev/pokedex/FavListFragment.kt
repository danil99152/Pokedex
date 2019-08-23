package com.danilkomyshev.pokedex

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.danilkomyshev.pokedex.adapters.FavAdapter
import com.danilkomyshev.pokedex.adapters.FavListClickListener
import com.danilkomyshev.pokedex.database.PokemonRepository
import com.danilkomyshev.pokedex.databinding.FragmentFavListBinding
import com.danilkomyshev.pokedex.entity.FavouriteList
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FavListFragment : Fragment(), FavListClickListener {
    companion object {

        const val TAG = "FavListFragment"
    }
    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private lateinit var binding: FragmentFavListBinding
    private lateinit var listAdapter: FavAdapter
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
        listAdapter = FavAdapter(this)
        binding = FragmentFavListBinding.inflate(inflater, container, false)
        binding.favList.adapter = listAdapter

        firstEntryLoad()
        return binding.root
    }

    private fun firstEntryLoad() {
        Log.i(TAG, "first entry load")
        startLoading()

        disposables.add(
            pokemonRepository.getFavPokemons()
                .applySchedulers()
                .subscribe({
                    Log.i(TAG, "on load finish. Items loaded: ${it.size}")
                    searchFieldEnabled(true)
                    setFavEntries(it)
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

    private fun setFavEntries(entries: List<FavouriteList>) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.list_of_pokemons -> toList()
            R.id.random_poke -> randomPoke()
        }
        return false
    }

    private fun toList() {
        this.findNavController().navigate(
            FavListFragmentDirections.actionFavListFragmentToPokeListFragment()
        )
    }

    private fun randomPoke(){
        disposables.add(
            pokemonRepository.getFavPokemons()
                .applySchedulers()
                .subscribe({
                val random = (1 .. it.size).random()
                Log.i("randomPoke", "randomed favourite pokemon: $random")
                onClick(random)
            }, {
                Log.e(PokeListFragment.TAG, it.toString())
                showError(it.toString())
            }))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fav_list_menu, menu)
        searchView = menu.findItem(R.id.search_bar)?.actionView as SearchView
        searchFieldEnabled(false)

        disposables.add(
            Flowable.create(FlowableOnSubscribe<String> { subscriber ->
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        subscriber.onNext(query!!)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        subscriber.onNext(newText!!)
                        return false
                    }
                })
            }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .map { text -> text.trim() }
                .debounce(200, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    queryEntries(it)
                }, {
                    Log.e(PokeListFragment.TAG, it.toString())
                })
        )
    }

    private fun queryEntries(query: String) {
        Log.i(TAG, "loading entries for $query ...")
        startLoading()

        disposables.add(
            pokemonRepository.getFavEntries(query)
                .applySchedulers()
                .subscribe(
                    {
                        Log.i(TAG, "on load finish. Favourite pokemons loaded: ${it.size}")
                        setFavEntries(it)

                    }, {
                        Log.e(TAG, it.toString())
                        showError(it.toString())
                    }
                )
        )
    }
}