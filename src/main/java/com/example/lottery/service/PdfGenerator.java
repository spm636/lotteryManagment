package com.example.lottery.service;

import com.example.lottery.model.Result;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PdfGenerator {
private List<Result> results;
    public void generate(HttpServletResponse response) throws DocumentException, IOException{
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        Paragraph paragraph = new Paragraph("Result", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3,3,3,3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Coupen Number", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Type", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Count", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Totel Amount", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Place", font));
        table.addCell(cell);
        for(Result res:results){
            table.addCell(String.valueOf(res.getTicketDetails().getCoupenNumber()));
            table.addCell(String.valueOf(res.getTicketDetails().getType()));
            table.addCell(String.valueOf(res.getTicketDetails().getCount()));

            table.addCell(String.valueOf(res.getTotelPrice()));
            table.addCell(String.valueOf(res.getPlace()));


        }
        document.add(table);
        document.close();
    }
}
