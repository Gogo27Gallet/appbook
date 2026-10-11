package com.example.util

import android.content.Context
import android.net.Uri
import com.example.model.BookChapter
import com.example.model.BookCoverStyle
import com.example.model.BookItem
import com.example.model.ReadingStatus
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object EpubParser {

    fun parseEpub(context: Context, uri: Uri): BookItem? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                parseEpubFromStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseEpubFromStream(inputStream: InputStream): BookItem {
        val entries = mutableMapOf<String, ByteArray>()
        val zip = ZipInputStream(inputStream)
        var entry: ZipEntry? = zip.nextEntry

        while (entry != null) {
            if (!entry.isDirectory) {
                val bytes = zip.readBytes()
                entries[entry.name] = bytes
            }
            zip.closeEntry()
            entry = zip.nextEntry
        }

        // 1. Locate container.xml to find OPF path
        var opfPath = "OEBPS/content.opf"
        val containerXml = entries["META-INF/container.xml"]
        if (containerXml != null) {
            val xmlText = String(containerXml, Charsets.UTF_8)
            val fullPathRegex = Regex("""full-path=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
            fullPathRegex.find(xmlText)?.let {
                opfPath = it.groupValues[1]
            }
        } else {
            // Find any .opf file in archive
            entries.keys.firstOrNull { it.endsWith(".opf", ignoreCase = true) }?.let {
                opfPath = it
            }
        }

        val opfContent = entries[opfPath]?.let { String(it, Charsets.UTF_8) } ?: ""
        val opfDir = if (opfPath.contains("/")) opfPath.substringBeforeLast("/") + "/" else ""

        // 2. Extract Title and Author
        var title = "Livre importé"
        var author = "Auteur inconnu"

        val titleRegex = Regex("""<dc:title[^>]*>([^<]+)</dc:title>""", RegexOption.IGNORE_CASE)
        titleRegex.find(opfContent)?.let {
            title = it.groupValues[1].trim()
        }

        val creatorRegex = Regex("""<dc:creator[^>]*>([^<]+)</dc:creator>""", RegexOption.IGNORE_CASE)
        creatorRegex.find(opfContent)?.let {
            author = it.groupValues[1].trim()
        }

        // 3. Extract Manifest and Spine items
        val itemRegex = Regex("""<item\s+[^>]*id=["']([^"']+)["'][^>]*href=["']([^"']+)["'][^>]*media-type=["']([^"']+)["'][^>]*>""", RegexOption.IGNORE_CASE)
        val idToHref = mutableMapOf<String, String>()
        val idToMediaType = mutableMapOf<String, String>()

        itemRegex.findAll(opfContent).forEach { match ->
            val id = match.groupValues[1]
            val href = match.groupValues[2]
            val mediaType = match.groupValues[3]
            idToHref[id] = href
            idToMediaType[id] = mediaType
        }

        // Also check reverse attribute order in <item> tags
        val altItemRegex = Regex("""<item\s+[^>]*href=["']([^"']+)["'][^>]*id=["']([^"']+)["'][^>]*>""", RegexOption.IGNORE_CASE)
        altItemRegex.findAll(opfContent).forEach { match ->
            val href = match.groupValues[1]
            val id = match.groupValues[2]
            idToHref[id] = href
        }

        // Look for cover image
        var coverBytes: ByteArray? = null
        for ((id, href) in idToHref) {
            val lower = href.lowercase()
            if (id.lowercase().contains("cover") || lower.contains("cover") && (lower.endsWith(".jpg") || lower.endsWith(".png") || lower.endsWith(".jpeg"))) {
                val fullImgPath = normalizePath(opfDir + href)
                val imgData = entries[fullImgPath] ?: entries[href]
                if (imgData != null) {
                    coverBytes = imgData
                    break
                }
            }
        }

        // Extract spine
        val itemrefRegex = Regex("""<itemref\s+[^>]*idref=["']([^"']+)["'][^>]*/>""", RegexOption.IGNORE_CASE)
        val spineHrefs = mutableListOf<String>()
        itemrefRegex.findAll(opfContent).forEach { match ->
            val idref = match.groupValues[1]
            idToHref[idref]?.let { spineHrefs.add(it) }
        }

        // If spine is empty, gather all html/xhtml files
        if (spineHrefs.isEmpty()) {
            idToHref.values.filter { it.endsWith(".html", true) || it.endsWith(".xhtml", true) }.forEach {
                spineHrefs.add(it)
            }
        }

        // 4. Parse Chapters
        val chapters = mutableListOf<BookChapter>()
        var chapterIndex = 1

        for (href in spineHrefs) {
            val fullChapterPath = normalizePath(opfDir + href)
            val fileBytes = entries[fullChapterPath] ?: entries[href] ?: continue
            val html = String(fileBytes, Charsets.UTF_8)

            // Extract Chapter Title
            var chapterTitle = "Chapitre $chapterIndex"
            val hRegex = Regex("""<h[1-3][^>]*>(.*?)</h[1-3]>""", RegexOption.IGNORE_CASE)
            hRegex.find(html)?.let {
                val rawH = cleanHtml(it.groupValues[1])
                if (rawH.isNotBlank()) chapterTitle = rawH
            }

            // Extract Paragraphs
            val paragraphs = mutableListOf<String>()
            val pRegex = Regex("""<p[^>]*>(.*?)</p>""", RegexOption.IGNORE_CASE)
            pRegex.findAll(html).forEach { pMatch ->
                val text = cleanHtml(pMatch.groupValues[1])
                if (text.isNotBlank()) {
                    paragraphs.add(text)
                }
            }

            if (paragraphs.isEmpty()) {
                // Fallback to body text split by double newlines
                val bodyText = cleanHtml(html)
                val split = bodyText.split("\n\n").map { it.trim() }.filter { it.length > 20 }
                paragraphs.addAll(split)
            }

            if (paragraphs.isNotEmpty()) {
                chapters.add(
                    BookChapter(
                        number = chapterIndex,
                        title = chapterTitle,
                        paragraphs = paragraphs
                    )
                )
                chapterIndex++
            }
        }

        val estimatedPages = (chapters.sumOf { it.paragraphs.sumOf { p -> p.length } } / 1500).coerceAtLeast(12)

        return BookItem(
            id = "epub_${UUID.randomUUID()}",
            title = title,
            author = author,
            subtitle = "EPUB Importé",
            coverStyle = BookCoverStyle.CUSTOM,
            coverImageBytes = coverBytes,
            rating = 5.0f,
            pageCount = estimatedPages,
            currentPage = 1,
            category = "Imports",
            quote = chapters.firstOrNull()?.paragraphs?.firstOrNull()?.take(100) ?: "",
            description = "Livre importé depuis votre appareil au format EPUB.",
            status = ReadingStatus.READING,
            chapters = chapters,
            isEpubImported = true
        )
    }

    private fun cleanHtml(html: String): String {
        return html
            .replace(Regex("""<script[^>]*>.*?</script>""", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)), "")
            .replace(Regex("""<style[^>]*>.*?</style>""", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)), "")
            .replace(Regex("""<[^>]+>"""), " ")
            .replace("&nbsp;", " ")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&rsquo;", "’")
            .replace("&lsquo;", "‘")
            .replace("&ldquo;", "“")
            .replace("&rdquo;", "”")
            .replace("&mdash;", "—")
            .replace(Regex("""\s+"""), " ")
            .trim()
    }

    private fun normalizePath(path: String): String {
        val segments = path.split("/").filter { it.isNotEmpty() && it != "." }
        val result = mutableListOf<String>()
        for (seg in segments) {
            if (seg == ".." && result.isNotEmpty()) {
                result.removeAt(result.size - 1)
            } else if (seg != "..") {
                result.add(seg)
            }
        }
        return result.joinToString("/")
    }
}
