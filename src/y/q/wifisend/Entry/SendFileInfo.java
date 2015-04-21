package y.q.wifisend.Entry;

import android.net.Uri;

/**
 * Created by CFun on 2015/4/21.
 */
public class SendFileInfo
{
    Uri file;
    float sendPercent;
    int sendStatu = SendStatus.WAITTING;
    FileType fileType = FileType.Other;

    public Uri getFile() {
        return file;
    }

    public void setFile(Uri file) {
        this.file = file;
    }

    public float getSendPercent() {
        return sendPercent;
    }

    public void setSendPercent(float sendPercent) {
        this.sendPercent = sendPercent;
    }

    public int getSendStatu() {
        return sendStatu;
    }

    public void setSendStatu(int sendStatu) {
        this.sendStatu = sendStatu;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
