package com.github.zharovvv.open.source.weather.app.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable

fun EditText.textObservable(): Observable<String> {
    return Observable
        .create { emitter ->
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s != null && !emitter.isDisposed) {
                        emitter.onNext(s.toString())
                    }
                }
            }
            emitter.setCancellable { this.removeTextChangedListener(textWatcher) }
            this.addTextChangedListener(textWatcher)
        }

}