package eu.csgroup.coprs.ps2.core.common.model.execution;

import eu.csgroup.coprs.ps2.core.common.model.FileInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
public class L0cExecutionInput {

    private String session;
    private String satellite;
    private Set<FileInfo> files;
    private String inputFolder;

    // TODO need JO by name by step
    private Map<String, String> jobOrders;

}
