package eu.csgroup.coprs.ps2.pw.l0u.service.prepare;

import eu.csgroup.coprs.ps2.core.common.model.l0.L0uExecutionInput;
import eu.csgroup.coprs.ps2.core.common.service.catalog.CatalogService;
import eu.csgroup.coprs.ps2.core.common.test.AbstractTest;
import eu.csgroup.coprs.ps2.pw.l0u.config.L0uPreparationProperties;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class L0uPWExecutionInputServiceTest extends AbstractTest {

    @Mock
    private CatalogService catalogService;
    @Mock
    private JobOrderService jobOrderService;
    @Mock
    private L0uPreparationProperties l0uPreparationProperties;

    @InjectMocks
    private L0uPWExecutionInputService executionInputService;

    @Override
    public void setup() throws Exception {
        executionInputService = new L0uPWExecutionInputService(catalogService, jobOrderService, l0uPreparationProperties);
    }

    @Override
    public void teardown() throws Exception {
        //
    }

    @Test
    void create() {

        // Given
        when(l0uPreparationProperties.getCaduBucket()).thenReturn("bucket");
        when(catalogService.retrieveSessionData(TestHelper.SESSION_NAME)).thenReturn(TestHelper.SESSION_CATALOG_DATA_LIST);
        when(jobOrderService.create(any())).thenReturn(Map.of("JobOrder.xml", "foo"));

        // When
        final List<L0uExecutionInput> executionInputs = executionInputService.create(List.of(TestHelper.SESSION, TestHelper.UPDATED_SESSION));

        // Then
        assertNotNull(executionInputs);
        assertEquals(2, executionInputs.size());
        executionInputs.forEach(
                l0uExecutionInput -> {
                    assertEquals(1, l0uExecutionInput.listJobOrders().size());
                    assertEquals(TestHelper.SESSION_FILES_COUNT, l0uExecutionInput.getFiles().size());
                    assertEquals(TestHelper.SESSION_NAME, l0uExecutionInput.getSession());
                }
        );
    }

}
