package com.pdfmerge;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentMerger {

    private static Logger logger = Logger.getLogger(DocumentMerger.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.ALL);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter path for documents:");
        try {
            String docPath = br.readLine();
            while (docPath == null || "".equals(docPath)) {
                logger.severe("Path cannot be empty!!");
                System.out.println("Enter path for documents:");
                docPath = br.readLine();
            }

            File docPathDir = new File(docPath);

            if (!docPathDir.exists() || !docPathDir.isDirectory())
                throw new AppException("Invalid Path !!");

            File[] files = docPathDir.listFiles();
            if (files == null || files.length == 0)
                throw new AppException("No files !!");
            else if (files.length == 1)
                throw new AppException("Only one file !!");

            PDFMergerUtility merger = new PDFMergerUtility();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            merger.setDestinationStream(os);
            for (File f : files){
                if (!f.isDirectory())
                    merger.addSource(f);
            }
            merger.mergeDocuments(MemoryUsageSetting.setupMixed(-1, -1));

            File outputDir = new File(docPath.concat("/output"));
            if (!outputDir.exists() || !outputDir.isDirectory())
                outputDir.mkdir();

            FileOutputStream fout = new FileOutputStream(outputDir.getAbsolutePath()+"/merge.pdf");
            fout.write(os.toByteArray());
            fout.close();

            logger.info("Documents merged");

        } catch (IOException e) {
            logger.severe(e.getMessage());
        } catch (AppException apex){
            logger.severe(apex.getMessage());
        }
    }
}

class AppException extends Exception{
    public AppException(String msg){
        super(msg);
    }
}
