package in.magle.rules;

import in.magle.model.ApplicantProfile;
import in.magle.model.RuleResult;

public interface EligibilityRule {
    /**
     * Evaluates the applicant profile against this rule's criteria.
     *
     * @param profile the applicant being evaluated
     * @return a RuleResult indicating pass/fail, reason, and weight
     */
    RuleResult evaluate(ApplicantProfile profile);

    /**
     * Returns the human-readable name of this rule.
     * Used in reports and audit logs.
     */
    String getRuleName();
}
