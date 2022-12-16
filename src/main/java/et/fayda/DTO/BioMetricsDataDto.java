package et.fayda.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class BioMetricsDataDto {

	public String getDigitalId() {
		return digitalId;
	}

	public void setDigitalId(String digitalId) {
		this.digitalId = digitalId;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceServiceVersion() {
		return deviceServiceVersion;
	}

	public void setDeviceServiceVersion(String deviceServiceVersion) {
		this.deviceServiceVersion = deviceServiceVersion;
	}

	public String getBioType() {
		return bioType;
	}

	public void setBioType(String bioType) {
		this.bioType = bioType;
	}

	public String getBioSubType() {
		return bioSubType;
	}

	public void setBioSubType(String bioSubType) {
		this.bioSubType = bioSubType;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getDomainUri() {
		return domainUri;
	}

	public void setDomainUri(String domainUri) {
		this.domainUri = domainUri;
	}

	public String getBioValue() {
		return bioValue;
	}

	public void setBioValue(String bioValue) {
		this.bioValue = bioValue;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRequestedScore() {
		return requestedScore;
	}

	public void setRequestedScore(String requestedScore) {
		this.requestedScore = requestedScore;
	}

	public String getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(String qualityScore) {
		this.qualityScore = qualityScore;
	}

	public String digitalId;
	public String deviceCode;
	public String deviceServiceVersion;
	public String bioType;
	public String bioSubType;
	public String purpose;
	public String env;
	public String domainUri;
	public String bioValue;
//	public String bioExtract;
//	public String registrationId;
	public String transactionId;
	public String timestamp;
	public String requestedScore;
	public String qualityScore;
	@JsonIgnore
	public Image capturedImage;
	@JsonIgnore
	public DigitalId digitalIdObj;

	@JsonIgnore
	public byte[] imageByteArray;

}
