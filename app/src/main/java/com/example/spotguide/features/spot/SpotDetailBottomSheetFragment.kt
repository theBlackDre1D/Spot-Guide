package com.example.spotguide.features.spot

import android.app.Dialog
import android.os.Bundle
import android.view.*
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.example.spotguide.R
import com.example.spotguide.core.App
import com.example.spotguide.core.extension.afterTextChanged
import com.example.spotguide.core.extension.hideKeyboard
import com.example.spotguide.core.extension.showKeyboard
import com.example.spotguide.core.extension.stringFromRes
import com.example.spotguide.features.main.NamedFun
import com.example.spotguide.features.spot.logic.Review
import com.example.spotguide.features.spot.logic.SpotStates
import com.example.spotguide.features.spot.logic.SpotViewModel
import com.example.spotguide.ui.ViewHolders
import com.example.spotguide.ui.adapters.BaseRecyclerViewAdapter
import com.example.spotguide.utils.BottomButtonsUtils
import com.example.spotguide.utils.GeoCoderUtils
import com.example.spotguide.utils.StarRatingBarUtils
import io.uniflow.androidx.flow.onEvents
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.dialog_add_review.*
import kotlinx.android.synthetic.main.fragment_spot_detail.*
import org.koin.android.ext.android.inject

class SpotDetailBottomSheetFragment(
    private val spot: Spot
) : SuperBottomSheetFragment() {

    private val spotViewModel: SpotViewModel by inject()

    private var reviewDialog: Dialog? = null
    private val photosAdapter: BaseRecyclerViewAdapter<Int, ViewHolders.SpotPhoto> by lazy {
        BaseRecyclerViewAdapter(
            models = mutableListOf(R.drawable.nature_1, R.drawable.nature_2, R.drawable.nature_3),
            viewHolderClass = ViewHolders.SpotPhoto::class,
            bind = { v, m, p -> bind(v, m, p) }
        )
    }
//    private val reviewsAdapter: BaseRecyclerViewAdapter<Review, ViewHolders.Review> by lazy {
//        BaseRecyclerViewAdapter(
//            models = mutableListOf(),
//            viewHolderClass = ViewHolders.Review::class,
//            bind = { v, m, p -> bindReview(v, m, p) }
//        )
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_spot_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupMockupPhotos()
        setupSpotInfo()
        setupViewModelStatesAndEvents()

    }

    private fun setupMockupPhotos() {
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        rvPhotos.adapter = photosAdapter
        rvPhotos.layoutManager = layoutManager
        rvPhotos.setHasFixedSize(true)
        rvPhotos.addOnScrollListener(CenterScrollListener())
        bAddReview.setOnClickListener { addReviewDialog() }
    }

    private fun setupViewModelStatesAndEvents() {
        onStates(spotViewModel) { state ->
            when (state) {
                is SpotStates.AddingReview -> App.currentActivity.get()?.showLoading(state.text)
                is SpotStates.ReviewAdded -> onReviewAdded()
//                is SpotStates.ReviewsForSpot ->
            }
        }

        onEvents(spotViewModel) { event ->
            when (val data = event.take()) {

            }
        }
    }

    private fun onReviewAdded() {
        App.currentActivity.get()?.hideLoading()
        reviewDialog?.dismiss()
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

    private fun bindReview(viewHolder: ViewHolders.Review, model: Review, position: Int) {
//        viewHolder.photo.setImageResource(model)
    }

    private fun addReviewDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_review)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        var newReview = Review(spotId = spot.id, rating = 1)

        StarRatingBarUtils.setStarsSize(dialog.vStarRatingBar, 20)
        StarRatingBarUtils.setupView(dialog.vStarRatingBar) {
            newReview = newReview.copy(rating = it)
        }
        val left = NamedFun(R.string.spot_add_new_cancel.stringFromRes()) {
            App.currentActivity.get()?.hideKeyboard()
            dialog.dismiss()
        }
        val right = NamedFun(R.string.spot_add_new_confirm.stringFromRes()) {
            spotViewModel.addReview(newReview)
        }
        BottomButtonsUtils.setupButtons(dialog.vBottomButtons, left, right)
        dialog.etReviewBody.showKeyboard()
        dialog.etReviewBody.afterTextChanged { newReview = newReview.copy(reviewText = it) }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER

        dialog.window?.attributes = lp
        dialog.show()
        reviewDialog = dialog
    }

}