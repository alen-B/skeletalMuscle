package com.fjp.skeletalmuscle.app.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.fjp.skeletalmuscle.app.App
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.IBlockElement
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.BorderRadius
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


/**
 *Author:Mr'x
 *Time:2024/12/21
 *Description:
 */
object PDFManager {
    lateinit var font: PdfFont
    lateinit var document: Document
    lateinit var file: File
    fun createPDF(context: Context, time: String) {
        font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false)
        val dir = File(Environment.getExternalStorageDirectory(), "骨骼肌报告")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        var title = "${App.userInfo?.name}骨骼肌运动报告"
        // 创建要保存的PDF文件对象
        file = File(dir, "${title}${time}.pdf")
        file.createNewFile()
        try {
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

    fun createCell(value: String, textAligment: TextAlignment = TextAlignment.CENTER, color: Color = DeviceRgb(0, 0, 0)): Cell {
        return Cell().add(Paragraph(value).setFontColor(color).setFont(font)).setFontSize(10f).setTextAlignment(textAligment).setBorder(null).setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE)
    }

    fun createCellAndBackground(vararg text: Text, color: Color = DeviceRgb(0, 0, 0), cellBackGround: Color, isFirst: Boolean, isLast: Boolean): Cell {
        val paragraph = Paragraph().setFontSize(10f)
        text.forEach {
            it.setFont(font)
            paragraph.add(it)
        }
        val cell = Cell().add(paragraph.setFont(font)).setBorder(null).setBackgroundColor(cellBackGround).setHeight(36f).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE)
        if (isFirst) {
            cell.setBorderTopLeftRadius(BorderRadius(4f))
            paragraph.setMarginLeft(20f)
            cell.setBorderBottomLeftRadius(BorderRadius(4f))
        }
        if (isLast) {
            cell.setBorderTopRightRadius(BorderRadius(4f))
            cell.setBorderBottomRightRadius(BorderRadius(4f))
        }
        return cell
    }

    fun createParagraph(value: String, textAligment: TextAlignment = TextAlignment.LEFT, fontSize: Float = 10f, fontColor: Color = ColorConstants.BLACK) {
        document.add(Paragraph(value).setTextAlignment(textAligment).setFont(font).setFontSize(fontSize).setFontColor(fontColor))
    }

    fun createParagraph(vararg value: Text) {
        val paragraph = Paragraph()
        paragraph.setMarginTop(16f)
        value.forEach {
            it.setFont(font)
            paragraph.add(it)
        }
        document.add(paragraph)
    }

    fun createParagraphAndBgColor(value: String, textAligment: TextAlignment = TextAlignment.LEFT, fontSize: Float = 12f, bgColor: Color = DeviceRgb(244, 249, 255)) {
        document.add(Paragraph(value).setTextAlignment(textAligment).setFont(font).setFontSize(fontSize).setBackgroundColor(bgColor))
    }

    fun createLine() {
        val solidLine = SolidLine(0.5f)
        solidLine.color = ColorConstants.GRAY
        val line = LineSeparator(solidLine).setMarginTop(5f)
        // 将分割线添加至文档
        document.add(line)
    }

    fun add(element: IBlockElement) {
        document.add(element)
    }

    fun createText(text: String, size: Float, textColor: Color): Text {
        return Text(text).setFont(font).setFontSize(size).setFontColor(textColor)
    }

    fun addImage(img: Image) {
        document.add(img)
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

    fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}