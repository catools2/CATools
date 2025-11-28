# CATools Common Extension Classes Guide

**Module:** `common.extension`  
**Version:** CATools 2.0.0  
**Java:** 21  
**Purpose:** Type-safe extensions for verification, waiting, and state management

---

## Overview

The `common.extension` module provides three primary groupings of classes that work together to enable fluent, expressive testing APIs:

1. **Verification Classes** - Assert and validate expected behavior
2. **Wait Classes** - Condition-based waiting mechanisms  
3. **State Classes** - Represent object states and boolean conditions

Each grouping supports common Java types: `String`, `Number`, `Boolean`, `Date`, `File`, `Collection`, `Map`, `Iterable`, and `Object`.

---

## 1. VERIFICATION GROUP

### Purpose
Verification classes are used for **asserting and validating** expected behavior in tests. They come in two flavors:
- **Hard Assertions** (package: `verify.hard`) - Immediate failure on assertion failure
- **Soft Assertions** (package: `verify.soft`) - Accumulate failures for later reporting

### Core Components

#### A. **CVerifier** (Main Entry Point)
**Location:** `verify/CVerifier.java`

Central verification orchestrator that manages all type-specific verifiers and verification queue.

**Key Features:**
- Provides fluent API for multiple verification types
- Manages verification queue and batch reporting
- Supports both hard and soft assertion strategies

**Usage Pattern:**
```java
CVerifier verifier = new CVerifier();

// Chain multiple verifications (soft assertions)
verifier.String.equals(actual, expected);
verifier.Number.isGreaterThan(value, 10);
verifier.Bool.isTrue(condition);

// Verify all accumulated expectations
verifier.verify();
verifier.verify("Custom header message");
```

**Type-Specific Verifiers Available:**
- `verifier.Object` - Object verification
- `verifier.String` - String verification
- `verifier.Number.*` - Numeric types (Long, Double, Float, Integer, BigDecimal)
- `verifier.Bool` - Boolean verification
- `verifier.Date` - Date/time verification
- `verifier.File` - File verification
- `verifier.Collection` - Collection verification
- `verifier.Map` - Map verification

---

#### B. **Hard Verification Classes** (Immediate Failure)
**Location:** `verify/hard/`

Extend `CBaseVerification` and fail immediately when assertion fails.

**Classes by Type:**
- `CStringVerification` - String operations (3173 methods including center, compare, concat, etc.)
- `CNumberVerification` - Numeric comparisons and operations
- `CBooleanVerification` - Boolean logic and comparisons
- `CDateVerification` - Date/time operations and comparisons
- `CFileVerification` - File existence, content, and permissions
- `CObjectVerification` - Generic object comparisons
- `CCollectionVerification` - Collection size, content, and membership
- `CMapVerification` - Map key/value operations
- `CIterableVerification` - Iterable traversal and comparisons

**Characteristics:**
- Throw exceptions immediately on assertion failure
- Suitable for early-stage test validation
- Clear, immediate test failure feedback

**Example Method Patterns:**
```java
// String verification methods
public void centerPadEquals(String actual, int size, String padStr, String expected)
public void centerPadEquals(String actual, int size, String padStr, String expected, String message, Object... params)
public void centerPadNotEquals(String actual, int size, String padStr, String expected)
// ... hundreds more methods

// Signature Pattern:
// public void verify[Condition](actual, ...params [, message, params])
```

---

#### C. **Soft Verification Implementations** (Deferred Failure)
**Location:** `verify/soft/`

Extend hard verification classes and queue failures instead of throwing immediately.

**Implementation Classes:**
- `CStringVerifierImpl<T>` - String verification with queuing
- `CNumberVerifierImpl<T, N>` - Numeric verification with queuing
- `CBooleanVerifierImpl<T>` - Boolean verification with queuing
- `CDateVerifierImpl<T>` - Date verification with queuing
- `CFileVerifierImpl<T>` - File verification with queuing
- `CObjectVerifierImpl<T>` - Object verification with queuing
- `CCollectionVerifierImpl<T>` - Collection verification with queuing
- `CMapVerifierImpl<T>` - Map verification with queuing

**Characteristics:**
- Queues verification failures instead of throwing
- Collects all failures for batch reporting
- Defers assertions to `verify()` call
- Returns `CVerificationQueue` reference for fluent chaining

**Generic Type Parameters:**
- `<T extends CVerificationQueue>` - Reference to parent verifier for queue management
- `<N>` (for numeric) - Actual numeric type (Long, Double, Float, Integer, BigDecimal)

---

#### D. **CVerificationInfo** (Failure Information)
**Location:** `verify/CVerificationInfo.java`

