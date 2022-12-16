package et.fayda.Utils;

import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.DateUtils;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;



public class JwtUtility {

	//TODO Need to be implement using properties
	//@Value("${mosip.kernel.crypto.sign-algorithm-name:RS256}")
	private static String signAlgorithm="RS256";
	private static String AUTH_REQ_TEMPLATE = "{ \"id\": \"string\",\"metadata\": {},\"request\": { \"appId\": \"%s\", \"clientId\": \"%s\", \"secretKey\": \"%s\" }, \"requesttime\": \"%s\", \"version\": \"string\"}";


	public static String getJwt(byte[] data, PrivateKey privateKey, X509Certificate x509Certificate) {
		String jwsToken = null;
		JsonWebSignature jws = new JsonWebSignature();

		if(x509Certificate != null) {
			List<X509Certificate> certList = new ArrayList<>();
			certList.add(x509Certificate);
			X509Certificate[] certArray = certList.toArray(new X509Certificate[] {});
			jws.setCertificateChainHeaderValue(certArray);
		}

		jws.setPayloadBytes(data);
		jws.setAlgorithmHeaderValue(signAlgorithm);
		jws.setKey(privateKey);
		jws.setDoKeyValidation(false);
		try {
			jwsToken = jws.getCompactSerialization();
		} catch (JoseException e) {
			e.printStackTrace();
		}
		return jwsToken;

	}

	public static X509Certificate getCertificate() {

		try {
			FileInputStream certfis = new FileInputStream(

					new File(System.getProperty("user.dir") + "/files/keys/MosipTestCert.pem").getPath());

			String cert = getFileContent(certfis, "UTF-8");

			cert = trimBeginEnd(cert);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(cert)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static PrivateKey getPrivateKey() {
		try {
			FileInputStream pkeyfis = new FileInputStream(
					new File(System.getProperty("user.dir") + "/files/keys/PrivateKey.pem").getPath());

			String pKey = getFileContent(pkeyfis, "UTF-8");
			pKey = trimBeginEnd(pKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pKey)));
		} catch (Exception ex) {
			ex.printStackTrace();
			//throw new Exception("Failed to get private key");
		}
		return null;
	}

	public static PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		FileInputStream pkeyfis = new FileInputStream(
				new File(System.getProperty("user.dir") + "/files/keys/PublicKey.pem").getPath());
		String pKey = getFileContent(pkeyfis, "UTF-8");
		pKey = trimBeginEnd(pKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		return (PublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pKey)));

	}

	/**
	 * Gets the file content.
	 *
	 * @param fis      the fis
	 * @param encoding the encoding
	 * @return the file content
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getFileContent(FileInputStream fis, String encoding) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			return sb.toString();
		}
	}

	public PublicKey getPublicKeyToEncryptCaptureBioValue() throws Exception {
		String certificate = getPublicKeyFromIDA();
		certificate = trimBeginEnd(certificate);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate x509Certificate = (X509Certificate) cf.generateCertificate(
				new ByteArrayInputStream(Base64.getDecoder().decode(certificate)));

		return x509Certificate.getPublicKey();
	}

	public String getThumbprint() throws Exception {
		String certificate = getPublicKeyFromIDA();
		certificate = trimBeginEnd(certificate);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate x509Certificate = (X509Certificate) cf.generateCertificate(
				new ByteArrayInputStream(Base64.getDecoder().decode(certificate)));
		String thumbprint = CryptoUtil.computeFingerPrint(x509Certificate.getEncoded(), null);

		return thumbprint;
	}

	public String getPublicKeyFromIDA() {
		// read the public key from file

		String cert = "-----BEGIN CERTIFICATE-----\n" +
				"MIIDkDCCAnigAwIBAgIIOrwi5taD+14wDQYJKoZIhvcNAQELBQAwbTELMAkGA1UE\n" +
				"BhMCSU4xCzAJBgNVBAgMAktBMRIwEAYDVQQHDAlCQU5HQUxPUkUxDTALBgNVBAoM\n" +
				"BElJVEIxGjAYBgNVBAsMEU1PU0lQLVRFQ0gtQ0VOVEVSMRIwEAYDVQQDDAlNT1NJ\n" +
				"UC1JREEwHhcNMjIwNzI1MTE1NjAyWhcNMjQwNzI0MTE1NjAyWjBvMQswCQYDVQQG\n" +
				"EwJJTjELMAkGA1UECAwCS0ExEjAQBgNVBAcMCUJBTkdBTE9SRTENMAsGA1UECgwE\n" +
				"SUlUQjEaMBgGA1UECwwRTU9TSVAtVEVDSC1DRU5URVIxFDASBgNVBAMMC0lEQS1J\n" +
				"REEtRklSMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1cKEhNX72XPJ\n" +
				"kMvotPQdlFehia/3GRjtv9qy/E2f97PR4o2+SeFXK8ZtZA7DLVk4GZWQQi9sCiDN\n" +
				"nS8uAsxsEm+7OuUkKCn7txiJYoiqHyipcTtQT37hH+mMWHcXfYc811+FMW/okUXU\n" +
				"prBwZQ3BkqM2kbGFKeWNBxh0gzbdU91kJh+ybZSsJKraLxxmt1nVugqloo1o/otU\n" +
				"Cv+VeHNEMCAeCqxFC/QVZOoFcNUZilbWcg1eV22WA6RyUgN7+bldmHad/mt7hInJ\n" +
				"pqaz4rRnMxlXZ8meZ22LSIJ/uFKWqWazpka4d32k6Zoau6+eboW03stJ025FBnjQ\n" +
				"1Ry1j0LcKQIDAQABozIwMDAPBgNVHRMBAf8EBTADAQH/MB0GA1UdDgQWBBTf1D2V\n" +
				"E7PXFgGYY60FHAP7bNflpDANBgkqhkiG9w0BAQsFAAOCAQEAZi1lIfO3adSiW5R8\n" +
				"cwzDlJbqW6t0bc+VS21RqtFcApdYRwNA+OvvFvsZDLZjo3wgA5n3/LUuFHercacm\n" +
				"0JBg0i6ZW/orGk7MyAamfzfJ77pRrrYWfIkSdQ6TpYJ9+C5lYJ+c4vVaAEvvWvIo\n" +
				"IaWUfDT/0FG7tRSr4KDiF8ZpokyCC/yQtreEbsvn0M7MADA4dFWR3spDdYOky3VK\n" +
				"GoTeb5o7bl3hpksS2dj39Pza9tcwythiljuIfY7tIJ/PG1VmLADiExk/x/uidytZ\n" +
				"b+Pesz2pgTmC4hY8wVxxL0bFTAzgY+/waQjlihYBmtK0VetWDN6/tOmWeSUTe22Z\n" +
				"SD+sLg==\n" +
				"-----END CERTIFICATE-----";

		return cert;
	}

	private static String trimBeginEnd(String pKey) {
		pKey = pKey.replaceAll("-*BEGIN([^-]*)-*(\r?\n)?", "");
		pKey = pKey.replaceAll("-*END([^-]*)-*(\r?\n)?", "");
		pKey = pKey.replaceAll("\\s", "");
		return pKey;
	}

}