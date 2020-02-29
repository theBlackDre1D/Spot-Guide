package com.example.spotguide.features.spot

import com.example.spotguide.R
import com.example.spotguide.core.base.BaseInputFragment
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

class AddNewSpotFragment : BaseInputFragment<AddNewSpotFragment.Param>() {

    data class Param(
        val location: LatLng
    ) : Serializable

    override val layoutResId: Int
        get() = R.layout.fragment_add_new_spot

    override fun setViewModelStates() {}
    override fun setViewModelEvents() {}

}