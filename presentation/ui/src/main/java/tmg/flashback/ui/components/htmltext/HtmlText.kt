package tmg.flashback.ui.components.htmltext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody2

@Composable
fun HtmlText(
    html: String,
    modifier: Modifier = Modifier,
    linkColor: Color = AppTheme.colors.primary, // Default link color
) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = remember(html) {
        val spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        val text = spanned.toString()
        buildAnnotatedString {
            append(text)
            val urlSpans =
                spanned.getSpans(0, spanned.length, android.text.style.URLSpan::class.java)
            urlSpans.forEach { urlSpan ->
                val start = spanned.getSpanStart(urlSpan)
                val end = spanned.getSpanEnd(urlSpan)
                val url = urlSpan.url
                addStyle(
                    style = SpanStyle(
                        color = linkColor,
                        fontSize = AppTheme.typography.body2.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    ), start = start, end = end
                )
                addLink(
                    url = LinkAnnotation.Url(
                        url = url,
                        linkInteractionListener = {
                            uriHandler.openUri(url)
                        }
                    ),
                    start = start,
                    end = end
                )
            }
        }
    }
    TextBody2(
        modifier = modifier,
        annotatedString = annotatedText
    )
}