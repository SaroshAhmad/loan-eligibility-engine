package in.magle.rules;

import in.magle.model.ApplicantProfile;
import in.magle.model.RuleResult;

/**
 * Evaluates whether the requested loan amount is proportionate to income.
 *
 * Loan-to-Income ratio (LTI) = requested loan amount / annual income
 *
 * Example: loan request £20,000, annual income £35,000
 * LTI = 20000 / 35000 = 0.57 (57% of annual income)
 *
 * Industry thresholds:
 * - LTI below 3x annual income : acceptable
 * - LTI 3x to 4.5x annual income : high — referred
 * - LTI above 4.5x annual income : excessive — declined
 *
 * Weight: 7 out of 10
 * Borrowing more than 4.5x income is considered high risk
 * by most UK lenders (FCA guidance).
 */

public class LoanToIncomeRule implements EligibilityRule {
    private static final double MAX_LTI_RATIO      = 4.5;
    private static final double WARNING_LTI_RATIO  = 3.0;
    private static final int    RULE_WEIGHT        = 7;
    private static final String RULE_NAME          = "Loan-to-Income Rule";

    @Override
    public RuleResult evaluate(ApplicantProfile profile) {
        double lti = profile.getRequestedLoanAmount() / profile.getAnnualIncome();

        if (lti <= WARNING_LTI_RATIO) {
            return RuleResult.pass(
                    RULE_NAME,
                    String.format("Loan-to-income ratio %.2fx is within comfortable range (max: %.1fx)",
                            lti, MAX_LTI_RATIO),
                    RULE_WEIGHT
            );
        }

        if (lti <= MAX_LTI_RATIO) {
            return RuleResult.pass(
                    RULE_NAME,
                    String.format("Loan-to-income ratio %.2fx is elevated but within limit (max: %.1fx)",
                            lti, MAX_LTI_RATIO),
                    RULE_WEIGHT
            );
        }

        return RuleResult.fail(
                RULE_NAME,
                String.format("Loan-to-income ratio %.2fx exceeds maximum of %.1fx annual income",
                        lti, MAX_LTI_RATIO),
                RULE_WEIGHT
        );
    }

    @Override
    public String getRuleName() {
        return RULE_NAME;
    }
}
