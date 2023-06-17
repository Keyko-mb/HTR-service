package ru.shafikova.Text.Recognition.Services;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

import leadtools.document.writer.*;
import leadtools.ocr.*;
import leadtools.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class RecognitionService {
    public String recognize(MultipartFile file) {
        try {
            Platform.setLibPath("E:\\Program Files\\LEADTOOLS22\\Bin\\CDLL\\x64");
            Platform.loadLibrary(LTLibrary.LEADTOOLS);
            Platform.loadLibrary(LTLibrary.OCR);
            SetLicense();
            log.info("License is confirmed");
            return RunICR(file);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            log.error("Failed to recognize {}", ex.getMessage());
            return null;
        }
    }

    public void SetLicense() throws IOException {
        String licenseFile = "E:\\Program Files\\LEADTOOLS22\\Support\\Common\\License\\LEADTOOLS.LIC";
        String developerKey = Files.readString(Paths.get(licenseFile + ".key"));
        RasterSupport.setLicense(licenseFile, developerKey);
    }

    public String RunICR(MultipartFile inputFile) throws IOException {
        String fileFormat = inputFile.getContentType().split("/")[1];
        String _file = "C:\\Users\\shafi\\Downloads\\Text Recognition\\Text-Recognition\\files\\file." + fileFormat;
        File file = new File(_file);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(inputFile.getBytes());
        fileOutputStream.close();

        OcrEngine _ocrEngine = OcrEngineManager.createEngine(OcrEngineType.LEAD);
        _ocrEngine.startup(null, null, null, "E:\\Program Files\\LEADTOOLS22\\Bin\\Common\\OcrLEADRuntime");

        // Create an OCR document
        OcrDocument _ocrDocument = _ocrEngine.getDocumentManager().createDocument();
        ILeadStream _fileStream = new LeadFileStream(_file);
        OcrPage _ocrPage = _ocrDocument.getPages().addPage(_fileStream, null);
        _ocrPage.autoZone(null);

        for (int i = 0; i < _ocrPage.getZones().size(); i++) {
            OcrZone _zone = _ocrPage.getZones().get(i);
            _zone.setZoneType(OcrZoneType.ICR);
        }

        _ocrPage.recognize(null);

        _ocrDocument.save("C:\\Users\\\\shafi\\Downloads\\Text Recognition\\Text-Recognition\\files\\icr.txt", DocumentFormat.TEXT, null);

        _ocrDocument.dispose();
        _ocrEngine.dispose();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\\\shafi\\Downloads\\Text Recognition\\Text-Recognition\\files\\icr.txt"));
        StringBuilder recognizedText = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
            recognizedText.append(line);

        try (Stream<Path> pathStream = Files.walk(Path.of("C:\\Users\\\\shafi\\Downloads\\Text Recognition\\Text-Recognition\\files"))) {
            pathStream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        log.info("Successful recognition");
        return recognizedText.toString();
    }

}
