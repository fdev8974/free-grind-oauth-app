package dev.estopia.free_grind.google_oauth

import android.content.Intent
import org.mozilla.geckoview.GeckoResult

class RequestTokenActivity : MainActivity() {

    override fun isLaunchAllowed(): Boolean = isTrustedFreeGrind(callingPackage)

    override fun onToken(token: String): GeckoResult<Any> {
        if (!isTrustedFreeGrind(callingPackage)) {
            setResult(MainActivity.RESULT_REFUSED)
            finish()
            return GeckoResult.fromException(SecurityException("caller is not trusted"))
        }
        setResult(RESULT_OK, Intent().putExtra(MainActivity.EXTRA_TOKEN, token))
        finish()
        return GeckoResult.fromValue(true)
    }
}
