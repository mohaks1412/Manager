package com.finance.manager.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)
class DuplicateResourceException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String) : RuntimeException(message)
class ForbiddenException(message: String) : RuntimeException(message)
class BadRequestException(message: String) : RuntimeException(message)