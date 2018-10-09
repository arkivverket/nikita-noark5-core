package nikita.common.util;

import javax.validation.constraints.NotNull;

public class FileExtensionAndMimeType {

    private String mimeType;
    private String fileExtension;

    public FileExtensionAndMimeType(@NotNull String mimeType,
                                    @NotNull String fileExtension) {
        this.mimeType = mimeType;
        this.fileExtension = fileExtension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
