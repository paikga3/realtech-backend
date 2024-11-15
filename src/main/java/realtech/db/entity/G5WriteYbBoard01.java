package realtech.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the g5_write_yb_board01 database table.
 * 
 */
@Entity
@Table(name="g5_write_yb_board01")
@NamedQuery(name="G5WriteYbBoard01.findAll", query="SELECT g FROM G5WriteYbBoard01 g")
public class G5WriteYbBoard01 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="wr_id")
	private int wrId;

	@Column(name="ca_name")
	private String caName;

	@Column(name="mb_id")
	private String mbId;

	@Column(name="wr_1")
	private String wr1;

	@Column(name="wr_10")
	private String wr10;

	@Column(name="wr_2")
	private String wr2;

	@Column(name="wr_3")
	private String wr3;

	@Column(name="wr_4")
	private String wr4;

	@Column(name="wr_5")
	private String wr5;

	@Column(name="wr_6")
	private String wr6;

	@Column(name="wr_7")
	private String wr7;

	@Column(name="wr_8")
	private String wr8;

	@Column(name="wr_9")
	private String wr9;

	@Column(name="wr_comment")
	private int wrComment;

	@Column(name="wr_comment_reply")
	private String wrCommentReply;

	@Lob
	@Column(name="wr_content")
	private String wrContent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="wr_datetime")
	private Date wrDatetime;

	@Column(name="wr_email")
	private String wrEmail;

	@Column(name="wr_facebook_user")
	private String wrFacebookUser;

	@Column(name="wr_file")
	private byte wrFile;

	@Column(name="wr_good")
	private int wrGood;

	@Column(name="wr_hit")
	private int wrHit;

	@Column(name="wr_homepage")
	private String wrHomepage;

	@Column(name="wr_ip")
	private String wrIp;

	@Column(name="wr_is_comment")
	private byte wrIsComment;

	@Column(name="wr_last")
	private String wrLast;

	@Lob
	@Column(name="wr_link1")
	private String wrLink1;

	@Column(name="wr_link1_hit")
	private int wrLink1Hit;

	@Lob
	@Column(name="wr_link2")
	private String wrLink2;

	@Column(name="wr_link2_hit")
	private int wrLink2Hit;

	@Column(name="wr_name")
	private String wrName;

	@Column(name="wr_nogood")
	private int wrNogood;

	@Column(name="wr_num")
	private int wrNum;

	@Column(name="wr_option")
	private String wrOption;

	@Column(name="wr_parent")
	private int wrParent;

	@Column(name="wr_password")
	private String wrPassword;

	@Column(name="wr_reply")
	private String wrReply;

	@Column(name="wr_seo_title")
	private String wrSeoTitle;

	@Column(name="wr_subject")
	private String wrSubject;

	@Column(name="wr_twitter_user")
	private String wrTwitterUser;

	public G5WriteYbBoard01() {
	}

	public int getWrId() {
		return this.wrId;
	}

	public void setWrId(int wrId) {
		this.wrId = wrId;
	}

	public String getCaName() {
		return this.caName;
	}

	public void setCaName(String caName) {
		this.caName = caName;
	}

	public String getMbId() {
		return this.mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	public String getWr1() {
		return this.wr1;
	}

	public void setWr1(String wr1) {
		this.wr1 = wr1;
	}

	public String getWr10() {
		return this.wr10;
	}

	public void setWr10(String wr10) {
		this.wr10 = wr10;
	}

	public String getWr2() {
		return this.wr2;
	}

	public void setWr2(String wr2) {
		this.wr2 = wr2;
	}

	public String getWr3() {
		return this.wr3;
	}

	public void setWr3(String wr3) {
		this.wr3 = wr3;
	}

	public String getWr4() {
		return this.wr4;
	}

	public void setWr4(String wr4) {
		this.wr4 = wr4;
	}

	public String getWr5() {
		return this.wr5;
	}

	public void setWr5(String wr5) {
		this.wr5 = wr5;
	}

	public String getWr6() {
		return this.wr6;
	}

	public void setWr6(String wr6) {
		this.wr6 = wr6;
	}

	public String getWr7() {
		return this.wr7;
	}

	public void setWr7(String wr7) {
		this.wr7 = wr7;
	}

	public String getWr8() {
		return this.wr8;
	}

	public void setWr8(String wr8) {
		this.wr8 = wr8;
	}

	public String getWr9() {
		return this.wr9;
	}

	public void setWr9(String wr9) {
		this.wr9 = wr9;
	}

	public int getWrComment() {
		return this.wrComment;
	}

	public void setWrComment(int wrComment) {
		this.wrComment = wrComment;
	}

	public String getWrCommentReply() {
		return this.wrCommentReply;
	}

	public void setWrCommentReply(String wrCommentReply) {
		this.wrCommentReply = wrCommentReply;
	}

	public String getWrContent() {
		return this.wrContent;
	}

	public void setWrContent(String wrContent) {
		this.wrContent = wrContent;
	}

	public Date getWrDatetime() {
		return this.wrDatetime;
	}

	public void setWrDatetime(Date wrDatetime) {
		this.wrDatetime = wrDatetime;
	}

	public String getWrEmail() {
		return this.wrEmail;
	}

	public void setWrEmail(String wrEmail) {
		this.wrEmail = wrEmail;
	}

	public String getWrFacebookUser() {
		return this.wrFacebookUser;
	}

	public void setWrFacebookUser(String wrFacebookUser) {
		this.wrFacebookUser = wrFacebookUser;
	}

	public byte getWrFile() {
		return this.wrFile;
	}

	public void setWrFile(byte wrFile) {
		this.wrFile = wrFile;
	}

	public int getWrGood() {
		return this.wrGood;
	}

	public void setWrGood(int wrGood) {
		this.wrGood = wrGood;
	}

	public int getWrHit() {
		return this.wrHit;
	}

	public void setWrHit(int wrHit) {
		this.wrHit = wrHit;
	}

	public String getWrHomepage() {
		return this.wrHomepage;
	}

	public void setWrHomepage(String wrHomepage) {
		this.wrHomepage = wrHomepage;
	}

	public String getWrIp() {
		return this.wrIp;
	}

	public void setWrIp(String wrIp) {
		this.wrIp = wrIp;
	}

	public byte getWrIsComment() {
		return this.wrIsComment;
	}

	public void setWrIsComment(byte wrIsComment) {
		this.wrIsComment = wrIsComment;
	}

	public String getWrLast() {
		return this.wrLast;
	}

	public void setWrLast(String wrLast) {
		this.wrLast = wrLast;
	}

	public String getWrLink1() {
		return this.wrLink1;
	}

	public void setWrLink1(String wrLink1) {
		this.wrLink1 = wrLink1;
	}

	public int getWrLink1Hit() {
		return this.wrLink1Hit;
	}

	public void setWrLink1Hit(int wrLink1Hit) {
		this.wrLink1Hit = wrLink1Hit;
	}

	public String getWrLink2() {
		return this.wrLink2;
	}

	public void setWrLink2(String wrLink2) {
		this.wrLink2 = wrLink2;
	}

	public int getWrLink2Hit() {
		return this.wrLink2Hit;
	}

	public void setWrLink2Hit(int wrLink2Hit) {
		this.wrLink2Hit = wrLink2Hit;
	}

	public String getWrName() {
		return this.wrName;
	}

	public void setWrName(String wrName) {
		this.wrName = wrName;
	}

	public int getWrNogood() {
		return this.wrNogood;
	}

	public void setWrNogood(int wrNogood) {
		this.wrNogood = wrNogood;
	}

	public int getWrNum() {
		return this.wrNum;
	}

	public void setWrNum(int wrNum) {
		this.wrNum = wrNum;
	}

	public Object getWrOption() {
		return this.wrOption;
	}

	public void setWrOption(String wrOption) {
		this.wrOption = wrOption;
	}

	public int getWrParent() {
		return this.wrParent;
	}

	public void setWrParent(int wrParent) {
		this.wrParent = wrParent;
	}

	public String getWrPassword() {
		return this.wrPassword;
	}

	public void setWrPassword(String wrPassword) {
		this.wrPassword = wrPassword;
	}

	public String getWrReply() {
		return this.wrReply;
	}

	public void setWrReply(String wrReply) {
		this.wrReply = wrReply;
	}

	public String getWrSeoTitle() {
		return this.wrSeoTitle;
	}

	public void setWrSeoTitle(String wrSeoTitle) {
		this.wrSeoTitle = wrSeoTitle;
	}

	public String getWrSubject() {
		return this.wrSubject;
	}

	public void setWrSubject(String wrSubject) {
		this.wrSubject = wrSubject;
	}

	public String getWrTwitterUser() {
		return this.wrTwitterUser;
	}

	public void setWrTwitterUser(String wrTwitterUser) {
		this.wrTwitterUser = wrTwitterUser;
	}

}