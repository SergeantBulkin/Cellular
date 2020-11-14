package by.sergeantbulkin.cellular

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposableManager
{
    companion object
    {
        private val compositeDisposable = CompositeDisposable()

        fun add(disposable: Disposable)
        {
            compositeDisposable.add(disposable)
        }

        fun dispose()
        {
            compositeDisposable.dispose()
            compositeDisposable.clear()
        }
    }
}