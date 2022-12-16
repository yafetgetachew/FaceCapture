package et.fayda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import et.fayda.DTO.BioMetricsDataDto;
import et.fayda.DTO.CaptureRequestDeviceDetailDto;
import et.fayda.DTO.CaptureRequestDto;
import et.fayda.DTO.NewBioAuthDto;
import et.fayda.Utils.CryptoUtility;
import et.fayda.Utils.JwtUtility;
import io.mosip.kernel.core.util.HMACUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import org.json.JSONArray;
import org.json.JSONObject;

public class FaceCapture {
    private static final String HASH = "hash";
    private static ObjectMapper oB = null;

    public String getEncryptedFaceData(byte[] face, String transactionID) throws JsonProcessingException {

        BioMetricsDataDto bioDto = new BioMetricsDataDto();

        bioDto.setBioValue(Base64.getUrlEncoder().encodeToString(face));

        JSONArray result = null;

        try {
            result = doAuthCapture(transactionID, bioDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return oB.writeValueAsString(result);
    }


    public JSONArray doAuthCapture(String transactionID, BioMetricsDataDto bioDTO) throws Exception {

        List<BioMetricsDataDto> list = new ArrayList<>();
        list.add(bioDTO);
        String previousHash = HMACUtils.digestAsPlainText(HMACUtils.generateHash("".getBytes()));
        List<Map<String, Object>> listOfBiometric = new ArrayList<>();


        for (BioMetricsDataDto dto : list) {
            Map<String, String> result = CryptoUtility.encrypt(new JwtUtility().getPublicKeyToEncryptCaptureBioValue(),
                    dto.getBioValue(), transactionID);

            NewBioAuthDto data = buildAuthNewBioDto(dto, "FACE", 90,
                    transactionID, result);

            Map<String, Object> biometricData = getAuthMinimalResponse("1.0",
                    data, previousHash, result);

            listOfBiometric.add(biometricData);
            previousHash = (String) biometricData.get(HASH);

        }




        JSONArray jsonData = new JSONArray(listOfBiometric);



        return jsonData;
    }


    private Map<String, Object> getAuthMinimalResponse(String specVersion, NewBioAuthDto data, String previousHash,
                                                       Map<String, String> cryptoResult) {
        Map<String, Object> biometricData = new LinkedHashMap<>();
        try {
            biometricData.put("SPEC_VERSION", specVersion);
            String dataAsString = oB.writeValueAsString(data);
            String presentHash = HMACUtils.digestAsPlainText(HMACUtils.generateHash(dataAsString.getBytes(StandardCharsets.UTF_8)));
            String concatenatedHash = previousHash + presentHash;
            String finalHash = HMACUtils.digestAsPlainText(HMACUtils.generateHash(concatenatedHash.getBytes()));
            biometricData.put("HASH", finalHash);
            biometricData.put("SESSION_KEY", cryptoResult.get("ENC_SESSION_KEY"));
            biometricData.put("THUMB_PRINT", new JwtUtility().getThumbprint());
            biometricData.put("error", null);
            String dataBlock = JwtUtility.getJwt(dataAsString.getBytes(StandardCharsets.UTF_8), JwtUtility.getPrivateKey(),
                    JwtUtility.getCertificate());
            biometricData.put("DATA", dataBlock);

        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, String> map = new HashMap<String, String>();
            map.put("errorCode", "UNKNOWN");
            map.put("errorInfo", ex.getMessage());
            biometricData.put("error", map);
        }
        return biometricData;
    }





    private NewBioAuthDto buildAuthNewBioDto(BioMetricsDataDto bioMetricsData, String bioType, int requestedScore, String transactionId,
                                             Map<String, String> cryptoResult) throws Exception {

        NewBioAuthDto bioResponse = new NewBioAuthDto();
        bioResponse.setBioSubType(bioMetricsData.getBioSubType());
        bioResponse.setBioType(bioType);
        bioResponse.setDeviceCode(bioMetricsData.getDeviceCode());
        //TODO Device service version should be read from file
        bioResponse.setDeviceServiceVersion("MOSIP.MDS.001");
        bioResponse.setEnv(bioMetricsData.getEnv());
        //TODO - need to change, should handle based on deviceId
        bioResponse.setDigitalId(getDigitalId("FACE"));
        bioResponse.setPurpose(bioMetricsData.getPurpose());
        bioResponse.setRequestedScore(requestedScore);
        bioResponse.setQualityScore(bioMetricsData.getQualityScore());
        bioResponse.setTransactionId(transactionId);
        //TODO Domain URL need to be set
        bioResponse.setDomainUri("");

        bioResponse.setTimestamp(cryptoResult.get("TIMESTAMP"));
        bioResponse.setBioValue(cryptoResult.containsKey("ENC_DATA") ?
                cryptoResult.get("ENC_DATA") : null);
        return bioResponse;
    }
    public String getDigitalId(String moralityType) throws IOException {

        String digitalId = null;

        digitalId = getDigitalModality(oB.readValue(
                new String(Files.readAllBytes(
                        Paths.get( "DigitalFaceId.txt"))),
                Map.class));
        return digitalId;
    }
    private String getDigitalModality(Map<String, String> digitalIdMap) {

        String result = null;
        Map<String, String> digitalMap = new LinkedHashMap<>();
        digitalMap.put("dateTime", CryptoUtility.getTimestamp());
        digitalMap.put("deviceProvider", digitalIdMap.get("deviceProvider"));
        digitalMap.put("deviceProviderId", digitalIdMap.get("deviceProviderId"));
        digitalMap.put("make", digitalIdMap.get("make"));
        digitalMap.put("serialNo", digitalIdMap.get("serialNo"));
        digitalMap.put("model", digitalIdMap.get("model"));
        digitalMap.put("deviceSubType", digitalIdMap.get("deviceSubType"));
        digitalMap.put("type", digitalIdMap.get("type"));
        try {
            result = JwtUtility.getJwt(oB.writeValueAsBytes(digitalMap), JwtUtility.getPrivateKey(),
                    JwtUtility.getCertificate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}
