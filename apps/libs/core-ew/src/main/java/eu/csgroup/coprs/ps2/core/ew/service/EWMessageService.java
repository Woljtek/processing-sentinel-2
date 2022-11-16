package eu.csgroup.coprs.ps2.core.ew.service;

import eu.csgroup.coprs.ps2.core.common.model.ExecutionInput;
import eu.csgroup.coprs.ps2.core.common.model.FileInfo;
import eu.csgroup.coprs.ps2.core.common.model.processing.ProcessingMessage;
import eu.csgroup.coprs.ps2.core.common.model.processing.ProductFamily;
import eu.csgroup.coprs.ps2.core.common.model.processing.Timeliness;
import eu.csgroup.coprs.ps2.core.common.settings.MessageParameters;
import eu.csgroup.coprs.ps2.core.common.utils.ProcessingMessageUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class EWMessageService<T extends ExecutionInput> {

    public Set<ProcessingMessage> build(T executionInput, Map<ProductFamily, Set<FileInfo>> fileInfosByFamily, String... options) {

        log.info("Building outgoing messages");

        Set<ProcessingMessage> messages = doBuild(executionInput, fileInfosByFamily, options);

        log.info("Finished building outgoing messages");

        return messages;
    }

    protected abstract Set<ProcessingMessage> doBuild(T executionInput, Map<ProductFamily, Set<FileInfo>> fileInfosByFamily, String... options);

    protected Set<ProcessingMessage> buildCatalogMessages(Map<ProductFamily, Set<FileInfo>> fileInfosByFamily, T executionInput) {

        Set<ProcessingMessage> messages = new HashSet<>();

        fileInfosByFamily.forEach((productFamily, fileInfoSet) ->

                fileInfoSet.forEach(fileInfo -> {

                    ProcessingMessage processingMessage = ProcessingMessageUtils.create()
                            .setProductFamily(productFamily)
                            .setStoragePath(fileInfo.getObsURL())
                            .setKeyObjectStorage(fileInfo.getObsName())
                            .setSatelliteId(executionInput.getSatellite())
                            .setTimeliness(getTimeliness(productFamily));

                    processingMessage.getAdditionalFields().put(MessageParameters.T0_PDGS_DATE_FIELD, executionInput.getT0PdgsDate());

                    messages.add(processingMessage);
                })
        );

        return messages;
    }

    private Timeliness getTimeliness(ProductFamily productFamily) {
        return switch (productFamily) {
            case S2_SAD, S2_HKTM -> Timeliness.S2_SESSION;
            case S2_L0_DS, S2_L0_GR -> Timeliness.S2_L0;
            case S2_L1A_DS, S2_L1A_GR, S2_L1B_DS, S2_L1B_GR, S2_L1C_DS, S2_L1C_TC, S2_L1C_TL -> Timeliness.S2_L1;
            case S2_L2A_DS, S2_L2A_TL -> Timeliness.S2_L2;
            default -> Timeliness.EMPTY;
        };
    }

}
