package com.itomakiweb.android.bank.libraries

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * should use when async
 * NOTE ScopedAppActivityを参照して記述
 * TODO これで使い方があっているかは要確認
 */
abstract class ScopedFragment: Fragment(), CoroutineScope by MainScope() {
    override fun onDestroy() {
        super.onDestroy()
        cancel() // CoroutineScope.cancel
    }
}
