# CSE 373 Projects

A set of educational programming projects built around 4 components: [**Deques**](src/deques/), [**Autocomplete**](src/autocomplete/), [**Priority Queues**](src/minpq/), and [**Seam Finding**](src/seamfinding/). See the [**Releases**](https://github.com/kevinlin1/huskymaps/releases) for instructions about each project component.

## Setup

This project is pre-configured for [IntelliJ IDEA](https://www.jetbrains.com/idea/download/). Run any of the top-level client classes.

- `BrowserHistory` to simulate web browser history using a `Deque`.
- `CitySearch` to search city names using `Autocomplete`.
- `DNASearch` to search all the suffixes of a DNA sequence using `Autocomplete`.
- `MapServer` to run Husky Maps, an educational web app for getting around Seattle.
- `ReportAnalyzer` to count web accessibility statistics from Lighthouse reports using `MinPQ`.
- `SeamCarver` to remove the least-noticeable vertical or horizontal seams from an image.

To see the map images in the Husky Maps web app, [sign up for a free MapBox account](https://account.mapbox.com/auth/signup/?route-to=%22https://account.mapbox.com/access-tokens/%22) to get an access token. Once you have your access token, in the IntelliJ toolbar, select the "MapServer" dropdown, **Edit Configurations...**, under **Environment variables** write `TOKEN=` and then paste your token. Re-run the `MapServer` class to launch the web app and enjoy the ["Ice Cream" map style by Maya Gao](https://www.mapbox.com/gallery/).
