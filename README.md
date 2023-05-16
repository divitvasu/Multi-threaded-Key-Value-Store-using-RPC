# Multi-threaded-Key-Value-Store-using-RPC (2021)

> Tools - Java, JAVA RMI, Docker, Shell Scripting

## About

Key-Value-Store based on a Client-Server architecture, leveraging RPC (Java RMI) for inter-communication. The server application is multithreaded for added concurrency and can handle multiple clients at once. 
- Both the server and the client applications define a logger to store the logs into separate files for the server and the clients.
- Upon execution, the pre-defined operations are performed and then the user is asked for the input of whatever it wants from the list of choices. The user has the option to perform three operations on the server. The user input is received at the server, the input message is checked for consistency and correctness. If the message is invalid, a log is made, and the client moves on to providing the next input. If the message is valid, the respective operation is performed on the key-value store as directed by the user input. The acknowledgement of the same is sent back to the client, and the client logs the same on its side.
- The remote methods are made available to the client using Java RMI. When the server is executed, it binds an object to a registry for a client to access. Whenever the client invokes the remote method, the corresponding thread is executed on the server, which in turns calls a synchronized method to facilitate the required operation. A separate class, which serves as a common store for all clients defines a concurrent hashmap.

## Project-Structure

- *Server_app.java* Contains the java code for the server application. This file accepts one optional command-line argument: the port it is supposed to listen on. If this file is run without the shell scripts and not inside any docker container (java from cmd prompt), there is additional support for falling to the default port to listen on; if no command-line input is provided. This file takes in one optional command-line argument in the following format: `java Server_app <server_listening_port>`

- *Key_Store_Int.java* Contains the java code for the definition of the interface. This is where the remote methods are defined. This file supports the Server java file.

- *Key_Store_Int_Imp.java* Contains the java code for the implementation of the interface and a class for storing the key-store values. The remote methods and the different threads are implemented/defined here. This file supports the Server java file.

- *Client1_app.java* Contains the java code for the client application. This file accepts two optional command-line arguments: the address of the server and the port it is supposed to send the request to the server. If this file is run without the shell scripts and not inside any docker container (java from cmd prompt), there is additional support for falling to the default server address and port to communicate on; if no command-line input is provided. This file takes in two optional command-line arguments in the following format: `java Client1_app <server_address> <server_listening_port>`

- *Client2_app.java* Contains the java code for another client application (essentially a clone of the first one, with slight difference in key-value pairs). This file accepts two optional command-line arguments: the address of the server and the port it is supposed to send the request to the server. If this file is run without the shell scripts and not inside any docker container (java from cmd prompt), there is additional support for falling to the default server address and port to communicate on; if no command-line input is provided. This file takes in two optional command-line arguments in the following format: `java Client2_app <server_address> <server_listening_port>`

- *Dockerfile* This contains the parameters to generate and create images for generating the builds for the server and both the clients. JDK 17 from the alpine image has been used.

- *server.sh* This file contains all the configurations needed to start from scratch by creating the docker images for the clients and the server and assigning them to the default 'host' network. After this, the script launches the container for the server. Note that the default 'host' network has been used to avoid exposing any ports, as, on a custom network, the required ports need to be exposed manually. This has been done with the aim to reduce any complexities during the execution. The script terminates with deleting the container from the environment to avoid any conflicts. This file takes in one command-line argument in the following format: `./server.sh <server_listening_port>`

- *client1.sh* This file contains all the configurations needed to start the first client container that has been created in the previous step. The client container also gets attached to the default 'host' network. This script terminates with deleting the container from the environment to avoid any conflicts. This file takes in two command-line arguments in the following format: `./client1.sh <server_address> <server_listening_port>`

- *client2.sh* This file contains all the configurations needed to start the second client container that has been created in the previous step. Running this file is optional. This is to demonstrate the working of threads on the server when two separate clients are querying the server. This client container also gets attached to the default 'host' network. This script terminates with deleting the container from the environment to avoid any conflicts. This file takes in two command-line arguments in the following format: `./client2.sh <server_address> <server_listening_port>`

## Sample Execution

There are two possible execution methods for this project as follows:

### Method 1 (CMD prompt)

