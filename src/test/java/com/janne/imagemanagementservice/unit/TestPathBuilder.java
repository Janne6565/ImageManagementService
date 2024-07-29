package com.janne.imagemanagementservice.unit;

import com.janne.imagemanagementservice.services.PathBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.UUID;

@SpringBootTest
public class TestPathBuilder {

    @Autowired
    private PathBuilder pathBuilder;

    @Test
    public void given_id_checkIfPathIsReversible() {
        String id = UUID.randomUUID().toString();
        Path pathOriginal = pathBuilder.buildPathOriginal(id);
        Path pathThumbnail = pathBuilder.buildPathThumbnail(id);

        Assertions.assertNotEquals(pathOriginal.toString(), pathThumbnail.toString());

        Assertions.assertEquals(pathBuilder.getIdFromOriginalPath(pathOriginal.toString()), id);
        Assertions.assertEquals(pathBuilder.getIdFromThumbnailPath(pathThumbnail.toString()), id);
    }

}
