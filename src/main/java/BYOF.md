We'll first design an interface and write a simple implementation together before branching off to write and test optimized implementations individually.

## Collaboratively identify a feature

> [!important]
> Identify a feature you'd like to add to Husky Maps. Explain why you chose this feature and how it would improve Husky Maps. If it requires an outside dataset, where would we find it?

Your feature should be clearly representable as a data structures and algorithms problem that involves storing, retrieving, and searching for data in a structured and deterministic way. Avoid non-deterministic features inspired by machine learning algorithms. Below are some example features that you may use for your project.

1. **Tagged location search**: Search for places not only by name, but by their tags.
1. **Custom collection creation**: Create personalized sets of locations.
1. **Walking or biking loop search**: Find natural loops starting from a chosen point.
1. **Neighborhood block detection**: Identify small cycles that form city blocks.
1. **Jogging route generation**: Generate loops of a target minimum or maximum length.
1. **Broken trail detection**: Find hiking trails that are almost complete loops.
1. **Sightseeing loops**: Generate scenic loop tours that pass by a number of landmarks.
1. **Flattest route**: Incorporate elevation data to minimize slope when finding routes.
1. **Quiet route finder**: Use noise-level data to avoid loud or high-traffic roads.
1. **Park amenities filtering**: Use a park amenities dataset to find parks with features.
1. **Public art & landmarks**: Use public art datasets to find the nearest public art.
1. **Bike rack finder**: Use Seattle's bike infrastructure dataset to find nearby bike racks.

If you prefer a feature not on this list, discuss your idea with the course staff as soon as possible to assess feasibility.

## Collaboratively describe and implement an interface

> [!important]
> Describe an interface for your feature with at least 3 public methods that modify object state in a meaningful way. Commit and push your Java interface as a new package under `src/main/java`.