- `javac Server_app.java Key_Store_Int.java Key_Store_Int_Imp.java Client1_app.java Client2_app.java` (On 1st prompt)
- `rmiregistry` (On 1st prompt)
- `java Server_app` (On 2nd prompt)
- `java Client1_app` (On 3rd prompt)
- `java Client2_app` (On 4th prompt-optional)
- The respective logs for the server and client fall into the working directory in a separate file.
- Note, ports and addresses can be supplied too, if needed. The commands above invoke the default set ports.

**Client 'x' output**
<p align="center">
<img src="https://github.com/divitvasu/Multi-threaded-Key-Value-Store-using-RPC/assets/30820920/7c053297-3350-4b28-9616-0b3645e2c7ab" alt="Image" width="500" height="500">
</p>

**Server output**
<p align="center">
<img src="https://github.com/divitvasu/Multi-threaded-Key-Value-Store-using-RPC/assets/30820920/808bc503-191e-4e6a-bba8-d8c71a693b6e" alt="Image" width="500" height="500">
</p>

**Server Log output**
<p align="center">
<img src="https://github.com/divitvasu/Multi-threaded-Key-Value-Store-using-RPC/assets/30820920/0f5ff05e-1503-4fb5-891f-d1fe0ac5b7b6" alt="Image" width="400" height="400">
</p>

**Client Log output**
<p align="center">
<img src="https://github.com/divitvasu/Multi-threaded-Key-Value-Store-using-RPC/assets/30820920/893c263e-f1cb-454a-abe2-6232b3c5deb8" alt="Image" width="400" height="400">
</p>

### Method 2 (Docker and shell script)

- `./server.sh 9992`
- `./client1.sh localhost 9992`
- `./client2.sh localhost 9992`
- The respective logs fall inside the docker images.
- Note the scripts will need the command line arguments to run properly.

**Server Script output**
<p align="center">
<img src="https://github.com/divitvasu/Multi-threaded-Key-Value-Store-using-RPC/assets/30820920/050288a9-d02b-4c99-9085-adc078870ba7" alt="Image" width="600" height="500">
</p>

**Client Script output**
<p align="center">
<img src="https://github.com/divitvasu/Multi-threaded-Key-Value-Store-using-RPC/assets/30820920/a2196fa9-45bf-4f1c-a2c2-75c380c33ab2" alt="Image" width="600" height="500">
</p>

## Final Thoughts

This application leverages JAVA RMI and concepts of multi-threading. The server provides with three multi-threaded remote methods to the client. The clients access the references to these remote methods via the rmi registry that the server makes available by binding. The server basically binds the object associated with the interface (which defines these remote methods) to the registry, which the client accesses. These methods on the server-end invoke multi-threaded operations. That is either of the 3 separate queries can be executed concurrently by separate clients, depending on which thread is free. This design is helpful when multiple clients want to perform operations on the server. If one performs put, the other client can concurrently perform a get or a del. However, the key-store needs to be locked momentarily whenever a change is being made, to maintain consistency. Thus, it has been defined as a separate class, whose object is used by all the threads and in turn, methods. The project moreover uses a concurrent hashmap in lieu of the simple implementation, for added concurrency, whenever possible. Whenever a remote method is invoked, the interface is instantiated, a thread is called, and the process moves onto a using a ‘synchronized’ helper function for that operation. The synchronized method helps ensure that no other thread accesses a particular helper method whenever it is occupied by some thread. A key assumption here is that the number of clients is small, such a system would not work on a very large scale and some modifications would be needed. One possible enhancement that can be brought is that instead of waiting for the thread’s output, we could make a thread pool of some sort, say using a List. Then we would have to iterate over the same to fetch and return the respective outputs, as we would not be sure, which threads finish execution first or in which order the priorities to the threads were assigned unless explicitly set. Callbacks and Futures could also potentially be leveraged.

## References

- [JAVA RMI](https://www.javatpoint.com/RMI)
- [Multi-threading in Java](https://www.geeksforgeeks.org/multithreading-in-java/)
- Distributed Systems – Concepts and Design – George Coulouris, Jean Dollimore, Tim Kindberg, Gordon Blair
