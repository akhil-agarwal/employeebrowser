package com.sap;

import sfdatamodel.EmployeeDetails;
import lombok.Data;
import sfdatamodel.EmployeeDetails;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompanyCodeToEmployee {

    String companyCode;
    String costCenter;

    List<EmployeeDetails> employees = new ArrayList<>();
}
