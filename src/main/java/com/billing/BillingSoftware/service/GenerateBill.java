package com.billing.BillingSoftware.service;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.*;

import org.apache.pdfbox.pdmodel.font.PDType1Font;


import org.springframework.stereotype.Service;

import com.billing.BillingSoftware.DTO.BillReportDTO;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

@Service
public class GenerateBill{

    public byte[] generateBillPDF(List<BillReportDTO> billItems, double gstPercentage, double discountPercentage)
            throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                Map<Long, List<BillReportDTO>> bills = billItems.stream()
                        .collect(Collectors.groupingBy(BillReportDTO::getBillno));

                float yPosition = 700;
                final float leftMargin = 50;
                final float rightMargin = 550;

                for (Map.Entry<Long, List<BillReportDTO>> entry : bills.entrySet()) {
                    List<BillReportDTO> items = entry.getValue();
                    BillReportDTO firstItem = items.get(0);

                    // ✅ Bill Header
                    try {
                        contentStream.beginText(); // Start text block
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.newLineAtOffset(leftMargin, yPosition);
                        contentStream.showText("Bill No: " + firstItem.getBillno());
                    } finally {
                        contentStream.endText(); // Ensure endText() is always called
                    }

                    // Phone (center)
                    try {
                        contentStream.beginText(); // Start text block
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        float phoneX = (page.getMediaBox().getWidth() - 100) / 2;
                        contentStream.newLineAtOffset(phoneX, yPosition);
                        contentStream.showText("Phone: " + firstItem.getPhone());
                    } finally {
                        contentStream.endText(); // Ensure endText() is always called
                    }

                    // Date (right)
                    try {
                        contentStream.beginText(); // Start text block
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        String dateStr = "Date: " + firstItem.getDate().toString();
                        float dateWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(dateStr) / 1000 * 12;
                        contentStream.newLineAtOffset(rightMargin - dateWidth, yPosition);
                        contentStream.showText(dateStr);
                    } finally {
                        contentStream.endText(); // Ensure endText() is always called
                    }

                    yPosition -= 30;

                    // ✅ Table Header
                    try {
                        contentStream.beginText(); // Start text block
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.newLineAtOffset(leftMargin, yPosition);
                        contentStream.showText("Item");
                        contentStream.newLineAtOffset(200, 0);
                        contentStream.showText("MRP");
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText("Qty");
                        contentStream.newLineAtOffset(50, 0);
                        contentStream.showText("Total");
                    } finally {
                        contentStream.endText(); // Ensure endText() is always called
                    }

                    yPosition -= 20;

                    // ✅ Table Rows
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    double subtotal = 0;
                    for (BillReportDTO item : items) {
                        try {
                            contentStream.beginText(); // Start text block
                            contentStream.newLineAtOffset(leftMargin, yPosition);
                            contentStream.showText(item.getItem());
                            contentStream.newLineAtOffset(200, 0);
                            contentStream.showText(String.format("%.2f", item.getMrp()));
                            contentStream.newLineAtOffset(80, 0);
                            contentStream.showText(item.getQuantity().toString());
                            contentStream.newLineAtOffset(50, 0);
                            contentStream.showText(String.format("%.2f", item.getTotal()));
                        } finally {
                            contentStream.endText(); // Ensure endText() is always called
                        }

                        subtotal += item.getTotal();
                        yPosition -= 15;
                    }

                    // ✅ GST & Discount Calculations
                    double gstAmount = (subtotal * gstPercentage) / 100;
                    double discountAmount = (subtotal * discountPercentage) / 100;
                    double grandTotal = subtotal + gstAmount - discountAmount;

                    yPosition -= 15;

                    // GST
                    try {
                        contentStream.beginText(); // Start text block
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.newLineAtOffset(rightMargin - 150, yPosition);
                        contentStream.showText(String.format("GST (%.2f%%):  %.2f", gstPercentage, gstAmount));
                    } finally {
                        contentStream.endText(); // Ensure endText() is always called
                    }

                    yPosition -= 15;

                    // Discount
                    try {
                        contentStream.beginText(); // Start text block
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.newLineAtOffset(rightMargin - 150, yPosition);
                        contentStream.showText(String.format("Discount (%.2f%%):  %.2f", discountPercentage, discountAmount));
                    } finally {
                        contentStream.endText(); // Ensure endText() is always called
                    }

                    yPosition -= 15;

                    // ✅ Grand Total (Bold & Right-Aligned)
                    try {
                        contentStream.beginText(); // Start text block
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        String grandTotalStr = String.format("Grand Total:  %.2f", grandTotal);
                        float totalWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(grandTotalStr) / 1000 * 12;
                        contentStream.newLineAtOffset(rightMargin - totalWidth, yPosition);
                        contentStream.showText(grandTotalStr);
                    } finally {
                        contentStream.endText(); // Ensure endText() is always called
                    }

                    yPosition -= 40;  // Space between bills
                }
                contentStream.close();
            }
            doc.save(out);
            
        }
        return out.toByteArray();
    }
}
