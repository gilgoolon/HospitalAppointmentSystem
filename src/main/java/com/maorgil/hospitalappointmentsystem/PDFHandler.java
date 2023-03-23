package com.maorgil.hospitalappointmentsystem;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.RoundDotsBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFHandler {
    private static final String AUTHOR = "Hospital Appointment System";
    private final static String DOC_DOESNT_EXIST = "Document does not exist";

    private Document document; // current document to work on
    private final String fileName; // name of the file to export to
    private final String filePath; // path of the file to export to

    public PDFHandler(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void createDocument() {
        try {
            if (document == null) {
                // create file
                Files.createDirectories(Paths.get(filePath));
                // current writer to work on
                PdfWriter pdfWriter = new PdfWriter(filePath + fileName);
                // create document
                // current pdf document to work on
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                document = new Document(pdfDocument, PageSize.A4);
                // set margins
                document.setMargins(50, 50, 50, 50);
                // set author
                document.getPdfDocument().getDocumentInfo().setAuthor(AUTHOR);
            }
            else throw new IllegalStateException("Document already exists");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create document", e);
        }
    }

    public void closeDocument() {
        if (document != null) {
            document.close();
            document = null;
        }
        else throw new IllegalStateException(DOC_DOESNT_EXIST);
    }

    public void addTitle(String s) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        // add bold title
        Paragraph title = new Paragraph(new Text(s).setBold().setFontSize(30f));

        // make bigger
        document.add(title);
    }

    public void addDate(Date d) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        // add date
        Paragraph date = new Paragraph(new Text(new SimpleDateFormat("dd/MM/yyyy").format(d))
                .setFontSize(14f))
                .setBorder(new RoundDotsBorder(1))
                .setPadding(3f);

        // set fixed position at top left
        date.setFixedPosition(50, 800, 75);

        document.add(date);
    }

    public void addPublisher(String s) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        // add publisher
        Paragraph publisher = new Paragraph(new Text(s)
                .setFontSize(18f))
                .setBorder(new SolidBorder(2))
                .setPaddingLeft(5)
                .setPaddingRight(5);

        // set fixed position at top right
        publisher.setFixedPosition(PageSize.A4.getWidth() - 300, 800, 300 - 50 - 5*2);

        document.add(publisher);
    }

    public void addSubTitle(String s) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        // add bold title
        Paragraph title = new Paragraph(new Text(s)
                .setBold()
                .setFontSize(20f)
                .setUnderline());

        // add spacing after title
        title.setMarginBottom(5f);

        document.add(title);
    }

    public void addInfoTable(int cols, String... fields) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        Table table = new Table(cols);
        // set width of table to entire page
        table.setWidth(PageSize.A4.getWidth() - 100);
        // set spacing before and after table
        table.setMarginTop(20);
        table.setMarginBottom(20);

        int i = 0;
        for (String field : fields) {
            Paragraph paragraph = new Paragraph(field);
            paragraph.setBorder(new SolidBorder(1));

            if ((i++ / cols) % 2 == 0) // field is a field name
                paragraph.setBold();
            else
                paragraph.setBackgroundColor(ColorConstants.LIGHT_GRAY);

            table.addCell(paragraph);
        }

        document.add(table);
    }

    /**
     * Add a big outlined text box to the document.
     * @param text - text to add to the box
     */
    public void addBigTextBox(String text) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        Paragraph paragraph = new Paragraph(text)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY, 5, 5, 5, 5)
                .setBorder(new SolidBorder(1))
                .setHeight(200f)
                .setWidth(PageSize.A4.getWidth() - 100)
                .setMarginBottom(10f);

        document.add(paragraph);
    }

    public void addParagraph(String text, boolean isItalic) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        Paragraph paragraph = new Paragraph(text);
        if (isItalic) paragraph.setItalic();

        document.add(paragraph);
    }

    /**
     * Set the footer of the document.
     * @param footer - text to add to the footer
     */
    public void setFooter(String footer) {
        if (document == null) throw new IllegalStateException(DOC_DOESNT_EXIST);

        // add the footer to the bottom of the page
        class FooterEventHandler implements IEventHandler {
            @Override
            public void handleEvent(Event event) {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfDocument pdf = docEvent.getDocument();
                PdfPage page = docEvent.getPage();

                Rectangle pageSize = page.getPageSize();
                PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);

                //noinspection resource
                new Canvas(pdfCanvas, pageSize)
                             .showTextAligned(new Paragraph(footer)
                                             .setFontSize(14)
                                             .setTextAlignment(TextAlignment.CENTER)
                                             .setBackgroundColor(ColorConstants.LIGHT_GRAY, 0, 20, 0, 20),
                                     pageSize.getWidth() / 2, 14*Utils.count(footer, "\n") + 20, pdf.getNumberOfPages(),
                                     TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
            }
        }

        document.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler());
    }
}
