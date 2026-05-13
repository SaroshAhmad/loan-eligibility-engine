package in.magle;

import in.magle.model.ApplicantProfile;
import in.magle.model.EmploymentType;


public class App {
    public static void main(String[] args) {
        ApplicantProfile profile = new ApplicantProfile.Builder()
                .applicantId("APP-001")
                .fullName("Ahmad Khan")
                .age(28)
                .annualIncome(35000)
                .creditScore(720)
                .employmentType(EmploymentType.FULL_TIME)
                .existingMonthlyDebt(400)
                .requestedLoanAmount(15000)
                .build();

        System.out.println(profile);

        // Now test that validation works
        try {
            ApplicantProfile invalid = new ApplicantProfile.Builder()
                    .applicantId("APP-002")
                    .fullName("Bad Applicant")
                    .age(15)          // Invalid: under 18
                    .annualIncome(30000)
                    .creditScore(600)
                    .employmentType(EmploymentType.FULL_TIME)
                    .existingMonthlyDebt(200)
                    .requestedLoanAmount(10000)
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected validation error: " + e.getMessage());
        }
    }
}

