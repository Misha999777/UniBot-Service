package tk.tcomad.unibot.service;

import java.io.IOException;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.tcomad.unibot.config.S3Config;
import tk.tcomad.unibot.exception.FileStorageException;

@Service
@NoArgsConstructor
public class S3FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(S3FileStorageService.class);

    private AmazonS3 amazonS3;
    private String bucketName;
    private String bucketAddress;

    @Autowired(required = false)
    public S3FileStorageService(AmazonS3 amazonS3, S3Config s3Config) {
        this.amazonS3 = amazonS3;
        this.bucketName = s3Config.getBucketName();
        this.bucketAddress = s3Config.getBucketAddress();
    }

    public String storeFile(MultipartFile file) {
        if (amazonS3 == null) {
            log.error("Trying to store file to S3 without specifying credentials");
            throw new FileStorageException("Trying to store file to S3 without specifying credentials");
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "." + extension);
        ObjectMetadata md = new ObjectMetadata();
        md.setContentLength(file.getSize());
        md.setContentType(file.getContentType());

        try {
            PutObjectRequest put = new PutObjectRequest(this.bucketName, fileName, file.getInputStream(), md);
            amazonS3.putObject(put);
            log.info("Stored {} to S3", fileName);
            return this.bucketAddress + "/" + fileName;
        } catch (IOException e) {
            log.error("Can't store file. Reason: {}", e.getMessage());
            throw new FileStorageException("Internal error storing file");
        }
    }
}
