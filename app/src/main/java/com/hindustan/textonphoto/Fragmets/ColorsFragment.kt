package com.hindustan.textonphoto.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hindustan.textonphoto.Adapter.ColorsAdapter
import com.hindustan.textonphoto.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ColorsFragment : Fragment() {

    var colorList:ArrayList<Int> = ArrayList()
    lateinit var rv_colors:RecyclerView
    lateinit var colorAdapter:ColorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_colors, container, false)

        rv_colors = view.findViewById(R.id.rv_colors)

        if (!::colorAdapter.isInitialized) {
            colorAdapter = ColorsAdapter(requireActivity(), colorList)
        }

        if (colorList.isEmpty()){
            colorList.add(R.drawable.one)
            colorList.add(R.drawable.two)
            colorList.add(R.drawable.three)
            colorList.add(R.drawable.four)
            colorList.add(R.drawable.five)
            colorList.add(R.drawable.six)
            colorList.add(R.drawable.seven)
            colorList.add(R.drawable.eught)
            colorList.add(R.drawable.nine)
            colorList.add(R.drawable.ten)
            colorList.add(R.drawable.eleven)
            colorList.add(R.drawable.twelav)
            colorList.add(R.drawable.tharedteen)
            colorList.add(R.drawable.fourteen)
        }



        rv_colors.setHasFixedSize(true)

        rv_colors.layoutManager = GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)

        rv_colors.adapter = colorAdapter




        return view
    }


}