package com.app.bank.serviceImpl;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.entity.Transaction;
import com.app.bank.entity.User;
import com.app.bank.repository.TransactionRepository;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.BankStatementService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BankStatementServiceImpl implements BankStatementService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String FILE = "C:\\Users\\Lenovo\\Downloads\\BankStatements\\MyStatement.pdf";

    @Override
    public List<Transaction> generateStatement(BankStatementDto bankStatementDto) {

        LocalDate start = LocalDate.parse(bankStatementDto.getStartDate(), DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(bankStatementDto.getEndDate(), DateTimeFormatter.ISO_DATE);

        List<Transaction> transactions = transactionRepository.findAll()
                .stream().filter(transaction -> transaction.getAccountNumber().equals(bankStatementDto.getAccountNumber()))
                .filter(transaction -> transaction.getCreatedAt().equals(start))
                .filter(transaction -> transaction.getCreatedAt().equals(end)).toList();

        User user = userRepository.findByAccountNumber(bankStatementDto.getAccountNumber());

        designStatement(transactions, bankStatementDto,user);

        return transactions;
    }

    private void designStatement(List<Transaction> transactions,BankStatementDto bankStatementDto, User user) {
        Rectangle documentSize = new Rectangle(PageSize.A4);
        Document document = new Document(documentSize);

        String customerName = user.getFirstName()+" "+user.getMiddleName()+" "+user.getLastName();
        String customerAddress = user.getAddressLine1()+" "+user.getAddressLine2()+" "+user.getCity()+" "+
                user.getStateOfOrigin()+" "+user.getPinCode()+" "+user.getCountry();
        try{
            OutputStream outputStream = new FileOutputStream(FILE);

            PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfPTable bankInfoTable = new PdfPTable(1);
            PdfPCell bankName = new PdfPCell(new Phrase("Banking System"));
            bankName.setBorder(0);
            bankName.setBackgroundColor(BaseColor.BLUE);
            bankName.setPadding(20f);

            PdfPCell bankAddress = new PdfPCell(new Phrase("82, Address, India"));
            bankAddress.setBorder(0);

            bankInfoTable.addCell(bankName);
            bankInfoTable.addCell(bankAddress);

            PdfPTable statementInfo = new PdfPTable(2);
            PdfPCell startDate = new PdfPCell(new Phrase("Start Date: "+bankStatementDto.getStartDate()));
            startDate.setBorder(0);

            PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
            statement.setBorder(0);

            PdfPCell endDate = new PdfPCell(new Phrase("End Date: "+bankStatementDto.getEndDate()));
            endDate.setBorder(0);

            PdfPCell customerInfo = new PdfPCell(new Phrase("Customer Name: "+customerName));
            customerInfo.setBorder(0);

            PdfPCell space = new PdfPCell();

            PdfPCell addressCell = new PdfPCell(new Phrase("Address: "+customerAddress));
            addressCell.setBorder(0);

            PdfPTable transactionTable = new PdfPTable(4);

            PdfPCell dateHeader = new PdfPCell(new Phrase("DATE"));
            dateHeader.setBackgroundColor(BaseColor.BLUE);
            dateHeader.setBorder(0);

            PdfPCell transactionTypeHeader = new PdfPCell(new Phrase("TRANSACTION TYPE"));
            transactionTypeHeader.setBackgroundColor(BaseColor.BLUE);
            transactionTypeHeader.setBorder(0);

            PdfPCell transactionAmountHeader = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
            transactionAmountHeader.setBackgroundColor(BaseColor.BLUE);
            transactionAmountHeader.setBorder(0);

            PdfPCell statusHeader = new PdfPCell(new Phrase("STATUS"));
            statusHeader.setBackgroundColor(BaseColor.BLUE);
            statusHeader.setBorder(0);

            transactionTable.addCell(dateHeader);
            transactionTable.addCell(transactionTypeHeader);
            transactionTable.addCell(transactionAmountHeader);
            transactionTable.addCell(statusHeader);


            transactions.forEach(transaction ->{
                transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
                transactionTable.addCell(new Phrase(transaction.getTransactionType()));
                transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
                transactionTable.addCell(new Phrase(transaction.getStatus()));
            });

            statementInfo.addCell(startDate);
            statementInfo.addCell(endDate);
            statementInfo.addCell(statement);
            statementInfo.addCell(customerInfo);
            statementInfo.addCell(space);
            statementInfo.addCell(addressCell);

            document.add(bankInfoTable);
            document.add(statementInfo);
            document.add(transactionTable);

            document.close();

        } catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
