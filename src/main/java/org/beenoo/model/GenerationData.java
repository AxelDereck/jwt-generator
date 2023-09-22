package org.beenoo.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class GenerationData {
    String clientId;
    String tokenURL;
    String privateKeyPath;

}