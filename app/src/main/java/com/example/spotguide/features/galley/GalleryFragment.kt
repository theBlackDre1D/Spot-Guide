package com.example.spotguide.features.galley

import android.Manifest
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.example.spotguide.R
import com.example.spotguide.core.base.BaseInputFragment
import com.example.spotguide.core.extension.loadImageFromImageModel
import com.example.spotguide.core.extension.stringFromRes
import com.example.spotguide.core.extension.visibleOrGone
import com.example.spotguide.core.navigation.Navigation
import com.example.spotguide.features.galley.logic.ImageModel
import com.example.spotguide.features.galley.logic.ImagesStates
import com.example.spotguide.features.galley.logic.ImagesViewModel
import com.example.spotguide.features.main.NamedFun
import com.example.spotguide.ui.ViewHolders
import com.example.spotguide.ui.action_bar.ActionBarParams
import com.example.spotguide.ui.action_bar.Image
import com.example.spotguide.ui.action_bar.Text
import com.example.spotguide.ui.adapters.BaseRecyclerViewAdapter
import com.example.spotguide.ui.decorators.GridSpacingDecorator
import com.example.spotguide.utils.BottomButtonsUtils
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.android.ext.android.inject
import java.io.Serializable
import java.util.*

class GalleryFragment : BaseInputFragment<GalleryFragment.Param>() {

    override val actionBarParams = ActionBarParams(
        leftIcon = Image { Navigation.pop(activity) },
        middleText = Text(R.string.gallery_screen_header.stringFromRes())
    )

    data class Param(
        val afterPick: (List<ImageModel>) -> Unit
    ) : Serializable

    override val layoutResId: Int
        get() = R.layout.fragment_gallery

    private val imagesViewModel: ImagesViewModel by inject()

    private val pickedPhotos = mutableListOf<ImageModel>()
    private val adapter: BaseRecyclerViewAdapter<ImageModel, ViewHolders.GalleryImage> by lazy {
        BaseRecyclerViewAdapter(
            viewHolderClass = ViewHolders.GalleryImage::class,
            bind = { v, m, p -> bind(v, m, p) }
        )
    }

    override fun setViewModelStates() {
        onStates(imagesViewModel) { state ->
            when (state) {
                is ImagesStates.ImagesLoading -> activity.showLoading(state.text)
                is ImagesStates.ImagesLoaded -> onImagesLoaded(state.photos)
            }
        }
    }
    override fun setViewModelEvents() {}

    override fun setupUI() {
        handlePermission()
        setupRV()
        setupBottomButtons()
    }

    // State handling section START

    private fun onImagesLoaded(images: List<ImageModel>) {
        adapter.addOnlyNewData(images)
        activity.hideLoading()
    }

    // State handling section END

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

    private fun setupRV() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.addItemDecoration(GridSpacingDecorator(3, 8, true))
    }

    private fun bind(viewHolder: ViewHolders.GalleryImage, model: ImageModel, position: Int) {
        viewHolder.image.loadImageFromImageModel(model)
        viewHolder.blur.visibleOrGone(pickedPhotos.contains(model))
        viewHolder.rootView.setOnClickListener {
            if (pickedPhotos.contains(model)) pickedPhotos.remove(model)
            else pickedPhotos.add(model)
            bind(viewHolder, model, position)
        }
    }

    private fun setupBottomButtons() {
        val left = NamedFun(R.string.spot_add_new_cancel.stringFromRes()) { Navigation.pop(activity) }
        val right = NamedFun(R.string.spot_add_new_confirm.stringFromRes()) {
            params!!.afterPick.invoke(pickedPhotos)
            Navigation.pop(activity)
        }
        BottomButtonsUtils.setupButtons(vBottomButtons, left, right)
    }

}