Container for verification failure details and test predicates.

**Purpose:**
- Encapsulates assertion information
- Stores test condition and context
- Used internally by soft verification queue

**Structure:**
- Generic type parameters for actual and expected values
- Contains predicate for re-evaluation
- Supports custom failure messages

---

### Verification Workflow

```
User writes test with CVerifier
        ↓
CVerifier delegates to type-specific Verifier (e.g., CStringVerifierImpl)
        ↓
Verification method called
        ↓
Hard Path: Immediate exception throw
Soft Path: Add to CVerificationInfo queue
        ↓
(Soft only) verifier.verify() called
        ↓
Process all queued verifications
        ↓
Report results (pass or fail)
```

---

## 2. WAIT GROUP

### Purpose
Wait classes provide **condition-based waiting mechanisms** for asynchronous operations. They poll a condition at regular intervals until it succeeds or timeout is reached.

### Core Components

#### A. **CBaseWaiter<O>** (Foundation Interface)
**Location:** `wait/interfaces/CBaseWaiter.java`

Base interface inherited by all waiter types, extending `CBaseState<O>`.

**Key Features:**
- Default wait configuration (timeout, interval)
- Configurable per-class or globally
- Supports predicate-based conditions

**Configuration Methods:**
```java
// Get default wait interval (configurable via CTypeExtensionConfigs)
default int getDefaultWaitIntervalInMilliSeconds()

// Get default max wait time (configurable via CTypeExtensionConfigs)
default int getDefaultWaitInSeconds()
```

**Global Configuration:**
- `EXTENSION_DEFAULT_WAIT_IN_SECONDS` - Default timeout (config parameter)
- `EXTENSION_DEFAULT_WAIT_INTERVAL_IN_MILLIS` - Default polling interval (config parameter)

---

#### B. **Type-Specific Waiter Interfaces**
**Location:** `wait/interfaces/`

Specialized waiter interfaces for different data types, all extending `CBaseWaiter<T>`.

**Classes by Type:**
- `CStringWaiter extends CObjectWaiter<String>` - String condition waiting (4639 methods)
- `CNumberWaiter extends CObjectWaiter<N>` - Numeric condition waiting
- `CBooleanWaiter extends CObjectWaiter<Boolean>` - Boolean condition waiting
- `CDateWaiter extends CObjectWaiter<Date>` - Date condition waiting
- `CFileWaiter extends CObjectWaiter<File>` - File condition waiting
- `CObjectWaiter<T>` - Generic object condition waiting
- `CCollectionWaiter extends CIterableWaiter` - Collection condition waiting
- `CIterableWaiter extends CObjectWaiter<Iterable>` - Iterable condition waiting
- `CMapWaiter extends CObjectWaiter<Map>` - Map condition waiting

**Method Pattern - Three Overloads per Condition:**

```java
// 1. Default wait time (using getDefaultWaitInSeconds())
default boolean waitCondition(expectedValue) {
    return waitCondition(expectedValue, getDefaultWaitInSeconds());
}

// 2. Custom wait time
default boolean waitCondition(expectedValue, int waitInSeconds) {
    return waitCondition(expectedValue, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
}

// 3. Custom wait time + interval
default boolean waitCondition(expectedValue, int waitInSeconds, int intervalInMilliSeconds) {
    return _waiter(predicate, waitInSeconds, intervalInMilliSeconds);
}
```

---

#### C. **Wait Method Example**
**From CStringWaiter (4639+ methods):**

```java
/**
 * Wait for default wait time until string center-pad equals expected value
 * @param size the width of the padding
 * @param padStr the padding string
 * @param expected the expected result
 * @return true if condition met within timeout, false otherwise
 */
default boolean waitCenterPadEquals(int size, String padStr, String expected) {
    return waitCenterPadEquals(size, padStr, expected, getDefaultWaitInSeconds());
}

/**
 * Wait for specified time until string center-pad equals expected value
 * @param size the width of the padding
 * @param padStr the padding string
 * @param expected the expected result
 * @param waitInSeconds maximum time to wait
 * @return true if condition met within timeout, false otherwise
 */
default boolean waitCenterPadEquals(int size, String padStr, String expected, final int waitInSeconds) {
    return waitCenterPadEquals(size, padStr, expected, waitInSeconds, getDefaultWaitIntervalInMilliSeconds());
}

/**
 * Wait for specified time with custom interval
 * @param size the width of the padding
 * @param padStr the padding string
 * @param expected the expected result
 * @param waitInSeconds maximum time to wait
 * @param intervalInMilliSeconds polling interval
 * @return true if condition met within timeout, false otherwise
 */
default boolean waitCenterPadEquals(int size, String padStr, String expected, final int waitInSeconds, final int intervalInMilliSeconds) {
    return _waiter(a -> toState(a).centerPadEquals(size, padStr, expected), waitInSeconds, intervalInMilliSeconds);
}
```

