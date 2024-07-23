package com.example.lottery.service;

import com.example.lottery.dto.DailyResult;
import com.example.lottery.dto.DailyTicketReport;
import com.example.lottery.model.Result;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PdfReport {
    private List<DailyTicketReport> reports;
    private List<Result> results;
    private List<DailyResult> dailyResults;
    private Double totelCoupenAmount;
    private Double totelPrize;
    private Double profit;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        Font fontTiltle2 = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle2.setSize(17);
        Paragraph paragraph = new Paragraph("Result and Report", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        LocalDate date = null;
        String ticket = null;
        for (Result res : results) {
            date = res.getTicketDetails().getDate();
            ticket = res.getTicket().getTicket();
        }
        Paragraph paragraph1 = new Paragraph(String.valueOf(date), fontTiltle2);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph1);



        Paragraph paragraph2 = new Paragraph(String.valueOf(ticket), fontTiltle2);
        paragraph2.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph2);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(60f);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[]{3, 3, 3});
        table.setSpacingBefore(5);
        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.pink);
        cell.setPadding(5);


        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.BLACK);

        cell.setPhrase(new Phrase("Coupon Type", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Count", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
        for (DailyTicketReport report : reports) {
            table.addCell(String.valueOf(report.getCoupenType()));
            table.addCell(String.valueOf(report.getTotelCount()));
            table.addCell(String.valueOf(report.getAmount()));
        }
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total Amount", font));
        totalLabelCell.setColspan(2); // Spanning 2 columns for the label
        table.addCell(totalLabelCell);



        PdfPCell totalAmountCell = new PdfPCell(new Phrase(String.valueOf(totelCoupenAmount), font));
        table.addCell(totalAmountCell);
        document.add(table);
        PdfPTable resultTable=new PdfPTable(2);
        resultTable.setWidthPercentage(50f);
        resultTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        resultTable.setWidths(new int[]{3,3});
        resultTable.setSpacingBefore(5);
        PdfPCell pdfPCell=new PdfPCell();
        pdfPCell.setBackgroundColor(CMYKColor.PINK);
        pdfPCell.setPadding(5);
        pdfPCell.setPhrase(new Phrase("Prize",font));
        resultTable.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Prize Amount",font));
        resultTable.addCell(pdfPCell);
        for(DailyResult res:dailyResults){
            resultTable.addCell(String.valueOf(res.getPrize()));
            resultTable.addCell(String.valueOf(res.getPrizeAmout()));
        }
        PdfPCell prizeCell=new PdfPCell(new Phrase("Total Prize amount",font));
        resultTable.addCell(prizeCell);
        PdfPCell prizAmount=new PdfPCell(new Phrase(String.valueOf(totelPrize),font));
        resultTable.addCell(prizAmount);
        document.add(resultTable);

        Font fontTiltle3 = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle3.setSize(20);
        Paragraph paragraph3=new Paragraph("Profit:"+String.valueOf(profit),fontTiltle3);
       paragraph3.setAlignment(Element.ALIGN_BASELINE);
        document.add(paragraph3);


        PdfPTable result=new PdfPTable(4);
        result.setWidthPercentage(100f);
        result.setHorizontalAlignment(Element.ALIGN_CENTER);
        result.setWidths(new int[]{3,3,3,3});
        result.setSpacingBefore(5);
        PdfPCell PCell=new PdfPCell();
        PCell.setBackgroundColor(CMYKColor.pink);
        PCell.setPadding(5);
        PCell.setPhrase(new Phrase("Coupon Number",font));
        result.addCell(PCell);
        PCell.setPhrase(new Phrase("Prize",font));
        result.addCell(PCell);
        PCell.setPhrase(new Phrase("Count",font));
        result.addCell(PCell);
        PCell.setPhrase(new Phrase("Amount",font));
        result.addCell(PCell);

        for(Result res:results){
            result.addCell(String.valueOf(res.getTicketDetails().getCoupenNumber()));
            result.addCell(String.valueOf(res.getPlace()));
            result.addCell(String.valueOf(res.getTicketDetails().getCount()));
            result.addCell(String.valueOf(res.getTotelPrice()));

        }
        document.add(result);

        document.close();
    }
}
