package org.cerion.symcalc.exception


class ValidationException(error: String) : Exception(error)

class OperationException(error: String) : Exception(error)

class IterationLimitExceeded : Exception("Iteration limit exceeded")