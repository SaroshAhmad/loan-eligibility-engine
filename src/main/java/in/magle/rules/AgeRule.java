package in.magle.rules;

import in.magle.model.ApplicantProfile;
import in.magle.model.RuleResult;

/**
 * Evaluates whether the applicant's age is within acceptable lending range.
 *
 * UK lending regulations and lender policies typically require:
 * - Minimum age 18 (legal requirement — cannot enter financial contracts)
 * - Maximum age 70 at time of application for standard products
 *   (loan must typically be repaid before age 75-80)
 *
 * Note: age 18 minimum is already enforced in ApplicantProfile.Builder.
 * This rule applies the lender's policy maximum.
 *
 * Weight: 5 out of 10
 * Age is a softer criterion — older applicants may still qualify
 * on shorter loan terms, so this is not an automatic disqualifier
 * in a more sophisticated system.
 */

public class AgeRule implements EligibilityRule {
    private static final int    MAX_AGE     = 70;
    private static final int    RULE_WEIGHT = 5;
    private static final String RULE_NAME   = "Age Rule";

    @Override
    public RuleResult evaluate(ApplicantProfile profile) {
        int age = profile.getAge();

        if (age <= MAX_AGE) {
            return RuleResult.pass(
                    RULE_NAME,
                    String.format("Age %d is within acceptable lending range (max: %d)", age, MAX_AGE),
                    RULE_WEIGHT
            );
        }

        return RuleResult.fail(
                RULE_NAME,
                String.format("Age %d exceeds maximum lending age of %d", age, MAX_AGE),
                RULE_WEIGHT
        );
    }

    @Override
    public String getRuleName() {
        return RULE_NAME;
    }
}
