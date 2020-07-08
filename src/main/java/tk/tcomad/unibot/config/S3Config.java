package tk.tcomad.unibot.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class S3Config {

  @Value("${aws.accessKey:#{null}}")
  private String accessKey;
  @Value("${aws.secretKey:#{null}}")
  private String secretKey;
  @Getter
  @Value("${aws.bucketName:#{null}}")
  private String bucketName;
  @Getter
  @Value("${aws.bucketAddress:#{null}}")
  private String bucketAddress;

  @Bean
  public AmazonS3 amazonS3() {
    if (StringUtils.hasText(accessKey)) {
      BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
      return AmazonS3ClientBuilder.standard()
          .withRegion(Regions.US_EAST_2)
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .build();
    } else {
      return null;
    }
  }

}
