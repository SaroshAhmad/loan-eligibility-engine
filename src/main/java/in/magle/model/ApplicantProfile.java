package in.magle.model;
import in.magle.model.EmploymentType;

/**
 * Represents the financial profile of a loan applicant.
 *
 * Design decisions:
 * - Immutable: all fields are final. Once an ApplicantProfile is created,
 *   it cannot be modified. This is important because our decision engine
 *   reads this object from multiple places — we never want a rule to
 *   accidentally modify the profile being evaluated.
 *
 * - Builder pattern: with 8 fields, a constructor with 8 parameters is
 *   unreadable. new ApplicantProfile("Ahmad", 28, 35000, 720, ...) tells
 *   you nothing about what each value means. The Builder makes construction
 *   self-documenting.
 */
public final class ApplicantProfile {

    // final means these fields can only be assigned once, in the constructor.
    // This enforces immutability.
    private final String applicantId;
    private final String fullName;
    private final int age;
    private final double annualIncome;    // in GBP
    private final int creditScore;        // 0 to 999 (UK Experian scale)
    private final EmploymentType employmentType;
    private final double existingMonthlyDebt; // total monthly debt payments in GBP
    private final double requestedLoanAmount;

    // The constructor is private. The ONLY way to create an ApplicantProfile
    // is through the Builder. This enforces that the Builder's validation runs.
    private ApplicantProfile(Builder builder) {
        this.applicantId = builder.applicantId;
        this.fullName = builder.fullName;
        this.age = builder.age;
        this.annualIncome = builder.annualIncome;
        this.creditScore = builder.creditScore;
        this.employmentType = builder.employmentType;
        this.existingMonthlyDebt = builder.existingMonthlyDebt;
        this.requestedLoanAmount = builder.requestedLoanAmount;
    }

    // Getters only — no setters. Immutable.
    public String getApplicantId() { return applicantId; }
    public String getFullName() { return fullName; }
    public int getAge() { return age; }
    public double getAnnualIncome() { return annualIncome; }
    public int getCreditScore() { return creditScore; }
    public EmploymentType getEmploymentType() { return employmentType; }
    public double getExistingMonthlyDebt() { return existingMonthlyDebt; }
    public double getRequestedLoanAmount() { return requestedLoanAmount; }

    /**
     * The Builder is a static inner class. Static means it can be instantiated
     * without an instance of ApplicantProfile — which makes sense, since we
     * use it to CREATE the ApplicantProfile.
     *
     * Usage:
     *   ApplicantProfile profile = new ApplicantProfile.Builder()
     *       .applicantId("APP-001")
     *       .fullName("Ahmad Khan")
     *       .age(28)
     *       .annualIncome(35000)
     *       .creditScore(720)
     *       .employmentType(EmploymentType.FULL_TIME)
     *       .existingMonthlyDebt(400)
     *       .requestedLoanAmount(15000)
     *       .build();
     *
     * Each method sets one field and returns 'this' (the Builder itself),
     * allowing the chained call style above.
     */
    public static class Builder {

        // These are not final — the Builder is mutable while being populated.
        private String applicantId;
        private String fullName;
        private int age;
        private double annualIncome;
        private int creditScore;
        private EmploymentType employmentType;
        private double existingMonthlyDebt;
        private double requestedLoanAmount;

        public Builder applicantId(String applicantId) {
            this.applicantId = applicantId;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder annualIncome(double annualIncome) {
            this.annualIncome = annualIncome;
            return this;
        }

        public Builder creditScore(int creditScore) {
            this.creditScore = creditScore;
            return this;
        }

        public Builder employmentType(EmploymentType employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public Builder existingMonthlyDebt(double existingMonthlyDebt) {
            this.existingMonthlyDebt = existingMonthlyDebt;
            return this;
        }

        public Builder requestedLoanAmount(double requestedLoanAmount) {
            this.requestedLoanAmount = requestedLoanAmount;
            return this;
        }

        /**
         * build() is where validation happens.
         * We check that the data is sensible before constructing the object.
         * If anything is wrong, we throw immediately with a clear message.
         * An ApplicantProfile that exists is guaranteed to be valid.
         * This principle is called "make illegal states unrepresentable."
         */
        public ApplicantProfile build() {
            if (applicantId == null || applicantId.isBlank()) {
                throw new IllegalArgumentException("Applicant ID cannot be null or blank");
            }
            if (fullName == null || fullName.isBlank()) {
                throw new IllegalArgumentException("Full name cannot be null or blank");
            }
            if (age < 18 || age > 85) {
                throw new IllegalArgumentException(
                        "Age must be between 18 and 85, received: " + age);
            }
            if (annualIncome <= 0) {
                throw new IllegalArgumentException(
                        "Annual income must be positive, received: " + annualIncome);
            }
            if (creditScore < 0 || creditScore > 999) {
                throw new IllegalArgumentException(
                        "Credit score must be between 0 and 999, received: " + creditScore);
            }
            if (employmentType == null) {
                throw new IllegalArgumentException("Employment type cannot be null");
            }
            if (existingMonthlyDebt < 0) {
                throw new IllegalArgumentException(
                        "Existing monthly debt cannot be negative, received: " + existingMonthlyDebt);
            }
            if (requestedLoanAmount <= 0) {
                throw new IllegalArgumentException(
                        "Requested loan amount must be positive, received: " + requestedLoanAmount);
            }

            return new ApplicantProfile(this);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "ApplicantProfile{id='%s', name='%s', age=%d, income=£%.2f, " +
                        "creditScore=%d, employment=%s, monthlyDebt=£%.2f, loanRequest=£%.2f}",
                applicantId, fullName, age, annualIncome,
                creditScore, employmentType, existingMonthlyDebt, requestedLoanAmount
        );
    }
}