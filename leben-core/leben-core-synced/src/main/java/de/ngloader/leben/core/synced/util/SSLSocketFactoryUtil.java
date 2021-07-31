package de.ngloader.leben.core.synced.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import de.ngloader.leben.core.synced.LebenCoreConfig;
import io.netty.util.CharsetUtil;

public class SSLSocketFactoryUtil {

	public static SSLSocketFactory createSocketFactory(LebenCoreConfig.SSLConfig config) throws Exception {
		KeyStore keyStore = createKeyStore(config);

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, new char[0]);

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
		trustManagerFactory.init(keyStore);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
		return sslContext.getSocketFactory();
	}

	public static KeyStore createKeyStore(LebenCoreConfig.SSLConfig config) throws Exception {
		PrivateKey privateKey = readPrivateKey(config.privateKey);
		Certificate certificate = readCertifiacte(config.publicCrt);
		Certificate authCertificate = readCertifiacte(config.caCrt);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(null, null);
		keyStore.setKeyEntry("private", privateKey, new char[0], new Certificate[] { certificate });
		keyStore.setCertificateEntry("public", certificate);
		keyStore.setCertificateEntry("ca", authCertificate);
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