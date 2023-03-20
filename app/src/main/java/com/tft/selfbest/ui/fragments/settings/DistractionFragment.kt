package com.tft.selfbest.ui.fragments.settings

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tft.selfbest.R
import com.tft.selfbest.data.entity.DistractedApp
import com.tft.selfbest.databinding.FragmentDistractionBinding
import com.tft.selfbest.models.AddDistraction
import com.tft.selfbest.models.SuggestedDistraction
import com.tft.selfbest.models.ToggleDistraction
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.MyButton
import com.tft.selfbest.ui.activites.MyButtonListener
import com.tft.selfbest.ui.activites.MySwipeHelper
import com.tft.selfbest.ui.adapter.CurrentDistractionAdapter
import com.tft.selfbest.ui.adapter.SuggestedDistractionsAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DistractionFragment : Fragment(), SuggestedDistractionsAdapter.AddDistractionListener,
    SuggestedDistractionsAdapter.CollapseListListener,
    SuggestedDistractionsAdapter.DeleteSugDistraction,
    SuggestedDistractionsAdapter.ExpandListListener,
    CurrentDistractionAdapter.DeleteDistractionListener,
    CurrentDistractionAdapter.ToggleDistractionListener {
    lateinit var binding: FragmentDistractionBinding
    val viewModel by viewModels<DistractionViewModel>()
    private var distractions: List<DistractedApp>? = null
    private val suggestedDistractionList = ArrayList<SuggestedDistraction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDistractionBinding.inflate(layoutInflater)
        val flexBoxLayout = FlexboxLayoutManager(context)
        //searchBarListener()
        flexBoxLayout.flexDirection = FlexDirection.ROW
        flexBoxLayout.justifyContent = JustifyContent.FLEX_START
        binding.suggestedDistractions.layoutManager = flexBoxLayout

        suggestedDistractionList.add(
            SuggestedDistraction(
                R.drawable.ic_icon_youtube,
                "Youtube",
                false,
                "www.youtube.com",
                0
            )
        )
        suggestedDistractionList.add(
            SuggestedDistraction(
                R.drawable.ic_icons8_facebook,
                "Facebook",
                false,
                "www.facebook.com",
                0
            )
        )
        suggestedDistractionList.add(
            SuggestedDistraction(
                R.drawable.icons_instagram,
                "Instagram",
                false,
                "www.instagram.com",
                0
            )
        )
        suggestedDistractionList.add(
            SuggestedDistraction(
                R.drawable.ic_icons8_netflix,
                "Netflix",
                false,
                "www.netflix.com",
                0
            )
        )

        var adapter1: SuggestedDistractionsAdapter
        var adapter: CurrentDistractionAdapter

        val swipe = object : MySwipeHelper(requireActivity(), binding.currentDistractions, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(activity!!, "Delete", 30, Color.parseColor("#E20404"),
                        object : MyButtonListener {
                            override fun onClick(pos: Int) {
                                //Log.e("Swipe", " Test 1 ${distractions!![pos].id}")
                                distractions!![pos].state = false
                                viewModel.deleteDistraction(distractions!![pos].id)
                            }

                        })
                )
            }

        }

        viewModel.getDistraction()
        viewModel.distractionObserver.observe(viewLifecycleOwner) {
            //Log.e("Swipe ", "Test 5")
            if (it is NetworkResponse.Success) {
                binding.progressBar.visibility = View.GONE
                distractions = it.data?.list
                if (distractions != null) {
                    binding.currentDistractions.layoutManager =
                        LinearLayoutManager(binding.root.context)
                    for (dist in suggestedDistractionList)
                        for (cdist in distractions!!) {
                            //Log.e("Distraction ", "No" + dist.url + " " + cdist.url)
                            if (dist.url == cdist.url) {
                                //Log.e("Distraction ", "Yes" + dist.url + " " + cdist.url)
                                dist.state = cdist.state!!
                                dist.id = cdist.id
                                //Log.e("Distraction ", " " + dist.state)
                                break
                            }
                        }

                    adapter1 =
                        SuggestedDistractionsAdapter(
                            suggestedDistractionList,
                            this,
                            this,
                            this,
                            this
                        )
                    if(distractions != null) {
                        if(distractions!!.size > 10) {
                            binding.loadMoreBtn.visibility = View.VISIBLE
                            adapter = CurrentDistractionAdapter(
                                binding.root.context,
                                distractions!!.take(10),
                                this,
                                this,
                            )

                            binding.currentDistractions.adapter = adapter
                        }
                        else {
                            binding.loadMoreBtn.visibility = View.GONE
                            adapter = CurrentDistractionAdapter(
                                binding.root.context,
                                distractions,
                                this,
                                this,
                            )

                            binding.currentDistractions.adapter = adapter
                        }
                    }
                    binding.suggestedDistractions.adapter = adapter1
                }
            }
        }

        binding.searchBar.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
        binding.searchBar.setOnEditorActionListener { _, _, _ ->
            if(binding.searchBar.text.toString()==""){
                Toast.makeText(requireContext(), "Empty URL", Toast.LENGTH_SHORT).show()
            } else {
                val addDistractionObject = AddDistraction(binding.searchBar.text.toString(), "")
                viewModel.addDistraction(addDistractionObject)
            }
            binding.searchBar.text.clear()
            hideSoftKeyboard(requireActivity())
            true
        }
        viewModel.addDistractionObserver.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            if (distractions != null) {
                for (dist in suggestedDistractionList)
                    for (cdist in distractions!!) {
                        //Log.e("Distraction ", "No" + dist.url + " " + cdist.url)
                        if (dist.url == cdist.url) {
                            //Log.e("Distraction ", "Yes" + dist.url + " " + cdist.url)
                            dist.state = cdist.state!!
                            dist.id = cdist.id
                            //Log.e("Distraction ", " " + dist.state)
                            break
                        }
                    }

                adapter1 =
                    SuggestedDistractionsAdapter(suggestedDistractionList, this, this, this, this)
                if(distractions != null) {
                    if(distractions!!.size > 10) {
                        binding.loadMoreBtn.visibility = View.VISIBLE
                        adapter = CurrentDistractionAdapter(
                            binding.root.context,
                            distractions!!.take(10),
                            this,
                            this,
                        )

                        binding.currentDistractions.adapter = adapter
                    }
                    else {
                        binding.loadMoreBtn.visibility = View.GONE
                        adapter = CurrentDistractionAdapter(
                            binding.root.context,
                            distractions,
                            this,
                            this,
                        )

                        binding.currentDistractions.adapter = adapter
                    }
                }
                binding.suggestedDistractions.adapter = adapter1

            }
        }

        viewModel.deleteDistractionObserver.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            if (distractions != null) {
                for (dist in suggestedDistractionList)
                    for (cdist in distractions!!) {
                        //Log.e("Distraction ", "No delete" + dist.url + " " + cdist.url)
                        if (dist.url == cdist.url) {
                            //Log.e("Distraction ", "Yes delete" + dist.url + " " + cdist.url)
                            dist.state = cdist.state!!
                            dist.id = cdist.id
                            //Log.e("Distraction ", " " + dist.state)
                            break
                        }
                    }

                adapter1 =
                    SuggestedDistractionsAdapter(suggestedDistractionList, this, this, this, this)
                if(distractions != null) {
                    if(distractions!!.size > 10) {
                        binding.loadMoreBtn.visibility = View.VISIBLE
                        adapter = CurrentDistractionAdapter(
                            binding.root.context,
                            distractions!!.take(10),
                            this,
                            this,
                        )

                        binding.currentDistractions.adapter = adapter
                    }
                    else {
                        binding.loadMoreBtn.visibility = View.GONE
                        adapter = CurrentDistractionAdapter(
                            binding.root.context,
                            distractions,
                            this,
                            this,
                        )

                        binding.currentDistractions.adapter = adapter
                    }
                }
                binding.suggestedDistractions.adapter = adapter1

            }

        }

        binding.loadMoreBtn.setOnClickListener {
            binding.loadMoreBtn.visibility = View.GONE
            if (distractions != null) {
                adapter = CurrentDistractionAdapter(
                    binding.root.context,
                    distractions,
                    this,
                    this,
                )

                binding.currentDistractions.adapter = adapter
            }
        }

        return binding.root
    }

