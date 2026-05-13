package in.magle.model;

/**
 * Represents the outcome of a single rule evaluation.
 *
 * Every EligibilityRule returns one of these.
 * The RuleEngine collects all RuleResults and passes them
 * to the ScoringEngine to produce a final decision.
 *
 * Why immutable? Because a result is a historical fact —
 * "this rule passed/failed for this reason." Facts do not change.
 */

public class RuleResult {
    private final String ruleName;
    private final boolean passed;
    private final String reason;
    private final int weight;   // how much this rule contributes to the final score(1-10)

    // Private constructor - use the static factory methods below
    private RuleResult(String ruleName, boolean passed, String reason, int weight) {
        this.ruleName = ruleName;
        this.passed = passed;
        this.reason = reason;
        this.weight = weight;
    }

    /**
     * Static factory methods — a cleaner alternative to constructors
     * when the method name can describe what is being created.
     *
     * Instead of: new RuleResult("CreditScore", true, "...", 8)
     * You write:  RuleResult.pass("CreditScore", "...", 8)
     *
     * The intent is immediately obvious.
     */
    public static RuleResult pass(String ruleName, String reason, int weight) {
        return new RuleResult(ruleName, true, reason, weight);
    }

    public static RuleResult fail(String ruleName, String reason, int weight) {
        return new RuleResult(ruleName, false, reason, weight);
    }

    public String getRuleName()
    {
        return ruleName;
    }

    public boolean isPassed()
    {
        return passed;
    }

    public String getReason()
    {
        return reason;
    }

    public int getWeight()
    {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (weight=%d) -%s",
                passed?"PASS":"FAIL",ruleName, weight, reason);
    }
}
