package realtech.db.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the notice database table.
 * 
 */
@Entity
@NamedQuery(name="Notice.findAll", query="SELECT n FROM Notice n")
public class Notice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="notice_id")
	private int noticeId;

	@Column(name="author_ip")
	private String authorIp;

	@Column(name="author_name")
	private String authorName;

	@Lob
	private String content;

	@Column(name="created_at")
	private String createdAt;

	@Column(name="edited_at")
	private String editedAt;

	@Column(name="editor_ip")
	private String editorIp;

	@Column(name="editor_name")
	private String editorName;

	private String title;

	public Notice() {
	}

	public int getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}

	public String getAuthorIp() {
		return this.authorIp;
	}

	public void setAuthorIp(String authorIp) {
		this.authorIp = authorIp;
	}

	public String getAuthorName() {
		return this.authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getEditedAt() {
		return this.editedAt;
	}

	public void setEditedAt(String editedAt) {
		this.editedAt = editedAt;
	}

	public String getEditorIp() {
		return this.editorIp;
	}

	public void setEditorIp(String editorIp) {
		this.editorIp = editorIp;
	}

	public String getEditorName() {
		return this.editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}