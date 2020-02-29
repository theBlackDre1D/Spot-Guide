package com.example.spotguide.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotguide.core.extension.colorFromRes
import com.example.spotguide.core.extension.visibleOrGone
import com.example.spotguide.ui.action_bar.ActionBarParams
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.action_bar.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates

abstract class BaseFragment: Fragment() {

    data class Event(val func: () -> Unit)

    val onFragmentLoaded = mutableListOf<Event>()

    enum class FragmentState { INIT, RESUMED, LOADED }

    open fun onLoadedState() {
        onFragmentLoaded.forEach { it.func.invoke() }
        onFragmentLoaded.clear()
    }

    protected val isInit: Boolean
        get() = fragState == FragmentState.INIT

    protected var fragState: FragmentState by Delegates.observable(FragmentState.INIT, { _, old, new ->
        when (new) {
            FragmentState.LOADED, FragmentState.RESUMED -> onLoadedState()
            else -> { /* nothing */ }
        }
    })

    open val actionBarParams = ActionBarParams(false)

    abstract val layoutResId: Int

    private val job = SupervisorJob()
    open val isLastFragment = false
    protected val storageRef: StorageReference by lazy { FirebaseStorage.getInstance().reference }

    protected val activity: BaseActivity
        get() = getActivity() as BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewModelStates()
        setViewModelEvents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()
        setupUI()
    }

    override fun onDestroy() {
        if (!job.isCancelled) job.cancel()
        super.onDestroy()
    }

    open fun setupUI() {}
    open fun handleBackPress() = false

    protected fun setupActionBar(actionParams: ActionBarParams? = null) {
        val params = actionParams ?: actionBarParams
        activity.vToolbar?.let { toolbar ->
            toolbar.visibleOrGone(params.actionBarVisible)
            params.leftIcon?.let { image ->
                toolbar.ivLeft.setImageResource(image.imageResId)
                toolbar.ivLeft.setOnClickListener { image.onClick() }
            }
            params.middleText?.let { text ->
                toolbar.tvMiddle.text = text.text
                toolbar.tvMiddle.setOnClickListener { text.onClick?.invoke() }
                text.textColor?.let { toolbar.tvMiddle.setTextColor(it.colorFromRes()) }
            }
            params.rightText?.let { text ->
                toolbar.tvRight.text = text.text
                toolbar.tvRight.setOnClickListener { text.onClick?.invoke() }
                text.textColor?.let { toolbar.tvRight.setTextColor(it.colorFromRes()) }
            }

            toolbar.ivLeft.visibleOrGone(params.leftIcon != null)
            toolbar.tvMiddle.visibleOrGone(params.middleText != null)
            toolbar.tvRight.visibleOrGone(params.rightText != null)
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return try {
            val animation = AnimationUtils.loadAnimation(getActivity(), nextAnim)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    fragState = if (enter) FragmentState.LOADED else FragmentState.RESUMED
                }
                override fun onAnimationStart(animation: Animation?) {}
            })
            return animation
        } catch (e: Exception) { null }
    }

    override fun onPause() {
        super.onPause()

        fragState = FragmentState.RESUMED
    }

    abstract fun setViewModelStates()
    abstract fun setViewModelEvents()

    fun showMessage(message: String, longDuration: Boolean = false) {
        Toast.makeText(requireContext(), message, if (longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

    fun showErrorToast(message: String, longDuration: Boolean = false) {
        Toasty.error(requireContext(), message, if (longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT, true).show()
    }

    fun showSuccessToast(message: String, longDuration: Boolean = false) {
        Toasty.success(requireContext(), message, if (longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT, true).show()
    }

    protected fun doOnUICoroutine(run: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch { run() }
    }

    protected fun doOnBackgroundCoroutine(run: () -> Unit) {
        val deferred = CoroutineScope(Dispatchers.IO + job).async { run() }
        CoroutineScope(Dispatchers.Main).launch { deferred.await() }
    }

    fun handleError(errorText: String) {
        activity.hideLoading()
        showErrorToast(errorText)
    }
}