//    private fun searchBarListener() {
//        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (!query.isNullOrEmpty()) {
//                    val addDistractionObject = AddDistraction(query.toString(), "")
//                    viewModel.addDistraction(addDistractionObject)
//                    binding.searchBar.setQuery("", false)
//                    binding.searchBar.clearFocus()
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(query: String): Boolean {
//                if (query.isEmpty()) {
//                    return false
//                }
//                return true
//            }
//        })
//    }

    override fun getAddDistraction(addDistraction: AddDistraction) {
        viewModel.addDistraction(addDistraction)
    }

    override fun collapseList(list: ArrayList<SuggestedDistraction>) {
        list.subList(4, 8).clear()
        binding.suggestedDistractions.adapter =
            SuggestedDistractionsAdapter(list, this, this, this, this)
    }

    override fun expandList(list: ArrayList<SuggestedDistraction>) {
        list.add(
            SuggestedDistraction(
                R.drawable.ic_icons8_yahoo,
                "Yahoo",
                false,
                "www.yahoo.com",
                0
            )
        )
        list.add(
            SuggestedDistraction(
                R.drawable.icon_witter,
                "Twitter",
                false,
                "www.twitter.com",
                0
            )
        )
        list.add(
            SuggestedDistraction(
                R.drawable.ic_buzzfeed_svgrepo_com,
                "Buzzfeed",
                false,
                "www.buzzfeed.com",
                0
            )
        )
        list.add(
            SuggestedDistraction(
                R.drawable.ic_iconmonstr_reddit_4,
                "Reddit",
                false,
                "www.reddit.com", 0
            )
        )
        for (dist in suggestedDistractionList)
            for (cdist in distractions!!) {
                Log.e("Distraction ", "No" + dist.url + " " + cdist.url)
                if (dist.url == cdist.url) {
                    Log.e("Distraction ", "Yes" + dist.url + " " + cdist.url)
                    dist.state = cdist.state!!
                    dist.id = cdist.id
                    Log.e("Distraction ", " " + dist.state)
                    break
                }
            }
    }

    override fun deleteDistraction(id: Int) {
        viewModel.deleteDistraction(id)
    }

    override fun deleteSugDistraction(id: Int) {
        for (curr in distractions!!) {
            if (id == curr.id) {
                curr.state = false
                break
            }
        }
        viewModel.deleteDistraction(id)
    }

    override fun toggleDistraction(id: Int, state: Boolean?) {
        val togDist = ToggleDistraction(id, state)
        viewModel.toggleDistraction(togDist)
    }

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.getCurrentFocus() == null){
            return
        }
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }
}