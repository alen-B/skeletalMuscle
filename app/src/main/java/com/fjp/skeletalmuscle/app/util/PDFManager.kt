package com.fjp.skeletalmuscle.app.util

import android.content.Context
import android.os.Environment
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.IBlockElement
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.io.IOException

/**
 *Author:Mr'x
 *Time:2024/12/21
 *Description:
 */
object PDFManager {
    lateinit var font : PdfFont
    lateinit var document: Document
    lateinit var file: File
    fun createPDF(context: Context) {
        font= PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false)
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "骨骼肌PDF")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        var title = "${App.userInfo?.name}爷爷，骨骼肌运动报告"
        if (App.userInfo?.sex == context.getString(R.string.setting_sex_woman)) {
            title = "${App.userInfo?.name}奶奶的，骨骼肌运动报告"
        }
        // 创建要保存的PDF文件对象
        file = File(dir, "${title}${DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS)}.pdf")
        try {
            val font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false)
            // 创建PdfWriter，将其与要生成的PDF文件关联起来
            val writer = PdfWriter(file)
            // 通过PdfWriter实例创建PdfDocument对象
            val pdfDoc = PdfDocument(writer)
            // 基于PdfDocument创建Document对象，用于向PDF中添加内容
            document = Document(pdfDoc)
            // 向PDF文档中添加一段简单的文本内容示例

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun createCell(value: String, textAligment: TextAlignment = TextAlignment.CENTER): Cell {
        return Cell().add(Paragraph(value).setFont(font)).setTextAlignment(textAligment)
    }

    fun createParagraph(value: String, textAligment: TextAlignment = TextAlignment.LEFT, fontSize: Float = 12f) {
        document.add(Paragraph(value).setTextAlignment(textAligment).setFont(font).setFontSize(fontSize))
    }

    fun createLine() {
        val line = LineSeparator(SolidLine())
        // 将分割线添加至文档
        document.add(line)
    }

    fun add(element: IBlockElement) {
        document.add(element)
    }

    fun getFilePath(): String {
        if (this@PDFManager::file.isInitialized) {
            return file.absolutePath
        }
        return ""
    }

    fun close() {
        document.close()
    }
}