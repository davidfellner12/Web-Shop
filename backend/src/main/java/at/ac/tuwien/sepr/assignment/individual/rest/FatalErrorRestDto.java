package at.ac.tuwien.sepr.assignment.individual.rest;

import java.util.List;

public record FatalErrorRestDto(
        /**
         * Dto describing a validation error.
         *
         * @param message Message attached to the validation error
         * @param errors  List of validation errors that occured
         */
        String message
) {
}

