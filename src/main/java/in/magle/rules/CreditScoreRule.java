package in.magle.rules;

import in.magle.model.ApplicantProfile;
import in.magle.model.RuleResult;

/**
 * Evaluates the applicant's credit score.
 *
 * In the UK, Experian scores range from 0 to 999.
 * - Below 560  : Very Poor  -> automatic decline
 * - 560 to 720 : Fair/Good  -> referred for manual review
 * - Above 720  : Very Good  -> passes this rule
 *
 * Weight: 9 out of 10
 * Credit score is the single strongest predictor of default risk.
 * Most banks treat a very poor score as an automatic disqualifier.
 */

public class CreditScoreRule implements EligibilityRule {

    private static final int MINIMUM_SCORE = 560;
    private static final int GOOD_SCORE = 720;
    private static final int RULE_WEIGHT = 9;
    private static final String RULE_NAME = "Credit Score Rule";

    @Override
    public RuleResult evaluate(ApplicantProfile profile) {
        int score = profile.getCreditScore();

        if (score >= GOOD_SCORE) {
            return RuleResult.pass(
                    RULE_NAME,
                    String.format("Credit score %d is strong (threshold: %d)", score, GOOD_SCORE),
                    RULE_WEIGHT
            );
        }

        if (score >= MINIMUM_SCORE) {
            // Score is acceptable but not strong — we still pass this rule
            // but the ScoringEngine will reflect the weakness in the final score
            return RuleResult.pass(
                    RULE_NAME,
                    String.format("Credit score %d is acceptable but borderline (threshold: %d)",
                            score, GOOD_SCORE),
                    RULE_WEIGHT
            );
        }

        return RuleResult.fail(
                RULE_NAME,
                String.format("Credit score %d is below minimum threshold of %d",
                        score, MINIMUM_SCORE),
                RULE_WEIGHT
        );
    }

    @Override
    public String getRuleName() {
        return RULE_NAME;
    }
}
