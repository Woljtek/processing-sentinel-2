package eu.csgroup.coprs.ps2.ew.l0c.service.setup;

import eu.csgroup.coprs.ps2.core.common.config.CleanupProperties;
import eu.csgroup.coprs.ps2.core.common.model.l0.L0cExecutionInput;
import eu.csgroup.coprs.ps2.core.common.utils.FileOperationUtils;
import eu.csgroup.coprs.ps2.core.ew.service.EWCleanupService;
import eu.csgroup.coprs.ps2.ew.l0c.settings.L0cFolderParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Set;

@Slf4j
@Service
public class L0cEWCleanupService extends EWCleanupService<L0cExecutionInput> {

    protected L0cEWCleanupService(CleanupProperties cleanupProperties) {
        super(cleanupProperties);
    }

    @Override
    protected void doPrepare() {
        FileOperationUtils.createFolders(L0cFolderParameters.WORKSPACE_FOLDERS);
    }

    @Override
    protected void doCleanBefore() {
        //
    }

    @Override
    protected void doCleanAfter(L0cExecutionInput executionInput) {
        FileOperationUtils.deleteFolders(Set.of(executionInput.getDtFolder()));
        FileOperationUtils.deleteFolderIfEmpty(Paths.get(executionInput.getDtFolder()).getParent().toString());
    }

}