---

### Wait Workflow

```
User calls waitCondition(expectedValue)
        ↓
Resolve to 3-parameter version with defaults
        ↓
Create predicate using toState() conversion
        ↓
Loop until timeout:
  - Test predicate
  - If true: return true
  - If false: sleep for interval
        ↓
On timeout: return false
```

---

## 3. STATE GROUP

### Purpose
State classes represent **testable boolean conditions** on objects. They form the foundation for both verification and waiting mechanisms.

### Core Components

#### A. **CBaseState<O>** (Foundation Interface)
**Location:** `states/interfaces/CBaseState.java`

Base interface for all state classes, providing core behavior representation.

**Key Features:**
- Serializable for test data persistence
- Provides internal getter: `_get()`
- Foundation for condition evaluation

**Interface Structure:**
```java
public interface CBaseState<O> extends Serializable {
    long serialVersionUID = 6067874018185613757L;
    Logger logger = LoggerFactory.getLogger(CBaseVerify.class);
    
    /**
     * For internal use only - returns the wrapped object
     */
    @JsonIgnore
    O _get();
}
```

---

#### B. **Type-Specific State Interfaces**
**Location:** `states/interfaces/`

Specialized state interfaces for different data types, all extending `CBaseState<T>`.

**Classes by Type:**
- `CStringState extends CObjectState<String>` - String conditions (1790+ methods)
- `CNumberState extends CObjectState<N>` - Numeric conditions
- `CBooleanState extends CObjectState<Boolean>` - Boolean conditions
- `CDateState extends CObjectState<Date>` - Date conditions
- `CFileState extends CObjectState<File>` - File conditions
- `CObjectState<T>` - Generic object conditions
- `CCollectionState extends CIterableState` - Collection conditions
- `CIterableState extends CObjectState<Iterable>` - Iterable conditions
- `CMapState extends CObjectState<Map>` - Map conditions

---

#### C. **State Method Pattern**
**From CStringState (1790+ methods):**

All state methods return `boolean` representing condition success/failure.

```java
/**
 * State: Check if string center-pad equals expected value
 * @param size the width of the padding
 * @param padStr the padding string
 * @param expected the expected result
 * @return true if condition is true, false otherwise
 */
default boolean centerPadEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && Strings.CS.equals(StringUtils.center(_get(), size, padStr), expected);
}

/**
 * State: Check if string center-pad NOT equals expected value
 * @param size the width of the padding
 * @param padStr the padding string
 * @param expected the expected result
 * @return true if condition is true, false otherwise
 */
default boolean centerPadNotEquals(int size, String padStr, String expected) {
    return _get() != null
        && expected != null
        && !Strings.CS.equals(StringUtils.center(_get(), size, padStr), expected);
}
```

**Characteristics:**
- Purely functional, no side effects
- Null-safe evaluations
- Composable conditions
- Support for both positive and negative assertions

---

### State-Verification-Wait Integration

```
State Classes
  ↓
  ├─→ Verification Classes use states to evaluate assertions
  │   (State method called, result is asserted)
  │
  └─→ Wait Classes use states in polling loop
      (State method called repeatedly until success or timeout)
```

**Example Flow:**
```
Wait for String condition
  ↓
CStringWaiter.waitCenterPadEquals()
  ↓
Use CStringState.centerPadEquals() in predicate
  ↓
Poll state evaluation
  ↓
When state returns true, wait succeeds
```

---

## 4. TYPE-SPECIFIC EXTENSION CLASSES

### Purpose
Type extension classes provide **fluent API extensions** to standard Java types, enabling method chaining for common operations.

### Components

#### A. **Static Extensions** (Utility Methods)
**Location:** `types/` and `types/interfaces/`

Classes with 'C' prefix plus "Extension" providing static utility methods.

**Classes:**
- `CStaticStringExtension` - String utilities
- `CStaticNumberExtension` - Numeric utilities
- `CStaticBooleanExtension` - Boolean utilities
- `CStaticDateExtension` - Date/time utilities
- `CStaticFileExtension` - File utilities
- `CStaticObjectExtension` - Object utilities
- `CStaticCollectionExtension` - Collection utilities
- `CStaticIterableExtension` - Iterable utilities
- `CStaticMapExtension` - Map utilities

