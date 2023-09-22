package org.beenoo.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;

@Slf4j
public class JwtGenerationService {

    public PrivateKey loadPrivateKeyFromFile(String privateKeyPath) throws IOException {
        FileReader reader = new FileReader(privateKeyPath);
        PEMParser pemParser = new PEMParser(reader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());

        return converter.getPrivateKey(privateKeyInfo);
    }
}
