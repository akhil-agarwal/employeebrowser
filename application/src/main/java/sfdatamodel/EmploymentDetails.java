package sfdatamodel;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class EmploymentDetails
{
    @ElementName( "jobInfoNav" )
    private JobDetailsContainer jobDetailsContainer;
}
