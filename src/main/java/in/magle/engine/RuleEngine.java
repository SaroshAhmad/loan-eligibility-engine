package in.magle.engine;

import in.magle.model.ApplicantProfile;
import in.magle.model.RuleResult;
import in.magle.rules.*;

import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    private final List<EligibilityRule> rules;

    public RuleEngine() {
        // Register all rules here.
        // Order matters for readability of reports, not for logic.
        rules = new ArrayList<>();
        rules.add(new CreditScoreRule());
        rules.add(new DebtToIncomeRule());
        rules.add(new EmploymentRule());
        rules.add(new LoanToIncomeRule());
        rules.add(new AgeRule());
    }

    /**
     * Evaluates the applicant against every registered rule.
     *
     * @param profile the applicant to evaluate
     * @return a list of results, one per rule, in registration order
     */
    public List<RuleResult> evaluate(ApplicantProfile profile) {
        List<RuleResult> results = new ArrayList<>();

        for (EligibilityRule rule : rules) {
            RuleResult result = rule.evaluate(profile);
            results.add(result);
        }

        return results;
    }
}
