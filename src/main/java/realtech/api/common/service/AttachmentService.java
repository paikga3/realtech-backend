package realtech.api.common.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.db.entity.Attachment;
import realtech.db.repository.AttachmentRepository;
import realtech.util.AppUtils;

@Service
public class AttachmentService {
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private S3Service s3Service;
    
    public Attachment getAttachmentById(int attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found with ID: " + attachmentId));
        return attachment;
    }
    
    public InputStream downloadAttachment(Attachment attachment) {
        return s3Service.downloadFile("realtech-board", AppUtils.extractPathUsingString(attachment.getS3Filename()));
    }
}
