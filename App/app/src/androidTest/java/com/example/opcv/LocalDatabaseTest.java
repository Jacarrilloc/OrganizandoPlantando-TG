package com.example.opcv;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.opcv.model.persistance.repository.local_db.LocalDatabase;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocalDatabase.class)
public class LocalDatabaseTest {
    @Mock
    private Context context;

    private LocalDatabase localDatabase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        localDatabase = new LocalDatabase(context);
    }

    @Test
    public void testCreateJsonForm_InfoFormIsValid_FileUpdated() throws Exception {
        String idGarden = "garden1";
        Map<String, Object> infoForm = new HashMap<>();
        infoForm.put("CreatedBy", "John");

        File gardenDir = mock(File.class);
        when(context.getExternalFilesDir(null)).thenReturn(gardenDir);
        when(gardenDir.exists()).thenReturn(true);

        File infoFormFile = mock(File.class);
        when(gardenDir.isDirectory()).thenReturn(true);
        when(new File(gardenDir, "infoForm.json")).thenReturn(infoFormFile);
        when(infoFormFile.exists()).thenReturn(false);

        FileWriter fileWriter = mock(FileWriter.class);
        PowerMockito.whenNew(FileWriter.class).withArguments(infoFormFile).thenReturn(fileWriter);

        localDatabase.createJsonForm(idGarden, infoForm);

        verify(gardenDir).mkdirs();
        verify(fileWriter).write(anyString());
        verify(fileWriter).flush();
        verify(fileWriter).close();
    }
}


