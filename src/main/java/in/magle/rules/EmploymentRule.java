package in.magle.rules;

import in.magle.model.ApplicantProfile;
import in.magle.model.EmploymentType;
import in.magle.model.RuleResult;

import static in.magle.model.EmploymentType.FULL_TIME;

/**
 * Evaluates the applicant's employment stability.
 *
 * Lenders need confidence that the applicant has ongoing income
 * to service the loan. Employment type is a proxy for income stability.
 *
 * FULL_TIME  : stable, predictable income → strong pass
 * CONTRACT   : stable but time-limited → pass with note
 * PART_TIME  : reduced income reliability → pass, reflected in score
 * SELF_EMPLOYED : variable income → pass, but higher scrutiny
 * UNEMPLOYED : no income source → automatic fail
 *
 * Weight: 7 out of 10
 */

public class EmploymentRule implements EligibilityRule {
    private static final String RULE_NAME = "Employment Rule";
    private static final int    RULE_WEIGHT = 7;

    @Override
    public RuleResult evaluate(ApplicantProfile profile) {
        EmploymentType employment = profile.getEmploymentType();

        switch (employment) {
            case FULL_TIME:
                return RuleResult.pass(RULE_NAME,
                        "Full-time employment provides stable income", RULE_WEIGHT);

            case CONTRACT:
                return RuleResult.pass(RULE_NAME,
                        "Contract employment accepted — income stability assumed for duration",
                        RULE_WEIGHT);

            case PART_TIME:
                return RuleResult.pass(RULE_NAME,
                        "Part-time employment accepted — income level will be reflected in scoring",
                        RULE_WEIGHT);

            case SELF_EMPLOYED:
                return RuleResult.pass(RULE_NAME,
                        "Self-employment accepted — variable income noted, further evidence may be required",
                        RULE_WEIGHT);

            case UNEMPLOYED:
                return RuleResult.fail(RULE_NAME,
                        "Unemployed applicants do not meet minimum employment criteria",
                        RULE_WEIGHT);

            default:
                return RuleResult.fail(RULE_NAME,
                        "Employment type could not be determined", RULE_WEIGHT);
        }
    }

    @Override
    public String getRuleName() {
        return RULE_NAME;
    }
}
