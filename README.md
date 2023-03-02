# TODO APP 

![Course](https://camo.githubusercontent.com/1e2c2cdb65f7dd7985d83f4f59cf7d95d3d565af032d7b64122334428f4ac74c/68747470733a2f2f692e706f7374696d672e63632f5373714a72626a582f7468756d622e706e67)

## 🤖 Summary
- Fully Functional To-Do app in Kotlin with XML and Jetpack Compose.
- Based on: 
  - **Teacher:** Stefan Jovanovic.
  - **Link Course Udemy:** [To-Do App & Clean Architecture -Android Development - Kotlin](https://www.udemy.com/course/to-do-app-clean-architecture-android-development-kotlin)

## 💼 Libraries 
- Room 
- Navigation Component
- LiveData
- ViewModel
- Data Binding

## 🫡 Main Features 
- Display List of Todos (title, description and importance color dot)
- Search / Filter into List of Todos through Search Bar
- Sort By Priority with collection method
- Delete All / Delete specific Todo
- Add local Todos (Local DB)
- Modify existing Todos (Local DB)
- Swipe to delete function
- Sort By Priority with DB (story/sort_by_form)

## 🔥 Main Topics 
- Use Single Activity Principle with multiple fragments.
- Use Navigation Component with Safe Args to pass data between fragments.
- Create custom transition animations to navigate to destinations.
- Use Room Database and create queries to filter data.
- Use Data Binding to cleanup our presentation logic / code. Also, create custom Binding Adapters.
- Learn about Grid and Staggered Grid Layout and DiffUtil in our Recycler View.

## 🤔 Also learning about...
- Use string array into a spinner XML
- Setup new Menu Provider into Fragments => setHasOptionsMenu is deprecated
- Configure Toolbar and Navigation sync
- Use @Volatile and synchronized to create our Local DB Instance
  - @Volatile => Make  our variable immediately available in another threads 
  - synchronized() => Lock the process to another threads until it finishes
  - We use both to ensure that our Database instance will be created only once.
- Create custom Type Converter in Room
- Use DiffUtil to improve performance in our recycler view.
- Use FragmentContainerView instead of fragment tag in out Navigation Component.

## 🐙 Changes 
- Not use AndroidViewModel to get Context and instantiate classes => Use Hilt.
- Use Navigation Component and ViewModel to get parameters (savedState)
- No use any kind on SharedViewModel. Create two different ViewModels.
- In order to set priority from an existing Todo in UpdateFragment, I put an extra property in our 
  enum. That way I don't have to create an extra function to map Priority -> spinner position.

## 🤕 Problem Resolve 
- I was trying to get todo navArg immediately. It causes a crash. I must get safeArgs properties with 
  lazy block.
- Bug: When searching is active, if you delete a todo then recycler view will be populated with all todos.
  ignoring query.

## 💡 Ideas 
- IMO If it would be a real project, sort by functionality will be made by priority by changing Priority Enum
  to simple Int in order to make a easy SORT BY into DAO.