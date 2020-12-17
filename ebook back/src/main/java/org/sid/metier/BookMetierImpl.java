package org.sid.metier;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.sid.dao.BookRepository;
import org.sid.entities.BookApp;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

@Service
@Transactional
public class BookMetierImpl implements BookMetier {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AccountService accountService;
    String current = System.getProperty("user.dir");

    @Override
    public void uploadFile(String authName, String desc,MultipartFile file) {
        String nom = file.getOriginalFilename().substring(0,file.getOriginalFilename().length()-4);

        /*String pdfFileInText = "";
        //String img=current+File.separator+image;
        try {
            PDDocument document = PDDocument.load(new File(current+File.separator+nom));

                if (!document.isEncrypted()) {
                    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                    stripper.setSortByPosition(true);
                    PDFTextStripper tStripper = new PDFTextStripper();
                    pdfFileInText = tStripper.getText(document);
                    //System.out.println(pdfFileInText);
                }
            BookApp book=new BookApp(nom,"text/plain", pdfFileInText.getBytes());
            bookRepository.save(book);

        }
        catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            BookApp bookApp = new BookApp(nom,file.getBytes(),authName,desc,"no");
            bookRepository.save(bookApp);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + nom + ". Please try again!", ex);
        }
    }

    String home = System.getProperty("user.home");
    @Override
    public BookApp downloadFile(int id){
         BookApp bookApp= bookRepository.findById(id);
         String nom=bookApp.getBookName();
        try {
            //ecrire un fichier text, le convertir en pdf et supprimer le fichier text car on veut que le pdf
            FileOutputStream outputStream = new FileOutputStream(home+File.separator+"Downloads"+ File.separator+nom+".txt");
            outputStream.write(bookApp.getData());
            //pdf convertion
            Document pdfDoc = new Document(PageSize.A4);
            PdfWriter.getInstance(pdfDoc, new FileOutputStream(home+File.separator+"Downloads"+ File.separator+nom+".pdf"));
            pdfDoc.open();
            Font myfont = new Font();
            myfont.setStyle(Font.NORMAL);
            myfont.setSize(11);
            pdfDoc.add(new Paragraph("\n"));
            BufferedReader br = new BufferedReader(new FileReader(home+File.separator+"Downloads"+ File.separator+nom+".txt"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Paragraph para = new Paragraph(strLine + "\n", myfont);
                para.setAlignment(Element.ALIGN_JUSTIFIED);
                pdfDoc.add(para);
            }
            pdfDoc.close();
            br.close();
            outputStream.close();//il faut close le file pour le supprimer si nn ca marchera pas la suppression
            //delte text file
            File fileToDelete = new File(home + File.separator + "Downloads" + File.separator +nom+ ".txt");
            fileToDelete.delete();
        }
        catch (IOException | DocumentException ex) {
            throw new RuntimeException("Could not download file " + bookApp.getBookName() + ". Please try again!", ex);
        }
        return bookApp;
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public ArrayList<BookApp> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public ArrayList<BookApp> favoriteColor(String username) {
        ArrayList<BookApp> books=findAll();
        ArrayList<BookApp> favBooks=accountService.findBooksByUserName(username);
        books.removeAll(favBooks);
        for (BookApp b:favBooks) {

            b.setFavorite("yes");
        }
            for (BookApp c:books){
                c.setFavorite("no");

        }
        books.addAll(favBooks);
        books.sort(Comparator.comparing(BookApp::getId).reversed());
        return books;
    }
}
