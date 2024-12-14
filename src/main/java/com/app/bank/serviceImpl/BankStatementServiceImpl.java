package com.app.bank.serviceImpl;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.dto.EmailDetails;
import com.app.bank.entity.BankStatement;
import com.app.bank.entity.Transaction;
import com.app.bank.entity.User;
import com.app.bank.repository.BankStatementRepository;
import com.app.bank.repository.TransactionRepository;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.BankStatementService;
import com.app.bank.service.EmailService;
import com.app.bank.utility.BankResponse;
import com.app.bank.utility.BankStatementResponse;
import com.app.bank.utility.TransactionType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BankStatementServiceImpl implements BankStatementService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    private BigDecimal totalAmountSpent = BigDecimal.ZERO;

    private BigDecimal totalAmountCredited = BigDecimal.ZERO;

    private final String rupeeSymbol = "Rs. ";

    @Override
    public BankResponse generateStatement(BankStatementDto bankStatementDto) {

        LocalDateTime start = LocalDate.parse(bankStatementDto.getStartDate(), DateTimeFormatter.ISO_DATE).atTime(0,0,0);
        LocalDateTime end = LocalDate.parse(bankStatementDto.getEndDate(), DateTimeFormatter.ISO_DATE).atTime(23,59,59);

        List<Transaction> transactions = transactionRepository.findAllByAccountNumberOrderByCreatedAtDesc(bankStatementDto.getAccountNumber())
                .stream()
                .filter(transaction -> transaction.getCreatedAt().isAfter(start.minusDays(1)))
                .filter(transaction -> transaction.getCreatedAt().isBefore(end.plusDays(1))).toList();

        if(transactions.isEmpty()){
            return BankResponse.builder()
                    .responseCode(BankStatementResponse.NO_TRANSACTIONS_FOUND.getCode())
                    .responseMessage(BankStatementResponse.NO_TRANSACTIONS_FOUND.getMessage())
                    .build();
        }

        User user = userRepository.findByAccountNumber(bankStatementDto.getAccountNumber());

        designStatement(transactions, bankStatementDto,user);

        return BankResponse.builder()
                .responseCode(BankStatementResponse.STATEMENT_GENERATED_SUCCESSFULLY.getCode())
                .responseMessage(BankStatementResponse.STATEMENT_GENERATED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public ResponseEntity<List<BankStatement>> getAllBankStatement(String accountNumber) {
        return ResponseEntity.ok(bankStatementRepository.findAllByAccountNumberOrderByCreatedOnDesc(accountNumber));
    }

    @Override
    public ResponseEntity<byte[]> downloadBankStatement(String accountNumber,Long id) throws SQLException {

        Optional<byte[]> pdfFile = bankStatementRepository.getPdfFileByIdAndAccountNumber(id,accountNumber);
        HttpHeaders headers = new HttpHeaders();
        byte[] fileContent = null;
        if(pdfFile.isPresent()){
            fileContent = pdfFile.get();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename(accountNumber+"_"+LocalDateTime.now()).build());
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(new byte[0], headers, HttpStatus.OK);
    }

    private void designStatement(List<Transaction> transactions,BankStatementDto bankStatementDto, User user) {
        Rectangle documentSize = new Rectangle(PageSize.A4);
        Document document = new Document(documentSize);

        String customerName = user.getFirstName()+" "+user.getMiddleName()+" "+user.getLastName();
        String customerAddress = user.getAddressLine1()+" "+user.getAddressLine2()+" "+user.getCity()+" "+
                user.getStateOfOrigin()+" "+user.getPinCode()+" "+user.getCountry();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_HH_mm"); // Format: date_month_hours_minutes
        String timestamp = now.format(formatter);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Construct the file name with the timestamp
        String fileName = user.getAccountNumber().substring(0, 6) + "_" + timestamp + "_statement.pdf";

        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            PdfPTable bankInfoTable = new PdfPTable(1);
            bankInfoTable.setWidthPercentage(100);
            PdfPCell bankName = new PdfPCell(new Phrase("Banking System", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.WHITE)));
            bankName.setBorder(Rectangle.NO_BORDER);
            bankName.setBackgroundColor(BaseColor.DARK_GRAY);
            bankName.setPadding(10f);
            bankName.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell bankAddress = new PdfPCell(new Phrase("82, Address, India", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK)));
            bankAddress.setBorder(Rectangle.NO_BORDER);
            bankAddress.setPaddingTop(5f);
            bankAddress.setHorizontalAlignment(Element.ALIGN_CENTER);

            bankInfoTable.addCell(bankName);
            bankInfoTable.addCell(bankAddress);

            document.add(bankInfoTable);

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 10);

            // Title
            Paragraph statementTitle = new Paragraph("STATEMENT OF ACCOUNT", titleFont);
            statementTitle.setAlignment(Element.ALIGN_CENTER);
            statementTitle.setSpacingAfter(20f);
            document.add(statementTitle);

            // Start Date
            Paragraph startDate = new Paragraph("Start Date: " + bankStatementDto.getStartDate(), textFont);
            document.add(startDate);

            // End Date
            Paragraph endDate = new Paragraph("End Date: " + bankStatementDto.getEndDate(), textFont);
            document.add(endDate);

            // Customer Name
            Paragraph customerInfo = new Paragraph("Customer Name: " + customerName, textFont);
            customerInfo.setSpacingBefore(10f);
            document.add(customerInfo);

            Paragraph accountInfo = new Paragraph("Account Number: " + user.getAccountNumber(), textFont);
            document.add(accountInfo);

            // Address
            Paragraph addressInfo = new Paragraph("Address: " + customerAddress, textFont);
            document.add(addressInfo);

            PdfPTable transactionTable = new PdfPTable(4);
            transactionTable.setWidthPercentage(100); // Table spans full width
            transactionTable.setSpacingBefore(20f); // Adds space before the table
            transactionTable.setSpacingAfter(20f); // Adds space after the table

            // Set column widths for better spacing
            transactionTable.setWidths(new float[]{2f, 4f, 3f, 2f});

            // Header Cells
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

            PdfPCell dateHeader = new PdfPCell(new Phrase("Date", headerFont));
            dateHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            dateHeader.setBorder(Rectangle.NO_BORDER);
            dateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            dateHeader.setPadding(8f);

            PdfPCell transactionTypeHeader = new PdfPCell(new Phrase("Transaction Type", headerFont));
            transactionTypeHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            transactionTypeHeader.setBorder(Rectangle.NO_BORDER);
            transactionTypeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionTypeHeader.setPadding(8f);

            PdfPCell transactionAmountHeader = new PdfPCell(new Phrase("Transaction Amount", headerFont));
            transactionAmountHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            transactionAmountHeader.setBorder(Rectangle.NO_BORDER);
            transactionAmountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionAmountHeader.setPadding(8f);

            PdfPCell statusHeader = new PdfPCell(new Phrase("Status", headerFont));
            statusHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            statusHeader.setBorder(Rectangle.NO_BORDER);
            statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            statusHeader.setPadding(8f);

            // Add headers to the table
            transactionTable.addCell(dateHeader);
            transactionTable.addCell(transactionTypeHeader);
            transactionTable.addCell(transactionAmountHeader);
            transactionTable.addCell(statusHeader);

            // Body Cells
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

            transactions.forEach(transaction -> {
                PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().format(dateFormatter), bodyFont));
                dateCell.setBorder(Rectangle.BOTTOM);
                dateCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
                dateCell.setPadding(5f);

                PdfPCell transactionTypeCell = new PdfPCell(new Phrase(transaction.getTransactionType(), bodyFont));
                transactionTypeCell.setBorder(Rectangle.BOTTOM);
                transactionTypeCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
                transactionTypeCell.setPadding(5f);

                PdfPCell transactionAmountCell = new PdfPCell(new Phrase(rupeeSymbol+transaction.getAmount().toString(), bodyFont));
                transactionAmountCell.setBorder(Rectangle.BOTTOM);
                transactionAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
                transactionAmountCell.setPadding(5f);

                if(!transaction.getTransactionType().equals(TransactionType.DEPOSIT.toString())){
                    totalAmountSpent = totalAmountSpent.add(transaction.getAmount());
                } else{
                    totalAmountCredited = totalAmountCredited.add(transaction.getAmount());
                }

                PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus(), bodyFont));
                statusCell.setBorder(Rectangle.BOTTOM);
                statusCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
                statusCell.setPadding(5f);

                // Add cells to the table
                transactionTable.addCell(dateCell);
                transactionTable.addCell(transactionTypeCell);
                transactionTable.addCell(transactionAmountCell);
                transactionTable.addCell(statusCell);
            });

            document.add(transactionTable);

            PdfPTable transactionSummaryTable = new PdfPTable(4);
            transactionSummaryTable.setWidthPercentage(100); // Table spans full width
            transactionSummaryTable.setSpacingBefore(20f); // Adds space before the table
            transactionSummaryTable.setSpacingAfter(20f); // Adds space after the table

            // Set column widths for better spacing
            transactionSummaryTable.setWidths(new float[]{2f, 4f, 3f, 2f});

            PdfPCell periodHeader = new PdfPCell(new Phrase("Period", headerFont));
            periodHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            periodHeader.setBorder(Rectangle.NO_BORDER);
            periodHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            periodHeader.setPadding(8f);

            PdfPCell amountSpentHeader = new PdfPCell(new Phrase("Total Amount Spent", headerFont));
            amountSpentHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            amountSpentHeader.setBorder(Rectangle.NO_BORDER);
            amountSpentHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountSpentHeader.setPadding(8f);


            PdfPCell amountCreditHeader = new PdfPCell(new Phrase("Total Amount Credit", headerFont));
            amountCreditHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            amountCreditHeader.setBorder(Rectangle.NO_BORDER);
            amountCreditHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCreditHeader.setPadding(8f);

            PdfPCell currentBalanceHeader = new PdfPCell(new Phrase("Current Balance", headerFont));
            currentBalanceHeader.setBackgroundColor(BaseColor.DARK_GRAY);
            currentBalanceHeader.setBorder(Rectangle.NO_BORDER);
            currentBalanceHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            currentBalanceHeader.setPadding(8f);

            transactionSummaryTable.addCell(periodHeader);
            transactionSummaryTable.addCell(amountSpentHeader);
            transactionSummaryTable.addCell(amountCreditHeader);
            transactionSummaryTable.addCell(currentBalanceHeader);

            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            PdfPCell periodCell = new PdfPCell(new Phrase(
                    String.valueOf(ChronoUnit.DAYS.between(
                            LocalDate.parse(bankStatementDto.getStartDate(),formatterDate),
                            LocalDate.parse(bankStatementDto.getEndDate(),formatterDate)
                    )) + " Day(s)"
                    , bodyFont));
            periodCell.setBorder(Rectangle.BOTTOM);
            periodCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
            periodCell.setPadding(5f);

            PdfPCell amountSpentCell = new PdfPCell(new Phrase(rupeeSymbol+totalAmountSpent.toString(), bodyFont));
            amountSpentCell.setBorder(Rectangle.BOTTOM);
            amountSpentCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
            amountSpentCell.setPadding(5f);

            PdfPCell amountCreditCell = new PdfPCell(new Phrase(rupeeSymbol+totalAmountCredited.toString(), bodyFont));
            amountCreditCell.setBorder(Rectangle.BOTTOM);
            amountCreditCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
            amountCreditCell.setPadding(5f);

            PdfPCell accountBalanceCell = new PdfPCell(new Phrase(rupeeSymbol+user.getAccountBalance().toString(), bodyFont));
            accountBalanceCell.setBorder(Rectangle.BOTTOM);
            accountBalanceCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Align center
            accountBalanceCell.setPadding(5f);

            transactionSummaryTable.addCell(periodCell);
            transactionSummaryTable.addCell(amountSpentCell);
            transactionSummaryTable.addCell(amountCreditCell);
            transactionSummaryTable.addCell(accountBalanceCell);

            document.add(transactionSummaryTable);

            document.close();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(user.getEmail())
                    .subject("STATEMENT")
                    .message(
                            "Dear "+customerName+ ",\n\n" +
                                    "I hope this email finds you well. Please find attached your latest bank statement for your reference.\n\n" +
                                    "If you have any questions or need further assistance, feel free to reach out.\n\n" +
                                    "Best regards,\n" +
                                    "Banking System\n\n" +
                                    "This is a system generated mail, please do not reply."
                    )
                    .attachment(byteArrayOutputStream.toByteArray())
                    .attachmentName(fileName)
                    .build();

            BankStatement bankStatement = BankStatement.builder()
                    .customerName(customerName)
                    .accountNumber(user.getAccountNumber())
                    .pdfFile(byteArrayOutputStream.toByteArray())
                    .createdOn(LocalDateTime.now())
                    .build();

            bankStatementRepository.save(bankStatement);

            emailService.sendBankStatementViaEmail(emailDetails);

        } catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
