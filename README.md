Project distributed system - a web page based on multiple microservices (Users, devices, monitoring and chat) each with it's own microservice and db. It has security via JWT tokens

You can see the deployment diagram of each part of the project.

The final project contains Users, devices, monitoring (using RabitMQ) and chat via WebScoket
A user can have as many devices as it wants but only the admin can assign and create new devices
A user (client) can register then login to see his devices (if any).
Then all users can go to '/chat to chat with all other users and admins privately.

Admins can make other users admin and change all of user's (beside password) and device's fields as they want.

A user can see his device energy usage/day hour by hour in a nice grafic

And the simulator, simulates the current usage of the device using a predifined csv with the usage / 10 min per column
(it loads in the RabbitMQ the id, timestamp and current usage)

All the user's, device's and monitoring's microservices have security based on JWT tokens