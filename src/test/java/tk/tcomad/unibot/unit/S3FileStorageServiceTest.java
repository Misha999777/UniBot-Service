package tk.tcomad.unibot.unit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;
import tk.tcomad.unibot.config.S3Config;
import tk.tcomad.unibot.service.S3FileStorageService;

public class S3FileStorageServiceTest {

  private static final String BUCKET_NAME = "testBucketName";
  private static final String BUCKET_ADDRESS = "testBucketAddress";
  private static final String TEST_DATA = "TestData";

  private S3FileStorageService s3FileStorageService;
  private AmazonS3 amazonS3;

  @Before
  public void setup() {
    amazonS3 = mock(AmazonS3.class);
    S3Config s3Config = new S3Config();
    ReflectionTestUtils.setField(s3Config, "bucketName", BUCKET_NAME);
    ReflectionTestUtils.setField(s3Config, "bucketAddress", BUCKET_ADDRESS);
    s3FileStorageService = new S3FileStorageService(amazonS3, s3Config);
  }

  @Test
  public void shouldStoreFile() throws IOException {
    MockMultipartFile mockFile = new MockMultipartFile(TEST_DATA, TEST_DATA.getBytes());

    ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor
        .forClass(PutObjectRequest.class);
    String url = s3FileStorageService.storeFile(mockFile);
    Mockito.verify(amazonS3, times(1)).putObject(captor.capture());

    assertArrayEquals(captor.getValue().getInputStream().readAllBytes(),
        mockFile.getInputStream().readAllBytes());
    assertEquals(BUCKET_NAME, captor.getValue().getBucketName());
    assertEquals(mockFile.getContentType(),
        captor.getValue().getMetadata().getContentType());
    assertEquals(mockFile.getSize(),
        captor.getValue().getMetadata().getContentLength());
    assertTrue(StringUtils.hasText(captor.getValue().getKey()));
    assertTrue(url.contains(BUCKET_ADDRESS + "/"));
    assertTrue(url.contains(captor.getValue().getKey()));
  }

}