package org.beenoo.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.beenoo.model.GenerationData;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;

import static org.beenoo.tool.DateTools.addMinutes;

@Slf4j
public class JwtGenerationService {

    public String generateJwtToken(GenerationData data, PrivateKey loadedPrivateKey) throws JOSEException {
        Date issueDate = Date.from(Instant.now());
        Date expirationDate = addMinutes(issueDate, 1440);

        // Create a JWT Claims Set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .issuer(data.getClientId())
            .issueTime(issueDate)
            .expirationTime(expirationDate) // 1 hour from now
            .audience(data.getTokenURL())
            .subject(data.getClientId())
            .build();

        // Create a JWT with a private key
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID("your_private_key")
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(new RSASSASigner(loadedPrivateKey));

        // Serialize the JWT to a string
        String jwtString = signedJWT.serialize();
        log.info("Generated JWT: " + jwtString);
        return jwtString;
    }

    public PrivateKey loadPrivateKeyFromFile(String privateKeyPath) throws IOException {
        FileReader reader = new FileReader(privateKeyPath);
        PEMParser pemParser = new PEMParser(reader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());

        return converter.getPrivateKey(privateKeyInfo);
    }
}
