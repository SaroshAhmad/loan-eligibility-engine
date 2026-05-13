package in.magle.model;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * The final output of the loan evaluation process.
 *
 * Contains:
 * - The decision (APPROVED / DECLINED / REFERRED)
 * - The applicant it was made for
 * - All individual rule results that led to this decision
 * - The final score (0-100)
 * - A timestamp (every decision must be traceable in time)
 */

public class DecisionResult {

    /**
     * The three possible outcomes of evaluation.
     *
     * APPROVED  — all critical rules passed, score is strong
     * DECLINED  — one or more critical rules failed, or score too low
     * REFERRED  — score is borderline; a human underwriter should review
     */
    public enum Decision {
        APPROVED, DECLINED, REFERRED
    }

    private final String applicantId;
    private final Decision decision;
    private final int finalScore;
    private final List<RuleResult> ruleResults;
    private final String summaryReason;
    private final LocalDateTime timestamp;

    public DecisionResult(String applicantId,
                          Decision decision,
                          int finalScore,
                          List<RuleResult> ruleResults,
                          String summaryReason) {
        this.applicantId=applicantId;
        this.decision=decision;
        this.finalScore=finalScore;
        // Defensive copy - the caller cannot modify our internal list
        this.ruleResults = Collections.unmodifiableList(ruleResults);
        this.summaryReason = summaryReason;
        this.timestamp = LocalDateTime.now();
    }

    public String getApplicantId() { return applicantId; }
    public Decision getDecision() { return decision; }
    public int getFinalScore() { return finalScore; }
    public List<RuleResult> getRuleResults() { return  ruleResults; }
    public String getSummaryReason() { return summaryReason; }
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Prints a formatted decision report to the console.
     * In Phase 2 this will be replaced by a proper ReportService.
     */
    public void printReport() {
        System.out.println("=".repeat(55));
        System.out.println("       LOAN ELIGIBILITY DECISION REPORT");
        System.out.println("=".repeat(55));
        System.out.printf("  Applicant ID : %s%n", applicantId);
        System.out.printf("  Decision     : %s%n", decision);
        System.out.printf("  Final Score  : %d / 100%n", finalScore);
        System.out.printf("  Timestamp    : %s%n", timestamp);
        System.out.println("-".repeat(55));
        System.out.println("  Rule Breakdown:");
        for (RuleResult result : ruleResults) {
            System.out.printf("    %s%n", result);
        }
        System.out.println("-".repeat(55));
        System.out.printf("  Summary : %s%n", summaryReason);
        System.out.println("=".repeat(55));
    }
}
