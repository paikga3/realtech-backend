package realtech.db.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the attachment database table.
 * 
 */
@Entity
@Table(name="attachment")
@NamedQuery(name="Attachment.findAll", query="SELECT a FROM Attachment a")
public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="attachment_id")
	private int attachmentId;

	@Column(name="display_filename")
	private String displayFilename;

	@Column(name="file_size_kb")
	private BigDecimal fileSizeKb;

	@Column(name="ref_id")
	private int refId;

	@Column(name="ref_table")
	private String refTable;

	@Column(name="s3_filename")
	private String s3Filename;

	public Attachment() {
	}

	public int getAttachmentId() {
		return this.attachmentId;
	}

	public void setAttachmentId(int attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getDisplayFilename() {
		return this.displayFilename;
	}

	public void setDisplayFilename(String displayFilename) {
		this.displayFilename = displayFilename;
	}

	public BigDecimal getFileSizeKb() {
		return this.fileSizeKb;
	}

	public void setFileSizeKb(BigDecimal fileSizeKb) {
		this.fileSizeKb = fileSizeKb;
	}

	public int getRefId() {
		return this.refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

	public String getRefTable() {
		return this.refTable;
	}

	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}

	public String getS3Filename() {
		return this.s3Filename;
	}

	public void setS3Filename(String s3Filename) {
		this.s3Filename = s3Filename;
	}

}