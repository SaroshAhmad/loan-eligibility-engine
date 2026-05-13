package in.magle.rules;

import in.magle.model.ApplicantProfile;
import in.magle.model.EmploymentType;
import in.magle.model.RuleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreditScoreRuleTest {
    private CreditScoreRule rule;

    /**
     * @BeforeEach runs before every single test method.
     * We create a fresh rule instance so tests cannot affect each other.
     */
    @BeforeEach
    void setUp() {
        rule = new CreditScoreRule();
    }

    /**
     * Helper method — builds a minimal valid profile with a given credit score.
     * Avoids repeating the full Builder in every test.
     */
    private ApplicantProfile profileWithCreditScore(int creditScore) {
        return new ApplicantProfile.Builder()
                .applicantId("TEST-001")
                .fullName("Test User")
                .age(30)
                .annualIncome(40000)
                .creditScore(creditScore)
                .employmentType(EmploymentType.FULL_TIME)
                .existingMonthlyDebt(300)
                .requestedLoanAmount(10000)
                .build();
    }

    @Test
    void shouldPassWhenCreditScoreIsStrong() {
        ApplicantProfile profile = profileWithCreditScore(780);
        RuleResult result = rule.evaluate(profile);
        assertTrue(result.isPassed(), "Expected PASS for credit score 780");
    }

    @Test
    void shouldPassWhenCreditScoreIsAtGoodThreshold() {
        // Boundary value: exactly at the good threshold
        ApplicantProfile profile = profileWithCreditScore(720);
        RuleResult result = rule.evaluate(profile);
        assertTrue(result.isPassed(), "Expected PASS for credit score exactly 720");
    }

    @Test
    void shouldPassWhenCreditScoreIsBorderline() {
        // Between minimum and good — still a pass but borderline
        ApplicantProfile profile = profileWithCreditScore(620);
        RuleResult result = rule.evaluate(profile);
        assertTrue(result.isPassed(), "Expected PASS for borderline credit score 620");
    }

    @Test
    void shouldFailWhenCreditScoreIsBelowMinimum() {
        ApplicantProfile profile = profileWithCreditScore(480);
        RuleResult result = rule.evaluate(profile);
        assertFalse(result.isPassed(), "Expected FAIL for credit score 480");
    }

    @Test
    void shouldFailAtExactMinimumBoundary() {
        // Boundary value: one below the minimum threshold
        ApplicantProfile profile = profileWithCreditScore(559);
        RuleResult result = rule.evaluate(profile);
        assertFalse(result.isPassed(), "Expected FAIL for credit score 559");
    }

    @Test
    void resultShouldContainRuleName() {
        ApplicantProfile profile = profileWithCreditScore(700);
        RuleResult result = rule.evaluate(profile);
        assertNotNull(result.getRuleName(), "Rule name should not be null");
        assertFalse(result.getRuleName().isBlank(), "Rule name should not be blank");
    }

    @Test
    void resultShouldContainReason() {
        ApplicantProfile profile = profileWithCreditScore(400);
        RuleResult result = rule.evaluate(profile);
        assertNotNull(result.getReason(), "Reason should not be null");
        assertFalse(result.getReason().isBlank(), "Reason should not be blank");
    }
}
