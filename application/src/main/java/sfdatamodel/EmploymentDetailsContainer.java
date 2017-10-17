package sfdatamodel;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

import java.util.List;

@Data
public class EmploymentDetailsContainer
{
    @ElementName( "results" )
    private List<EmploymentDetails> employmentDetails;
}
