package com.danilkomyshev.pokedex

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.danilkomyshev.pokedex.adapters.PokeAdapter
import com.danilkomyshev.pokedex.adapters.PokeListClickListener
import com.danilkomyshev.pokedex.database.PokemonRepository
import com.danilkomyshev.pokedex.databinding.FragmentPokeListBinding
import com.danilkomyshev.pokedex.entity.PokeListEntry
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PokeListFragment : Fragment(), PokeListClickListener {
    companion object {

        const val TAG = "PokeListFragment"
    }

    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private lateinit var binding: FragmentPokeListBinding
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
        binding = FragmentPokeListBinding.inflate(inflater, container, false)
        binding.pokeList.adapter = listAdapter

        firstEntryLoad()
        return binding.root
    }

    private fun startLoading() {
        Log.i(TAG, "on start loading")
        binding.pokeList.visibility = View.GONE
        binding.probarPokeList.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
    }

    private fun finishLoading(emptyList: Boolean) {
        Log.i(TAG, "on finish loading")
        binding.pokeList.visibility = View.VISIBLE
        binding.probarPokeList.visibility = View.GONE
        if (emptyList) {
            binding.emptyView.visibility = View.VISIBLE
        }
    }

    private fun showError(error: String) {
        finishLoading(false)

        alertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.pokedex))
            .setMessage(error)
            .setOnCancelListener { cancelError() }
            .show()
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

    private fun cancelError() {
        if (::alertDialog.isInitialized && alertDialog.isShowing) {
            alertDialog.hide()
        }
    }

    override fun onClick(entry: Int) {
        Log.i(TAG, "clicked on pokemon: $entry")
        this.findNavController().navigate(
            PokeListFragmentDirections.actionPokeListFragmentToPokeDetailsFragment(entry)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.poke_list_menu, menu)
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
                    Log.e(TAG, it.toString())
                })
        )
    }

    private fun toFav() {
        this.findNavController().navigate(
            PokeListFragmentDirections.actionPokeListFragmentToFavListFragment()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.list_of_favourites -> toFav()
            R.id.random_poke -> randomPoke()
        }

        return false
    }

    private fun randomPoke(){
        disposables.add(
        pokemonRepository.initEntries().applySchedulers().subscribe({
            val random = (1 .. it.size).random()
            Log.i("randomPoke", "randomed pokemon: $random")
            onClick(random)
        }, {
            Log.e(TAG, it.toString())
            showError(it.toString())
        }))
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun firstEntryLoad() {
        Log.i(TAG, "first entry load")
        startLoading()

        disposables.add(
            pokemonRepository.initEntries()
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

    private fun queryEntries(query: String) {
        Log.i(TAG, "loading entries for $query ...")
        startLoading()

        disposables.add(
            pokemonRepository.getEntries(query)
                .applySchedulers()
                .subscribe(
                    {
                        Log.i(TAG, "on load finish. All pokemons loaded: ${it.size}")
                        setEntries(it)

                    }, {
                        Log.e(TAG, it.toString())
                        showError(it.toString())
                    }
                )
        )
    }
}