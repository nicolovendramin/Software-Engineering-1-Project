The Council of Four
====================
To get the application running you should open the package servers and start the **ServerCD4** class as a Java Application.
Once opened the server will ask you to insert the port number for Socket and RMI connection. After the port numbers have been correctly set the server will be online, ready to accept connections.

When the server is running the server admin has the possibility to type in some commands that are summarized by the help menu which is displayed every time you type the command `?`. Here we report the output of the `?` command:

>Here's a list of some useful server commands

>(if you are in a hurry you can write them with no spaces)

>* active games number *get the number of running active games*
>* pending games number *get the number of penging games*
>* emporiums to win:<integer>	*set the number of emporium to build to end the game*
>* set min number:<integer>  *set the minimum number of player per game*
>* set max number:<integer>  *set the maximum number of player per game*
>* set action timeout:<integer>  *set the action timeout timer*
>* enable market      *enable the use of the Market for new games*
>* disable market      *disable the use of the Market for new games*
>* quit        *server shutdown (without killing active and pending games)*
>* quit and kill      *server shutdown (with no mercy. This is not very kind if there are active games)*

To run an instance of a **Client** you should open the package clients and run one between GuiSimpleClient and CliSimpleClient. The **Launcher** class is an application which simply prompts the user whether he prefers to play using the graphical user interface or the command line interface, and runs the correct between the two clients named above. 

On **Cli** You will be kindly requested to insert in the order:

1. the connection type, Socket or RMI
2. the IP server address
3. the number of the server welcome socket 

Once the connection with the server has been correctly done, the user can type several commands, which are summarized by the list displayed after giving to the application the command `?`:

>Useful commands

>  * [SetUsername]<name>    *set the user's nickname*
  * [Action]<action number>    *choose the selected action*
  * [Action]abort        *exit an action and start it again*
  * [Chat]<message>      *send the message to all the other players*
  * [Pm]<message>@@<receiver>  *send a private message to the specified player*
  * [Disconnect]<report message>  *disconnects you from the game*
  * [Connect]<report message>  *reconnects you to the game*
  * [Refresh]      *asks the server to send you the updated status of the game*

To kill the Cli client you have to type the command quit in the console.

The **Gui** is easy to understand, once is running you have to set the connection parameters with the menu Settings/Connection Settings (CTRL + t) and then start the connection with Game/Start (CTRL + s).
The other commands and available features are easy to understand from the graphics. If you want more infos about one element, with respect to what you see, try clicking it!
* * *
The complete development of the application has been done by Davide Piantella, Giovanni Scotti and Nicolò Vendramin. All of the three has equally contributed in all the different parts of the development, starting from the design up to the implementation and testing. 

The game from which the application is inspired is *The Council of Four* from Cranio Creations. Thanks to the Cranio guys for having elaborated such a challenging game to implement. 

**Have fun!**