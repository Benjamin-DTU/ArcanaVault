# ArcanaVault
*A Kotlin-Based Mobile Application for D&D Spell Management*
## Group
Group number 8

### Group members 
**Name:** Frederik Bode Hendrichsen, **Student number:** s224804, **Github Name:** frebbers

**Name:** Sofus Jejlskov Brandt, **Student number:** s214972, **Github Name:** sjbrandt

**Name:** Nicolai Dahl Madsen, **Student number:** s213364, **Github Name:** Nicodm13

**Name:** Mathias Hidan, **Student number:** s215874, **Github Name:** MJHidan

**Name:** Adrian Akinade Colin Macauley, **Student number:** s225733, **Github Name:** s225733

**Name:** Benjamin Andresen, **Student number:** s195828, **Github Name:** Benjamin-DTU

GitHub repo: https://github.com/Benjamin-DTU/ArcanaVault



## Preface
The initial concept for this project was developed collaboratively by the group members. Additionally, the group worked together on various aspects of the development and implementation, leading to the project's successful execution and final presentation.

In the preparation of this report, Grammarly has been utilised to ensure grammatical accuracy and improve the clarity of the content. Additionally, during  development, we used ChatGPT for troubleshooting and code generation. This is clearly referenced, where applicable, in the code comments.

