package realtech.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the comment database table.
 * 
 */
@Entity
@Table(name="comment")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="comment_id")
	private int commentId;

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

	@Column(name="is_deleted")
	private int isDeleted;

	@Column(name="parent_comment_id")
	private int parentCommentId;

	@Column(name="ref_id")
	private int refId;

	@Column(name="ref_table")
	private String refTable;

	public Comment() {
	}

	public int getCommentId() {
		return this.commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
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

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getParentCommentId() {
		return this.parentCommentId;
	}

	public void setParentCommentId(int parentCommentId) {
		this.parentCommentId = parentCommentId;
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

}