**Usage Pattern:**
```java
String result = CStaticStringExtension.someUtility(input);
int count = CStaticCollectionExtension.findMatches(collection, predicate);
```

---

#### B. **Dynamic Extensions** (Instance Methods)
**Location:** `types/` and `types/interfaces/`

Classes enabling fluent chaining on object instances.

**Classes:**
- `CDynamicStringExtension` - String instance methods
- `CDynamicNumberExtension` - Numeric instance methods
- `CDynamicBooleanExtension` - Boolean instance methods
- `CDynamicObjectExtension` - Object instance methods
- `CDynamicCollectionExtension` - Collection instance methods
- `CDynamicIterableExtension` - Iterable instance methods
- `CDynamicDateExtension` - Date instance methods
- `CDynamicFileExtension` - File instance methods
- `CDynamicMapExtension` - Map instance methods

**Usage Pattern (with extension methods):**
```java
// Pseudo-code showing fluent chaining capability
myString
    .normalize()
    .center(10, "*")
    .verify(expected)
    .wait(condition);
```

---

## 5. UTILITY SUPPORT CLASSES

### A. **CDate** and **CRandomDateGenerator**
**Location:** `date/`

Date manipulation and random generation utilities.

**Purpose:**
- Provide date/time utilities for testing
- Generate random dates for test data
- Support date verification and waiting

---

### B. **CFile** and **CResource**
**Location:** `io/`

File and resource handling utilities.

**Purpose:**
- File operation utilities for testing
- Resource access from classpath
- Support file verification and waiting

---

## 6. CLASS ORGANIZATION SUMMARY

```
common.extension/
│
├── verify/                          (VERIFICATION GROUP)
│   ├── CVerifier.java              # Main verifier orchestrator
│   ├── CVerificationInfo.java       # Failure information container
│   │
│   ├── hard/                        # Hard assertions (immediate failure)
│   │   ├── CBaseVerification.java
│   │   ├── CStringVerification.java
│   │   ├── CNumberVerification.java
│   │   ├── CBooleanVerification.java
│   │   ├── CDateVerification.java
│   │   ├── CFileVerification.java
│   │   ├── CObjectVerification.java
│   │   ├── CCollectionVerification.java
│   │   ├── CMapVerification.java
│   │   └── CIterableVerification.java
│   │
│   └── soft/                        # Soft assertions (deferred failure)
│       ├── CStringVerifierImpl.java
│       ├── CNumberVerifierImpl.java
│       ├── CBooleanVerifierImpl.java
│       ├── CDateVerifierImpl.java
│       ├── CFileVerifierImpl.java
│       ├── CObjectVerifierImpl.java
│       ├── CCollectionVerifierImpl.java
│       ├── CMapVerifierImpl.java
│       └── CIterableVerifierImpl.java
│
├── wait/                            (WAIT GROUP)
│   └── interfaces/
│       ├── CBaseWaiter.java         # Wait foundation
│       ├── CStringWaiter.java       # String waiting (4639+ methods)
│       ├── CNumberWaiter.java       # Numeric waiting
│       ├── CBooleanWaiter.java      # Boolean waiting
│       ├── CDateWaiter.java         # Date waiting
│       ├── CFileWaiter.java         # File waiting
│       ├── CObjectWaiter.java       # Generic waiting
│       ├── CCollectionWaiter.java   # Collection waiting
│       ├── CIterableWaiter.java     # Iterable waiting
│       └── CMapWaiter.java          # Map waiting
│
├── states/                          (STATE GROUP)
│   └── interfaces/
│       ├── CBaseState.java          # State foundation
│       ├── CStringState.java        # String states (1790+ methods)
│       ├── CNumberState.java        # Numeric states
│       ├── CBooleanState.java       # Boolean states
│       ├── CDateState.java          # Date states
│       ├── CFileState.java          # File states
│       ├── CObjectState.java        # Generic states
│       ├── CCollectionState.java    # Collection states
│       ├── CIterableState.java      # Iterable states
│       └── CMapState.java           # Map states
│
├── types/                           (EXTENSION GROUP)
│   ├── CStaticStringExtension.java
│   ├── CStaticNumberExtension.java
│   ├── CStaticBooleanExtension.java
│   ├── CStaticDateExtension.java
│   ├── CStaticFileExtension.java
│   ├── CStaticObjectExtension.java
│   ├── CStaticCollectionExtension.java
│   ├── CStaticIterableExtension.java
│   ├── CStaticMapExtension.java
│   ├── CDynamicStringExtension.java
│   ├── CDynamicNumberExtension.java
│   ├── CDynamicBooleanExtension.java
│   ├── CDynamicDateExtension.java
│   ├── CDynamicFileExtension.java
│   ├── CDynamicObjectExtension.java
│   ├── CDynamicCollectionExtension.java
│   ├── CDynamicIterableExtension.java
│   ├── CDynamicMapExtension.java
│   └── interfaces/
│       ├── CStaticStringExtension.java
│       ├── CStaticNumberExtension.java
│       ├── ... (interface definitions)
│       ├── CDynamicStringExtension.java
│       ├── CDynamicNumberExtension.java
│       └── ... (interface definitions)
│
├── collections/                     (UTILITY GROUP)
│   └── Collection utilities
│
├── date/                            (UTILITY GROUP)
│   ├── CDate.java
│   └── CRandomDateGenerator.java
│
└── io/                              (UTILITY GROUP)
    ├── CFile.java
    └── CResource.java
```

