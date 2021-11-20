package com.github.zharovvv.open.source.weather.app.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.zharovvv.open.source.weather.app.databinding.FragmentErrorBinding
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.ui.view.BaseFragment

class ErrorFragment : BaseFragment() {

    private var _binding: FragmentErrorBinding? = null
    private val binding: FragmentErrorBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val error = arguments.toDataStateError()
        if (error.isBlocking) {
            binding.errorButton.isVisible = false
        } else {
            binding.errorButton.setOnClickListener {
                hideError()
            }
        }
        with(binding) {
            errorIconImageView.setImageResource(error.errorIconId)
            errorTitleTextView.text = error.errorTitle
            errorDescriptionTextView.text = error.errorDescription
        }
    }

    private fun hideError() {
        val viewLifecycle = viewLifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                // Так как фрагмент будет удален не сразу (commit = Handler.post)
                // вызываем setFragmentResult именно таким образом, чтобы
                // начать действие, переданное в onHideErrorListener, только тогда,
                // когда ErrorFragment быдет скрыт.
                parentFragmentManager.setFragmentResult(ERROR_REQUEST_KEY, Bundle.EMPTY)
            }
        }
        viewLifecycle.addObserver(observer)
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private const val ERROR_REQUEST_KEY = "error_request_key"
private const val ERROR_FRAGMENT_TAG = "error_fragment_tag"

fun <T> Fragment.showError(
    errorModel: DataState.Error<T>,
    @IdRes
    errorContainerId: Int,
    onHideErrorListener: FragmentResultListener? = null
) {
    val showsRightNow = childFragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG) != null
    if (!showsRightNow) {
        onHideErrorListener?.let {
            childFragmentManager.setFragmentResultListener(
                ERROR_REQUEST_KEY,
                viewLifecycleOwner,
                it
            )
        }
        childFragmentManager.beginTransaction()
            .replace(
                errorContainerId,
                ErrorFragment::class.java,
                errorModel.toBundle(),
                ERROR_FRAGMENT_TAG
            )
            .commitAllowingStateLoss()  //Не самое хорошее решение.
        // Фиксит проблему, когда вызывается showError из закрывающегося фрагмента.
    }
}

/**
 * TODO Переделать в Parcelable?
 */
fun <T> DataState.Error<T>.toBundle(): Bundle {
    val bundle = Bundle()
    bundle.putInt("errorIconId", errorIconId)
    bundle.putString("errorTitle", errorTitle)
    bundle.putString("errorDescription", errorDescription)
    bundle.putBoolean("isBlocking", isBlocking)
    bundle.putString("message", message)
    return bundle
}

fun Bundle?.toDataStateError(): DataState.Error<Any> {
    if (this == null) throw IllegalArgumentException("Bundle is null.")
    return DataState.Error(
        errorIconId = getInt("errorIconId"),
        errorTitle = getString("errorTitle")!!,
        errorDescription = getString("errorDescription")!!,
        isBlocking = getBoolean("isBlocking"),
        message = getString("message")!!
    )
}