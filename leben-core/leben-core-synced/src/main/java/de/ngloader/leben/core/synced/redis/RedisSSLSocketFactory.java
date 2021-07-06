package de.ngloader.leben.core.synced.redis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.net.ssl.SSLSocketFactory;

import de.ngloader.leben.core.synced.LebenCoreConfig;
import io.netty.util.CharsetUtil;
import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.util.PemUtils;

public class RedisSSLSocketFactory {

	public static SSLSocketFactory createSocketFactory(LebenCoreConfig.RedisConfig config) throws Exception {
//		KeyStore keyStore = createKeyStore(config);
//
//		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
//		trustManagerFactory.init(keyStore);
//		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//
//		SSLContext sslContext = SSLContext.getInstance("TLS");
//		sslContext.init(null, trustManagers, new SecureRandom());
//		return sslContext.getSocketFactory();

		var keyManager = PemUtils.loadIdentityMaterial(Path.of(config.publicCrt), Path.of(config.privateKey));
		var trustManager = PemUtils.loadTrustMaterial(Path.of(config.pem));
		SSLFactory sslFactory = SSLFactory.builder()
				.withIdentityMaterial(keyManager)
				.withTrustMaterial(trustManager)
				.build();

		return sslFactory.getSslContext().getSocketFactory();
	}

	public static KeyStore createKeyStore(LebenCoreConfig.RedisConfig config) throws Exception {
		Certificate certificate = readCertifiacte(config.publicCrt);
		PrivateKey privateKey = readPrivateKey(config.privateKey);
		Certificate authCertificate = readCertifiacte(config.pem);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(null, null);
		keyStore.setKeyEntry("client-key", privateKey, new char[0], new Certificate[] { certificate });
		keyStore.setCertificateEntry("client-cert", certificate);
		keyStore.setCertificateEntry("ca-cert", authCertificate);
		return keyStore;
	}

	public static Certificate readCertifiacte(String file) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
		try (InputStream inputStream = Files.newInputStream(Path.of(file))) {
			return certificateFactory.generateCertificate(inputStream);
		}
	}

	public static PrivateKey readPrivateKey(String file) throws Exception {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(file), CharsetUtil.UTF_8)) {
			bufferedReader.lines().forEach(builder::append);
		}

		String privateKey = builder.toString()
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s", "");

		byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKey);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));
	}
}