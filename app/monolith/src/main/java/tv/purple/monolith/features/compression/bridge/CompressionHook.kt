package tv.purple.monolith.features.compression.bridge

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import tv.purple.monolith.bridge.RES_STRINGS
import tv.purple.monolith.core.LoggerImpl
import tv.purple.monolith.core.util.ViewUtil.getView
import tv.purple.monolith.core.util.ViewUtil.changeVisibility
import tv.purple.monolith.core.models.flag.Flag
import tv.purple.monolith.features.tracking.bridge.BugsnagUtil
import tv.twitch.android.core.crashreporter.CrashReporter
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate

object CompressionHook {

    @JvmStatic
    fun maybeShowCompressionButton(button: ImageView?) {
        button?.changeVisibility(Flag.SHOW_TIMER_BUTTON.asBoolean()) 
        // per ora puoi riusare un flag esistente, poi creeremo un flag dedicato
    }

    @JvmStatic
    fun createCompressionButton(view: View): ImageView {
        return view.getView(resName = RES_STRINGS.player_control_overlay_widget__compression_button)
    }

    @JvmStatic
    fun getCompressionButton(view: View): ImageView? {
        return try {
            createCompressionButton(view).apply {
                setOnClickListener { btn ->
                    try {
                        val activity = btn.context as FragmentActivity
                        // TODO: qui aprirai un dialog o attiverai la logica di compressione
                        LoggerImpl.debug("Compression button clicked")
                    } catch (th: Throwable) {
                        BugsnagUtil.logException(th, "CompressionHook", CrashReporter.ExceptionType.NON_FATAL)
                    }
                }
                changeVisibility(true) // sempre visibile per ora
            }
        } catch (th: Throwable) {
            BugsnagUtil.logException(th, "CompressionHook", CrashReporter.ExceptionType.NON_FATAL)
            null
        }
    }

    @JvmStatic
    fun bindCompressionButton(playerOverlayViewDelegate: PlayerOverlayViewDelegate) {
        getCompressionButton(playerOverlayViewDelegate.contentView)?.let {
            LoggerImpl.debug("Binding compression button: $it")
        }
    }
}
