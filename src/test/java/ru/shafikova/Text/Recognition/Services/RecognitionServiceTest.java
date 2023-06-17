package ru.shafikova.Text.Recognition.Services;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RecognitionServiceTest {
    private final RecognitionService recognitionService = new RecognitionService();

    @Test
    void recognize() throws IOException {
        File fileBeforeRecognition = new File("src\\test\\resources\\demoicr2.tif");
        MultipartFile multipartFile = new MockMultipartFile("file", fileBeforeRecognition.getName(), "image/tiff", Files.readAllBytes(fileBeforeRecognition.toPath()));

        String textAfterRecognition = recognitionService.recognize(multipartFile);

        String expected = "LEAD TECHNOLOGIES IS THE WORLDLEAUIN@ SUPPLIER OE IMAGINGDeVELOPMeNT snxs. LEADTOOLS ISDESIGNED TQ HELP PMGRAMMERSINTEGRATe CQLQR, GRAYSCALE.DOCuMENT, ME0ICAL MULtIMEV1A,INTERNET, AND VECTOR IMAGING INTOTHEIR APPLICATIONS QUICKLY ANnEASILY. LEADS IMAGING TECHNOLOGYHAS BEEN CHGSEN BY MICROSOFT AP,1~rEL, BOEtNG XEROX, KQDAK, Eonsmoron COMPANY� Ago TEOQSANWS OOTHER CONPANIES FQR THEIR l-YIGHvoLumE APPLICATIONS AND INTERNALSYSTEMS.";

        boolean isRecognized = textAfterRecognition.equals(expected);

        assertThat(isRecognized).isTrue();
    }
}