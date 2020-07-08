package tk.tcomad.unibot.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
public class UploadFileResponse {
    @Getter
    @Setter
    @NonNull
    private String fileDownloadUri;
}
