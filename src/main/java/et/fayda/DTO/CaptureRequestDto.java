package et.fayda.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CaptureRequestDto {

	public String env;
	public String purpose;
	public String specVersion="0.9.5";
	public int timeout;
	@JsonIgnore
	public String domainUri;
	@JsonIgnore
	public String captureTime;
	public String transactionId;

	@JsonProperty("bio")
	public List<CaptureRequestDeviceDetailDto> bio;

	@JsonIgnore
	public List<Map<String, String>> customOpts;

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getSpecVersion() {
		return specVersion;
	}

	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getDomainUri() {
		return domainUri;
	}

	public void setDomainUri(String domainUri) {
		this.domainUri = domainUri;
	}

	public String getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(String captureTime) {
		this.captureTime = captureTime;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public List<CaptureRequestDeviceDetailDto> getBio() {
		return bio;
	}

	public void setBio(List<CaptureRequestDeviceDetailDto> bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getCustomOpts() {
		return customOpts;
	}

	public void setCustomOpts(List<Map<String, String>> customOpts) {
		this.customOpts = customOpts;
	}
}
