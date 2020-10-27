package tk.tcomad.unibot.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.tcomad.unibot.payload.UploadFileResponse;
import tk.tcomad.unibot.service.S3FileStorageService;

@RestController
@CrossOrigin
@AllArgsConstructor
public class FileController {

    private final S3FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        return ResponseEntity.status(HttpStatus.OK).body(
                new UploadFileResponse(fileName));
    }

}
