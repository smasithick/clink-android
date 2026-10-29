package com.clink.app

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

/**
 * Clink — Share Sheet URL Cleaner
 * Copies a sanitized link and briefly shows the cleaned URL in a compact popup chip.
 */
class CleanLinkActivity : Activity() {

    private val trackingParams = setOf(
        "a", "s", "t", "twclid", "cxt", "si", "feature", "kw", "pp", "gclid", "gclsrc", "dclid", "igsh", "igshid", "fbclid", "mibext", "__cft__", "__tn__", "is_from_webapp", "sender_device", "sender_web_id", "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content", "utm_id", "ref", "ref_src", "ref_url", "share_id", "context", "tag", "linkCode", "camp", "creative", "creativeASIN", "smid", "mc_cid", "mc_eid", "_hsenc", "_hsmi", "mkt_tok"
    )

    private val stripAllQueryDomains = setOf(
        "x.com", "twitter.com", "vxtwitter.com", "fxtwitter.com", "youtu.be", "instagram.com", "tiktok.com", "threads.net"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrBlank()) {
                val cleaned = processSharedText(sharedText)
                copyToClipboard(cleaned)
                showUrlChip(cleaned)
                return
            }
        }

        finish()
    }

    private fun showUrlChip(url: String) {
        val dialog = Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.url_chip_dialog)

        val urlText = dialog.findViewById<TextView>(R.id.urlText)
        urlText.text = url

        dialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        dialog.window?.attributes?.y = 140
        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            finish()
        }, 1800)
    }

    private fun processSharedText(text: String): String {
        val matcher = Patterns.WEB_URL.matcher(text)
        val sb = StringBuffer()
        var firstCleanUrl: String? = null

        while (matcher.find()) {
            val rawUrl = matcher.group().trimEnd('.', ',', ';', ')', '!')
            val cleanedUrl = cleanSingleUrl(rawUrl)
            if (firstCleanUrl == null) firstCleanUrl = cleanedUrl
            matcher.appendReplacement(sb, Regex.escapeReplacement(cleanedUrl))
        }
        matcher.appendTail(sb)

        return firstCleanUrl ?: text
    }

    private fun cleanSingleUrl(urlString: String): String {
        return try {
            val cleanedPathUrl = urlString.replace(Regex("/ref=[^/?#]+"), "")
            val uri = Uri.parse(cleanedPathUrl)
            val host = uri.host?.lowercase()?.removePrefix("www.") ?: return cleanedPathUrl

            if (stripAllQueryDomains.any { host == it || host.endsWith(".$it") }) {
                if ((host == "x.com" || host == "twitter.com") && uri.path?.startsWith("/search") == true) {
                    return filterQueryParams(uri)
                }
                return uri.buildUpon().clearQuery().build().toString()
            }

            filterQueryParams(uri)
        } catch (e: Exception) {
            urlString
        }
    }

    private fun filterQueryParams(uri: Uri): String {
        if (uri.query == null) return uri.toString()

        val builder = uri.buildUpon().clearQuery()
        for (paramName in uri.queryParameterNames) {
            val lower = paramName.lowercase()
            val isTracker = lower in trackingParams || lower.startsWith("utm_") || lower.startsWith("__")
            if (!isTracker) {
                for (value in uri.getQueryParameters(paramName)) {
                    builder.appendQueryParameter(paramName, value)
                }
            }
        }
        return builder.build().toString()
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("clink_clean_url", text))
    }
}