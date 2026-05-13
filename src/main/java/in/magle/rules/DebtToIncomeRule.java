package in.magle.rules;

import in.magle.model.ApplicantProfile;
import in.magle.model.RuleResult;

/**
 * Evaluates the applicant's debt-to-income ratio (DTI).
 *
 * DTI = (total monthly debt payments / monthly income) × 100
 *
 * Example: monthly income £2,500, existing monthly debt £800
 * DTI = (800 / 2500) × 100 = 32%
 *
 * Industry thresholds:
 * - DTI below 35% : low risk → passes
 * - DTI 35% to 50%: moderate risk → passes with warning
 * - DTI above 50% : high risk → fails
 *
 * Weight: 8 out of 10
 * High existing debt dramatically increases default probability.
 */

public class DebtToIncomeRule implements EligibilityRule {

    private static final double MAX_ACCEPTABLE_DTI = 50.0;
    private static final double WARNING_DTI = 35.0;
    private static final int    RULE_WEIGHT = 8;
    private static final String RULE_NAME  = "Debt-to-Income Rule";

    @Override
    public RuleResult evaluate(ApplicantProfile profile) {
        double monthlyIncome = profile.getAnnualIncome() / 12.0;
        double monthlyDebt   = profile.getExistingMonthlyDebt();
        double dti           = (monthlyDebt / monthlyIncome) * 100.0;

        if (dti <= WARNING_DTI) {
            return RuleResult.pass(
                    RULE_NAME,
                    String.format("DTI ratio %.1f%% is healthy (max acceptable: %.0f%%)",
                            dti, MAX_ACCEPTABLE_DTI),
                    RULE_WEIGHT
            );
        }

        if (dti <= MAX_ACCEPTABLE_DTI) {
            return RuleResult.pass(
                    RULE_NAME,
                    String.format("DTI ratio %.1f%% is acceptable but elevated (warning level: %.0f%%)",
                            dti, WARNING_DTI),
                    RULE_WEIGHT
            );
        }

        return RuleResult.fail(
                RULE_NAME,
                String.format("DTI ratio %.1f%% exceeds maximum threshold of %.0f%%",
                        dti, MAX_ACCEPTABLE_DTI),
                RULE_WEIGHT
        );
    }

    @Override
    public String getRuleName() {
        return RULE_NAME;
    }
}
