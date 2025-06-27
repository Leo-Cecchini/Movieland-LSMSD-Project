# Movieland
Project for the Large Scale and Multi-Structured Databases course of the Master Degree in Artificial Intelligence and Data Engineering of the University of Pisa.
<br><br>
Movieland is a social platform for movie enthusiasts, designed to combine rich content exploration with intelligent recommendations and social interaction. Users can browse movies, follow actors and directors, write reviews, and discover content based on their preferences and social connections.

(*all the details about the implementation and all the tools that we used can be found in the pdf documentation of the project*)

## MAIN FEATURES
* **Browse and search** movies, actors, and directors
* Write and read reviews, **rate movies**
* Follow other users, like posts, and comment
* **Add automatically** brand new movies from IMDB using an external API
* **Personalized movie recommendations** based on interests and user activity
* **Graph-based suggestions** powered by actor collaborations and user connections
* RESTful APIs: all the functionalities are delivered as APIs

## IMPLEMENTATION
* **Back-end**: Java and SpringBoot framework
* **Databases**: **MongoDB** for storing most of documents and **Neo4j** used for the managing all the relationships that we have
* **Testing**: Postman was used throughout development for testing, documenting, and debugging the REST API endpoints

## EXTERNAL APIs
In order to let the admin to add brand new movies into the platform, we used an external API for retrieving all the information and details of the movie. Just by typing the movie title and by using the imdb_id of the movie, we can retrieve everything that we need to keep the platform always up-to-date.
<br>The external API used are the ones freely available on MDBList (https://mdblist.com/).  

## DATASET
The datasets have been taken from some public repository in Kaggle.
* **Movie and Actors**: multiple datasets published by Victor Soeiro (https://www.kaggle.com/victorsoeiro)
* **IMDB Movie Reviews**: updated and realistic reviews from IMDB, published by Vishakh Patel (https://www.kaggle.com/vishakhdapat)
* **Movies Post and Comments**: big collection of posts and related comments about movies, published by Curiel (https://www.kaggle.com/curiel)
We performed some pre-processing on the data in order to make it suitable for our platform and our database schema (all the details can be found in the final documentation) 

## FUTURE IMPLEMENTATION
* Frontend interface
* Machine learning-based recommendation engine
* Notification system and real-time chat