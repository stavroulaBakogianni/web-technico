package gr.europeandynamics.web.technico.validators;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Repair;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RepairValidator {

    public void validateRepairType(Enum<?> repairType) throws CustomException {
        if (repairType == null) {
            throw new CustomException("Repair type cannot be null.");
        }
    }

    public void validateSubmissionDate(LocalDateTime submissionDate) throws CustomException {
        if (submissionDate == null || submissionDate.isAfter(LocalDateTime.now())) {
            throw new CustomException("Submission date must be in the past or present.");
        }
    }

    public void validateProposedStartDate(LocalDateTime proposedStartDate) throws CustomException {
        if (proposedStartDate != null && proposedStartDate.isBefore(LocalDateTime.now())) {
            throw new CustomException("Proposed start date must be in the future or present.");
        }
    }

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

    public void validateProposedCost(BigDecimal proposedCost) throws CustomException {
        if (proposedCost != null && proposedCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException("Proposed cost must be zero or more.");
        }
    }

    public void validateRepairStatus(Enum<?> repairStatus) throws CustomException {
        if (repairStatus == null) {
            throw new CustomException("Repair status cannot be null.");
        }
    }

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
