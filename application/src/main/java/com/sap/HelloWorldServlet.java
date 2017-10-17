package com.sap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.ReadCostCenterDataNamespace;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.ReadCostCenterDataService;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import sfdatamodel.EmployeeDetails;

@WebServlet("/costcenter-employees")
public class HelloWorldServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = CloudLoggerFactory.getLogger(HelloWorldServlet.class);

    @Override
    protected void doGet( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException
    {
        try {

            response.setContentType("application/json");

            final List<ReadCostCenterDataNamespace.CostCenter> costCenterList = ReadCostCenterDataService.getAllCostCenter()
                    .select(ReadCostCenterDataNamespace.CostCenter.COMPANY_CODE, ReadCostCenterDataNamespace.CostCenter.COST_CENTER_I_D)
                    .execute(new ErpConfigContext());

            List<EmployeeDetails> employeeDetailsList = ODataQueryBuilder.withEntity("/odata/v2", "PerPerson")
                    .expand("employmentNav/jobInfoNav")
                    .select("personIdExternal",
                            "employmentNav/jobInfoNav/costCenter",
                            "employmentNav/jobInfoNav/department",
                            "employmentNav/jobInfoNav/division",
                            "employmentNav/jobInfoNav/employmentType",
                            "employmentNav/jobInfoNav/jobTitle",
                            "employmentNav/jobInfoNav/payGrade")
                    .withoutMetadata()
                    .build()
                    .execute("SuccessFactorsODataEndpoint")
                    .asList(EmployeeDetails.class);

            List<CompanyCodeToEmployee> companyCodeToEmployees = new ArrayList<>();

            for(ReadCostCenterDataNamespace.CostCenter costCenter : costCenterList) {
                CompanyCodeToEmployee companyCodeToEmployee = new CompanyCodeToEmployee();

                companyCodeToEmployee.setCompanyCode(costCenter.getCompanyCode());
                companyCodeToEmployee.setCostCenter(costCenter.getCostCenterID());

                for(EmployeeDetails employee : employeeDetailsList) {
                    if(employee.getCostCenter()!=null && costCenter.getCostCenterID().equals(employee.getCostCenter().getKeyAsString()))
                        companyCodeToEmployee.getEmployees().add(employee);
                }

                if(!companyCodeToEmployee.getEmployees().isEmpty())
                    companyCodeToEmployees.add(companyCodeToEmployee);
            }

            response.getWriter().write(new ObjectMapper().writeValueAsString(companyCodeToEmployees));


        } catch (ODataException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
