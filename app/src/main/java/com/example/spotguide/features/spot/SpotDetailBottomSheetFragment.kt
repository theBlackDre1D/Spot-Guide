package com.example.spotguide.features.spot

import android.app.Dialog
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.example.spotguide.R
import com.example.spotguide.core.App
import com.example.spotguide.core.extension.*
import com.example.spotguide.features.main.NamedFun
import com.example.spotguide.features.spot.logic.Review
import com.example.spotguide.features.spot.logic.SpotEvents
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
    private val spot: Spot,
    private val currentLocation: Location?
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

    private val reviewsAdapter: BaseRecyclerViewAdapter<Review, ViewHolders.ReviewCell> by lazy {
        BaseRecyclerViewAdapter(
            models = mutableListOf(),
            viewHolderClass = ViewHolders.ReviewCell::class,
            bind = { v, m, p -> bindReview(v, m, p) }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_spot_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModelStatesAndEvents()
        setupMockupPhotos()
        setupSpotInfo()
        setupReviewsRV()
        spot.id?.let { spotViewModel.getReviewsForSpot(it) }
    }

    private fun setupMockupPhotos() {
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        rvPhotos.adapter = photosAdapter
        rvPhotos.layoutManager = layoutManager
        rvPhotos.setHasFixedSize(true)
        rvPhotos.addOnScrollListener(CenterScrollListener())
    }

    private fun setupViewModelStatesAndEvents() {
        onStates(spotViewModel) { state ->
            when (state) {
                is SpotStates.AddingReview -> App.currentActivity.get()?.showLoading(state.text)
                is SpotStates.ReviewAdded -> onReviewAdded()
                is SpotStates.ReviewsForSpot -> onReviewsForSpot(state.reviews)
            }
        }

        onEvents(spotViewModel) { event ->
            when (val data = event.take()) {
                is SpotEvents.ReviewsForSpotLoadFail -> reviewsLoading.visibleOrGone(false)
            }
        }
    }

    // State handling section START

    private fun onReviewAdded() {
        App.currentActivity.get()?.hideLoading()
        reviewDialog?.dismiss()
    }

    private fun onReviewsForSpot(reviews: List<Review>) {
        reviewsAdapter.addOnlyNewData(reviews)
        reviewsLoading.visibleOrGone(false)
        tvNoReviews.visibleOrGone(reviews.isEmpty())
    }

    // State handling section END

    private fun setupSpotInfo() {
        tvSpotName.text = spot.name
        spot.location?.let {
            tvSpotLocation.text = GeoCoderUtils.getNameFromLocation(requireContext(), it)
        }
        tvDescription.text = spot.description
        bNavigate.setOnClickListener { openMaps() }
        bAddReview.setOnClickListener { addReviewDialog() }
    }

    private fun setupReviewsRV() {
        rvReviews.adapter = reviewsAdapter
        rvReviews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun bind(viewHolder: ViewHolders.SpotPhoto, model: Int, position: Int) {
        viewHolder.photo.setImageResource(model)
    }

    private fun bindReview(viewHolder: ViewHolders.ReviewCell, model: Review, position: Int) {
        viewHolder.reviewtext.text = model.reviewText
        StarRatingBarUtils.setStarsSize(viewHolder.stars, 20)
        StarRatingBarUtils.setStarsRating(viewHolder.stars, model.rating ?: 0)
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

    private fun openMaps() {
        currentLocation?.let {
            val uri =
                "http://maps.google.com/maps?saddr=" + it.latitude.toString() + "," +
                        it.longitude.toString() + "&daddr=" +
                        spot.latitude.toString() + "," + spot.longitude
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }

}