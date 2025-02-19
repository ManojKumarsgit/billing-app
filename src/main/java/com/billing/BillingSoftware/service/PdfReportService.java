package com.billing.BillingSoftware.service;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.billing.BillingSoftware.DTO.SalesReportDTO;

//import com.billing.BillingSoftware.DTO.SalesReportDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfReportService {

	public byte[] generateSalesReportPDF(List<SalesReportDTO> salesReport) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        try (PDDocument doc = new PDDocument()) {
//            PDPage page = new PDPage();
//            doc.addPage(page);
//
//            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
//                // Set initial Y position
//                float yPosition = 700;
//                final float leftMargin = 50;
//                final float columnWidth = 100;
//
//                // Title
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(leftMargin, yPosition);
//                contentStream.showText("Sales Report");
//                contentStream.endText();
//                yPosition -= 30;
//
//                // Table Headers
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(leftMargin, yPosition);
//                contentStream.showText("S.No");
//                contentStream.newLineAtOffset(columnWidth, 0);
//                contentStream.showText("Bill No");
//                contentStream.newLineAtOffset(columnWidth, 0);
//                contentStream.showText("Total");
////                contentStream.newLineAtOffset(columnWidth, 0);
////                contentStream.showText("Grand Total");
//                contentStream.newLineAtOffset(columnWidth, 0);
//                contentStream.showText("Date");
//                contentStream.endText();
//                yPosition -= 20;
//
//                // Table Rows
//                contentStream.setFont(PDType1Font.HELVETICA, 10);
//                for (SalesReportDTO report : salesReport) {
//                    contentStream.beginText();
//                    contentStream.newLineAtOffset(leftMargin, yPosition);
//                    contentStream.showText(String.valueOf(report.getSno()));
//                    contentStream.newLineAtOffset(columnWidth, 0);
//                    contentStream.showText(String.valueOf(report.getBillno()));
//                    contentStream.newLineAtOffset(columnWidth, 0);
//                    contentStream.showText(String.format("%.2f", report.getTotal()));
//                    contentStream.newLineAtOffset(columnWidth, 0);
////                    contentStream.showText(String.format("%.2f", report.getGrandTotal()));
////                    contentStream.newLineAtOffset(columnWidth, 0);
//                    contentStream.showText(report.getDate().toString());
//                    contentStream.endText();
//                    yPosition -= 15;
//                }
//            }
//
//            // Save the document
//            doc.save(out);
//        }
//        return out.toByteArray();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (PDDocument doc = new PDDocument()) {
		    PDPage page = new PDPage();
		    doc.addPage(page);

		    try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
		        // Set initial Y position
		        float yPosition = 700;
		        final float leftMargin = 50;
		        final float columnWidth = 100;

		        // Title
		        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
		        contentStream.beginText();
		        contentStream.newLineAtOffset(leftMargin, yPosition);
		        contentStream.showText("Sales Report");
		        contentStream.endText();
		        yPosition -= 30;

		        // Table Headers
		        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
		        contentStream.beginText();
		        contentStream.newLineAtOffset(leftMargin, yPosition);
		        contentStream.showText("S.No");
		        contentStream.newLineAtOffset(columnWidth, 0);
		        contentStream.showText("Bill No");
		        contentStream.newLineAtOffset(columnWidth, 0);
		        contentStream.showText("Total");
		        contentStream.newLineAtOffset(columnWidth, 0);
		        contentStream.showText("Date");
		        contentStream.endText();
		        yPosition -= 20;

		        // Table Rows
		        contentStream.setFont(PDType1Font.HELVETICA, 10);

		        // Calculate Grand Total
		        double grandTotal = 0;

		        for (SalesReportDTO report : salesReport) {
		            contentStream.beginText();
		            contentStream.newLineAtOffset(leftMargin, yPosition);
		            contentStream.showText(String.valueOf(report.getSno()));
		            contentStream.newLineAtOffset(columnWidth, 0);
		            contentStream.showText(String.valueOf(report.getBillno()));
		            contentStream.newLineAtOffset(columnWidth, 0);
		            contentStream.showText(String.format("%.2f", report.getTotal()));
		            contentStream.newLineAtOffset(columnWidth, 0);
		            contentStream.showText(report.getDate().toString());
		            contentStream.endText();

		            // Add to Grand Total
		            grandTotal += report.getTotal();

		            // Move to the next line
		            yPosition -= 15;
		        }

		        // Display Grand Total
		        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
		        contentStream.beginText();
		        contentStream.newLineAtOffset(leftMargin + (2 * columnWidth), yPosition - 10); // Align with "Total" column
		        contentStream.showText("Grand Total: " + String.format("%.2f", grandTotal));
		        contentStream.endText();
		    }

		    // Save the document
		    doc.save(out);
		}
		return out.toByteArray();
    }
 }
