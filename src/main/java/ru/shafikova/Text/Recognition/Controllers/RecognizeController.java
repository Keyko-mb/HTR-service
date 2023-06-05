package ru.shafikova.Text.Recognition.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shafikova.Text.Recognition.Services.RecognitionService;

@RestController
@RequestMapping("/recognize")
public class RecognizeController {

    private final RecognitionService recognitionService;

    @Autowired
    public RecognizeController(RecognitionService recognitionService) {
        this.recognitionService = recognitionService;
    }

    @PostMapping()
    public String put(@RequestParam("file") MultipartFile file) {
        return recognitionService.recognize(file);
    }
}
