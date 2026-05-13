package in.magle;


import in.magle.engine.RuleEngine;
import in.magle.engine.ScoringEngine;
import in.magle.model.ApplicantProfile;
import in.magle.model.DecisionResult;
import in.magle.model.EmploymentType;
import in.magle.model.RuleResult;

import java.util.List;

public class App {
    public static void main(String[] args) {
        RuleEngine    ruleEngine    = new RuleEngine();
        ScoringEngine scoringEngine = new ScoringEngine();

        // --- Applicant 1: Strong profile, should be APPROVED ---
        ApplicantProfile applicant1 = new ApplicantProfile.Builder()
                .applicantId("APP-001")
                .fullName("Ahmad Khan")
                .age(28)
                .annualIncome(45000)
                .creditScore(780)
                .employmentType(EmploymentType.FULL_TIME)
                .existingMonthlyDebt(300)
                .requestedLoanAmount(15000)
                .build();

        List<RuleResult> results1 = ruleEngine.evaluate(applicant1);
        DecisionResult decision1  = scoringEngine.decide(applicant1.getApplicantId(), results1);
        decision1.printReport();

        System.out.println();

        // --- Applicant 2: Weak profile, should be DECLINED ---
        ApplicantProfile applicant2 = new ApplicantProfile.Builder()
                .applicantId("APP-002")
                .fullName("Jane Smith")
                .age(45)
                .annualIncome(22000)
                .creditScore(480)
                .employmentType(EmploymentType.UNEMPLOYED)
                .existingMonthlyDebt(1200)
                .requestedLoanAmount(50000)
                .build();

        List<RuleResult> results2 = ruleEngine.evaluate(applicant2);
        DecisionResult decision2  = scoringEngine.decide(applicant2.getApplicantId(), results2);
        decision2.printReport();

        System.out.println();

        // --- Applicant 3: Borderline profile, should be REFERRED ---
        ApplicantProfile applicant3 = new ApplicantProfile.Builder()
                .applicantId("APP-003")
                .fullName("Carlos Rivera")
                .age(35)
                .annualIncome(30000)
                .creditScore(610)
                .employmentType(EmploymentType.SELF_EMPLOYED)
                .existingMonthlyDebt(1400)
                .requestedLoanAmount(145000)
                .build();

        List<RuleResult> results3 = ruleEngine.evaluate(applicant3);
        DecisionResult decision3  = scoringEngine.decide(applicant3.getApplicantId(), results3);
        decision3.printReport();
    }
}

