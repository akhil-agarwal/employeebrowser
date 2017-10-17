package sfdatamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;
import com.sap.cloud.sdk.s4hana.serialization.CostCenter;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDetails
{
    @ElementName( "personIdExternal" )
    private String personIdExternal;

    @ElementName( "employmentNav" )
    @JsonIgnore
    private EmploymentDetailsContainer employmentDetailsContainer = null;

    /* transitive properties */

    @JsonProperty("costCenter")
    public CostCenter getCostCenter() {
        return hasDetails() ? getDetails().getCostCenter() : null;
    }

    @JsonProperty( "department" )
    public String getDepartment() {
        return hasDetails() ? getDetails().getDepartment() : null;
    }

    @JsonProperty( "division" )
    public String getDivision() {
        return hasDetails() ? getDetails().getDivision() : null;
    }

    @JsonProperty( "employmentType" )
    public String getEmploymentType() {
        return hasDetails() ? getDetails().getEmploymentType() : null;
    }

    @JsonProperty( "jobTitle" )
    public String getJobTitle() {
        return hasDetails() ? getDetails().getJobTitle() : null;
    }

    @JsonProperty( "payGrade" )
    public String getPayGrade() {
        return hasDetails() ? getDetails().getPayGrade() : null;
    }

    /* internal property access */

    private boolean hasDetails() {
        if(employmentDetailsContainer==null) {
            return false;
        }
        final List<EmploymentDetails> employmentDetails = employmentDetailsContainer.getEmploymentDetails();
        if( employmentDetails == null
                || employmentDetails.size()!=1
                || employmentDetails.get(0).getJobDetailsContainer()==null
                || employmentDetails.get(0).getJobDetailsContainer().getJobDetails()==null )
        {
            return false;
        }
        final List<JobDetails> jobDetails = employmentDetails.get(0).getJobDetailsContainer().getJobDetails();
        return jobDetails!=null && jobDetails.size()==1;
    }

    private JobDetails getDetails() {
        return employmentDetailsContainer.getEmploymentDetails().get(0).getJobDetailsContainer().getJobDetails().get(0);
    }
}
