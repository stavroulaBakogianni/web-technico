package gr.europeandynamics.web.technico.validators;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Repair;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RepairValidator {

    /**
     * Validates the type of the repair.
     *
     * @param repairType the repair type to validate
     * @throws CustomException if the repair type is null
     */
    public void validateRepairType(Enum<?> repairType) throws CustomException {
        if (repairType == null) {
            throw new CustomException("Repair type cannot be null.");
        }
    }

    /**
     * Validates the submission date of the repair.
     *
     * @param submissionDate the submission date to validate
     * @throws CustomException if the submission date is null or is in the
     * future
     */
    public void validateSubmissionDate(LocalDateTime submissionDate) throws CustomException {
        if (submissionDate == null || submissionDate.isAfter(LocalDateTime.now())) {
            throw new CustomException("Submission date must be in the past or present.");
        }
    }

    /**
     * Validates the proposed start date of the repair.
     *
     * @param proposedStartDate the proposed start date to validate
     * @throws CustomException if the proposed start date is in the past
     */
    public void validateProposedStartDate(LocalDateTime proposedStartDate) throws CustomException {
        if (proposedStartDate != null && proposedStartDate.isBefore(LocalDateTime.now())) {
            throw new CustomException("Proposed start date must be in the future or present.");
        }
    }

    /**
     * Validates the proposed end date of the repair.
     *
     * @param proposedEndDate the proposed end date to validate
     * @param proposedStartDate the proposed start date for comparison
     * @throws CustomException if the proposed end date is in the past or before
     * the proposed start date
     */
    public void validateProposedEndDate(LocalDateTime proposedEndDate, LocalDateTime proposedStartDate) throws CustomException {
        if (proposedEndDate != null) {
            if (proposedEndDate.isBefore(LocalDateTime.now())) {
                throw new CustomException("Proposed end date must be in the future or present.");
            }
            if (proposedStartDate != null && proposedEndDate.isBefore(proposedStartDate)) {
                throw new CustomException("Proposed end date must be after proposed start date.");
            }
        }
    }

    /**
     * Validates the proposed cost of the repair.
     *
     * @param proposedCost the proposed cost to validate
     * @throws CustomException if the proposed cost is negative
     */
    public void validateProposedCost(BigDecimal proposedCost) throws CustomException {
        if (proposedCost != null && proposedCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException("Proposed cost must be zero or more.");
        }
    }

    /**
     * Validates the status of the repair.
     *
     * @param repairStatus the repair status to validate
     * @throws CustomException if the repair status is null
     */
    public void validateRepairStatus(Enum<?> repairStatus) throws CustomException {
        if (repairStatus == null) {
            throw new CustomException("Repair status cannot be null.");
        }
    }

    /**
     * Validates the actual start and end dates of the repair.
     *
     * @param actualStartDate the actual start date to validate
     * @param actualEndDate the actual end date to validate
     * @throws CustomException if the actual start date is in the past or if the
     * actual end date is before the actual start date
     */
    public void validateActualDates(LocalDateTime actualStartDate, LocalDateTime actualEndDate) throws CustomException {
        if (actualStartDate != null && actualStartDate.isBefore(LocalDateTime.now())) {
            throw new CustomException("Actual start date must be in the future or present.");
        }
        if (actualEndDate != null) {
            if (actualEndDate.isBefore(LocalDateTime.now())) {
                throw new CustomException("Actual end date must be in the future or present.");
            }
            if (actualStartDate != null && actualEndDate.isBefore(actualStartDate)) {
                throw new CustomException("Actual end date must be after actual start date.");
            }
        }
    }

    /**
     * Validates all properties of a given Repair object.
     *
     * @param repair the Repair object to validate
     * @throws CustomException if any property validation fails
     */
    public void validateRepair(Repair repair) throws CustomException {
        validateRepairType(repair.getRepairType());
        validateSubmissionDate(repair.getSubmissionDate());
        validateProposedStartDate(repair.getProposedStartDate());
        validateProposedEndDate(repair.getProposedEndDate(), repair.getProposedStartDate());
        validateProposedCost(repair.getProposedCost());
        validateRepairStatus(repair.getRepairStatus());
        validateActualDates(repair.getActualStartDate(), repair.getActualEndDate());
    }
}
