package et.fayda.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptureRequestDeviceDetailDto {

	private String type;
	//public String[] bioSubType;
	private int count;
	private String[] exception;
	private String[] bioSubType;
	private int requestedScore;
	private String deviceId;
	private String deviceSubId;
	private String previousHash;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String[] getException() {
		return exception;
	}

	public void setException(String[] exception) {
		this.exception = exception;
	}

	public String[] getBioSubType() {
		return bioSubType;
	}

	public void setBioSubType(String[] bioSubType) {
		this.bioSubType = bioSubType;
	}

	public int getRequestedScore() {
		return requestedScore;
	}

	public void setRequestedScore(int requestedScore) {
		this.requestedScore = requestedScore;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceSubId() {
		return deviceSubId;
	}

	public void setDeviceSubId(String deviceSubId) {
		this.deviceSubId = deviceSubId;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
}
