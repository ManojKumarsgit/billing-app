package com.billing.BillingSoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.billing.BillingSoftware.DTO.SalesReportDTO;
import com.billing.BillingSoftware.model.User;
import com.billing.BillingSoftware.repository.OrderRepository;
import com.billing.BillingSoftware.service.PdfReportService;
import com.billing.BillingSoftware.service.ReportService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class ReportController {

	@Autowired
    private  ReportService reportService;
	
	@Autowired
	private PdfReportService pdfReportService;
	
	@Autowired
	private OrderRepository salesRepository;
    
//    @GetMapping
//    public String viewReportsPage() {
//        return "reports";
//    }
	 @GetMapping
	    public String showReports(@SessionAttribute(name = "loggedInUser", required = false) User loggedInUser, Model model) {
	        if (loggedInUser == null) {
	            return "redirect:/auth/login";
	        }
	        model.addAttribute("role", loggedInUser.getRole());
	        return "reports";
	    }

    @PostMapping("/sales")
    public String generateSalesReport(@RequestParam String startDate, @RequestParam String endDate, Model model,@SessionAttribute(name = "loggedInUser", required = false) User loggedInUser) {
    	 if (loggedInUser == null) {
	            return "redirect:/auth/login";
	        }
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<SalesReportDTO> reports = salesRepository.getSalesReport(start, end).stream()
        	    .map(obj -> new SalesReportDTO(
        	        ((Number) obj[0]).intValue(), // ROW_NUMBER
        	        ((Number) obj[1]).longValue(), // billNo
        	        ((Number) obj[2]).doubleValue(), // total
        	        ((Number) obj[3]).doubleValue(), // grandTotal
        	        ((java.sql.Date) obj[4]).toLocalDate() // billingDate
        	    ))
        	    .collect(Collectors.toList());
        model.addAttribute("reports", reports);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("role", loggedInUser.getRole());
        return "sales-report";
    }
    @PostMapping("/sales/pdf")
    public ResponseEntity<byte[]> generateSalesReportPdf(@RequestParam String startDate, @RequestParam String endDate) throws IOException {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        List<SalesReportDTO> salesReport = salesRepository.getSalesReport(start, end).stream()
        	    .map(obj -> new SalesReportDTO(
        	        ((Number) obj[0]).intValue(), // ROW_NUMBER
        	        ((Number) obj[1]).longValue(), // billNo
        	        ((Number) obj[2]).doubleValue(), // total
        	        ((Number) obj[3]).doubleValue(), // grandTotal
        	        ((java.sql.Date) obj[4]).toLocalDate() // billingDate
        	    ))
        	    .collect(Collectors.toList());
        
        String printDate = startDate.substring(5,startDate.length())+" | "+ endDate.substring(5,endDate.length());

        byte[] pdfBytes = pdfReportService.generateSalesReportPDF(salesReport);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+printDate+".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
