package org.beenoo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.beenoo.exception.GenerationDataNotFoundException;
import org.beenoo.exception.GenerationDataParsingException;
import org.beenoo.model.GenerationData;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@Slf4j
public class GenerationDataExtractionServiceTest {
    final String GENERATION_DATA_FILENAME = "sample-generation-data.yml";
    final String BAD_GENERATION_DATA_FILENAME = "sample-generation-data-bad.yml";
    final String PRIVATE_KEY_FILENAME = "privateKey.key";
    final String TEMP_FOLDERNAME = "tmp";
    GenerationDataExtractionService generationDataExtractionService;
    ClassLoader classLoader;
    File dirLocation;

    @Before
    public void setup() {
        log.info("Setup the GenerationDataServiceTest");
        generationDataExtractionService = new GenerationDataExtractionService();
        classLoader = getClass().getClassLoader();
        dirLocation = checkTempFolder();
    }

    @Test
    public void check_ShouldExtractGenerationDataFromYamlFilePassed() {
        // Given
        // Copy required folder in temporary folder
        String generationDataFilePath = copyFileToTempFolder(GENERATION_DATA_FILENAME);
        copyFileToTempFolder(PRIVATE_KEY_FILENAME);

        // When
        GenerationData generationData = generationDataExtractionService.parseFile(generationDataFilePath);

        // Then
        assertNotNull(generationData);
    }

    @Test
    public void check_ShouldThrowExceptionWhenWrongYamlFilePassed() {
        // Given
        // Copy required folder in temporary folder
        String generationDataFilePath = copyFileToTempFolder(BAD_GENERATION_DATA_FILENAME);
        copyFileToTempFolder(PRIVATE_KEY_FILENAME);

        // When
        assertThrows(
            GenerationDataParsingException.class,
            () -> generationDataExtractionService.parseFile(generationDataFilePath)
        );
    }

    @Test
    public void check_ShouldThrowExceptionWhenInexistingYamlFilePassed() {
        // Given
        String generationDataFilePath = "/temp/InexistingFile.yml";

        // When
        assertThrows(
                GenerationDataNotFoundException.class,
            () -> generationDataExtractionService.parseFile(generationDataFilePath)
        );
    }

    private File checkTempFolder() {
        File[] rootFiles = File.listRoots();
        File tempFolder = 0 >= rootFiles.length ? null : rootFiles[0];
        if(null == tempFolder) {
            tempFolder = new File(System.getProperty("java.io.tmpdir"));
        } else {
            tempFolder = new File( tempFolder.getAbsolutePath() + File.separator + TEMP_FOLDERNAME);
            if(!tempFolder.exists()) {
                assert(tempFolder.mkdir());
            }
        }
        assert(tempFolder.exists());
        assert(tempFolder.isDirectory());
        return tempFolder;
    }

    private String copyFileToTempFolder(String filename) {
        String resultingFilePath = "";
        try {
            URL resourceURL = classLoader.getResource(filename);
            assertNotNull(resourceURL);
            File fileToCopy = new File(resourceURL.getFile());
            FileUtils.copyFileToDirectory(fileToCopy, dirLocation);
            File resultingFile = new File(dirLocation.getAbsolutePath() + File.separator + filename);
            if(!resultingFile.exists()) {
                String msgErr = "Not able to copy file " + filename + " to folder " + dirLocation.getAbsolutePath();
                log.error(msgErr);
                throw new RuntimeException(msgErr);
            } else {
                resultingFilePath = resultingFile.getAbsolutePath();
            }
        } catch(IOException | NullPointerException exception) {
            log.debug("Fail to copy file {} to temp Folder {}", filename, dirLocation.getAbsoluteFile().getAbsolutePath());
        }
        return resultingFilePath;
    }
}
