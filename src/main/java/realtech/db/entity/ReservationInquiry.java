package realtech.db.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the reservation_inquiry database table.
 * 
 */
@Entity
@Table(name="reservation_inquiry")
@NamedQuery(name="ReservationInquiry.findAll", query="SELECT r FROM ReservationInquiry r")
public class ReservationInquiry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="inquiry_id")
	private int inquiryId;

	@Lob
	@Column(name="additional_info")
	private String additionalInfo;

	@Column(name="address_basic")
	private String addressBasic;

	@Column(name="address_detail")
	private String addressDetail;

	@Column(name="author_ip")
	private String authorIp;

	@Column(name="bracket_type")
	private int bracketType;

	@Column(name="braket_type_other")
	private String braketTypeOther;

	private String contact;

	@Column(name="created_at")
	private String createdAt;

	@Column(name="installation_date")
	private String installationDate;

	@Column(name="is_private")
	private int isPrivate;

	private String name;

	private String password;

	@Column(name="postal_code")
	private String postalCode;

	@Column(name="settop_box_embed")
	private int settopBoxEmbed;

	@Column(name="tv_size")
	private int tvSize;

	@Column(name="tv_size_other")
	private String tvSizeOther;

	private int views;

	@Column(name="wall_type")
	private int wallType;

	@Column(name="wall_type_other")
	private String wallTypeOther;

	public ReservationInquiry() {
	}

	public int getInquiryId() {
		return this.inquiryId;
	}

	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}

	public String getAdditionalInfo() {
		return this.additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getAddressBasic() {
		return this.addressBasic;
	}

	public void setAddressBasic(String addressBasic) {
		this.addressBasic = addressBasic;
	}

	public String getAddressDetail() {
		return this.addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getAuthorIp() {
		return this.authorIp;
	}

	public void setAuthorIp(String authorIp) {
		this.authorIp = authorIp;
	}

	public int getBracketType() {
		return this.bracketType;
	}

	public void setBracketType(int bracketType) {
		this.bracketType = bracketType;
	}

	public String getBraketTypeOther() {
		return this.braketTypeOther;
	}

	public void setBraketTypeOther(String braketTypeOther) {
		this.braketTypeOther = braketTypeOther;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getInstallationDate() {
		return this.installationDate;
	}

	public void setInstallationDate(String installationDate) {
		this.installationDate = installationDate;
	}

	public int getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getSettopBoxEmbed() {
		return this.settopBoxEmbed;
	}

	public void setSettopBoxEmbed(int settopBoxEmbed) {
		this.settopBoxEmbed = settopBoxEmbed;
	}

	public int getTvSize() {
		return this.tvSize;
	}

	public void setTvSize(int tvSize) {
		this.tvSize = tvSize;
	}

	public String getTvSizeOther() {
		return this.tvSizeOther;
	}

	public void setTvSizeOther(String tvSizeOther) {
		this.tvSizeOther = tvSizeOther;
	}

	public int getViews() {
		return this.views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getWallType() {
		return this.wallType;
	}

	public void setWallType(int wallType) {
		this.wallType = wallType;
	}

	public String getWallTypeOther() {
		return this.wallTypeOther;
	}

	public void setWallTypeOther(String wallTypeOther) {
		this.wallTypeOther = wallTypeOther;
	}

}