Your interface should build on your chosen feature in a creative way. Provide details about intended behavior, but avoid discussing implementation details. Before proceeding to the next step, consider whether your interface would allow for multiple substantively different approaches, enough so that each team member can [implement their own improved approach](#individually-implement-an-improved-approach).

Agentic coding tools will automatically read the `AGENTS.md` file located in the [project root](../../../AGENTS.md), which will limit the model's ability to generate large amounts of code. Write your own `AGENTS.md` file (or [explore examples](https://agents.md/#examples)) and save it in your new package to enable code generation for your feature.

> [!TIP]
> **Engineer the Harness**: Adopt the mindset of "Harness Engineering" described in [My AI Adoption Journey](https://mitchellh.com/writing/my-ai-adoption-journey).
> 1. **Iterate on Prompts**: Whenever the AI makes a mistake, don't just fix the code. Update your `AGENTS.md` to prevent that specific mistake from happening again.
> 2. **Automated Verification**: Your tests act as the "Programmed Tools" of the harness. Use them to instantly verify the AI's work.

## Collaboratively describe and implement a simple approach

> [!important]
> Describe in English a specification for a simple approach that could implement your interface. Commit and push an implementation of the simple approach.

Make sure your approach is simple enough that it is unambiguously correct. Your submitted code should primarily use Java programming features and concepts that we have learned in this course or prerequisite courses. Out-of-scope Java features may be used sparingly with justification and clear explanation as to why it would be much more difficult to implement without the out-of-scope Java features. Since generative AI is known to make mistakes, it is your responsibility to carefully review and verify all work for correctness.

## Collaboratively test the simple implementation

> [!important]
> Commit and push property-based or example-based testing methods that together achieve at least 85% [test coverage](https://code.visualstudio.com/docs/debugtest/testing#_test-coverage) for the simple implementation. Add your tests to a new package under `src/test/java`. Provide screenshots of your test coverage panel and links to your test files.

As with program logic, tests should also primarily use Java programming features and concepts that we have learned before, but you may refer to the [user guide](https://jqwik.net/docs/current/user-guide.html) for examples of how more advanced Java features can be used to reduce the amount of code needed to express testing logic. The user guide also discusses how to [create example-based tests](https://jqwik.net/docs/current/user-guide.html#creating-an-example-based-test). Examples can be found in the tests for [Deques](../../test/java/deques/DequeTests.java), [Autocomplete](../../test/java/autocomplete/AutocompleteTests.java), and [Priority Queues](../../test/java/minpq/MinPQTests.java).

These tests serve as the "Harness" for your future work. By establishing a verified simple implementation and comprehensive tests now, you create a safety net that allows you to confidently delegate complex optimization tasks to the AI later.

The remaining steps in this project to be completed individually. Each team member will be responsible for submitting their own work beyond this point. You may continue to draw on other sources of human and artificial intelligence to support your learning and project work, subject to the course academic honesty policies.

## Individually set up your personal branch

> [!important]
> Obtain the group repository code (if you haven't done so already) and switch to your personal feature branch.

From your group's GitLab repository, create a new branch named after yourself. Since we're implementing a feature, we can name our branch semantically, such as `feature/vagar343-improved...`. This branch should contain only your personal implementation and any testing code for your personal implementation.

## Individually implement an improved approach

> [!important]
> Commit and push your improved implementation to your personal feature branch.

Develop an improved implementation of your interface that improves upon the simple approach. This improved implementation must still satisfy the exact same interface and produce the same outputs as the simple approach for all inputs, but it should be substantively different in its use of data structures and algorithms and, ideally, asymptotic runtime analysis. Each team member should develop their own substantively different approach. Consider using a more efficient data structure such as a tree or a heap instead of a list, adding caching or indexing to carefully save information, or applying algorithmic optimizations that we studied.

With your "Harness" (tests and simple implementation) in place, you can apply the "Outsource the Slam Dunks" or "Always Have an Agent Running" strategies. Delegate the implementation of complex data structures or algorithms to the AI, using your harness to verify correctness. If the AI fails, refine your `AGENTS.md` instructions (Harness Engineering) rather than just fixing the code manually.

## Individually test your improved implementation

> [!important]
> Commit and push property-based or example-based testing methods that together achieve at least 85% [test coverage](https://code.visualstudio.com/docs/debugtest/testing#_test-coverage) for your improved implementation. Add your tests to a new package under `src/test/java`. Provide screenshots of your test coverage panel and links to your test files.

Since the simple approach and improved approach both implement the same interface, you can write property-based tests that ensure their behaviors agree for all possible sequences of operations. Examples can be found in the tests for [Deques](../../test/java/deques/DequeTests.java), [Autocomplete](../../test/java/autocomplete/AutocompleteTests.java), and [Priority Queues](../../test/java/minpq/MinPQTests.java).

## Individually configure continuous integration

> [!important]
> Commit and push an updated `.gitlab-ci.yml` file configured to automatically run your tests when any relevant file is changed.

`.gitlab-ci.yml` is a YAML file, a specially-formatted text file format commonly used to configure computer systems. To reconfigure this file, we will first add a new stage for your feature and then a new job for testing both the simple approach and the improved approach.

1. At the top of the file, under `stages` add a new stage for your feature.
1. At the bottom of the file, add a job to run tests for the simple approach.
1. At the bottom of the file, add a job to run tests for the improved approach.

Under the `changes` rule, it can help to also list `.gitlab-ci.yml` so that edits to the configuration file will also trigger a continuous integration run.

## Individually create a merge request

> [!important]
> Submit a link to your GitLab merge request.

After you've pushed your improved implementation, tests, and continuous integration configuration, we can now open a GitLab merge request to begin the code review process. In GitLab, open a new merge request from your personal feature branch onto `main`. Give your merge request a descriptive title and use the following template for the description.

```md
## Summary of Changes

_What does this branch implement?_

## Data Structure Choices

_Explain how this approach differs from the simple approach._

## Algorithm Overview

_How do the various methods work?_

## Time Complexity

_Give the best and worst-case runtime for each method._

## Testing

_Which new tests did you add? Show your test coverage too._

## Harness Engineering

_What instructions did you add to AGENTS.md to fix AI mistakes?_

## Limitations
```

## Individually review a teammate's merge request

> [!important]
> Submit a link to the merge request for the teammate you reviewed.

In GitLab, navigate to a teammate's merge request. Open the "**Changes**" tab to see the diff between the `main` branch and their feature branch. As you read through their implementation and test cases, hover next to a line and click `+` to leave line comments. Leave at least one meaningful comment, such as:

- a correctness question
- a clarity suggestion
- a performance improvement note
- a missing edge case

Use **Add to review** rather than **Add comment now** when leaving comments to batch your comments together for easier review. Finally, approve the merge request when you believe it is correct and complete, or leave it unapproved if you believe the merge request is missing any previous step or if further changes are needed.

If you're worried about completing this task due to unresponsive teammates, bring this up to the course staff so that we can assign a different team's merge request for you to review.
