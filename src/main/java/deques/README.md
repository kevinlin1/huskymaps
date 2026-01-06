# Deques

A **deque** (pronounced "deck") is an abstract data type representing a **d**ouble-**e**nded **que**ue. Deques are linear collections (like lists, stacks, and queues) optimized for accessing, adding, and removing elements from both the front and the back. Deques differ from lists in that they do not allow elements to be added or removed from anywhere except for the front or the back. This restriction might make it seem like deques are much less useful than lists. Indeed, any problem we can solve using a deque we can also solve using a list!

But usefulness is not the only metric for determining the quality of a program. Imagine we're engineering a web browser, and we're addressing a performance problem in how the browser keeps track of history. When a user visits a web page, the visit is recorded in the browser history by adding the link (and extra information like when the page was visited) to the end of an `ArrayList`. But there's a problem: users with large browser histories are reporting major performance degradation when they enable some privacy cleaning features.

Open the [Testing Explorer](https://code.visualstudio.com/docs/java/java-testing#_testing-explorer) from the Activity Bar and navigate to **deques \| ArrayListDequeTests \| browserHistoryRuntimeSimulation**. Run the test and see what happens. How long does it take to run? What might be the cause of the performance problem?

## Purpose

In this project, we'll explore the performance problem you observed in the browser history problem by applying data structure and algorithm engineering perspectives.

First, we'll [**design and implement**](#design-and-implement) data structures using two different approaches. Starting with **array-based structures**, we'll examine the limits of the `ArrayListDeque` invariants and address the performance problem with a more clever `ArrayDeque`. Then, we'll build a **node-based structure**, the `LinkedDeque`. While we'll rarely need to write a linked list from scratch in the real world, understanding how to design and build linked data structures is crucial for the rest of this course. It's the foundational concept behind the advanced data structures that power everything from search databases (trees) to navigation maps (graphs) that we'll encounter throughout the rest of the course.

After implementing the different deques, we'll [**analyze and compare**](#analyze-and-compare) them using two methods to understand their trade-offs. **Asymptotic analysis** offers a theoretical approach—it allows you to predict performance and talk about efficiency using a formal mathematical language. This is often helpful for deciding between approaches _before_ you invest programming time implementing an approach. **Experimental analysis** offers a practical approach—by timing exactly how long code takes to run, we can see how it _actually_ performs in the real world.

## Deque interface

In Java, every variable has a **data type**, such as `int`, `boolean`, `String`, or `List`. Data types combine representation (how data is structured in the computer) with functionality (methods that can be applied to the data). For example, we can have an `int x = 373` and a `String s = "373"`. Although they both hold similar content, in Java, an `int` represents integer numbers within a certain range whereas a `String` represents a sequence of characters. The functionality of the plus operator differs for `x + x` (sum of two numbers) versus `s + s` (concatenation of two strings).

**Abstract data types** (ADTs) are data types that only include a specification for the functionality of the data type without specifying the representation. In Java, abstract data types are often represented using **interfaces** like `List`, `Set`, or `Map`. Then, **classes** like `ArrayList`, `TreeSet`, or `HashMap` provide specific implementations for an interface. For example, `List` is an interface with implementations such as `ArrayList` and `LinkedList`.

Deques are like lists but without the capability to add, remove, or get elements from anywhere except for the front or the back. Review [`Deque.java`](Deque.java) to see the interface and its methods.

<details>
<summary><strong>Which method in the <code>Deque</code> interface does not match the definition of a deque?</strong></summary>

The `get` method has the capability to get elements from anywhere in the deque (by index) whereas as the formal definition of a deque only allows access to the front or back element. This `get` method is included for testing purposes only.
</details>

### Reference implementation

We've provided a reference implementation that will help us evaluate the performance problem with `ArrayList`. The `ArrayListDeque` class implements `Deque` using an `ArrayList`. The class maintains a single field called `list` that stores all the elements in the deque, where the _i_-th element in the deque is always stored at `list[i]`.

<details>
<summary><strong>How does <code>ArrayListDeque</code> relate to <code>Deque</code>?</strong></summary>

`ArrayListDeque` is a class (implementation) for the interface (abstract data type) `Deque`. In other words, `Deque` only defines functionality; `ArrayListDeque` specifies how that functionality is actually achieved.
</details>

<details>
<summary><strong>How does <code>ArrayListDeque</code> relate to <code>ArrayList</code>?</strong></summary>

`ArrayListDeque` uses an `ArrayList` to implement the `Deque` functionality. But it's an implementation detail: since any programmer using `ArrayListDeque` will not be able to access this underlying `ArrayList` because it is _encapsulated_ with private fields.
</details>

## Design and implement

The focus of this course is not only to build programs that work according to specifications but also to compare different approaches and evaluate the consequences of our designs. In this project, we'll compare the `ArrayListDeque` reference implementation against two other ways to implement the `Deque` interface.

### Fix [`ArrayDeque.java`](ArrayDeque.java)

An **array deque** is like an `ArrayList`, but different in that elements aren't necessarily stored starting at index 0. Instead, their start and end positions are determined by two fields called `front` and `back`.

<details>
<summary><strong>How does <code>ArrayListDeque</code> relate to <code>ArrayDeque</code>?</strong></summary>

`ArrayListDeque` is not particularly related to `ArrayDeque` in concept. They just happen share a similar-sounding name. It would be more appropriate to read `ArrayListDeque` as the following sentence: a class that uses an `ArrayList` to implement `Deque` functionality.
</details>

We've provided an `ArrayDeque` class that includes a bug, and four failing test cases that cause the bug to emerge. Identify and fix the bug in the `ArrayDeque` class by **changing at least 2 lines of code**. Follow the debugging cycle to address the bug.

1. Review `ArrayDeque` to see how its methods and fields work together to implement `Deque`.

1. Run the `ArrayDequeTests` class inside the [`src/test/java/deques`](../../../test/java/deques/) folder.

1. Read the test result and review the stack trace (the chain of calls that caused the exception).

1. Review `ArrayDeque` again, this time focusing on methods most relevant to the failing test. Open [`DequeTests.java`](../../../test/java/deques/DequeTests.java) and [drag the tab for a side-by-side view](https://code.visualstudio.com/docs/getstarted/userinterface#_side-by-side-editing).

1. Based on what we know about the bug, develop a hypothesis for the cause of the problem.

For example, we might _hypothesize_ that the problem is caused by the `newIndex` variable inside the `resize` method going outside the bounds of the `newData` array. Gathering information that can confirm or deny this hypothesis can help us zero-in on the problem, leading us to generate another hypothesis or a potential fix to the bug. Debugging is the process of exploring hypotheses, generating potential fixes, trying them out, and learning more information about the problem until we finally identify the root cause of the bug.

> [!caution]
> It's easy to lose track of time and get stuck debugging. Come to office hours, chat with other students, or return after taking a break! Read the `confusingTest` carefully and write a more minimal test case that reproduces the problem using a simpler sequence of instructions.

To develop a hypothesis, we can use the debugger to pause the program at any point in time. At each step, we can compare our thinking to the state of the debugger.

> [!important]
> State your hypothesis for the bug in the `ArrayDeque` class and the lines of code that you changed to address the hypothesis. Explain why this change was necessary to help maintain the integrity of the codebase.

### Stage, commit, and push changes to GitLab

Unlike collaboration tools like Google Docs that automatically save changes as we make them, Git requires us to manually version our code. This is helpful for programming because we often want to try-out a change, but we might not be immediately ready to share it with others until we've fully-tested the change and ensure it works as intended.

Once we're satisfied with our implementations, the last step is to [stage and commit code changes](https://code.visualstudio.com/docs/sourcecontrol/intro-to-git#_staging-and-committing-code-changes). Provide a descriptive commit message that follows the scoped message style, such as "src: Fixed ArrayDeque.resize bug". You may choose a more specific scope, such as "deques: Fixed ArrayDeque.resize bug" to make the messages more useful for yourself in the future.

After committing your changes, [push the commit](https://code.visualstudio.com/docs/sourcecontrol/intro-to-git#_pushing-and-pulling-remote-changes) to GitLab.

### Check your GitLab pipeline

Now that the commit has been pushed to GitLab, let's check the status of the changes. View the latest GitLab [CI/CD pipelines](https://docs.gitlab.com/ci/pipelines/#view-pipelines) by visiting [CSE GitLab](https://gitlab.cs.washington.edu/), selecting your private repository, and then choosing **Build \| Pipelines** from the sidebar.

Since only `ArrayListDeque` and `ArrayDeque` are functional at this time, there will be an overall failure state for the tests even though the `ArrayListDequeTests` and `ArrayDequeTests` pass.

### Implement [`LinkedDeque.java`](LinkedDeque.java)

To address the `LinkedDequeTests` failure, we need to implement the `LinkedDeque` class with the following requirements:

1. The methods `addFirst`, `addLast`, `removeFirst`, and `removeLast` must run in constant time with respect to the size of the deque. To achieve this, don't use any iteration or recursion.

1. The amount of memory used by the deque must always be proportional to its size. If a client adds 10,000 elements and then removes 9,999 elements, the resulting deque should use about the same amount of memory as a deque where we only ever added 1 element. To achieve this, remove references to elements that are no longer in the deque.

1. The class is implemented with the help of two **sentinel nodes**: special nodes that don't contain any meaningful data and are always present in the data structure, even when it's empty. Sentinel nodes allow us to avoid special cases like checking if a deque is empty.

A `LinkedDeque` should always maintain the following invariants before and after each method call:

1. The `front` field always references the front sentinel node, and the `back` field always references the back sentinel node.

1. The sentinel nodes `front.prev` and `back.next` always reference null. If `size` is at least 1, `front.next` and `back.prev` reference the first and last regular nodes.

1. The nodes in the deque have consistent `next` and `prev` fields. If a node `curr` has a `curr.next`, we expect `curr.next.prev == curr`.

> [!tip]
> Write down what your `LinkedDeque` will look like on paper before writing code! Drawing more pictures often leads to more successful implementations. Better yet, if you can find a willing partner, have them give some instructions while you attempt to draw everything out. Plan-out and double-check what you want to change before writing any code. **The staff solution adds between 4 to 6 lines of code per method and doesn't introduce any additional `if` statements or unnecessary null assignments.**

To assist in debugging, we've provided a `checkInvariants` method that returns a string describing any problems with invariants (at the time the method is called), or null if there are no problems. Add debugging print statements with this `checkInvariants` method to help verify a hypothesis. Lastly, if the first try goes badly, don't be afraid to try again from scratch.

> [!important]
> Trace through the `addAndRemove` test with your `LinkedDeque` starting from the first iteration of the `for` loop. Explain the arguments and return values of each method call as well as how each line of code changes the `LinkedDeque` representation. For methods that are called more than once, just trace through the method the first time.

As before, stage, commit, and push the `LinkedDeque` implementation to GitLab with a descriptive, scoped commit message.

## Analyze and compare

### Asymptotic analysis

In computer science, simpler solutions are typically preferred over more complicated solutions because they're less likely to contain subtle bugs. `ArrayListDeque` provided a simple solution to implementing a deque, but exhibit performance problems. How does `ArrayDeque` compare to `ArrayListDeque`?

> [!important]
> Give a **big-theta best case** and **big-theta worst case** asymptotic runtime analysis for `addLast` and `removeFirst` in both `ArrayDeque` and `ArrayListDeque`. Then, explain your case analysis and reasoning for each implementation, and why it connects to your determined bound in a few sentences.

### Experimental analysis

At the bottom of [`DequeTests.java`](../../../test/java/deques/DequeTests.java) is a nested class called `RuntimeExperiments`. This nested class defines the code that will be used to evaluate the program's runtime by measuring how long it takes to run on your computer.

By default, the `RuntimeExperiments` class is annotated with the tag `@Disabled` right above the class header. Remove the `@Disabled` line and run the tests. For each implementation's `RuntimeExperiments`, open it to see the average time it takes to make a single call to `addLast` on a deque that already contains `size` number of elements.

Copy-paste each result into its own [Desmos graphing calculator](https://www.desmos.com/calculator) to plot all the points.

> [!important]
> Compare your plots for the `addLast` method between all three implementations: `ArrayListDeque`, `ArrayDeque`, and `LinkedDeque`. Then, identify an operation that should show a significant difference between `ArrayListDeque` and the `ArrayDeque`, and modify the `RuntimeExperiments` class so that it measures this difference. Compare your new plots to confirm that `ArrayDeque` is more efficient than `ArrayListDeque` for your operation.

To modify the `RuntimeExperiments` class to measure the runtime of a specific operation, change the call to `deque.addLast(size)` in the inner-most loop to the operation. Finally, change the following `deque.removeLast()` call to perform the opposite operation. For example, to measure the runtime of `removeLast`, then the opposite operation would be `addLast`.

> [!caution]
> Enabling the `RuntimeExperiments` class will significantly increase the overall time it takes to run tests. Be careful about staging changes to avoid mistakenly pushing a commit enabling `RuntimeExperiments`. If that happens, cancel the pipeline in GitLab and correct the situation by making a new commit that re-disables the `RuntimeExperiments` class.
