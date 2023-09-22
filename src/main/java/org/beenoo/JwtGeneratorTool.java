package org.beenoo;

import com.nimbusds.jose.JOSEException;
import lombok.extern.log4j.Log4j2;
import org.beenoo.model.GenerationData;
import org.beenoo.service.GenerationDataExtractionService;
import org.beenoo.service.JwtGenerationService;

import java.io.IOException;
import java.security.PrivateKey;

import static java.lang.System.exit;

/**
 * Main class for the Java Jar tool to generate JWT token
 * from OpenSSL Private key and a set of data passed
 * through a GenerationData file
 */
@Log4j2
public class JwtGeneratorTool
{
    public static void main( String[] args )
    {
        log.info( "Start of generation of JWT with arguments : {}", String.join(" ", args) );
        if(args.length != 1) {
            log.info("Abnormal number of arguments : Should have one argument corresponding to the absolute path of your Generation data file");
            exit(1);
        }
        String token = manageJwtDataGeneration(args[0]);
        if(token.isEmpty()) {
            log.error("Somehow we wasn't able to generate the expected token");
            exit(4);
        } else {
            exit(0);
        }
    }

    public static String manageJwtDataGeneration(String dataFilePath) {
        String resultingToken = "";
        GenerationDataExtractionService extractionService = new GenerationDataExtractionService();
        GenerationData data = extractionService.parseFile(dataFilePath);
        try {
            JwtGenerationService jwtGenerationService = new JwtGenerationService();
            PrivateKey providedKey = jwtGenerationService.loadPrivateKeyFromFile(data.getPrivateKeyPath());
            resultingToken = jwtGenerationService.generateJwtToken(data, providedKey);
        } catch (IOException ioException) {
            log.error("Unable to load specified private key from {} : {}", dataFilePath, ioException.getMessage());
            exit(2);
        } catch (JOSEException joseException) {
            log.error("Failed to sign generated token : {}", joseException.getMessage());
            exit(3);
        }
        return resultingToken;
    }
}
