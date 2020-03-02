package com.example.spotguide.features.galley

import android.Manifest
import android.content.Context
import com.example.spotguide.R
import com.example.spotguide.core.base.BaseInputFragment
import com.example.spotguide.core.extension.stringFromRes
import com.example.spotguide.features.galley.logic.ImageModel
import com.example.spotguide.features.galley.logic.ImagesStates
import com.example.spotguide.features.galley.logic.ImagesViewModel
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import io.uniflow.androidx.flow.onStates
import org.koin.android.ext.android.inject
import java.io.Serializable
import java.util.*

class GalleryFragment : BaseInputFragment<GalleryFragment.Param>() {

    data class Param(
        val afterPick: (List<ImageModel>) -> Unit
    ) : Serializable

    override val layoutResId: Int
        get() = R.layout.fragment_gallery

    private val imagesViewModel: ImagesViewModel by inject()

    override fun setViewModelStates() {
        onStates(imagesViewModel) { state ->
            when (state) {
                is ImagesStates.ImagesLoading -> activity.showLoading(state.text)
            }
        }
    }
    override fun setViewModelEvents() {}

    override fun setupUI() {
        handlePermission()
    }

    private fun handlePermission() {
        Permissions.check(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE, null,
            object : PermissionHandler() {
                override fun onGranted() { imagesViewModel.loadImages() }
                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    showErrorToast(R.string.general_missing_permission.stringFromRes())
                    handlePermission()
                }
            })
    }

}