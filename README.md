## Guide to MediLabApp
In this Guide, You will learn how to create an advanced mobile app, 
The application to be developed is an online Lab Tests Booking System which will allow patients book for specific Lab Tests form different Laboratories, 
it includes Authentication module, Services Listing, Shopping Cart Module, Maps and GPS Module, Fragments, Payment, APIs etc,
This app is inspired by Online Booking Apps such as Glovo, Uber Eats, Booking.com, Yum, Jumia Foods etc.

### Prerequisites
In order to develop this app, you must have an understanding of Kotlin Language and have Android Application Development Fundamentals.
<br>

This mobile application consumes an API in the backend, If you have not created the API, Please follow below link <br>
https://github.com/modcomlearning/medilab

### Part 1
#### Step 1 : App Structure
In this step we understand the app structure.
Create a New Android App. Use Empty Views Activity.

![img.png](img.png)

NB: Please use a different unique Package.<br><br>

In your app Package under Kotlin + Java, create four subpackages namely; <br>
adapters - To be used to store our Adapters used by Recycler Views<br>
constants - To be used in storing Constants variables that are used across the application <br>
helpers - To be used to hold helpers that help us with different functionalities in our App i.e APIHelper<br>
models - This package will store models that define the data we will be working on.<br>




