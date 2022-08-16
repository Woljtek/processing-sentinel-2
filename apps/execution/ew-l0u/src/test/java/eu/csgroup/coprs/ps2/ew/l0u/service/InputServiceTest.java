package eu.csgroup.coprs.ps2.ew.l0u.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.csgroup.coprs.ps2.core.common.model.FileInfo;
import eu.csgroup.coprs.ps2.core.common.model.l0.L0uExecutionInput;
import eu.csgroup.coprs.ps2.core.common.model.processing.ProcessingMessage;
import eu.csgroup.coprs.ps2.core.common.settings.MessageParameters;
import eu.csgroup.coprs.ps2.core.common.utils.ProcessingMessageUtils;
import eu.csgroup.coprs.ps2.ew.l0u.service.setup.L0uEWInputService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputServiceTest {

    private static L0uEWInputService inputService;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        inputService = new L0uEWInputService();
    }

    @Test
    void extract() {

        final ProcessingMessage processingMessage = ProcessingMessageUtils.create();

        final FileInfo fileInfo1 = new FileInfo().setObsName("key1").setObsPath("bucket/folder").setLocalName("name1").setLocalPath("folder");
        final FileInfo fileInfo2 = new FileInfo().setObsName("key2").setObsPath("bucket/folder").setLocalName("name2").setLocalPath("folder");
        final Map<String, String> jobOrders = Map.of("JobOrder1", "xmlstuff1");
        final L0uExecutionInput l0uExecutionInput = new L0uExecutionInput();
        l0uExecutionInput.setJobOrders(jobOrders)
                .setSession("Session_ajkzehazkjeh")
                .setSatellite("A")
                .setFiles(Set.of(fileInfo1, fileInfo2));

        final Map<String, Object> executionInputMap = objectMapper.convertValue(l0uExecutionInput, new TypeReference<>() {
        });

        processingMessage.getAdditionalFields().put(MessageParameters.EXECUTION_INPUT_FIELD, executionInputMap);

        final L0uExecutionInput extract = inputService.extract(processingMessage);

        assertEquals(l0uExecutionInput, extract);

    }

}