## Table of Contents
1. [Introduction](##Introduction)
2. [Design](##Design)
3. [Implementation](##Implementation)
4. [Discussion](##Discussion)
5. [Conclusion](##Conclusion)


## Introduction

The ArcanaVault project is an application designed to let users explore a database of spells for the game Dungeons and Dragons. It allows users to search, filter, and favorite spells with data synchronized across different screens. The project utilizes Jetpack Compose for UI, Realm DB for local storage, and REST APIs for fetching data. This report outlines the design and implementation of core features, challenges faced, and solutions applied during the development process.

## Requirements

### Customer Desire

Through communication with potential users multiple elements were made clear. One of the most important details was that we should use as much official 5e content as possible. 

### Functional requirements

* It must be effortless to find a spell
* Users can view a list of all available spells
* Users can save spells for fast navigation
* Users can view detailed information about each spell

### Non-functional requirements

* User-friendly: It must be simple to use, even for non-technical users
* Easy maintenance: It must be easy to maintain the application and add additional features in the future

### MoSCoW Prioritization Method

We used MoSCoW to prioritize our initial feature ideas.

#### Must Have:

1. Display categories of spells: List different categories of spells, for example, based on spell schools (category), levels, casting time, duration or class-specific spells.
**Implemented**
1. Implement pagination: Allow users to load more pages with additional spells in the chosen category, making it easy to browse through large lists.
**Not implemented:** after discussing this with Stefanos, we got this requirement waived on the condition that our app starts up quickly. We did this to make filtering simpler and more elegant.
1. Details about spells: Display detailed information about the selected spell, including: name, level, school, casting time, range, components,and a full description of the spell’s effects. Optionally, include other details such as upcasting effects or sources.  
**Implemented**
1. Favouriting: Users should be able to favourite specific spells. The spell favorite states should persist between app sessions.
**Implemented**
1. List favourite spells: Display a list of favourited spells, with the ability to filter by  different attributes like level, school, class, etc.
**Implemented**
1. List favourite spells: Display a list of favourited spells, with the ability to filter by different attributes like level, school, class, etc.
**Implemented**
1. Filtering: Be able to limit which spells are displayed using a filter
**Implemented**

#### Should Have

1. Local caching: Display spell information even when the app is offline by caching data locally. The cache must persist between app launches.
 **Implemented**
1. More in-depth navigation: Allow users to navigate from one spell’s details page to other related categories, such as spells from the same school, spells of a similar level, or other spells available to the same class.
**Partially implemented:** While it is possible to open rules and conditions explainers from within spell details pages, it is not possible to navigate from one spell to another or from a spell to a specific category or filter.
1. Search function: Create a search function where the player can search for spells using their name or description.
**Implemented**
1. Spell book building: Enable users to build spell books for their characters.
**Not implemented**

#### Could have 

1. Adding Monsters, Feats, and Other Game Elements: The app could be expanded to include additional D&D game elements, enhancing its functionality beyond just spells. This would provide users with a more comprehensive tool for managing and learning about different aspects of the game
**Not implemented**
1. Spell suggestions: Suggest additional spells that could complement the current selection in a spell book or spell list.
**Not implemented**
1. Dice simulation: Users can simulate dice rolls directly for spells that require rolling dice.
**Not implemented**


## Design

### Design considerations

**User Experience:** The application is designed with a user-centric approach, ensuring the
interface is intuitive and accessible for all users.

**Scalability:** The application should be designed to allow for easy expansion, enabling the addition of other game elements such as monsters or items with minimal effort.


### Frontend/UI

The application is designed with usability in mind, providing separate views for spell browsing, managing favorites, searching and filtering. Each screen focuses on displaying only the features and information relevant to its purpose. The design follows the KISS (Keep It Simple, Stupid) principle, emphasizing simplicity in functionality and interface. Inspired by Steven Krug's Don't Make Me Think, the application ensures intuitive navigation and interaction, reducing the load on users and making it easy for them to accomplish tasks without confusion.

### Application flow

![S1il_ksvyl](https://gist.github.com/user-attachments/assets/4f16b13a-baf8-44b1-b4f7-a5e6cd524744)

The application consists of two main screens: Home/Search and Favorites:
* The spells screen serves as the central hub for browsing and searching spells. Users can scroll through the entire list of available spells and tap them to view detailed information.
* The Favorites view provides quick access to spells that have been marked as favorites. Users can review their favorite spells and remove them from the list if needed.

The application ensures that actions taken in one view, like favoriting or unfavoriting spells, are immediately saved to the database and updated across all views. This helps maintain consistency and avoids the need for manual refreshing.


### Figma prototype
![rkEAMxb_1l](https://gist.github.com/user-attachments/assets/44e166f2-a819-46d0-b4d1-eca0066c6fb6)

The Figma prototype played an role in our design process. By offering a clear and interactive prototype of the application, it enabled us to refine our design without the need to alter the code. Notably, the prototype guided the implementation of the filter feature, which replaced the earlier categorization approach, enabling agile responses to changing requirements. This iterative process ensured that the application's overall interface remained intuitive and consistent by creating an ideal for everyone to align with. The hands-on exploration with Figma also allowed us to identify potential design challenges early and make informed decisions that improved both the aesthetic and functional aspects of the application.

While the protoype was helpful,our final product differs from the prototype in a few ways. The prototype was minimalistic, but we had to change the images for schools as the original ones didn’t scale well, and some parts of the design were not very usable although they looked good in the prototype.


*Figma prototype*: https://www.figma.com/design/KKjGqVJfET31GzKIJSJOdk/Software-moment?node-id=289-729&t=ie7OIgBY5J08SHOB-1

## Implementation

### Database

To implement local caching, we chose the object-oriented database RealmDB due to its simplicity and performance. The purpose of the database is to save our spell list, ensuring that users have access to spells and favorite state even if the third-party API is unavailable or if the user is in a location without internet connection.

### Backend

The backend of the application is built around a third-party API that provides the core data for spells, such as names, levels, schools, and descriptions. The application has a local caching system using RealmDB, allowing users to access data even without an internet connection or if the API becomes unavailable.

#### Integration with the Third-Party API
The application communicates with the publicly available D&D API at www.dnd5eapi.co to retrieve spell data. An API client manages requests, handling tasks such as:

* **Data Retrieval:** Fetching spells in batches and storing them in the local cache to reduce repeated network calls.
* **Error Handling:** Falling back to locally cached data if the API is inaccessible.
* **Data Synchronization:** Periodically checking for updates in the API data and syncing them with the local database when a connection is available.


### Frontend/UI

The application's user interface is implemented using Jetpack Compose.

The application consists of two primary screens: Spells and Favorites. The rest of the UI exists as components which are added to these two screens. We implemented a custom theme which includes a dark mode that adjusts based on the system theme. 
![HJqLI4-_1l](https://gist.github.com/user-attachments/assets/b3bd5c34-8d0a-4475-a2de-b4c305ea8bd6)
![BJLYINZ_Jg](https://gist.github.com/user-attachments/assets/98797503-f8f5-4710-9709-e5c0015d2684)
Using a theme instead of hard-coded colors also allows us to easily change the color scheme of the app, without having to change the composables individually.

We also implemented a test to measure the time to render our list items which helped us benchmark our code to optimize composables for smooth scrolling.

```
@Test
    fun measureTimeToRenderTenItemViews() {
        // Some sample data for each item
        val sampleDetails = listOf("school: Abjuration", "level: 1", "casting time: 1 action")
        val titles = (1..10).map { "Sample Title $it" }

        val renderTimeMillis = measureTimeMillis {
            composeTestRule.setContent {
                // Retrieve the context and ImageLoader inside the test environment
                val context = LocalContext.current
                val imageLoader = context.imageLoader

                Column {
                    // Render 10 ItemView composables
                    titles.forEach { title ->
                        ItemView(
                            title = title,
                            details = sampleDetails,
                            // Pass the ImageLoader here
                            imageLoader = imageLoader
                        )
                    }
                }
            }
            // Wait for all pending compositions and UI tasks to settle
            composeTestRule.waitForIdle()
        }

    Log.d("ItemViewPerformanceTest", "Rendering 10 ItemView composables took $renderTimeMillis ms")
    }
```

#### Spells screen

Features: 
* A search bar for finding spells by name.
* Filters to narrow down the spell list by attributes such as level, school, or components.
* Sorting options for ordering the spell list.
* A list displaying spells with options to view details or mark them as favorites.

#### Favorites screen
Features:
* A list of favorite spells.
* The ability to remove spells from the favorites list.
* Similar search/sort/filtering options as Home.

#### Reusable Components
The UI is structured with modular components
* **Header:** A top app bar that provides quick access to actions like toggling filters or search.
* **ListView:** A flexible component for displaying lists of spells, supporting item selection and favoriting.
* **FilterRow:** Displays active filters and allows users to remove them, dynamically updating the filtered results.
* **CustomScrollbar:** Offers visual feedback for navigating long lists.
* **FetchingDataView:** Indicates loading status when retrieving or processing data.

### Navigation
![ryCt-m-Oke](https://gist.github.com/user-attachments/assets/d9006884-f5b0-433a-969c-f85e6767889e)

**Home/search page:** This serves as the starting point of the application. From here, the user can either:
* Go to the Favorites page to view their list of saved favorite spells.
* Filter spells
* search for specific spells
* Go to the detail page for a specific spell
* Save a spell to favorite


**Favorites page:** This page displays all the spells that the user has marked as favorites.


**Spell details page:** This page provides detailed information about the selected spell. From here, users can also mark the selected spell as a favorite and view conditions or rules related to it.

**Animations:** Transition animations were added for every transition and appearing/disappearing element, including the header which dynamically slides away to free up screen space. The direction of the sliding animations were designed to fit the user's expectations, easing navigation.

## Discussion
Overall, group work and time management went well as we worked iteratively to build and trim our backlog and review implementations in our regular scrum-sessions.

Implementation-wise we ended up falling short of our ambitions. As we began work on our project before properly understanding the functional paradigm, we ended up making a few mistakes that quickly turned into technical debt. The first being our overuse of logic inside composables. With no view model controlling our views, logic was spread out across different views. 

Another mistake we made was thinking of screens like views. The result was, that we ended up making the favorites and spells screen into practically duplicates of one another. Instead, reusable elements like the list of spells should have been implemented as components.


## Conclusion
While the product met our goals, and is elegant and easy to use, a more carefully considered design language could have given our app more character, and more solid coding conventions could have prevented bugs and technical debt. Future work could focus on implementing a proper ViewModel to improve data handling, remaking the navigation structure for better usability, and refactoring the "SpellList" and "Favorites" screens into reusable components for cleaner and more maintainable code. These changes would further enhance the functionality and structure of the application.

