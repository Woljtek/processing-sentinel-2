package eu.csgroup.coprs.ps2.core.catalog.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuxCatalogData extends BaseCatalogData {

    private List<Double> footprint;
    private String insertionTime;

}
