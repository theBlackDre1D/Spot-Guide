package com.example.spotguide.features.spot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.example.spotguide.R
import com.example.spotguide.ui.ViewHolders
import com.example.spotguide.ui.adapters.BaseRecyclerViewAdapter
import com.example.spotguide.utils.GeoCoderUtils
import kotlinx.android.synthetic.main.fragment_spot_detail.*

class SpotDetailBottomSheetFragment(
    private val spot: Spot
) : SuperBottomSheetFragment() {

    private val adapter: BaseRecyclerViewAdapter<Int, ViewHolders.SpotPhoto> by lazy {
        BaseRecyclerViewAdapter(
            models = mutableListOf(R.drawable.nature_1, R.drawable.nature_2, R.drawable.nature_3),
            viewHolderClass = ViewHolders.SpotPhoto::class,
            bind = { v, m, p -> bind(v, m, p) }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_spot_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupMockupPhotos()
        setupSpotInfo()
    }

    private fun setupMockupPhotos() {
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = layoutManager
        rvPhotos.setHasFixedSize(true)
        rvPhotos.addOnScrollListener(CenterScrollListener())
    }

    private fun setupSpotInfo() {
        tvSpotName.text = spot.name
        spot.location?.let {
            tvSpotLocation.text = GeoCoderUtils.getNameFromLocation(requireContext(), it)
        }
        tvDescription.text = spot.description
    }

    private fun bind(viewHolder: ViewHolders.SpotPhoto, model: Int, position: Int) {
        viewHolder.photo.setImageResource(model)
    }

}