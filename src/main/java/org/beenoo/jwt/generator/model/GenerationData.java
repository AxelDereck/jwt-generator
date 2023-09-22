package org.beenoo.jwt.generator.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class GenerationData {
    String clientId;
    String tokenURL;
    String privateKeyPath;

}