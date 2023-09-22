package org.beenoo;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtGeneratorTool
{
    public static void main( String[] args )
    {
        log.info( "Start of generation of JWT with arguments : {}", String.join(" ", args) );
        if(args.length != 1) {
            log.info("Abnormal number of arguments : Should have one argument corresponding to the absolute path of your Generation data file");
            exit(1);
        }
        manageJwtDataGeneration(args[0]);
    }

    public static String manageJwtDataGeneration(String dataFilePath) {
        GenerationDataExtractionService extractionService = new GenerationDataExtractionService();
        GenerationData data = extractionService.parseFile(dataFilePath);
        try {
            JwtGenerationService jwtGenerationService = new JwtGenerationService();
            PrivateKey providedKey = jwtGenerationService.loadPrivateKeyFromFile(data.getPrivateKeyPath());
        } catch (IOException ioException) {
            log.error("Unable to load specified private key from {} : {}", dataFilePath, ioException.getMessage());
            exit(3);
        }
        return "";
    }
}
