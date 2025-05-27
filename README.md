# Concurrency Utilities

This library provides a set of concurrency utilities 
designed to simplify the use of concurrent execution and
synchronization in Kotlin for the Angelos Project™.

The utility builds on top of Kotlin's coroutines and provides 
additional lambda methods and classes to handle 
execution of tasks in an idiomatic way.

The library's primary focus is to provide a set of tools that
gives the developer a hassle-free experience without meddling with
the underlying implementation. Methods that can be called from 
any context whether suspending or blocking. Which doesn't use
any features that are experimental, and which is available across
all stable Kotlin build targets.

# Features
- **task()**: A higher-order function that runs a given lambda as a coroutine exactly once. It can be called from any synchronous code block and executes the lambda asynchronously.
- **schedule()**: Works like **task()** but also takes a given time duration as an argument and delays execution of the coroutine accordingly.
- **clock()**: Works like **schedule()** but also receives a number of ticks per time duration as an argument, and executes the lambda periodically at the given time interval.
- **loop()**: Indifferently from **clock()**, it takes no arguments beyond the lambda. It loops the lambda infinitly, yielding to the event loop after each iteration.
- **call()**: Takes another coroutine job and a time interval as arguments. It repeatedly checks the coroutine's state, only if the job is cancelled the given lambda is executed.
- **attend()**: Takes a lambda as a coroutine and returns a Steward instance. The Steward goes to sleep in an infinite loop and executes its lambda on wakeUp(), while executing the coroutine, subsequent wake-up calls are registered and queued for faithful execution.
- **answer()**: Like **attend()** it returns a similar instance called Waitress. The Waitress differently from the Steward only answers the wakeUp() call once, while subsequent calls are ignored until it goes to sleep again.
- **Dispenser**: A class that encapsulates a shared resource. It can dispense the resource in a higher-order function wrapped in a `Mutex`, to a single coroutine at a time. Acting as a synchronization primitive for shared resources.

# Installation
To use the concurrency utilities in your Kotlin project, add the following dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("org.angproj.conc:angelos-project-conc:0.1.3-SNAPSHOT")
}
```

# Contributing
We welcome contributions to this project! If you have an idea for a new feature, bug fix, or improvement, please open an issue or submit a pull request.

# License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
