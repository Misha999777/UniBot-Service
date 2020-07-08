package tk.tcomad.unibot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Can't store file")
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }
}
