package sfdatamodel;

import com.sap.cloud.sdk.result.ElementName;
import com.sap.cloud.sdk.s4hana.serialization.CostCenter;
import lombok.Data;

@Data
public class JobDetails
{
    @ElementName( "costCenter" )
    private CostCenter costCenter;

    @ElementName( "department" )
    private String department;

    @ElementName( "division" )
    private String division;

    @ElementName( "employmentType" )
    private String employmentType;

    @ElementName( "jobTitle" )
    private String jobTitle;

    @ElementName( "payGrade" )
    private String payGrade;
}
