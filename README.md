Project idea:
This is a small web application that aims to introduce potential customers to the company and make direct sales. The application supports two types of roles - admin and user. The administrator can control the entire process of adding food, deciding which other user can become an administrator and view all profiles of registered users.
Short Info and Functionalities:
  •	 public part (accessible without authentication) - the non-authenticated users can see:
1.	Home page - it contains brief information about the company and displays clients` opinions about the application.
2.	Login page - contains a login form.
3.	Register page - contains a registration form.
4.	Error page - "page not found 404" page pops up when someone tries to reach a non-existing page.
5.	Error page – “access denied 403” page pops up when someone tries to reach a page, they  do not have access to.
6.	Food page – it contains all available food with the most important information.
7.	Search page – it gives the ability to search food by category or/and maximum price.
  • 	private part (available for logged/registered users) - it contains the following pages:
1.	Product page - it contains all available food with the most important information with a “more information” button
2.	Detailed product page – it contains the chosen food with more information about it with an “add to order” button
3.	Review order – it holds the list of chosen products for buying and the delivery details.
4.	Delivery page - form in which the user write the wanted delivery information (country, city, address). Once the delivery details are added, they are visible in the “review order” section.
5.	Order done page - this is a page that inform the client that he has made the order successfully. 
6.	Add a comment – it contains a form, where customers can write their opinion about the application
  •	 administrator part (available for admins only):
1.	Add food page – contains an add food form
2.	Profiles page – it contains a list of all registered users and their roles. Administrators can make each of them an administrator.
________________________________________
Project structure:
The project structure is a standard MVC.
For this application are user CSS, HTML, Thymeleaf and JavaScript for the Front-End and Java (Spring framework, Hibernate) for the Back-End part along with MySQL. About the testing of the application are used JUnit5 - implemented in Unit and Integration tests.
