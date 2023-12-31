package org.beenoo.jwt.generator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.beenoo.jwt.generator.exception.GenerationDataNotFoundException;
import org.beenoo.jwt.generator.exception.GenerationDataParsingException;
import org.beenoo.jwt.generator.model.GenerationData;

import java.io.File;
import java.io.IOException;

public class GenerationDataExtractionService {

    private final ObjectMapper mapper;

    public GenerationDataExtractionService() {
        mapper = new ObjectMapper(new YAMLFactory());
    }

    public GenerationData parseFile(String generationDataFilePath) {
        File generationDataFile = new File(generationDataFilePath);
        if(!generationDataFile.exists()) {
            throw new GenerationDataNotFoundException("Generation Data file " + generationDataFile + "specified not found");
        }
        GenerationData generationData;
        try {
            generationData = mapper.readValue( generationDataFile, GenerationData.class);
        } catch (IOException e) {
            throw new GenerationDataParsingException(e);
        }
        return generationData;
    }
}
