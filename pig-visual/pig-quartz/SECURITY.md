# Quartz Module Security

## Remote Code Execution (RCE) Vulnerability Fix

### Issue Description
In versions 3.8.2 and below, the Quartz management module allowed execution of arbitrary Java classes through reflection without proper validation. This could be exploited to execute malicious code using classes like `jakarta.el.ELProcessor`.

### Attack Vector Example
An attacker with access to the Quartz management interface could create a task with:
- **Class Name**: `jakarta.el.ELProcessor`
- **Method Name**: `eval`
- **Parameters**: `Runtime.getRuntime().exec("malicious command")`

This would result in remote code execution on the server.

### Security Fix Implementation

#### 1. Class Name Validator (`ClassNameValidator.java`)
A comprehensive validator that maintains blacklists of dangerous classes, packages, and methods:

**Blocked Classes:**
- `jakarta.el.ELProcessor`, `javax.el.ELProcessor` - Expression Language processors
- `javax.script.ScriptEngineManager`, `javax.script.ScriptEngine` - Script engines
- `java.lang.Runtime`, `java.lang.ProcessBuilder` - System command execution
- `java.lang.reflect.*` - Reflection API
- `java.lang.ClassLoader`, `java.net.URLClassLoader` - Class loaders
- `javax.naming.*` - JNDI classes
- `java.rmi.*` - RMI classes
- `java.io.ObjectInputStream`, `java.io.ObjectOutputStream` - Serialization

**Blocked Packages:**
- `sun.*`, `com.sun.*` - Internal JDK classes
- `jdk.internal.*` - Internal JDK classes
- `java.lang.reflect.*` - Reflection API
- `java.security.*` - Security API
- `javax.naming.*` - JNDI
- `javax.script.*` - Scripting API
- `java.rmi.*` - RMI API
- `javax.management.*` - JMX
- `org.springframework.context.support.FileSystemXmlApplicationContext` - Spring context manipulation
- `org.springframework.expression.spel.*` - Spring Expression Language

**Blocked Methods:**
- `exec`, `eval`, `execute` - Code execution
- `invoke`, `newInstance` - Reflection
- `forName`, `loadClass`, `defineClass` - Class loading
- `getRuntime`, `getMethod`, `getDeclaredMethod` - System access

#### 2. Multi-Layer Defense

**Layer 1: Controller Validation** (`SysJobController.java`)
- Validates class and method names when creating tasks (POST `/sys-job`)
- Validates class and method names when updating tasks (PUT `/sys-job`)
- Returns HTTP 400 error with descriptive message if validation fails

**Layer 2: Execution Validation** (`JavaClassTaskInvok.java`)
- Validates class and method names before reflection execution
- Throws `TaskException` if validation fails
- Prevents execution even if controller validation is bypassed

### Testing

The fix includes comprehensive test coverage:

**Unit Tests** (`ClassNameValidatorTest.java`)
- 8 test cases covering validation logic
- Tests for dangerous classes, packages, methods
- Tests for valid inputs and edge cases

**Security Tests** (`JavaClassTaskInvokSecurityTest.java`)
- 7 test cases specifically for RCE attack vectors
- Tests blocking of:
  - `jakarta.el.ELProcessor` with `eval` method
  - `java.lang.Runtime` with `exec` method
  - `java.lang.ProcessBuilder` 
  - `javax.script.ScriptEngineManager`
  - `java.lang.ClassLoader`
  - `javax.naming.InitialContext` (JNDI injection)
  - Dangerous method names

All 15 tests pass successfully, confirming the vulnerability is fixed.

### Usage Guidelines

#### For Administrators
1. **Update immediately** to a version containing this fix
2. **Review existing tasks** in the Quartz management interface for any suspicious entries
3. **Audit logs** for any attempts to create tasks with dangerous classes
4. **Restrict access** to the Quartz management interface to trusted administrators only

#### For Developers
When creating custom task classes:
1. Use application-specific package names (e.g., `com.yourcompany.tasks.*`)
2. Avoid using reflection, class loaders, or system commands in task methods
3. Implement proper input validation in your task classes
4. Use descriptive method names that don't match dangerous patterns

### Safe Task Example

```java
package com.pig4cloud.pig.daemon.tasks;

public class DataCleanupTask {
    
    // Safe method - no dangerous operations
    public String execute() {
        // Perform data cleanup logic
        return "0"; // Return "0" for success
    }
    
    // Safe method with parameters
    public String executeWithParams(String params) {
        // Validate input
        if (params == null || params.isEmpty()) {
            return "1"; // Return "1" for failure
        }
        // Perform task logic
        return "0";
    }
}
```

### Configuration in Quartz Management

**Safe Configuration:**
- **Class Name**: `com.pig4cloud.pig.daemon.tasks.DataCleanupTask`
- **Method Name**: `execute` or `executeWithParams`
- **Parameters**: Simple string parameters only

**Blocked Configuration:**
- **Class Name**: `jakarta.el.ELProcessor` ❌
- **Method Name**: `eval` ❌
- **Class Name**: `java.lang.Runtime` ❌
- **Method Name**: `exec` ❌

### Security Summary

✅ **Fixed**: Remote Code Execution vulnerability through reflection
✅ **Protection**: Multi-layer validation (controller + execution)
✅ **Coverage**: Comprehensive blacklist of dangerous classes and methods
✅ **Tested**: 15 test cases verify the fix
✅ **Impact**: Prevents arbitrary code execution via Quartz tasks

### References

- CVE: Pending assignment
- Affected Versions: pig <= 3.8.2
- Fixed in Version: 3.9.2+
- Severity: Critical
