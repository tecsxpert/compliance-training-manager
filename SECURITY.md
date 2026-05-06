# Security Threat Model (OWASP Top 10)

## 1. Injection (SQL & Prompt Injection)
- **Attack Scenario**: An attacker submits malicious SQL in a search field, or malicious instructions in the AI description text box.
- **Damage Potential**: Data breach, unauthorized system access, or hijacked AI output.
- **Mitigation**: Use JPA Parameterized Queries for database. Use strict system prompts and input sanitisation for AI endpoints.

## 2. Broken Authentication
- **Attack Scenario**: An attacker guesses weak passwords or steals JWT tokens.
- **Damage Potential**: Unauthorized access to admin compliance data.
- **Mitigation**: Enforce strong JWT signing, short token expiration (1 hour), and bcrypt password hashing.

## 3. Sensitive Data Exposure
- **Attack Scenario**: PII or sensitive compliance data is sent to the AI service unencrypted.
- **Damage Potential**: Violation of GDPR and data leak.
- **Mitigation**: Enforce HTTPS/TLS. Strip names/emails before sending text to Groq API.

## 4. Broken Access Control (RBAC)
- **Attack Scenario**: A user with VIEWER role forces a POST request to create or delete a training record.
- **Damage Potential**: Unauthorised data modification.
- **Mitigation**: Use Spring Security `@PreAuthorize("hasRole('ADMIN')")` on sensitive endpoints.

## 5. Security Misconfiguration
- **Attack Scenario**: Leaving default Tomcat error pages or missing security headers.
- **Damage Potential**: Information leakage aiding further attacks.
- **Mitigation**: Implement `@ControllerAdvice` to handle exceptions securely and add security headers (X-Frame-Options).
