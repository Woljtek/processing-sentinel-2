package eu.csgroup.coprs.ps2.ew.l0u.service.exec;

import eu.csgroup.coprs.ps2.core.common.exception.ScriptExecutionException;
import eu.csgroup.coprs.ps2.core.common.model.l0.L0uExecutionInput;
import eu.csgroup.coprs.ps2.core.common.model.processing.Mission;
import eu.csgroup.coprs.ps2.core.common.model.script.ScriptWrapper;
import eu.csgroup.coprs.ps2.core.common.model.trace.TaskReport;
import eu.csgroup.coprs.ps2.core.common.model.trace.task.ReportTask;
import eu.csgroup.coprs.ps2.core.common.service.ew.EWExecutionService;
import eu.csgroup.coprs.ps2.core.common.utils.ScriptUtils;
import eu.csgroup.coprs.ps2.ew.l0u.settings.L0uFolderParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Slf4j
@Component
public class L0uEWExecutionService implements EWExecutionService<L0uExecutionInput> {

    @Override
    public void processing(L0uExecutionInput l0uExecutionInput, UUID parentTaskUid) {

        log.info("Starting L0U processing");

        TaskReport taskReport = new TaskReport()
                .setTaskName(ReportTask.PROCESSING_TASK.getName())
                .setSatellite(Mission.S2.name() + l0uExecutionInput.getSatellite())
                .setParentUid(parentTaskUid);

        String jobOrderName = l0uExecutionInput.getJobOrders().keySet().iterator().next();

        taskReport.begin("Start task " + L0uFolderParameters.SCRIPT_PATH);

        try {

            final Integer exitCode = ScriptUtils.run(
                    new ScriptWrapper()
                            .setRunId(jobOrderName)
                            .setWorkdir(L0uFolderParameters.WORKSPACE_PATH)
                            .setCommand(List.of(L0uFolderParameters.SCRIPT_PATH, L0uFolderParameters.WORKSPACE_PATH + "/" + jobOrderName))
            );

            if (exitCode == 0) {
                log.info("Finished L0U processing");
            } else {
                if (exitCode < 128) {
                    log.warn("Finished L0U processing with a warning");
                } else {
                    throw new ScriptExecutionException("Finished L0U processing with an error");
                }
            }

            taskReport.end("End task " + L0uFolderParameters.SCRIPT_PATH + " with exit code " + exitCode);

        } catch (Exception e) {
            taskReport.error(e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public String getLevel() {
        return "L0u";
    }

}