---

## 7. USAGE EXAMPLES

### Example 1: Hard Verification (Immediate Failure)
```java
CStringVerification verification = new CStringVerification();

// Immediate failure if assertion fails
verification.equals(actualString, "expected");
verification.contains(actualString, "substring");
// Test fails here if condition not met
```

---

### Example 2: Soft Verification (Deferred Failure)
```java
CVerifier verifier = new CVerifier();

// Assertions accumulate without throwing
verifier.String.equals(userName, "John");
verifier.Number.isGreaterThan(age, 18);
verifier.Bool.isTrue(isActive);

// All failures reported together at verification point
verifier.verify("User validation checks");
```

---

### Example 3: Waiting for Condition
```java
public class MyWaiter implements CStringWaiter {
    private String value;
    
    @Override
    public String _get() {
        return value;
    }
}

MyWaiter waiter = new MyWaiter();

// Wait with default timeout
boolean found = waiter.waitContains("expected");

// Wait with custom timeout (10 seconds)
boolean found = waiter.waitContains("expected", 10);

// Wait with custom interval
boolean found = waiter.waitContains("expected", 10, 500);
```

---

### Example 4: State Evaluation
```java
public class MyState implements CStringState {
    private String value;
    
    @Override
    public String _get() {
        return value;
    }
}

MyState state = new MyState();

// Evaluate conditions
if (state.equals("expected")) {
    // Condition is true
}

if (state.contains("substring")) {
    // Condition is true
}
```

---

## 8. DESIGN PATTERNS

### Pattern 1: Verification Pipeline
```
CVerifier
  ↓
Type-Specific VerifierImpl (e.g., CStringVerifierImpl)
  ↓
Hard Verification Class (e.g., CStringVerification)
  ↓
Verification Interface (e.g., CStringVerify)
  ↓
State Interface (e.g., CStringState)
  ↓
Boolean Result
```

### Pattern 2: Wait Loop
```
WaiterInterface (e.g., CStringWaiter)
  ↓
toState() conversion
  ↓
State condition evaluation
  ↓
Loop with polling interval
  ↓
Success (true) or Timeout (false)
```

### Pattern 3: Type Hierarchy
```
CBaseState<T> or CBaseWaiter<T>
  ↓
CObjectState<T> or CObjectWaiter<T>
  ↓
Type-Specific Interface (e.g., CStringState, CStringWaiter)
  ↓
Implementation or extension
```

---

## 9. KEY FEATURES

### Fluent API
All verification and wait methods support method chaining for expressive test code.

### Type Safety
Generic type parameters ensure compile-time type safety across all operations.

### Flexibility
- Configurable timeouts and intervals
- Both immediate and deferred failure modes
- Composable conditions and states

### Comprehensive Coverage
- 3000+ String methods
- Support for all primitive and common types
- Collections, Maps, Iterables
- File and Date operations

### Null Safety
State and verification methods include null checks to prevent NPE.

---

## 10. CONFIGURATION

All waiter and verifier behavior can be configured via `CTypeExtensionConfigs`:

```java
// Configuration parameters
EXTENSION_DEFAULT_WAIT_IN_SECONDS          // Default wait timeout
EXTENSION_DEFAULT_WAIT_INTERVAL_IN_MILLIS  // Default polling interval
```

Override via system properties or configuration files as needed for test environment.

---

## Summary Table

| Group | Purpose | Failure Mode | Entry Point | Base Class |
|-------|---------|--------------|-------------|-----------|
| **Verification** | Assert expected behavior | Immediate or Deferred | `CVerifier` | `CBaseVerification` |
| **Wait** | Poll condition until timeout | Boolean return | `CXxxWaiter` interface | `CBaseWaiter<T>` |
| **State** | Evaluate boolean condition | Boolean return | `CXxxState` interface | `CBaseState<T>` |

