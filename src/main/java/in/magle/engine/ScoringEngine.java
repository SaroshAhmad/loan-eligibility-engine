package in.magle.engine;

import in.magle.model.DecisionResult;
import in.magle.model.RuleResult;

import java.util.List;

/**
 * Converts a list of rule results into a final scored decision.
 *
 * HOW SCORING WORKS:
 *
 * Each rule has a weight (1-10). If a rule passes, it contributes
 * its full weight to the score. If it fails, it contributes zero.
 *
 * Final score = (sum of passed weights / sum of all weights) × 100
 *
 * Example:
 *   CreditScore  PASS weight 9  → contributes 9
 *   DTI          PASS weight 8  → contributes 8
 *   Employment   PASS weight 7  → contributes 7
 *   LoanToIncome FAIL weight 7  → contributes 0
 *   Age          PASS weight 5  → contributes 5
 *
 *   Total possible = 9+8+7+7+5 = 36
 *   Total earned   = 9+8+7+0+5 = 29
 *   Final score    = (29/36) × 100 = 80
 *
 * Decision thresholds:
 *   Score >= 75 → APPROVED
 *   Score >= 50 → REFERRED (borderline — human review)
 *   Score <  50 → DECLINED
 */

public class ScoringEngine {
    private static final int APPROVAL_THRESHOLD  = 75;
    private static final int REFERRAL_THRESHOLD  = 50;

    public DecisionResult decide(String applicantId, List<RuleResult> results) {

        int totalPossibleWeight = 0;
        int totalEarnedWeight   = 0;

        for (RuleResult result : results) {
            totalPossibleWeight += result.getWeight();
            if (result.isPassed()) {
                totalEarnedWeight += result.getWeight();
            }
        }

        // Calculate score as a percentage (0 to 100)
        int finalScore = (totalPossibleWeight == 0) ? 0 :
                (int) Math.round((double) totalEarnedWeight / totalPossibleWeight * 100);

        // Determine decision and summary reason
        DecisionResult.Decision decision;
        String summaryReason;

        if (finalScore >= APPROVAL_THRESHOLD) {
            decision = DecisionResult.Decision.APPROVED;
            summaryReason = String.format(
                    "Application approved. Score %d meets approval threshold of %d.",
                    finalScore, APPROVAL_THRESHOLD);

        } else if (finalScore >= REFERRAL_THRESHOLD) {
            decision = DecisionResult.Decision.REFERRED;
            summaryReason = String.format(
                    "Application referred for manual review. Score %d is borderline " +
                            "(approval threshold: %d, decline threshold: %d).",
                    finalScore, APPROVAL_THRESHOLD, REFERRAL_THRESHOLD);

        } else {
            decision = DecisionResult.Decision.DECLINED;

            // Build a specific reason listing which rules failed
            StringBuilder failedRules = new StringBuilder();
            for (RuleResult result : results) {
                if (!result.isPassed()) {
                    if (failedRules.length() > 0) failedRules.append(", ");
                    failedRules.append(result.getRuleName());
                }
            }
            summaryReason = String.format(
                    "Application declined. Score %d is below minimum threshold of %d. " +
                            "Failed rules: %s.",
                    finalScore, REFERRAL_THRESHOLD, failedRules);
        }

        return new DecisionResult(applicantId, decision, finalScore, results, summaryReason);
    }
}
